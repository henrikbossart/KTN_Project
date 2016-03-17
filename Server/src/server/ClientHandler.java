package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;

import javax.swing.JTextArea;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ClientHandler implements Runnable {

	private Socket client;
	private JTextArea log;
	private PrintWriter out;
	private BufferedReader in;
	private JSONObject message;
	private String username;
	private JSONParser jParser = new JSONParser();
	private boolean connection;
	private boolean loggedIn = false;
	private Thread thread;

	public ClientHandler(Socket client, JTextArea log) {
		this.client = client;
		connection = true;
		this.log = log;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public void run() {

		BufferedReader read = new BufferedReader(in);
		while (connection) {
			String text = "";
			try {
				text = read.readLine();
				message = (JSONObject) jParser.parse(text);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			parseMessage();
		}
	}

	private void parseMessage() {
		if (!loggedIn) {
			switch (message.get("request").toString()) {
			case "login":
				username = message.get("content").toString();
				logger();
				if (!isAlphaNumeric(username)) {
					sendMessage(createMessage("server", "error", "Invalid username"));
					closeConnection();
				} else if (Server.checkUsernam(username)) {
					sendMessage(createMessage("server", "error", "Username taken"));
				} else {
					sendMessage(createMessage("server", "info", "Login successful"));
					loggedIn = true;
					Server.addThread(this.thread, username);
					Server.getMessageHistory(this);
					Server.getUsers();
				}
				break;
			case "help":
				logger();
				sendMessage(createMessage("server", "info", "Welcome to help.\nEnter your username and the server IP. Press 'login' to log in.\nType your message in the text field and hit 'send' or the return key on your keyboard to send the message.\nPress 'logout' to log out.\n"));
				break;
			default:
				sendMessage(createMessage("server", "error", "unknown command"));
				break;
			}
		} else {
			switch (message.get("request").toString()) {

			case "logout":
				logger();
				sendMessage(createMessage("server", "info", "closing connection"));
				loggedIn = false;
				closeConnection();
				break;
			case "msg":
				Server.broadcast(createMessage(username, "msg", message.get("content").toString()));
				logger();
				break;
			case "names":
				Server.getUsers();
				logger();
				break;
			case "help":
				logger();
				sendMessage(createMessage("server", "info", "Welcome to help.\nEnter your username and the server IP. Press 'login' to log in.\nType your message in the text field and hit 'send' or the return key on your keyboard to send the message.\nPress 'logout' to log out.\n"));
				break;
			default:
				sendMessage(createMessage("server", "error", "unknown command"));
				break;
			}
		}
	}

	public void sendMessage(JSONObject newMessage) {
		out.write(newMessage.toJSONString() + "\n");
		out.flush();
	}

	private JSONObject createMessage(String username, String responseType, String content) {
		JSONObject newMessage = new JSONObject();
		String timestamp = LocalDateTime.now().toString();
		newMessage.put("timestamp", timestamp);
		newMessage.put("sender", username);
		newMessage.put("response", responseType);
		newMessage.put("content", content);
		return newMessage;
	}

	private boolean isAlphaNumeric(String s) {
		String pattern = "^[a-zA-Z0-9]*$";
		if (s.matches(pattern)) {
			return true;
		}
		return false;
	}

	private void logger() {
		String msg = "";
		msg += username + ">> Request: " + message.get("request") + " -- Content: " + message.get("content") + "\n";
		log.append(msg);
	}

	public void closeConnection() {
		try {
			Server.removeThread(username, this);
			Server.getUsers();
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			connection = false;
		}
	}
}