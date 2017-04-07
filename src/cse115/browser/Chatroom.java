package cse115.browser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * DO NOT TOUCH THIS FILE UNLESS YOU KNOW WHAT YOURE DOING!
 * Thanks :D
 * @author dan
 *
 */
public class Chatroom {

    String      appName     = "CSE 115 Chat";
    Chatroom    mainGUI;
    JFrame      newFrame    = new JFrame(appName);
    JButton     sendMessage;
    JTextField  messageBox;
    JTextArea   chatBox;
    JTextField  usernameChooser;
    JFrame      preFrame;
    String      username;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Chatroom mainGUI = new Chatroom();
                mainGUI.preDisplay();
            }
        });
    }

    /**
     * Set up the "Pick Username" panel.
     */
    public void preDisplay() {
        newFrame.setVisible(false);
        preFrame = new JFrame(appName);
        usernameChooser = new JTextField(15);
        JLabel chooseUsernameLabel = new JLabel("Pick a username:");
        JButton enterServer = new JButton("Enter Chat Server");
        enterServer.addActionListener(new enterServerButtonListener());
        JPanel prePanel = new JPanel(new GridBagLayout());

        GridBagConstraints preRight = new GridBagConstraints();
        preRight.insets = new Insets(0, 0, 0, 10);
        preRight.anchor = GridBagConstraints.EAST;
        GridBagConstraints preLeft = new GridBagConstraints();
        preLeft.anchor = GridBagConstraints.WEST;
        preLeft.insets = new Insets(0, 10, 0, 10);
        // preRight.weightx = 2.0;
        preRight.fill = GridBagConstraints.HORIZONTAL;
        preRight.gridwidth = GridBagConstraints.REMAINDER;

        prePanel.add(chooseUsernameLabel, preLeft);
        prePanel.add(usernameChooser, preRight);
        preFrame.add(BorderLayout.CENTER, prePanel);
        preFrame.add(BorderLayout.SOUTH, enterServer);
        preFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        preFrame.setLocationRelativeTo(null);
        preFrame.setSize(600, 300);
        preFrame.setVisible(true);
    }

    /**
     * Set up the main chat room panel
     */
    public void display() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.CYAN);
        southPanel.setLayout(new GridBagLayout());

        messageBox = new JTextField(30);
        messageBox.requestFocusInWindow();

        sendMessage = new JButton("Send Message");
        sendMessage.addActionListener(new sendMessageButtonListener());

        chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
        chatBox.setLineWrap(true);

        mainPanel.add(new JScrollPane(chatBox), BorderLayout.CENTER);

        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.LINE_START;
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 512.0D;
        left.weighty = 1.0D;

        GridBagConstraints right = new GridBagConstraints();
        right.insets = new Insets(0, 10, 0, 0);
        right.anchor = GridBagConstraints.LINE_END;
        right.fill = GridBagConstraints.NONE;
        right.weightx = 1.0D;
        right.weighty = 1.0D;

        southPanel.add(messageBox, left);
        southPanel.add(sendMessage, right);

        mainPanel.add(BorderLayout.SOUTH, southPanel);

        newFrame.add(mainPanel);
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setSize(600, 300);
        chatBox.setText("Use '.clear' to clear your messages.\n");
        newFrame.setLocationRelativeTo(null);
        newFrame.setVisible(true);
        messageBox.requestFocusInWindow();
        updateChat();
    }

    
    private void updateChat(){
    		for (String message : Lab9.getMessages()) {
			chatBox.append(message + "\n");
		}
    }
    
    /**
     * ActionListener class for when a message is sent.
     */
    class sendMessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
        	// Append all the messages to the chat room
        	updateChat();
        	
        	// If the message length is too short.
            if (messageBox.getText().length() < 5) {
                chatBox.append("Messages must be at least 5 characters long.\n");
                
            // If they want to clear the messages
            } else if (messageBox.getText().equals(".clear")) {
                chatBox.setText("Cleared all messages\n");
                messageBox.setText("");
                
            // Try to send the message
            } else {
            	// If the message successfully sends, log it.
            	String messageToSend = messageBox.getText().replace(",", "");
            	if (Lab9.sendMessage(messageToSend, username)) {
            		chatBox.setText("Use '.clear' to clear your messages.\n");
            		// Append all the messages to the chat room again
            		updateChat();
            	} else {
            		chatBox.append("Could not send the message.");
            	}
                messageBox.setText("");
            }
            messageBox.requestFocusInWindow();
        }
    }

    class enterServerButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            username = usernameChooser.getText().replace(",", "-");
            if (username.length() < 5) {
                System.out.println("No!");
            } else {
                preFrame.setVisible(false);
                display();
            }
        }

    }
}
