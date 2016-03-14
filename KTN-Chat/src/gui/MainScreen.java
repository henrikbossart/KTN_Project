package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import client.Client;

public class MainWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    private JButton send;
    private JTextField input;
    private JTextArea chatLog;
    Client client;

    public MainWindow() {
        this.setTitle("Chat");
        this.setLayout(new BorderLayout(5, 5));
        this.setMinimumSize(new Dimension(400, 300));
        this.setLocationRelativeTo(null);

        this.getContentPane().add(chatLog(), BorderLayout.CENTER);
        this.getContentPane().add(soutPanel(), BorderLayout.SOUTH);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

        this.client = new Client();


    }

    public void log(String msg) {
        chatLog.setText(chatLog.getText() + msg + "\n");
    }

    private JPanel soutPanel() {
        JPanel panel = new JPanel(new BorderLayout(5,5));
        send = new JButton(actions);
        send.setText("sending...");
        input = new JTextField();
        panel.add(input, BorderLayout.CENTER);
        panel.add(send, BorderLayout.EAST);
        return panel;
    }

    private JTextArea chatLog() {
        chatLog = new JTextArea();
        chatLog.setLineWrap(true);
        chatLog.setWrapStyleWord(true);
        chatLog.setEditable(false);
        return chatLog;
    }

    private Action actions = new AbstractAction() {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == send && input.getText().length() > 0) {
                Chat.sendMessage(input.getText());
                Chat.log("%nick%" + input.getText());
                input.setText("");
            }
        }
    };
}