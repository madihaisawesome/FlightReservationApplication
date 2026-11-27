import javax.swing.*;
import java.awt.*;

public class BookingGUI extends JFrame {
    private final ReservationController reservationController;
    private final Flight selectedFlight;

    private final JTextField nameField = new JTextField(15);
    private final JTextField contactField = new JTextField(15);
    private final JTextField idField = new JTextField(15);
    private final JTextField seatField = new JTextField(5);

    public BookingGUI(Flight flight) {
        this(flight, new ReservationController());
    }

    public BookingGUI(Flight flight, ReservationController reservationController) {
        super("Book Flight");
        this.selectedFlight = flight;
        this.reservationController = reservationController;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(new JLabel("Flight: " + selectedFlight.getFlightNumber() + 
                " (" + selectedFlight.getOrigin() + " -> " + selectedFlight.getDestination() + ")"), gbc);
        
        gbc.gridy = 1;
        formPanel.add(new JLabel("Price: $" + String.format("%.2f", selectedFlight.getPrice())), gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2; gbc.gridx = 0;
        formPanel.add(new JLabel("Passenger Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridy = 3; gbc.gridx = 0;
        formPanel.add(new JLabel("Contact Info:"), gbc);
        gbc.gridx = 1;
        formPanel.add(contactField, gbc);

        gbc.gridy = 4; gbc.gridx = 0;
        formPanel.add(new JLabel("ID Number:"), gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        gbc.gridy = 5; gbc.gridx = 0;
        formPanel.add(new JLabel("Seat Number:"), gbc);
        gbc.gridx = 1;
        formPanel.add(seatField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton bookBtn = new JButton("Confirm Booking");
        JButton cancelBtn = new JButton("Cancel");
        
        bookBtn.addActionListener(e -> processBooking());
        cancelBtn.addActionListener(e -> dispose());

        buttonPanel.add(bookBtn);
        buttonPanel.add(cancelBtn);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void processBooking() {
        String name = nameField.getText().trim();
        String contact = contactField.getText().trim();
        String id = idField.getText().trim();
        String seat = seatField.getText().trim();

        if (name.isEmpty() || contact.isEmpty() || id.isEmpty() || seat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all passenger details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Customer customer = new Customer(id, name, contact, id, name);
        
        boolean success = reservationController.processBooking(customer, selectedFlight, seat);

        if (success) {
            JOptionPane.showMessageDialog(this, "Booking confirmed! Proceeding to payment.");
            PaymentGUI paymentGUI = new PaymentGUI(selectedFlight.getPrice());
            paymentGUI.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Booking failed. Seat might be unavailable.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}