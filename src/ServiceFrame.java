import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import java.awt.*;
import java.util.List;

/**
 * A Jframe specially made to hold ServiceTablePanels.
 *
 * Author: Filip Bark
 */
public class ServiceFrame extends JFrame {

	// Serial version is in format date-time to certify uniqueness
	// I won't actually serialize it, but JFrames implements serializable so I have to declare
	private static final long serialVersionUID = 2021_04_02_13_12L;

	User currentUser;

	private ServiceTablePanel serviceTablePanel;
	private JTextField statusTextField;
	private List<User> allUsers;

	public ServiceFrame (String title, List<Service> services, User currentUser, List<User> allUsers) {
		super (title);
		this.currentUser = currentUser;
		this.allUsers = allUsers;

		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

		serviceTablePanel = new ServiceTablePanel (services, currentUser);
		getContentPane ().add (new JScrollPane (serviceTablePanel), BorderLayout.CENTER);

		statusTextField = new JTextField ("Status text");
		statusTextField.setEditable (false);
		getContentPane ().add (statusTextField, BorderLayout.SOUTH);

		setJMenuBar (createMenuBar ());
		pack ();
	}

	private JMenuBar createMenuBar () {
		JMenuBar menuBar = new JMenuBar ();
		JMenu menu = new JMenu ("File");

		// For now, the file menu will only have one button
		JMenuItem editUser = new JMenuItem ("Edit users...");
		editUser.setToolTipText ("Open a new dialog for editing users");
		editUser.addActionListener (e -> openUserManager ());
		menu.add (editUser);
		menuBar.add (menu);
		return menuBar;
	}

	private void openUserManager () {
		EditUserPanel panel = new EditUserPanel (currentUser, allUsers);

		JDialog dialog = new JDialog (this);
		dialog.getContentPane ().add (panel);
		dialog.pack ();
		dialog.setVisible (true);
	}

	public ServiceTablePanel getServicePanel () {
		return serviceTablePanel;
	}

	public void setStatusText (String statusText) {
		statusTextField.setText (statusText);
	}

}
