import javax.swing.*;
import java.awt.*;

public class LoginGUI extends JFrame {
    private final LoginController loginController;
    private final String role;
    private final boolean isRegistering;

    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JTextField emailField = new JTextField(15);
    private final JTextField nameField = new JTextField(15);
    private final JLabel errorLabel = new JLabel(" ");
    private final JButton submitBtn;
    private final JButton toggleBtn;
    private boolean inRegisterMode = false;

    public LoginGUI(String role) {
        this(role, new LoginController());
    }

    public LoginGUI(String role, LoginController loginController) {
        super(role + " Login");
        this.role = role;
        this.loginController = loginController;
        this.isRegistering = false;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridy = 1; gbc.gridx = 0;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridy = 3; gbc.gridx = 0;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);
        
        emailField.setVisible(false);
        nameField.setVisible(false);

        errorLabel.setForeground(Color.RED);
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        formPanel.add(errorLabel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        submitBtn = new JButton("Login");
        toggleBtn = new JButton("Switch to Register");
        
        submitBtn.addActionListener(e -> handleSubmit());
        toggleBtn.addActionListener(e -> toggleMode());

        buttonPanel.add(toggleBtn);
        buttonPanel.add(submitBtn);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void toggleMode() {
        inRegisterMode = !inRegisterMode;
        emailField.setVisible(inRegisterMode);
        nameField.setVisible(inRegisterMode);
        submitBtn.setText(inRegisterMode ? "Register" : "Login");
        toggleBtn.setText(inRegisterMode ? "Switch to Login" : "Switch to Register");
        errorLabel.setText(" ");
        revalidate();
        repaint();
    }

    private void handleSubmit() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            errorLabel.setText("Username and password required.");
            return;
        }

        if (inRegisterMode) {
            String email = emailField.getText().trim();
            String name = nameField.getText().trim();
            if (email.isEmpty() || name.isEmpty()) {
                errorLabel.setText("Email and Name required for registration.");
                return;
            }
            if (loginController.registerUser(user, pass, role, email, name)) {
                JOptionPane.showMessageDialog(this, "Registration successful. You can now login.");
                toggleMode();
            } else {
                errorLabel.setText("Username already exists.");
            }
        } else {
            User loggedInUser = loginController.authenticate(user, pass, role);
            if (loggedInUser != null) {
                routeUser(loggedInUser);
            } else {
                errorLabel.setText("Invalid credentials.");
            }
        }
    }

    private void routeUser(User user) {
        dispose();
        if (user instanceof Customer) {
            CustomerGUI customerGUI = new CustomerGUI((Customer) user);
            customerGUI.setVisible(true);
        } else if (user instanceof FlightAgent) {
            AgentGUI agentGUI = new AgentGUI();
            agentGUI.setVisible(true);
        } else if (user instanceof SystemAdmin) {
            AdminGUI adminGUI = new AdminGUI();
            adminGUI.setVisible(true);
        }
    }
}