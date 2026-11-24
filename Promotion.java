/**
 * <<entity>> Promotion — stub so SearchFlightGUI.displayPromotion matches the class diagram.
 * Full Observer wiring belongs with the promotions teammate.
 */
public class Promotion {
    private String title;
    private String message;

    public Promotion() {
    }

    public Promotion(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public final String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public final String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return title + ": " + message;
    }
}
