package main;


import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by henrikbossart on 16.03.2016.
 */
public class Client extends JFrame {
    private static final long serialVersionUID = 2L;

    private JButton send;
    private JButton login;
    private JButton logout;
    private JTextField username;
    private JTextField address;
    private JTextArea chatLog;
    private JTextField inputField;
    private JTextArea users;

    private Socket client;
    private MessageReciever messageReciever;
    private MessageParser messageParser;
    private PrintWriter outgoing;
    private boolean connected = false;
    private String ipaddress;
    private int port;
    private Thread thread;
    private boolean online = false;


    public Client(int port) {
        this.setTitle("Chat Application");
        this.setMinimumSize(new Dimension(500,500));
        this.setLayout(new BorderLayout(5,5));
        this.setLocationRelativeTo(null);

        chatLog = new JTextArea();
        chatLog.setLineWrap(true);
        chatLog.setWrapStyleWord(true);
        chatLog.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatLog);
        DefaultCaret caret = (DefaultCaret) chatLog.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        this.getContentPane().add(scrollPane, BorderLayout.CENTER);
        this.getContentPane().add(inputSend(), BorderLayout.SOUTH);
        this.getContentPane().add(loginPanel(), BorderLayout.NORTH);
        this.getContentPane().add(users(), BorderLayout.WEST);


        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.pack();

        this.username.setText("admin");
        this.address.setText("78.91.31.24");

        this.messageParser = new MessageParser(this.chatLog, this.users, this);
        this.port = port;

    }

    public MessageReciever getMessageReciever() {
        return messageReciever;
    }

    public void disconnect() throws IOException {
        client.close();
    }

    public JPanel inputSend() {
        JPanel panel = new JPanel(new BorderLayout(5,5));
        send = new JButton(actions);
        send.setText("Send");
        inputField = new JTextField();
        inputField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    if (inputField.getText().split(" ")[0].equals("help")) {
                        if (!online) {
                            chatLog.append("Welcome to help.\n Enter your username and the server IP. Press 'login' to log in.\n Type your message in the text field and hit 'send' or the return key on your keyboard to send the message.\n Press 'logout' to log out.\n");
                            inputField.setText("");
                        } else {
                            sendMessage(createPayload("help", "none"));
                            inputField.setText("");
                        }
                    } else if (inputField.getText().split(" ")[0].equals("names")) {
                        users.setText("");
                        sendMessage(createPayload("names", "none"));
                        inputField.setText("");
                    } else {
                        sendMessage(createPayload("msg", inputField.getText()));
                        inputField.setText("");
                    }
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        panel.add(inputField, BorderLayout.CENTER);
        panel.add(send, BorderLayout.EAST);
        return panel;
    }

    public JPanel loginPanel() {
        JPanel panel = new JPanel(new GridLayout(3,2));
        JLabel usernameLabel = new JLabel("Username");
        JLabel addressLabel = new JLabel("IP-Address");
        login = new JButton(actions);
        login.setText("Login");
        logout = new JButton(actions);
        logout.setText("Logout");
        username = new JTextField(10);
        address = new JTextField(15);
        panel.add(usernameLabel);
        panel.add(addressLabel);
        panel.add(username);
        panel.add(address);
        panel.add(login);
        panel.add(logout);
        return panel;
    }

    private JPanel users() {
        JPanel panel = new JPanel(new BorderLayout(5,5));
        users = new JTextArea(15,10);
        users.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(users);
        panel.add(scrollPane);
        return panel;
    }

    public void sendMessage(JSONObject payload) {
        outgoing.println(payload.toJSONString());
        outgoing.flush();
    }



    public static void errorLogger(String error) {
        System.out.println(error);
    }
    public JSONObject createPayload(String request, String content) {
        JSONObject payload = new JSONObject();
        payload.put("request", request);
        payload.put("content", content);
        return payload;

    }

    public void run() {
        connected = true;
        while (connected){}

        try {
            thread.join();
            client.close();
            online =false;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onWindowClose() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sendMessage(createPayload("logout", "none"));
                connected = false;
                messageReciever.disconnect();
                super.windowClosing(e);
            }
        });
    }

    private Action actions = new AbstractAction() {
        private static final long serialVersionUID = 2L;
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == send) {
                if (inputField.getText().split(" ")[0].equals("help")) {
                    if (!online) {
                        chatLog.append("Welcome to help.\n Enter your username and the server IP. Press 'login' to log in.\n Type your message in the text field and hit 'send' or the return key on your keyboard to send the message.\n Press 'logout' to log out.\n");
                        inputField.setText("");
                    } else {
                        sendMessage(createPayload("help", "none"));
                        inputField.setText("");
                    }
                } else if (inputField.getText().split(" ")[0].equals("names")) {
                    users.setText("");
                    sendMessage(createPayload("names", "none"));
                    inputField.setText("");
                } else {
                    sendMessage(createPayload("msg", inputField.getText()));
                    inputField.setText("");
                }
            }
            if (e.getSource() == login && username.getText().length() > 3 && address.getText().length() > 4) {
                ipaddress = address.getText();
                try {
                    client = new Socket(ipaddress, port);
                    online = true;
                    onWindowClose();
                    outgoing = new PrintWriter(client.getOutputStream());
                    messageReciever = new MessageReciever(client, messageParser);
                    thread = new Thread(messageReciever);
                    thread.start();
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                sendMessage(createPayload("login", username.getText()));
            }
            if (e.getSource() == logout) {
                sendMessage(createPayload("logout", "none"));
                connected = false;
                messageReciever.disconnect();
                users.setText("");
            }
        }
    };

    public static void main(String[] args) {
        Client client = new Client(7500);
        client.run();
    }
}
