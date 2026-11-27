import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {

    public MainGUI() {
        super("Flight Reservation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel titleLabel = new JLabel("Select Your Role");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridy = 0;
        add(titleLabel, gbc);

        JButton customerBtn = new JButton("Login as Customer");
        JButton agentBtn = new JButton("Login as Flight Agent");
        JButton adminBtn = new JButton("Login as System Administrator");

        customerBtn.addActionListener(e -> openLogin("Customer"));
        agentBtn.addActionListener(e -> openLogin("Flight Agent"));
        adminBtn.addActionListener(e -> openLogin("System Administrator"));

        gbc.gridy = 1;
        add(customerBtn, gbc);
        gbc.gridy = 2;
        add(agentBtn, gbc);
        gbc.gridy = 3;
        add(adminBtn, gbc);
    }

    private void openLogin(String role) {
        LoginGUI loginGUI = new LoginGUI(role);
        loginGUI.setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI().setVisible(true));
    }
}