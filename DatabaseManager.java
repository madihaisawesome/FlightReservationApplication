
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <<singleton>> DatabaseManager — data layer (matches class diagram).
 * Controllers obtain the single instance via getInstance().
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private static final String DB_URL = "jdbc:sqlite:flight_reservation.db";
    private Connection connection;

    private DatabaseManager() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            createTables();
            seedSampleFlightsIfEmpty();
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void createTables() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS flights ("
                + "flightNumber TEXT PRIMARY KEY,"
                + "airline TEXT NOT NULL,"
                + "origin TEXT NOT NULL,"
                + "destination TEXT NOT NULL,"
                + "departureDate TEXT NOT NULL,"
                + "departureTime TEXT NOT NULL,"
                + "arrivalTime TEXT NOT NULL,"
                + "price REAL NOT NULL,"
                + "capacity INTEGER NOT NULL,"
                + "bookedSeats INTEGER NOT NULL,"
                + "status TEXT NOT NULL"
                + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    private void seedSampleFlightsIfEmpty() throws SQLException {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS cnt FROM flights")) {
            if (rs.next() && rs.getInt("cnt") == 0) {
                stmt.executeUpdate(
                        "INSERT INTO flights (flightNumber, airline, origin, destination, departureDate,"
                                + "departureTime, arrivalTime, price, capacity, bookedSeats, status) VALUES "
                                + "('AC101', 'Air Canada', 'Calgary', 'Toronto', '2026-12-01', '08:30', '12:45', 349.99, 120, 0, 'OpenForBooking'),"
                                + "('WS205', 'WestJet', 'Calgary', 'Vancouver', '2026-12-01', '14:15', '15:05', 219.50, 90, 10, 'PartiallyBooked'),"
                                + "('AC310', 'Air Canada', 'Toronto', 'Montreal', '2026-12-05', '09:00', '10:15', 189.00, 75, 0, 'OpenForBooking'),"
                                + "('PD450', 'Porter', 'Vancouver', 'Calgary', '2026-12-10', '18:45', '21:30', 199.99, 60, 0, 'OpenForBooking'),"
                                + "('WS880', 'WestJet', 'Edmonton', 'Toronto', '2026-12-15', '06:20', '12:00', 299.00, 110, 0, 'OpenForBooking')"
                );
            }
        }
    }

    // --- Flight CRUD (as on class diagram) ---

    public boolean saveFlight(Flight flight) {
        String sql = "INSERT INTO flights (flightNumber, airline, origin, destination, departureDate,"
                + "departureTime, arrivalTime, price, capacity, bookedSeats, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            bindFlight(ps, flight);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("saveFlight failed: " + e.getMessage());
            return false;
        }
    }

    public boolean updateFlight(Flight flight) {
        String sql = "UPDATE flights SET airline=?, origin=?, destination=?, departureDate=?,"
                + "departureTime=?, arrivalTime=?, price=?, capacity=?, bookedSeats=?, status=? "
                + "WHERE flightNumber=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, flight.getAirline());
            ps.setString(2, flight.getOrigin());
            ps.setString(3, flight.getDestination());
            ps.setString(4, flight.getDepartureDate().toString());
            ps.setString(5, flight.getDepartureTime().toString());
            ps.setString(6, flight.getArrivalTime().toString());
            ps.setDouble(7, flight.getPrice());
            ps.setInt(8, flight.getCapacity());
            ps.setInt(9, flight.getBookedSeats());
            ps.setString(10, flight.getStatus());
            ps.setString(11, flight.getFlightNumber());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("updateFlight failed: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteFlight(String flightNumber) {
        String sql = "DELETE FROM flights WHERE flightNumber = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, flightNumber);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("deleteFlight failed: " + e.getMessage());
            return false;
        }
    }

    public Flight getFlight(String flightNumber) {
        String sql = "SELECT * FROM flights WHERE flightNumber = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, flightNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapFlight(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("getFlight failed: " + e.getMessage());
        }
        return null;
    }

    public List<Flight> getAllFlights() {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights ORDER BY departureDate, departureTime";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                flights.add(mapFlight(rs));
            }
        } catch (SQLException e) {
            System.err.println("getAllFlights failed: " + e.getMessage());
        }
        return flights;
    }

    public List<Flight> searchFlights(String origin, String destination, String date, String airline) {
        List<Flight> results = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM flights WHERE 1=1");
        List<String> params = new ArrayList<>();

        if (origin != null && !origin.isBlank()) {
            sql.append(" AND LOWER(origin) LIKE ?");
            params.add("%" + origin.trim().toLowerCase() + "%");
        }
        if (destination != null && !destination.isBlank()) {
            sql.append(" AND LOWER(destination) LIKE ?");
            params.add("%" + destination.trim().toLowerCase() + "%");
        }
        if (date != null && !date.isBlank()) {
            sql.append(" AND departureDate = ?");
            params.add(date.trim());
        }
        if (airline != null && !airline.isBlank()) {
            sql.append(" AND LOWER(airline) LIKE ?");
            params.add("%" + airline.trim().toLowerCase() + "%");
        }
        sql.append(" ORDER BY departureDate, departureTime");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapFlight(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("searchFlights failed: " + e.getMessage());
        }
        return results;
    }

    private void bindFlight(PreparedStatement ps, Flight flight) throws SQLException {
        ps.setString(1, flight.getFlightNumber());
        ps.setString(2, flight.getAirline());
        ps.setString(3, flight.getOrigin());
        ps.setString(4, flight.getDestination());
        ps.setString(5, flight.getDepartureDate().toString());
        ps.setString(6, flight.getDepartureTime().toString());
        ps.setString(7, flight.getArrivalTime().toString());
        ps.setDouble(8, flight.getPrice());
        ps.setInt(9, flight.getCapacity());
        ps.setInt(10, flight.getBookedSeats());
        ps.setString(11, flight.getStatus());
    }

    private Flight mapFlight(ResultSet rs) throws SQLException {
        return new Flight(
                rs.getString("flightNumber"),
                rs.getString("airline"),
                rs.getString("origin"),
                rs.getString("destination"),
                LocalDate.parse(rs.getString("departureDate")),
                LocalTime.parse(rs.getString("departureTime")),
                LocalTime.parse(rs.getString("arrivalTime")),
                rs.getDouble("price"),
                rs.getInt("capacity"),
                rs.getInt("bookedSeats"),
                rs.getString("status")
        );
    }
}
