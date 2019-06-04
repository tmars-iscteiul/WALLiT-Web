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

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import wallit_app.data.AckMessage;
import wallit_app.data.FundInfoEntry;
import wallit_app.data.FundInfoEntryChunk;
import wallit_app.data.MovementEntry;
import wallit_app.data.MovementEntryChunk;
import wallit_app.utilities.JsonHandler;
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
	
	public static final String USER_MOVEMENTS_LOCATION = "./userMovements/";
	
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
			s = new Scanner(new File(USER_MOVEMENTS_LOCATION + username + ".txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return lastValue;
		}
		while(s.hasNextLine())	{
			String nextLine = s.nextLine();
			if(!nextLine.startsWith("#") && !nextLine.isEmpty())	{	// If is not a comment
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
			s = new Scanner(new File(USER_MOVEMENTS_LOCATION + username + ".txt"));
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
		try {
			res.add(new FundInfoEntryChunk(JsonHandler.getTimescaledFundInfoList(TimeScaleType.ONE_MONTH), TimeScaleType.ONE_MONTH));
			res.add(new FundInfoEntryChunk(JsonHandler.getTimescaledFundInfoList(TimeScaleType.THREE_MONTHS), TimeScaleType.THREE_MONTHS));
			res.add(new FundInfoEntryChunk(JsonHandler.getTimescaledFundInfoList(TimeScaleType.SIX_MONTHS), TimeScaleType.SIX_MONTHS));
			res.add(new FundInfoEntryChunk(JsonHandler.getTimescaledFundInfoList(TimeScaleType.ONE_YEAR), TimeScaleType.ONE_YEAR));
			res.add(new FundInfoEntryChunk(JsonHandler.getTimescaledFundInfoList(TimeScaleType.FIVE_YEARS), TimeScaleType.FIVE_YEARS));
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	private boolean deposit(String username, double valueToDeposit) {
		// Updates the user's movement data file
		double currentUserBalance = getUpdatedBalanceByUser(username);
		PrintWriter pw;
		try {
			pw = new PrintWriter(new FileWriter(USER_MOVEMENTS_LOCATION + username + ".txt", true));
			currentUserBalance += valueToDeposit;
			String s = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "," + valueToDeposit + "," + currentUserBalance;
			pw.println(s);
			pw.close();
		} 	catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}	catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		// Updates the main fund data file
		try {
			double currentFundValue = JsonHandler.getLastFundInfoEntryValue();
			JsonHandler.addEntryToFundInfoDataFile(new FundInfoEntry(currentFundValue + valueToDeposit));
		} catch (JsonIOException | JsonSyntaxException | IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean withdraw(String username, double valueToWithdraw) {
		double currentUserBalance = getUpdatedBalanceByUser(username);
		if(currentUserBalance < valueToWithdraw)	{	// If the user doesn't have enough balance to withdraw
			return false;
		}
		// Updates the user's movement data file
		PrintWriter pw;
		try {
			pw = new PrintWriter(new FileWriter(USER_MOVEMENTS_LOCATION + username + ".txt", true));
			currentUserBalance -= valueToWithdraw;
			String s = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "," + -valueToWithdraw + "," + currentUserBalance;
			pw.println(s);
			pw.close();
		} 	catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}	catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		// Updates the main fund data file
		try {
			double currentFundValue = JsonHandler.getLastFundInfoEntryValue();
			JsonHandler.addEntryToFundInfoDataFile(new FundInfoEntry(currentFundValue - valueToWithdraw));
		} catch (JsonIOException | JsonSyntaxException | IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
		
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
