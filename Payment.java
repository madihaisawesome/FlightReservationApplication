import java.time.LocalDate;

public class Payment {
    private int paymentID; // Maybe use static int to track this?
    private double amount;
    private LocalDate date;
    private String status; // I think it may be better to use an enum for this?
    private PaymentStrategy method;


    public Payment(double amount, PaymentStrategy method) {
        // I am not sure if anything else needs to be directly set in this constructor, will check again later
        this.amount = amount;
        this.method = method;
        this.date = LocalDate.now();
        this.status = "Processing";

    }

    public boolean process() {
        return true;
    }
}
