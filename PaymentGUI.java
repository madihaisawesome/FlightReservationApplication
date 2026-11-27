import javax.swing.*;
import java.awt.*;

public class PaymentGUI extends JFrame {
    private final PaymentController paymentController;
    private final double amount;

    private final JTextField cardNumberField = new JTextField(15);
    private final JTextField expiryField = new JTextField(5);
    private final JTextField cvvField = new JTextField(4);
    private final JComboBox<String> methodComboBox = new JComboBox<>(new String[]{"Credit Card", "Debit Card"});
    private final JTextField amountField = new JTextField(10);

    public PaymentGUI(double amount) {
        this(amount, new PaymentController());
    }

    public PaymentGUI(double amount, PaymentController paymentController) {
        super("Payment");
        this.amount = amount;
        this.paymentController = paymentController;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        amountField.setText(String.format("%.2f", this.amount));
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
        String card = cardNumberField.getText().trim();
        String expiry = expiryField.getText().trim();
        String cvv = cvvField.getText().trim();
        String method = (String) methodComboBox.getSelectedItem();

        if (card.isEmpty() || expiry.isEmpty() || cvv.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all payment details.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = paymentController.processPayment(amount, method, card);

        if (success) {
            JOptionPane.showMessageDialog(this, "Payment successful! Receipt generated.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Payment failed. Invalid card.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}