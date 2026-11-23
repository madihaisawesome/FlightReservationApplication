import java.time.LocalDate;
import java.util.List;

public class Reservation {
    private int reservationID;
    //private Customer customer;
    //private Flight flight;
    private int numSeats;
    private List<String> seatNumbers;
    private LocalDate bookingDate;
    private String status;
    private Payment payment;

    //0..1 payment

    //public Reservation(Customer customer, Flight flight, int numSeats, Payment payment)

    public double getTotalPrice() {
        return 1.0;
    }

    public void confirm() {

    }

    public void cancel() {
        
    }

    // Getters and setters

    public final String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public final Payment getPayment() {
        return this.payment;
    }
    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public final int getNumSeats() {
        return this.numSeats;
    }
    public void setNumSeats(int numSeats) {
        this.numSeats = numSeats;
    }

    public final Customer getCustomer() {
        return this.customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public final Flight getFlight() {
        return this.flight;
    }
    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public final List<String> getSeatNumbers() {
        return this.seatNumbers;
    }

    public final LocalDate getBookingDate() {
        return this.bookingDate;
    }

    public final int getReservationID() {
        return this.reservationID;
    }


}
