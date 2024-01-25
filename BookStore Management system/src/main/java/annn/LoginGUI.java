package annn;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI().createAndShowGUI());
    }

    private void createAndShowGUI() {
        frame = new JFrame("Employee Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (authenticate()) {
                    frame.dispose(); // Close login frame
                    SwingUtilities.invokeLater(() -> new BookstoreGUI().createAndShowMainGUI()); // Open main GUI
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(loginButton);

        frame.getContentPane().add(BorderLayout.CENTER, panel);

        // Set frame size and center it on the screen
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null); // Center the frame on the screen

        // Set the frame to be non-resizable
        frame.setResizable(false);

        frame.setVisible(true);
    }

    private boolean authenticate() {
        // Implement authentication logic here
        // For simplicity, let's assume username: "admin" and password: "password"
        String enteredUsername = usernameField.getText();
        char[] enteredPasswordChars = passwordField.getPassword();
        String enteredPassword = new String(enteredPasswordChars);

        return enteredUsername.equals("admin") && enteredPassword.equals("password");
    }
}
