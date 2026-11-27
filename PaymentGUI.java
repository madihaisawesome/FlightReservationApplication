import javax.swing.*;
import java.awt.*;

public class PaymentGUI extends JFrame {
    private final PaymentController paymentController;
    private final Reservation reservation;

    private final JTextField cardNumberField = new JTextField(15);
    private final JTextField expiryField = new JTextField(5);
    private final JTextField cvvField = new JTextField(4);
    private final JComboBox<String> methodComboBox = new JComboBox<>(new String[]{"Credit Card", "Debit Card"});
    private final JTextField amountField = new JTextField(10);

    public PaymentGUI(Reservation reservation) {
        this(reservation, new PaymentController());
    }

    public PaymentGUI(Reservation reservation, PaymentController paymentController) {
        super("Payment");
        this.reservation = reservation;
        this.paymentController = paymentController;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        double totalAmount = reservation.getNumSeats() * reservation.getFlight().getPrice();
        
        amountField.setText(String.format("%.2f", totalAmount));
        amountField.setEditable(false);

        gbc.gridy = 0; gbc.gridx = 0;
        formPanel.add(new JLabel("Amount ($):"), gbc);
        gbc.gridx = 1;
        formPanel.add(amountField, gbc);

        gbc.gridy = 1; gbc.gridx = 0;
        formPanel.add(new JLabel("Payment Method:"), gbc);
        gbc.gridx = 1;
        formPanel.add(methodComboBox, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        formPanel.add(new JLabel("Card Number:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cardNumberField, gbc);

        gbc.gridy = 3; gbc.gridx = 0;
        formPanel.add(new JLabel("Expiry (MM/YY):"), gbc);
        gbc.gridx = 1;
        formPanel.add(expiryField, gbc);

        gbc.gridy = 4; gbc.gridx = 0;
        formPanel.add(new JLabel("CVV:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cvvField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton payBtn = new JButton("Pay Now");
        JButton cancelBtn = new JButton("Cancel");
        
        payBtn.addActionListener(e -> processPayment());
        cancelBtn.addActionListener(e -> dispose());

        buttonPanel.add(payBtn);
        buttonPanel.add(cancelBtn);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void processPayment() {
        String cardNum = cardNumberField.getText().trim();
        String expiry = expiryField.getText().trim();
        String cvv = cvvField.getText().trim();
        String method = (String) methodComboBox.getSelectedItem();

        if (cardNum.isEmpty() || expiry.isEmpty() || cvv.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all payment details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PaymentStrategy strategy;
        if ("Credit Card".equals(method)) {
            strategy = new CreditCardPayment(cardNum, reservation.getCustomer().getName(), expiry, cvv);
        } else {
            strategy = new DebitCardPayment(cardNum, "Generic Bank");
        }

        Payment paymentResult = paymentController.processPayment(reservation, strategy);

        if (paymentResult != null) {
            String receipt = paymentController.generateReceipt(paymentResult, reservation.getCustomer());
            JOptionPane.showMessageDialog(this, "Payment successful!\n\n" + receipt);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Payment failed. Invalid card.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}