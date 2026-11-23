public class DebitCardPayment implements PaymentStrategy {
    String cardNumber;
    String bankName;

    public DebitCardPayment(String number, String bank) {
        this.cardNumber = number;
        this.bankName = bank;
    }

    @Override 
    public boolean pay(double amount) {
        return true;
    }

    public final String getCardNumber() {
        return this.cardNumber;
    }
    public final String getBankName() {
        return this.bankName;
    } 
}
