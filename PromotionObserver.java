import javax.swing.JOptionPane;

public class PromotionObserver {
    
    public void updatePromotion(String title, String message) {
        JOptionPane.showMessageDialog(null, 
                message, 
                title, 
                JOptionPane.INFORMATION_MESSAGE);
    }
}