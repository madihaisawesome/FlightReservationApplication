import java.util.List;

/**
 * <<control>> FlightController — matches class diagram (Madiha).
 * Presentation -> this controller -> DatabaseManager.
 */
public class FlightController {
    private final DatabaseManager db;

    public FlightController() {
        this.db = DatabaseManager.getInstance();
    }

    public List<Flight> searchFlights(String origin, String destination,
                                      String date, String airlines) {
        return db.searchFlights(origin, destination, date, airlines);
    }

    public Flight getFlightDetails(String flightNumber) {
        return db.getFlight(flightNumber);
    }

    public void addFlight(Flight flight) {
        if (flight.getStatus() == null || flight.getStatus().isBlank()) {
            flight.setStatus("Scheduled");
        }
        db.saveFlight(flight);
    }

    public void updateFlight(Flight flight) {
        db.updateFlight(flight);
    }

    public List<Flight> viewAllFlightsByAirline(String airline) {
        return db.searchFlights(null, null, null, airline);
    }

    /** Extra for admin remove (requirements + Flight state diagram). */
    public boolean removeFlight(String flightNumber) {
        return db.deleteFlight(flightNumber);
    }

    public List<Flight> getAllFlights() {
        return db.getAllFlights();
    }
}
