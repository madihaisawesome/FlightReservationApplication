public class PaymentController {

    //private DatabaseManager db;
    
    public Reservation getReservation(int reservationID) {
        return new Reservation();
    }

    public Payment processPayment(Reservation r, PaymentStrategy card) {
        return new Payment();
    }

    public String generateReceipt(Payment p) {
        return "test";
    }

    private boolean validateCard(PaymentStrategy card) {
        return true;
    }
    
}