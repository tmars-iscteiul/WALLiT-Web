package wallit_app.javaserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

import wallit_app.data.AckMessage;
import wallit_app.data.FundInfoEntry;
import wallit_app.data.FundInfoEntryChunk;
import wallit_app.data.MovementEntry;
import wallit_app.data.MovementEntryChunk;
import wallit_app.utilities.TimeScaleType;


/**
 * @author skner
 *
 */
public class ConnectionHandler extends Thread {

	private Socket clientSocket;
	private ObjectInputStream objectIn;
	private ObjectOutputStream objectOut;
	private boolean online;
	private int handlerID;
	private String username;
	
	private JavaServer javaServer;
	
	public static final String USER_MOVEMENTS = "./userMovements/";
	
	public ConnectionHandler(Socket s, int id, JavaServer javaServer)	{
		try {
			online = true;
			handlerID = id;
			clientSocket = s;
			objectIn = new ObjectInputStream(clientSocket.getInputStream());
			objectOut = new ObjectOutputStream(clientSocket.getOutputStream());
			this.javaServer = javaServer;
			consolePrint("Handler online.");
			} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// TODO Add connection timeout (closes the socket, informing the client (somehow) that the connection has timed out).
	public void run()	{
		
		while(online)	{
			try {
				String receivedData = (String)objectIn.readObject();
				sleep(0);
				consolePrint("Received data from client: \"" + receivedData + "\".");
				handleUserRequest(receivedData.split(","));
			} catch (IOException e) {
				// TODO Add a way to know what happened with the connection
				consolePrint("Terminating the connection.");
				disconnect();
			} catch (InterruptedException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Message standard sent by android client
	 * Arg0: COMMAND
	 * Arg1: USERNAME
	 * Arg2: EXTRA DATA
	 */
	private void handleUserRequest(String receivedArgs[]) throws IOException	{
		AckMessage messageToSend = null;
		switch(receivedArgs[0])	{
		case "REQUEST_MOVEMENT_HISTORY":
			 messageToSend = new AckMessage("MSG_ACK_USER_DATA", getMovementEntriesByUser(receivedArgs[1]));
			break;
		case "REQUEST_FUND_INFO":
			messageToSend = new AckMessage("MSG_ACK_FUND_DATA", getFundInfoEntryChunks());
			break;
		case "REQUEST_LOGIN":
			// Logs in any user (for now)
			messageToSend = new AckMessage("MSG_ACK_POSITIVE", getUpdatedBalanceByUser(receivedArgs[1]));
			username = receivedArgs[1];
			consolePrint(username + " authenticated.");
			break;
		case "REQUEST_DEPOSIT":
			consolePrint(receivedArgs[1] + " wants to deposit: " + receivedArgs[2]);
			if(deposit(username, Double.parseDouble(receivedArgs[2])))	
				messageToSend = new AckMessage("MSG_ACK_POSITIVE", null);
			else
				messageToSend = new AckMessage("MSG_ACK_NEGATIVE", null);
			break;
		case "REQUEST_WITHDRAW":
			consolePrint(receivedArgs[1] + " wants to withdraw: " + receivedArgs[2]);
			if(withdraw(username, Double.parseDouble(receivedArgs[2])))	
				messageToSend = new AckMessage("MSG_ACK_POSITIVE", null);
			else
				messageToSend = new AckMessage("MSG_ACK_NEGATIVE", null);
			break;
		default:
			// Denies any other request
			messageToSend = new AckMessage("MSG_ACK_NEGATIVE", null);
			break;
		}
		objectOut.writeObject(messageToSend);
		consolePrint("Sent ack to client: " + messageToSend.getAckMessageType());
        objectOut.reset();
	}

	private double getUpdatedBalanceByUser(String username)	{
		Scanner s;
		double lastValue = 0;
		try {
			s = new Scanner(new File(USER_MOVEMENTS + username + ".txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return lastValue;
		}
		while(s.hasNextLine())	{
			String nextLine = s.nextLine();
			if(!nextLine.startsWith("#"))	{	// If is not a comment
				lastValue = Double.parseDouble(nextLine.split(",")[2]);
			}
		}
		s.close();
		return lastValue;
	}
	
	private ArrayList<MovementEntryChunk> getMovementEntriesByUser(String username)	{
		Scanner s;
		LinkedList<String> fileLines = new LinkedList<String>();
		try {
			s = new Scanner(new File(USER_MOVEMENTS + username + ".txt"));
			while (s.hasNextLine()) {
				String nextLine = s.nextLine();
				if(!nextLine.startsWith("#"))	{	// If is not a comment
					fileLines.push(nextLine);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		ArrayList<MovementEntryChunk> res = new ArrayList<MovementEntryChunk>();
		MovementEntryChunk aux = new MovementEntryChunk();
		while (!fileLines.isEmpty()) {
			String nextLine = fileLines.pop();
			String[] args = nextLine.split(",");
			if(!aux.addMovementEntry(new MovementEntry(args))) {	// If list is full
				// Add full chunk to the main list, create a new chunk and add the last entry to the new chunk
				res.add(aux);
				aux = new MovementEntryChunk();
				aux.addMovementEntry(new MovementEntry(args));
			}
		}
		System.out.println(aux.getMovementEntryList().get(0).getDate());
		res.add(aux);
		s.close();
		return res;
	}
	
	private ArrayList<FundInfoEntryChunk> getFundInfoEntryChunks()	{
		ArrayList<FundInfoEntryChunk> res = new ArrayList<FundInfoEntryChunk>();
		// TODO Remove repeated code if possible. This is just plain ugly
		res.add(new FundInfoEntryChunk(getFundInfoEntry(TimeScaleType.ONE_MONTH), TimeScaleType.ONE_MONTH));
		res.add(new FundInfoEntryChunk(getFundInfoEntry(TimeScaleType.THREE_MONTHS), TimeScaleType.THREE_MONTHS));
		res.add(new FundInfoEntryChunk(getFundInfoEntry(TimeScaleType.SIX_MONTHS), TimeScaleType.SIX_MONTHS));
		res.add(new FundInfoEntryChunk(getFundInfoEntry(TimeScaleType.ONE_YEAR), TimeScaleType.ONE_YEAR));
		res.add(new FundInfoEntryChunk(getFundInfoEntry(TimeScaleType.FIVE_YEARS), TimeScaleType.FIVE_YEARS));
		return res;
	}
	
	private ArrayList<FundInfoEntry> getFundInfoEntry(TimeScaleType timeScaleType)	{
		// Sends dummy data for testing purposes. 
		// TODO Add method call to get real data, instead of random numbers
		ArrayList<FundInfoEntry> res = new ArrayList<FundInfoEntry>();
		double oldValue = 1000.0;
		int max = 200;
		Calendar calendar = Calendar.getInstance();	// Only used to generate random dates
		for(int i = 0; i < timeScaleType.getEntriesPerScale(); i++)	{
			double variation = (new Random().nextDouble() * (new Random().nextInt(max*2) - max));	// Returns a random variation between max and -max
			calendar.add(Calendar.DATE, 1); // Only used to generate random dates (adds one day each entry)
			res.add(new FundInfoEntry(calendar.getTime(), oldValue + variation));
			oldValue = oldValue + variation;
		}
		return res;
	}
	
	private boolean deposit(String username, double valueToDeposit) {
		double balance = getUpdatedBalanceByUser(username);
		PrintWriter pw;
		try {
			pw = new PrintWriter(new FileWriter(USER_MOVEMENTS + username + ".txt", true));
			balance += valueToDeposit;
			String s = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "," + valueToDeposit + "," + balance;
			pw.println();
			pw.print(s);
			pw.close();
			return true;
		} 	catch (FileNotFoundException e) {
			e.printStackTrace();
		}	catch (IOException e) {
			e.printStackTrace();
		}
		return false;
		/*
		 * TODO Catarina, update json with deposit data
		 */
	}
	
	private boolean withdraw(String username, double valueToWithdraw) {
		double balance = getUpdatedBalanceByUser(username);
		if(balance < valueToWithdraw)	{	// If the user doesn't have enough balance to withdraw
			return false;
		}
		PrintWriter pw;
		try {
			pw = new PrintWriter(new FileWriter(USER_MOVEMENTS + username + ".txt", true));
			balance -= valueToWithdraw;
			String s = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "," + -valueToWithdraw + "," + balance;
			pw.println();
			pw.print(s);
			pw.close();
			return true;
		} 	catch (FileNotFoundException e) {
			e.printStackTrace();
		}	catch (IOException e) {
			e.printStackTrace();
		}
		return false;
		
		/*
		 * TODO Catarina, update json with withdraw data
		 */
	}
	
	private void disconnect()	{
		try {
			clientSocket.close();
			objectIn.close();
			objectOut.close();
			online = false;
			javaServer.clearConnectionHandler(handlerID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ObjectOutputStream getObjectOut()	{
		return objectOut;
	}
	
	public ObjectInputStream getObjectIn()	{
		return objectIn;
	}
	
	private void consolePrint(String text)	{
		System.out.println("[Handler " + handlerID +  "] " + text);
	}
	
}
