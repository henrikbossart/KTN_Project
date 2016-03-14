package gui;

import client.Client;

import javax.swing.*;
import java.awt.*;

/**
 * Created by henrikbossart on 14.03.2016.
 */
public class ChatWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    private JButton send;
    private JTextField input;
    private JTextArea chatLog;
    private JTextArea users;
    Client client;

    public ChatWindow() {
        this.setTitle("Chat");
        this.setMinimumSize(new Dimension(400, 300));
        this.setLayout(new BorderLayout(5, 5));
        this.setLocationRelativeTo(null);

        this.getContentPane().add(chatLog(), BorderLayout.CENTER);
        this.getContentPane().add(inputField(), BorderLayout.SOUTH);
        this.getContentPane().add(usersField(), BorderLayout.WEST);
        this.getContentPane().add(optionsField(), BorderLayout.EAST);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

        this.client = new Client();
    }

    public JTextArea chatLog() {
        chatLog = new JTextArea();
        chatLog.setLineWrap(true);
        chatLog.setWrapStyleWord(true);
        chatLog.setEditable(false);
        return chatLog;
    }

    public JPanel inputField() {
        JPanel panel = new JPanel(new BorderLayout(5,5));
        send = new JButton(actions);
        send.setText("Send");
        input = new JTextField();
        panel.add(input, BorderLayout.CENTER);
        panel.add(send, BorderLayout.EAST);
        return panel;
    }

    public JTextArea usersField() {

    }
}
