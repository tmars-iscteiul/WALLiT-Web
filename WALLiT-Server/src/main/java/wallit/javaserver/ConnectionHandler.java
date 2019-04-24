package wallit.javaserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 * @author skner
 *
 */
public class ConnectionHandler extends Thread {

	private Socket clientSocket;
	private ObjectInputStream objectIn;
	private ObjectOutputStream objectOut;
	private int handlerID;
	
	public ConnectionHandler(Socket s, int id)	{
		try {
			handlerID = id;
			consolePrint("Handler online.");
			objectIn = new ObjectInputStream(clientSocket.getInputStream());
			objectOut = new ObjectOutputStream(clientSocket.getOutputStream());
			run();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Stablish the connection. Run the thread.
	}
	
	public void run()	{
		// Wait for client requests and direct them to the main processing thread
		try {
			String receivedData = (String)objectIn.readObject();
			consolePrint("Received data from user. It reads: ");
			System.out.println(receivedData);
			consolePrint("Ending user data print.");
		} catch (IOException | ClassNotFoundException e) {
			consolePrint("Problem with receiving client's data.");
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
