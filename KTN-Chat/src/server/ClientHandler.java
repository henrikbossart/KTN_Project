package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;

import net.sf.json.JSONObject;

public class ClientHandler implements Runnable {

	private Socket client;
	private ObjectInputStream incoming;
	private ObjectOutputStream outgoing;

	public ClientHandler(Socket client) {
		this.client = client;
		try {
			this.outgoing = new ObjectOutputStream(client.getOutputStream());
			this.incoming = new ObjectInputStream(client.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		boolean connected = true;
		while (connected) {
			// get the incoming message
			try {
				JSONObject clientMessage = (JSONObject) incoming.readObject();
				String username = clientMessage.getString("content");
				// act based on content of JSONObject
				switch (clientMessage.getString("request")) {
					case "login":
						if (ThreadedTCPServer.addClient(username, this)) {
							// send reply: successful connection
							sendMessage(createMessage("server","info", "Loging successful"));
						} else {
							// send reply: unsuccessful connection, and terminate connection
							sendMessage(createMessage("server","error", "Username taken"));
							connected = false;
						}
						break;

					case "logout":
						// remove client from list of active users
						ThreadedTCPServer.removeClient(username, this.client);
						// terminate connection
						connected = false;
						break;

					case "msg":
						// tell server to broadcast message
						ThreadedTCPServer.broadcast(clientMessage);
						break;

					case "names":
						// send reply with list of user names
						
						// ThreadedTCPServer needs a method to format the list of client names that client handler can use
						
						break;

					case "help":
						// send reply with help text
						
						// ThreadedTCPServer needs a method that returns a string with help text for how to use the chat program
						break;

					default:
						// send reply with unknown command error
						break;
				}

			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendMessage(JSONObject message) {
		try {
			outgoing.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private JSONObject createMessage(String username, String responseType, String content) {
		JSONObject message = new JSONObject();
		String timestamp = LocalDateTime.now().toString();
		message.put("timestamp", timestamp);
		message.put("sender", username);
		message.put("response", responseType);
		message.put("content", content);
		return message;
	}
}