import java.time.LocalDate;
import java.time.LocalTime;

/**
 * <<entity>> Flight — matches class diagram (Madiha).
 */
public class Flight {
    private String flightNumber;
    private String airline;
    private String origin;
    private String destination;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private double price;
    private int capacity;
    private int bookedSeats;
    private String status; // Scheduled, OpenForBooking, FullyBooked, Cancelled, Departed, Completed

    public Flight() {
        this.status = "Scheduled";
        this.bookedSeats = 0;
    }

    public Flight(String flightNumber, String airline, String origin, String destination,
                  LocalDate departureDate, LocalTime departureTime, LocalTime arrivalTime,
                  double price, int capacity, int bookedSeats, String status) {
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.capacity = capacity;
        this.bookedSeats = bookedSeats;
        this.status = status;
    }

    /** New flight starts Scheduled with no bookings. */
    public Flight(String flightNumber, String airline, String origin, String destination,
                  LocalDate departureDate, LocalTime departureTime, LocalTime arrivalTime,
                  double price, int capacity) {
        this(flightNumber, airline, origin, destination, departureDate, departureTime,
                arrivalTime, price, capacity, 0, "Scheduled");
    }

    public int getAvailableSeats() {
        return capacity - bookedSeats;
    }

    public boolean reserveSeat() {
        if (getAvailableSeats() <= 0) {
            return false;
        }
        if ("Scheduled".equals(status) || "Cancelled".equals(status)
                || "Departed".equals(status) || "Completed".equals(status)
                || "FullyBooked".equals(status)) {
            return false;
        }
        bookedSeats++;
        refreshBookingStatus();
        return true;
    }

    public void releaseSeat() {
        if (bookedSeats > 0) {
            bookedSeats--;
            refreshBookingStatus();
        }
    }

    public void openForBooking() {
        this.status = "OpenForBooking";
        refreshBookingStatus();
    }

    private void refreshBookingStatus() {
        if ("Cancelled".equals(status) || "Departed".equals(status)
                || "Completed".equals(status) || "Scheduled".equals(status)) {
            return;
        }
        if (bookedSeats <= 0) {
            status = "OpenForBooking";
        } else if (bookedSeats >= capacity) {
            status = "FullyBooked";
        } else {
            status = "PartiallyBooked";
        }
    }

    public final String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public final String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public final String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public final String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public final LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public final LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public final LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public final double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public final int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public final int getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(int bookedSeats) {
        this.bookedSeats = bookedSeats;
    }

    public final String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return flightNumber + " | " + origin + " -> " + destination
                + " | " + departureDate + " " + departureTime + "-" + arrivalTime
                + " | " + airline + " | avail: " + getAvailableSeats()
                + "/" + capacity + " | $" + String.format("%.2f", price)
                + " | " + status;
    }
}
