package wallit_app.javaserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
				consolePrint("Received data from client: \"" + receivedData + "\".");
				handleUserRequest(receivedData.split(","));
			} catch (IOException | ClassNotFoundException e) {
				// TODO Add a way to know what happened with the connection
				consolePrint("Terminating the connection: reason to be determinded.");
				disconnect();
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Message standard sent by android client
	 * Arg1: COMMAND
	 * Arg2: USERNAME
	 * Arg3: EXTRA DATA
	 */
	private void handleUserRequest(String receivedArgs[]) throws IOException	{
		AckMessage messageToSend = null;
		switch(receivedArgs[0])	{
		case "REQUEST_MOVEMENT_HISTORY":
			// TODO Change "default" to the logged in username
			 messageToSend = new AckMessage("MSG_ACK_USER_DATA", getMovementEntriesByUser("default"));
			break;
		case "REQUEST_FUND_INFO":
			messageToSend = new AckMessage("MSG_ACK_FUND_DATA", getFundInfoEntryChunks());
			break;
		case "REQUEST_LOGIN":
			// Logs in any user (for now)
			messageToSend = new AckMessage("MSG_ACK_POSITIVE", null);
			username = receivedArgs[1];
			consolePrint(username + " authenticated.");
			break;
		case "REQUEST_DEPOSIT":
			System.out.println("User wants to deposit: " + receivedArgs[1]);
			deposit(username, Double.parseDouble(receivedArgs[1]));
			// TODO Sends negative ack because the deposit operation isn't fully implemented
			messageToSend = new AckMessage("MSG_ACK_NEGATIVE", null);
			break;
		case "REQUEST_WITHDRAW":
			System.out.println("User wants to withdraw: " + receivedArgs[1]);
			withdraw(username, Double.parseDouble(receivedArgs[1]));
			// TODO Sends negative ack because the withdraw operation isn't fully implemented
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

	private ArrayList<MovementEntryChunk> getMovementEntriesByUser(String username)	{
		Scanner s;
		try {
			s = new Scanner(new File(USER_MOVEMENTS + username + ".txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		ArrayList<MovementEntryChunk> res = new ArrayList<MovementEntryChunk>();
		MovementEntryChunk aux = new MovementEntryChunk();
		while (s.hasNextLine()) {
			String nextLine = s.nextLine();
			String[] args = nextLine.split(",");
			if(!nextLine.startsWith("#"))	{	// If is not a comment
				if(!aux.addMovementEntry(new MovementEntry(args))) {	// If list is full
					// Add full chunk to the main list, create a new chunk and add the last entry to the new chunk
					res.add(aux);
					aux = new MovementEntryChunk();
					aux.addMovementEntry(new MovementEntry(args));
				}
			}
		}
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
	
	private void deposit(String username, double valueToDeposit) {
		/*
		 * This method will do two main things:
		 * - Update user's movement file, adding a new line to it, with the calculated balance.
		 * - Update the fund's data file, calculating for each time scale what value should be added and in what date (based on all the rules)
		 * 
		 * method updateFile, is called to update fund data files.
		 */
		
		/*
		 * Open file, check last balance, add new line with deposit entry with updated balance
		 */
		
		/*
		 * TODO Catarina, update json with deposit data
		 */
	}
	
	private void withdraw(String username, double valueToWithdraw) {
		/*
		 * This method will do two main things:
		 * - Update user's movement file, adding a new line to it, with the calculated balance.
		 * - Update the fund's data file, calculating for each time scale what value should be added and in what date (based on all the rules)
		 * 
		 * method updateFile, is called to update fund data files.
		 */
		
		/*
		 * Open file, check last balance, add new line with withdraw entry with updated balance
		 */
		
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
