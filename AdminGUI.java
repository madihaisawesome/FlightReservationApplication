import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * <<boundary>> AdminGUI — flight management portion (Madiha).
 * Add, update, remove flights via FlightController (class diagram).
 */
public class AdminGUI extends JFrame {
    private final FlightController flightController;
    private final DefaultTableModel tableModel;
    private final JTable flightTable;

    private final JTextField flightNumberField = new JTextField(10);
    private final JTextField airlineField = new JTextField(10);
    private final JTextField originField = new JTextField(10);
    private final JTextField destinationField = new JTextField(10);
    private final JTextField dateField = new JTextField(10);
    private final JTextField departField = new JTextField(8);
    private final JTextField arriveField = new JTextField(8);
    private final JTextField capacityField = new JTextField(6);
    private final JTextField priceField = new JTextField(8);

    private String selectedFlightNumber = null;

    public AdminGUI() {
        this(new FlightController());
    }

    public AdminGUI(FlightController flightController) {
        super("Admin — Flight Management");
        this.flightController = flightController;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(960, 580);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        String[] columns = {
                "Flight #", "Airline", "Origin", "Destination", "Date",
                "Depart", "Arrive", "Capacity", "Booked", "Avail", "Price", "Status"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        flightTable = new JTable(tableModel);
        flightTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        flightTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedIntoForm();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Add Flight");
        JButton updateBtn = new JButton("Update Flight");
        JButton removeBtn = new JButton("Remove Flight");
        JButton openBtn = new JButton("Open for Booking");
        JButton clearBtn = new JButton("Clear Form");
        JButton refreshBtn = new JButton("Refresh");
        JButton byAirlineBtn = new JButton("Filter by Airline");

        addBtn.addActionListener(e -> addFlight());
        updateBtn.addActionListener(e -> updateFlight());
        removeBtn.addActionListener(e -> removeFlight());
        openBtn.addActionListener(e -> openSelectedForBooking());
        clearBtn.addActionListener(e -> clearForm());
        refreshBtn.addActionListener(e -> refreshTable(flightController.getAllFlights()));
        byAirlineBtn.addActionListener(e -> {
            String airline = airlineField.getText().trim();
            refreshTable(flightController.viewAllFlightsByAirline(airline));
        });

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(removeBtn);
        buttonPanel.add(openBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(byAirlineBtn);

        JPanel south = new JPanel(new BorderLayout());
        south.add(buildFormPanel(), BorderLayout.CENTER);
        south.add(buttonPanel, BorderLayout.SOUTH);

        add(new JScrollPane(flightTable), BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

        refreshTable(flightController.getAllFlights());
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 4, 3, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addLabeled(panel, gbc, 0, 0, "Flight #:", flightNumberField);
        addLabeled(panel, gbc, 1, 0, "Airline:", airlineField);
        addLabeled(panel, gbc, 2, 0, "Origin:", originField);
        addLabeled(panel, gbc, 3, 0, "Destination:", destinationField);

        addLabeled(panel, gbc, 0, 1, "Date:", dateField);
        addLabeled(panel, gbc, 1, 1, "Depart:", departField);
        addLabeled(panel, gbc, 2, 1, "Arrive:", arriveField);
        addLabeled(panel, gbc, 3, 1, "Capacity:", capacityField);

        addLabeled(panel, gbc, 0, 2, "Price:", priceField);
        return panel;
    }

    private void addLabeled(JPanel panel, GridBagConstraints gbc, int col, int row,
                            String label, JComponent field) {
        gbc.gridy = row;
        gbc.gridx = col * 2;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = col * 2 + 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private void refreshTable(List<Flight> flights) {
        tableModel.setRowCount(0);
        for (Flight f : flights) {
            tableModel.addRow(new Object[]{
                    f.getFlightNumber(),
                    f.getAirline(),
                    f.getOrigin(),
                    f.getDestination(),
                    f.getDepartureDate().toString(),
                    f.getDepartureTime().toString(),
                    f.getArrivalTime().toString(),
                    f.getCapacity(),
                    f.getBookedSeats(),
                    f.getAvailableSeats(),
                    String.format("%.2f", f.getPrice()),
                    f.getStatus()
            });
        }
    }

    private void loadSelectedIntoForm() {
        int row = flightTable.getSelectedRow();
        if (row < 0) {
            return;
        }
        selectedFlightNumber = String.valueOf(tableModel.getValueAt(row, 0));
        flightNumberField.setText(selectedFlightNumber);
        airlineField.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        originField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        destinationField.setText(String.valueOf(tableModel.getValueAt(row, 3)));
        dateField.setText(String.valueOf(tableModel.getValueAt(row, 4)));
        departField.setText(String.valueOf(tableModel.getValueAt(row, 5)));
        arriveField.setText(String.valueOf(tableModel.getValueAt(row, 6)));
        capacityField.setText(String.valueOf(tableModel.getValueAt(row, 7)));
        priceField.setText(String.valueOf(tableModel.getValueAt(row, 10)));
    }

    private void addFlight() {
        Flight flight = readFormFlight(false);
        if (flight == null) {
            return;
        }
        if (flightController.getFlightDetails(flight.getFlightNumber()) != null) {
            JOptionPane.showMessageDialog(this, "Flight number already exists.");
            return;
        }
        flightController.addFlight(flight);
        JOptionPane.showMessageDialog(this, "Flight added.");
        clearForm();
        refreshTable(flightController.getAllFlights());
    }

    private void updateFlight() {
        if (selectedFlightNumber == null) {
            JOptionPane.showMessageDialog(this, "Select a flight in the table first.");
            return;
        }
        Flight existing = flightController.getFlightDetails(selectedFlightNumber);
        Flight flight = readFormFlight(true);
        if (existing == null || flight == null) {
            return;
        }
        flight.setBookedSeats(existing.getBookedSeats());
        flight.setStatus(existing.getStatus());
        flight.setFlightNumber(selectedFlightNumber);
        flightController.updateFlight(flight);
        JOptionPane.showMessageDialog(this, "Flight updated.");
        clearForm();
        refreshTable(flightController.getAllFlights());
    }

    private void removeFlight() {
        if (selectedFlightNumber == null) {
            JOptionPane.showMessageDialog(this, "Select a flight in the table first.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Remove flight " + selectedFlightNumber + "?",
                "Confirm Remove",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        if (flightController.removeFlight(selectedFlightNumber)) {
            JOptionPane.showMessageDialog(this, "Flight removed.");
            clearForm();
            refreshTable(flightController.getAllFlights());
        } else {
            JOptionPane.showMessageDialog(this, "Failed to remove flight.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openSelectedForBooking() {
        if (selectedFlightNumber == null) {
            JOptionPane.showMessageDialog(this, "Select a flight first.");
            return;
        }
        Flight flight = flightController.getFlightDetails(selectedFlightNumber);
        if (flight == null) {
            return;
        }
        flight.openForBooking();
        flightController.updateFlight(flight);
        JOptionPane.showMessageDialog(this, "Flight opened for booking.");
        refreshTable(flightController.getAllFlights());
    }

    private Flight readFormFlight(boolean forUpdate) {
        try {
            String number = flightNumberField.getText().trim();
            String airline = airlineField.getText().trim();
            String origin = originField.getText().trim();
            String destination = destinationField.getText().trim();
            if (number.isEmpty() || airline.isEmpty() || origin.isEmpty() || destination.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Flight #, airline, origin, and destination are required.");
                return null;
            }
            LocalDate date = LocalDate.parse(dateField.getText().trim());
            LocalTime depart = LocalTime.parse(departField.getText().trim());
            LocalTime arrive = LocalTime.parse(arriveField.getText().trim());
            int capacity = Integer.parseInt(capacityField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());
            if (capacity < 0 || price < 0) {
                JOptionPane.showMessageDialog(this, "Capacity and price must be non-negative.");
                return null;
            }
            Flight flight = new Flight(number, airline, origin, destination,
                    date, depart, arrive, price, capacity);
            if (forUpdate) {
                flight.setFlightNumber(selectedFlightNumber);
            }
            return flight;
        } catch (DateTimeParseException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Check date (YYYY-MM-DD), times (HH:MM), capacity, and price.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

    private void clearForm() {
        selectedFlightNumber = null;
        flightTable.clearSelection();
        flightNumberField.setText("");
        airlineField.setText("");
        originField.setText("");
        destinationField.setText("");
        dateField.setText("");
        departField.setText("");
        arriveField.setText("");
        capacityField.setText("");
        priceField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminGUI().setVisible(true));
    }
}
