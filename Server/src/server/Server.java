package server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Server extends JFrame {
	private static final long serialVersionUID = 1L;

	private ServerSocket server;
	private Socket client;
	private JTextArea log;
	private ClientHandler handler;
	private boolean running;
	private static HashMap<String, Thread> threads;
	private static ArrayList<ClientHandler> handlers;
	private static ArrayList<JSONObject> messageHistory;

	private Server() {
		threads = new HashMap<String, Thread>();
		handlers = new ArrayList<ClientHandler>();
		messageHistory = new ArrayList<JSONObject>();
		try {
			server = new ServerSocket(7500);
			running = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		createGUI();
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				running = false;
			}
		});
		run();
	}

	private void run() {
		while (running) {
			try {
				client = server.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.append(("Client connected with IP " + client.getInetAddress().getHostAddress()) + "\n");
			handler = new ClientHandler(client, log);
			handlers.add(handler);
			Thread thread = new Thread(handler);
			handler.setThread(thread);
			thread.start();
		}
		try {
			for(ClientHandler clientHandler : handlers) {
				clientHandler.closeConnection();
			}
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createGUI() {
		this.setTitle("Server");
		this.setLayout(new BorderLayout(5, 5));
		this.setMinimumSize(new Dimension(300, 200));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		log = new JTextArea();
		log.setEditable(false);
		log.setLineWrap(true);
		log.setWrapStyleWord(true);
		DefaultCaret caret = (DefaultCaret) log.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scrollPane = new JScrollPane(log);

		this.getContentPane().add(scrollPane, BorderLayout.CENTER);

		this.setVisible(true);
		this.pack();
	}

	public static void addThread(Thread thread, String name) {
		threads.put(name, thread);
	}

	public static void removeThread(String name, ClientHandler handler) {
		if (threads.containsKey(name)) {
			Thread thread = threads.get(name);
			threads.remove(name);
			handlers.remove(handler);
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static boolean checkUsernam(String name) {
		return threads.containsKey(name);
	}

	public static void broadcast(JSONObject message) {
		messageHistory.add(message);
		for (ClientHandler handler : handlers) {
			handler.sendMessage(message);
		}
	}

	public static void getMessageHistory(ClientHandler handler) {
		JSONObject msg = new JSONObject();
		String timestamp = LocalDateTime.now().toString();
		if (messageHistory.size() > 0) {
			JSONArray history = new JSONArray();
			msg.put("timestamp", timestamp);
			msg.put("sender", "server");
			msg.put("response", "history");
			for (JSONObject message : messageHistory) {
				history.add(message);
			}
			msg.put("content", history);
		} else {
			msg.put("timestamp", timestamp);
			msg.put("sender", "server");
			msg.put("response", "historyEmpty");
			msg.put("content", "none");
		}
		handler.sendMessage(msg);
	}

	public static void getUsers() {
		JSONObject msg = new JSONObject();
		JSONArray names = new JSONArray();
		String timestamp = LocalDateTime.now().toString();
		msg.put("timestamp", timestamp);
		msg.put("sender", "server");
		msg.put("response", "names");
		for (String name : threads.keySet()) {
			names.add(name);
		}
		msg.put("content", names);
		for (ClientHandler handler : handlers) {
			handler.sendMessage(msg);
		}
	}

	public static void main(String[] args) {
		Server serverApp = new Server();
	}
}