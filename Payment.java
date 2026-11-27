import java.time.LocalDate;

public class Payment {
    private int paymentID; // Maybe use static int to track this?
    private double amount;
    private LocalDate date;
    private String status; // I think it may be better to use an enum for this?

    private enum statusEnum {
        PENDING,
        FAILED,
        COMPLETED
    }
    private PaymentStrategy method;


    public Payment(double amount, PaymentStrategy method) {
        // I am not sure if anything else needs to be directly set in this constructor, will check again later
        this.amount = amount;
        this.method = method;
        this.date = LocalDate.now();
        this.status = "PENDING";

    }

    public boolean process() {
        boolean success = method.pay(amount);
        
        if (success) {
            this.status = "COMPLETED";
        } else {
            this.status = "FAILED";
        }

        return success;
    }

    
    // Getters and setters

    public final PaymentStrategy getMethod() {
        return this.method;
    }
    public void setMethod(PaymentStrategy method) {
        this.method = method;
    }

    public final double getAmount() {
        return this.amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public final String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public final int getPaymentID() {
        return this.paymentID;
    }

    public final LocalDate getDate() {
        return this.date;
    }

}
