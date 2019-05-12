package wallit_app.javaserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import wallit_app.data.AckMessage;
import wallit_app.data.MovementEntry;
import wallit_app.data.MovementEntryChunk;


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
	public static final String USER_MOVEMENTS = "./userMovements/";
	
	
	// TODO Add major command handling to this handler
	public ConnectionHandler(Socket s, int id)	{
		try {
			online = true;
			handlerID = id;
			clientSocket = s;
			objectIn = new ObjectInputStream(clientSocket.getInputStream());
			objectOut = new ObjectOutputStream(clientSocket.getOutputStream());
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
				try {
					// Simulating delay on connection
					sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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
	// TODO Create a standardized ACK data class (both in this server and android client)
	private void handleUserRequest(String receivedArgs[]) throws IOException	{
		AckMessage messageToSend = null;
		switch(receivedArgs[0])	{
		case "REQUEST_MOVEMENT_HISTORY":
			// Change default to the username sent by the android client
			 messageToSend = new AckMessage("MSG_SEND_DATA", getMovementEntriesByUser("default"));
			break;
		case "REQUEST_LOGIN":
			// Logs in any user (for now)
			messageToSend = new AckMessage("MSG_ACK_POSITIVE", null);
			consolePrint(receivedArgs[1] + " authenticated.");
			break;
		default:
			// Denies any other request for now
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
			
			// If list is full
			if(aux.addMovementEntry(new MovementEntry(args))) {
				// Add full chunk to the main list, create a new chunk and add the last entry to the new chunk
				res.add(aux);
				aux = new MovementEntryChunk();
				aux.addMovementEntry(new MovementEntry(args));
			}
		}
		res.add(aux);
		s.close();
		return res;
	}
	
	private void disconnect()	{
		try {
			clientSocket.close();
			objectIn.close();
			objectOut.close();
			online = false;
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
