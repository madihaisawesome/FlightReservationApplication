import javax.swing.*;
import java.awt.*;

public class AgentGUI extends JFrame {

    public AgentGUI() {
        super("Flight Agent Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel welcomeLabel = new JLabel("Agent Management");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        add(welcomeLabel, gbc);

        JButton manageCustomersBtn = new JButton("Manage Customer Profiles");
        JButton manageReservationsBtn = new JButton("Manage Reservations");
        JButton searchFlightsBtn = new JButton("View Flight Schedule");
        JButton logoutBtn = new JButton("Logout");

        manageCustomersBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Customer profile management coming soon.");
        });

        manageReservationsBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Reservation management coming soon.");
        });

        searchFlightsBtn.addActionListener(e -> {
            SearchFlightGUI searchGUI = new SearchFlightGUI();
            searchGUI.setVisible(true);
        });

        logoutBtn.addActionListener(e -> {
            MainGUI mainGUI = new MainGUI();
            mainGUI.setVisible(true);
            dispose();
        });

        gbc.gridy = 1;
        add(manageCustomersBtn, gbc);
        gbc.gridy = 2;
        add(manageReservationsBtn, gbc);
        gbc.gridy = 3;
        add(searchFlightsBtn, gbc);
        gbc.gridy = 4;
        add(logoutBtn, gbc);
    }
}