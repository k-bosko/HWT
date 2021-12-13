package hwt.view;
import javax.swing.*;

public class DialogBox {
    public static void createDialogBox(String message, String title) {
      JOptionPane.showMessageDialog(null,
          message,
          title,
          JOptionPane.PLAIN_MESSAGE);
    }
}
