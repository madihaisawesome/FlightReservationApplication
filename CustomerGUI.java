import javax.swing.*;
import java.awt.*;

public class CustomerGUI extends JFrame {
    private final Customer customer;

    public CustomerGUI(Customer customer) {
        super("Customer Dashboard - " + customer.getName());
        this.customer = customer;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel welcomeLabel = new JLabel("Welcome, " + customer.getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        add(welcomeLabel, gbc);

        JButton searchBtn = new JButton("Search Flights");
        JButton historyBtn = new JButton("View Booking History");
        JButton logoutBtn = new JButton("Logout");

        searchBtn.addActionListener(e -> {
            SearchFlightGUI searchGUI = new SearchFlightGUI();
            searchGUI.setVisible(true);
        });

        historyBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Booking history feature coming soon.");
        });

        logoutBtn.addActionListener(e -> {
            MainGUI mainGUI = new MainGUI();
            mainGUI.setVisible(true);
            dispose();
        });

        gbc.gridy = 1;
        add(searchBtn, gbc);
        gbc.gridy = 2;
        add(historyBtn, gbc);
        gbc.gridy = 3;
        add(logoutBtn, gbc);
    }
}