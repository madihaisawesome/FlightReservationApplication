public class PaymentController {

    //private DatabaseManager db;
    
    public Reservation getReservation(int reservationID) {
        return new Reservation(); //get reservation
    }

    public Payment processPayment(Reservation r, PaymentStrategy card) {
       Payment newPayment = r.getPayment();
       newPayment.process();
       return newPayment;
    }

    public String generateReceipt(Payment p) {
        return "test";
    }

    private boolean validateCard(PaymentStrategy card) {
        return true;
    }
    
}