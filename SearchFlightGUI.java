import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * <<boundary>> SearchFlightGUI — matches class diagram (Madiha).
 * Search by origin, destination, date, airline; view seats, times, price.
 */
public class SearchFlightGUI extends JFrame {
    private final FlightController flightController;

    private final JTextField originField = new JTextField(12);
    private final JTextField destinationField = new JTextField(12);
    private final JTextField dateField = new JTextField(10);
    private final JTextField airlineField = new JTextField(12);
    private final DefaultTableModel tableModel;
    private final JTable resultsTable;
    private final JTextArea detailsArea = new JTextArea(5, 40);

    public SearchFlightGUI() {
        this(new FlightController());
    }

    public SearchFlightGUI(FlightController flightController) {
        super("Search / View Flights");
        this.flightController = flightController;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 560);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        String[] columns = {
                "Flight #", "Airline", "Origin", "Destination", "Date",
                "Depart", "Arrive", "Available", "Price", "Status"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultsTable = new JTable(tableModel);
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = resultsTable.getSelectedRow();
                if (row >= 0) {
                    String number = String.valueOf(tableModel.getValueAt(row, 0));
                    Flight flight = flightController.getFlightDetails(number);
                    if (flight != null) {
                        displayFlightDetails(flight);
                    }
                }
            }
        });

        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);

        add(displaySearchForm(), BorderLayout.NORTH);
        add(new JScrollPane(resultsTable), BorderLayout.CENTER);
        add(new JScrollPane(detailsArea), BorderLayout.SOUTH);

        displayResults(flightController.getAllFlights());
    }

    /** Builds and returns the search criteria panel. */
    public JPanel displaySearchForm() {
        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        searchPanel.add(new JLabel("Origin:"), gbc);
        gbc.gridx = 1;
        searchPanel.add(originField, gbc);

        gbc.gridx = 2;
        searchPanel.add(new JLabel("Destination:"), gbc);
        gbc.gridx = 3;
        searchPanel.add(destinationField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        searchPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        searchPanel.add(dateField, gbc);

        gbc.gridx = 2;
        searchPanel.add(new JLabel("Airline:"), gbc);
        gbc.gridx = 3;
        searchPanel.add(airlineField, gbc);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton searchBtn = new JButton("Search");
        JButton viewAllBtn = new JButton("View All");
        JButton detailsBtn = new JButton("View Details");
        buttonRow.add(searchBtn);
        buttonRow.add(viewAllBtn);
        buttonRow.add(detailsBtn);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4;
        searchPanel.add(buttonRow, gbc);

        searchBtn.addActionListener(e -> {
            List<Flight> flights = flightController.searchFlights(
                    originField.getText(),
                    destinationField.getText(),
                    dateField.getText(),
                    airlineField.getText()
            );
            displayResults(flights);
            if (flights.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No flights matched your search.",
                        "Search Results",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        viewAllBtn.addActionListener(e -> displayResults(flightController.getAllFlights()));
        detailsBtn.addActionListener(e -> {
            int row = resultsTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a flight first.");
                return;
            }
            String number = String.valueOf(tableModel.getValueAt(row, 0));
            Flight flight = flightController.getFlightDetails(number);
            if (flight != null) {
                displayFlightDetails(flight);
            }
        });

        return searchPanel;
    }

    public void displayResults(List<Flight> flights) {
        tableModel.setRowCount(0);
        if (flights == null) {
            return;
        }
        for (Flight f : flights) {
            tableModel.addRow(new Object[]{
                    f.getFlightNumber(),
                    f.getAirline(),
                    f.getOrigin(),
                    f.getDestination(),
                    f.getDepartureDate().toString(),
                    f.getDepartureTime().toString(),
                    f.getArrivalTime().toString(),
                    f.getAvailableSeats(),
                    String.format("$%.2f", f.getPrice()),
                    f.getStatus()
            });
        }
    }

    public void displayFlightDetails(Flight flight) {
        detailsArea.setText(
                "Flight " + flight.getFlightNumber() + " (" + flight.getAirline() + ")\n"
                        + flight.getOrigin() + " -> " + flight.getDestination() + "\n"
                        + "Date: " + flight.getDepartureDate()
                        + "  Depart: " + flight.getDepartureTime()
                        + "  Arrive: " + flight.getArrivalTime() + "\n"
                        + "Available seats: " + flight.getAvailableSeats()
                        + " / " + flight.getCapacity()
                        + "  Price: $" + String.format("%.2f", flight.getPrice()) + "\n"
                        + "Status: " + flight.getStatus()
        );
    }

    public void displayPromotion(Promotion p) {
        if (p == null) {
            return;
        }
        JOptionPane.showMessageDialog(this, p.getMessage(), p.getTitle(),
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SearchFlightGUI().setVisible(true));
    }
}
