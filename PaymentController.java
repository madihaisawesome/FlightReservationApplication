public class PaymentController {

    private DatabaseManager db;

    public PaymentController() {
        this.db = DatabaseManager.getInstance();
    }

    public Reservation getReservation(int reservationID) {
        return db.getReservation(reservationID);
    }

    public Payment processPayment(Reservation r, PaymentStrategy card) {
       Payment newPayment = r.getPayment();
       if (newPayment == null || validateCard(card) == false) {
            return null;
       }

       newPayment.setMethod(card);
       newPayment.process();

       db.savePayment(newPayment);
       return newPayment;
    }

    public String generateReceipt(Payment p, Customer c) {
        String receipt = """
        =====
        FLIGHT RESERVATION APPLICATION
        Payment ID %d
        =====

        DATE: %s
        CUSTOMER: %s

        PAYMENT METHOD: %s

        TOTAL: %f

        """.formatted(p.getPaymentID(), p.getDate(), c.getName(), p.getMethod().getClass().getSimpleName(), p.getAmount());
        return receipt;
    }

    private boolean validateCard(PaymentStrategy card) {
        System.out.println("Validating card...");
        // In an actual payment processor, would check the card against the provider and see if it exists
        if (card == null) {
            return false;
        }
        return true;
    }
    
}