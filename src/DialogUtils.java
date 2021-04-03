import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import java.awt.*;

/**
 * Utility methods for displaying dialogs of different kinds
 *
 * Author: Filip Bark
 */
public class DialogUtils {

	private static final String TRUE = "true";
	private static final String FALSE = "false";

	public static int showLoginDialog (Component parent, StringTuple tuple, String title) {

		JLabel nameLabel = new JLabel ("Username");
		JTextField nameField = new JTextField (20);

		JLabel pwdLabel = new JLabel ("Password");
		JTextField pwdField = new JPasswordField (20);

		return showStringDialog (parent, tuple, title, nameLabel, nameField, pwdLabel, pwdField, null, null);
	}

	private static int showStringDialog (Component parent, StringTuple tuple, String title, JLabel firstLabel,
					     JTextField firstField, JLabel secondLabel, JTextField secondField,
					     JLabel adminLabel, JRadioButton adminButton) {
		JPanel servicePanel;
		if (adminLabel == null) // When editing users, we need another row for admin settings
			servicePanel = new JPanel (new GridLayout (2, 2));
		else
			servicePanel = new JPanel (new GridLayout (3, 2));
			
		servicePanel.add (firstLabel);
		servicePanel.add (firstField);
		servicePanel.add (secondLabel);
		servicePanel.add (secondField);
		if (adminLabel != null)
			servicePanel.add (adminLabel);
		if (adminButton != null)
			servicePanel.add (adminButton);

		var choice = JOptionPane.showConfirmDialog (parent, servicePanel, title, JOptionPane.OK_CANCEL_OPTION,
		                                            JOptionPane.PLAIN_MESSAGE);

		if (choiceNotOk (choice))
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

		showStringDialog (parent, tuple, title, nameLabel, nameField, urlLabel, urlField, null, null);
	}

	public static int showEditUserDialog (EditUserPanel parent, User user, boolean currentlyAdmin, String title) {
		JLabel nameLabel = new JLabel ("Username");
		JTextField nameField;
		if (user.getUserName ().equals (""))
			nameField = new JTextField (20);
		else
			nameField = new JTextField (user.getUserName ());

		StringTuple tuple = new StringTuple (user.getUserName (), user.isAdmin () ? TRUE : FALSE);

		// Normally you would ask for current password here, but if you've gotten to this point you're
		// either logged in as this user or as an admin - so I'll allow it!
		// Leaving the new password empty will not change it
		JLabel newPwdLabel = new JLabel ("New Password ");
		JTextField newPwdField = new JTextField (20);

		JLabel adminLabel = new JLabel ("Administrative rights ");
		JRadioButton adminButton = new JRadioButton ();
		adminButton.setSelected (user.isAdmin ());
		adminButton.setEnabled (currentlyAdmin);
		
		var choice = showStringDialog (parent, tuple, title, nameLabel, nameField, newPwdLabel, newPwdField,
					       adminLabel, adminButton);

		if (choiceNotOk (choice))
			return choice;

		user.updateUserName (tuple.s1);
		if (!tuple.s2.equals ("")) // Password has been updated 2nd string isn't empty
			user.updatePassword (tuple.s2);
		if (adminButton.isEnabled ()) // Only if the radiobutton is enabled can this even change...
			user.setAdmin (adminButton.isSelected ());

		return choice;
	}

	public static boolean choiceNotOk (int choice) {
		return choice != JOptionPane.OK_OPTION;
	}

	public static void showMessageDialog (Component parent, String message, String title) {
		JOptionPane.showMessageDialog (parent, message, title, JOptionPane.ERROR_MESSAGE);
	}

}
