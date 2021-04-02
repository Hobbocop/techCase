import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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

	public ServiceFrame (String title, List<Service> services, User currentUser) {
		super (title);
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

		// Create a temporary label so we have something in the window...
		// JLabel emptyLabel = new JLabel ("Logged in as: " + currentUser);
		JPanel serviceTablePanel = new ServiceTablePanel (services, currentUser);
		getContentPane ().add (new JScrollPane (serviceTablePanel), BorderLayout.CENTER);
		pack ();
	}

}
