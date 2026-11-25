public class PaymentController {

    private DatabaseManager db;

    public PaymentController() {
        this.db = DatabaseManager.getInstance();
    }

    public Reservation getReservation(int reservationID) {
        return new Reservation(); //get reservation from db
    }

    public Payment processPayment(Reservation r, PaymentStrategy card) {
       Payment newPayment = r.getPayment();
       newPayment.process();
       return newPayment;
    }

    public String generateReceipt(Payment p) {
        //just put all payment information
        return "test";
    }

    private boolean validateCard(PaymentStrategy card) { //simulation
        return true;
    }
    
}