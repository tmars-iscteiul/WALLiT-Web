package wallit.javaserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;


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
	
	
	// TODO Add major command handling to this server
	public ConnectionHandler(Socket s, int id)	{
		try {
			online = true;
			handlerID = id;
			clientSocket = s;
			objectIn = new ObjectInputStream(clientSocket.getInputStream());
			objectOut = new ObjectOutputStream(clientSocket.getOutputStream());
			consolePrint("Handler online.");
			String receivedData = (String)objectIn.readObject();
			consolePrint("Received login from user: " + receivedData);
			consolePrint("Sending ack to client");
			try {
				// Simulating delay on connection
				sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			objectOut.writeObject("MSG_ACK_POSITIVE");
            objectOut.reset();
            consolePrint("Client authenticated.");
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// TODO Add connection timeout (closes the socket, informing the client (somehow) that the connection has timed out).
	
	public void run()	{
		while(online)	{
			try {
				String receivedData = (String)objectIn.readObject();
				consolePrint("Received data from user. It reads: ");
				System.out.println(receivedData);
				consolePrint("Sending ack to client");
				try {
					// Simulating delay on connection
					sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// Sends random acks (positive or negative) for now. TODO Change this (ofc)
				if(new Random().nextBoolean())	{
					objectOut.writeObject("MSG_ACK_POSITIVE");
				}	else	{
					objectOut.writeObject("MSG_ACK_NEGATIVE");
				}
	            objectOut.reset();
			} catch (IOException | ClassNotFoundException e) {
				consolePrint("Problem communicating with client, terminating the connection.");
				disconnect();
			}
		}
	}
	
	public ObjectOutputStream getObjectOut()	{
		return objectOut;
	}
	
	public ObjectInputStream getObjectIn()	{
		return objectIn;
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
	
	private void consolePrint(String text)	{
		System.out.println("[Handler " + handlerID +  "] " + text);
	}
	
}
