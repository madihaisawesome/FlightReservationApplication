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
}
