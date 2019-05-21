package wallit_app.javaserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import wallit_app.main.Main;


/**
 * @author skner
 *
 */
@Service
public class JavaServer extends Thread	{
	

	private ServerSocket serverSocket;
	private ArrayList<ConnectionHandler> connectedClients;
	// TODO Remove disconnected clients from list, clear resource space
	private boolean running;
	
	public JavaServer()	{
		try {
			serverSocket = new ServerSocket(Main.javaserverPort);
			System.out.println("[JavaServer] Server launched on " + InetAddress.getLocalHost().getHostAddress() + ":" + Main.javaserverPort);
			running = true;
			connectedClients = new ArrayList<ConnectionHandler>();
			start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()	{
		// This thread will ONLY await client connection attempts, setting up a connection handler for each connected client.
		System.out.println("[JavaServer] Awaiting connections...");
		while(running)	{
			try {
				Socket clientSocket = serverSocket.accept();	// Locked until a new client attempts to connect
				System.out.println("[JavaServer] Connection accepted: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
				connectedClients.add(new ConnectionHandler(clientSocket, connectedClients.size(), this));
				connectedClients.get(connectedClients.size()-1).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void clearConnectionHandler(int id)	{
		connectedClients.remove(id-1);
	}
	
	public boolean isOnline()	{
		return running;
	}
	
	public int getConnectedClients()	{
		return connectedClients.size();
	}
}
