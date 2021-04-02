import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.*;

/**
 * Utility methods for displaying dialogs of different kinds
 *
 * Author: Filip Bark
 */
public class DialogUtils {

	public static int showLoginDialog (Component parent, StringTuple tuple, String title) {

		JLabel nameLabel = new JLabel ("Username");
		JTextField nameField = new JTextField (20);

		JLabel urlLabel = new JLabel ("Password");
		JTextField urlField = new JTextField (20);

		return showStringDialog (parent, tuple, title, nameLabel, nameField, urlLabel, urlField);
	}

	private static int showStringDialog (Component parent, StringTuple tuple, String title, JLabel firstLabel,
	                                     JTextField firstField, JLabel secondLabel, JTextField secondField) {
		JPanel servicePanel = new JPanel (new GridLayout (2, 2));
		servicePanel.add (firstLabel);
		servicePanel.add (firstField);
		servicePanel.add (secondLabel);
		servicePanel.add (secondField);

		var choice = JOptionPane.showConfirmDialog (parent, servicePanel, title, JOptionPane.OK_CANCEL_OPTION,
		                                            JOptionPane.PLAIN_MESSAGE);

		if (choice != JOptionPane.OK_OPTION)
			return choice;

		tuple.s1 = firstField.getText ();
		tuple.s2 = secondField.getText ();

		return choice;
	}

	public static void showEditServiceDialog (Component parent, StringTuple tuple, String title) {

		JLabel nameLabel = new JLabel ("Service Name");
		JTextField nameField;
		if (tuple.s1.equals (""))
			nameField = new JTextField (20);
		else
			nameField = new JTextField (tuple.s1);

		JLabel urlLabel = new JLabel ("Service URL");
		JTextField urlField;
		if (tuple.s2.equals (""))
			urlField = new JTextField (20);
		else
			urlField = new JTextField (tuple.s2);

		showStringDialog (parent, tuple, title, nameLabel, nameField, urlLabel, urlField);
	}

	public static void showMessageDialog (Component parent, String message, String title) {
		JOptionPane.showMessageDialog (parent, message, title, JOptionPane.ERROR_MESSAGE);
	}
}
