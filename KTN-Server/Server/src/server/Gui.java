package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by henrikbossart on 15.03.2016.
 */
public class Gui extends JFrame{
    private static final long serialVersionUID = 2L;

    private JTextArea status;
    private JButton startServer;
    private JButton stopServer;
    private ThreadedTCPServer server;

    public Gui() {
        this.setTitle("Server");
        this.setMinimumSize(new Dimension(600, 400));
        this.setLayout(new BorderLayout(5, 5));
        this.setLocationRelativeTo(null);

        status = new JTextArea();
        status.setLineWrap(true);
        status.setWrapStyleWord(true);
        status.setEditable(false);

        this.getContentPane().add(status, BorderLayout.CENTER);
        this.getContentPane().add(faceInter(), BorderLayout.SOUTH);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.pack();

    }

    public JPanel faceInter() {
        JPanel panel = new JPanel(new BorderLayout(5,5));

        startServer = new JButton(actions);
        startServer.setText("Start!!!!!");
        stopServer = new JButton(actions);
        stopServer.setText("Stop!!!!!!");

        panel.add(startServer, BorderLayout.WEST);
        panel.add(stopServer, BorderLayout.EAST);
        return panel;
    }

    private Action actions = new AbstractAction() {
        private static final long serialVersionUID = 2L;
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == startServer) {
                server = new ThreadedTCPServer();
                new Thread(server).start();
            }
            if (e.getSource() == stopServer) {
                server.stop();

            }
        }
    };

    public static void main(String[] args) {
        Gui gui = new Gui();
    }

}
