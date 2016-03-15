package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import net.sf.json.JSONObject;

public class ThreadedTCPServer implements Runnable{
    private static ServerSocket serverSocket;
    private static HashMap<String, ClientHandler> clients;
    private static ArrayList<JSONObject> history;

    public void run() {
        // create the server
        try {
            serverSocket = new ServerSocket(10000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        clients = new HashMap<String, ClientHandler>();
        history = new ArrayList<JSONObject>();

        while (true) {
            Socket client = null;
            try {
                client = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            new Thread(new ClientHandler(client)).start();
        }
    }

    public static boolean addClient(String username, ClientHandler clientSocket) {
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
            ClientHandler client = clients.get(key);
            client.sendMessage(message);

        }
        addHistory(message.getString("sender"), message);
    }

    private static void addHistory(String sender, JSONObject message) {
        history.add(message);
    }
    public static String getNames() {
        String output = "";
        for (String name : clients.keySet()) {
            output += clients.get(name) + "\n";
        }
        return output;
    }
    public static void sendHistory(ClientHandler client) {
        for (JSONObject message : history) {
            message.put("response", "history");
            client.sendMessage(message);
        }
    }
    public static String getHelp() {
        String help = "Heeelp, Heeelp, Heeelp";
        return help;
    }

    public void stop() {
        try {

            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}