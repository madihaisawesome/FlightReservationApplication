public class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;
    private String cardHolder;
    private String expiryDate;
    private String cvv;

    public CreditCardPayment(String number, String holder, String expiry, String cvv) {
        this.cardNumber = number;
        this.cardHolder = holder;
        this.expiryDate = expiry;
        this.cvv = cvv;
    }

    @Override 
    public boolean pay(double amount) {
        return true;
    }

    public final String getCardNumber() {
        return this.cardNumber;
    }

    public final String getCardHolder() {
        return this.cardHolder;
    }

    public final String getExpiryDate() {
        return this.expiryDate;
    }

    public final String getCvv() {
        return this.cvv;
    }
}
