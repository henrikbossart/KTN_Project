<<<<<<< HEAD
package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import net.sf.json.JSONObject;

public class ThreadedTCPServer {
	private static ServerSocket serverSocket;
	private static HashMap<String, Socket> clients;

	public static void start() throws Exception {
		// create the server
		serverSocket = new ServerSocket(10000);
		clients = new HashMap<String, Socket>();

		while (true) {
			Socket client = serverSocket.accept();
			new Thread(new ClientHandler(client)).start();
		}
	}

	public static boolean addClient(String username, Socket clientSocket) {
		if (!clients.containsKey(username)) {
			clients.put(username, clientSocket);
			return true;
		} else {
			return false;
		}
	}

	public static void broadcast(JSONObject message) {
		// send JSONObject to all clients
	}
=======
package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import net.sf.json.JSONObject;

public class ThreadedTCPServer {
	private static ServerSocket serverSocket;
	private static HashMap<String, Socket> clients;
	private static ArrayList<JSONObject> history;

	public static void start() throws Exception {
		// create the server
		serverSocket = new ServerSocket(10000);
		clients = new HashMap<String, Socket>();
		history = new ArrayList<JSONObject>();

		while (true) {
			Socket client = serverSocket.accept();
			new Thread(new ClientHandler(client)).start();
		}
	}

	public static boolean addClient(String username, Socket clientSocket) {
		if (!clients.containsKey(username)) {
			clients.put(username, clientSocket);
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean removeClient(String username, Socket clientSocket) {
		if (clients.containsKey(username)) {
			clients.remove(username);
			return true;
		} else {
			return false;
		}
	}

	public static void broadcast(JSONObject message) {
		for (String key : clients.keySet()) {
			key.sendMessage(message);
		}
		addHistory(message.getString("sender"), message);
	}
	
	private static void addHistory(String sender, JSONObject message) {
		history.add(message);
	}
>>>>>>> master
}