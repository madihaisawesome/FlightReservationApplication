import javax.swing.*;
import java.awt.*;

public class BookingGUI extends JFrame {
    private final ReservationController reservationController;
    private final Flight selectedFlight;

    private final JTextField nameField = new JTextField(15);
    private final JTextField contactField = new JTextField(15);
    private final JTextField idField = new JTextField(15);
    private final JComboBox<Integer> seatsComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4});

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
        formPanel.add(new JLabel("Price per seat: $" + String.format("%.2f", selectedFlight.getPrice())), gbc);

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
        formPanel.add(new JLabel("Number of Seats:"), gbc);
        gbc.gridx = 1;
        formPanel.add(seatsComboBox, gbc);

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
        
        if (name.isEmpty() || contact.isEmpty() || id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all passenger details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int numSeats = (Integer) seatsComboBox.getSelectedItem();
        
        Customer customer = new Customer(Integer.parseInt(id), name, contact, name + "@email.com", "pass123");
        
        Reservation newReservation = reservationController.createReservation(customer, selectedFlight, numSeats);

        if (newReservation != null) {
            String confirmationStr = reservationController.generateConfirmation(newReservation);
            
            JOptionPane.showMessageDialog(this, "Booking confirmed!\n\n" + confirmationStr);
            
            PaymentGUI paymentGUI = new PaymentGUI(newReservation);
            paymentGUI.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Booking failed. Seats might be unavailable.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}