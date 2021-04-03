import java.util.List;
import java.util.Optional;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Main class for code test.
 *
 * Author: Filip Bark
 */
public class TechCase {
	private static final int THREAD_SLEEP_DURATION = 5000;

	public static void main (String[] args) {
		System.out.println ("Hello world!");

		// Initialize and load all data
		List<User> allUsers = fetchAllUsers ();
		User currentUser = logIn (allUsers);
		List<Service> services = fetchAllServices (allUsers, currentUser);

		// TODO - add a dialog/panel for managing users...

		// If we were unable to log in - just close the program. This shouldn't really happen...
		if (currentUser == null) {
			System.out.println ("No user found");
			return;
		}

		// Start the program!
		run (services, currentUser);
	}

	// Load all stored users from database
	private static List<User> fetchAllUsers () {
		return DataBaseUtils.selectAllUsers ();
	}

	private static User logIn (List<User> allUsers) {
		User currentUser = null;
		StringTuple tmpUser;
		while (currentUser == null) {
			tmpUser = new StringTuple ("", "");
			var choice = DialogUtils.showLoginDialog (null, tmpUser, "log in");

			if (choice != JOptionPane.OK_OPTION)
				return null;

			String userName = tmpUser.s1;
			String rawPassword = tmpUser.s2;

			Optional<User> tmp =
					   allUsers.stream ().filter (u -> u.verify (userName, rawPassword)).findAny ();

			// If we managed to find the user - let's return it and "log in" as that user
			if (tmp.isPresent ())
				return tmp.get ();

			DialogUtils.showMessageDialog (null, "Username or password was incorrect, try again", "log in");
		}

		return null;
	}

	private static List<Service> fetchAllServices (List<User> allUsers, User user) {
		return DataBaseUtils.selectAllServices ();
	}

	private static void run (List<Service> services, User currentUser) {
		ServiceFrame frame = new ServiceFrame ("Kry tech case", services, currentUser);

		// Open/Show the frame
		frame.setVisible (true);

		Thread httpChecker = createCheckerThread (frame, services);

		// Setting the background thread to a Daemon so that it shuts down when the program finishes.
		httpChecker.setDaemon (true);
		httpChecker.start ();
	}

	private static Thread createCheckerThread (ServiceFrame frame, List<Service> services) {
		return new Thread (new Runnable () {
			@Override public void run () {
				// This thread will periodically check all services
				while (true) {
					frame.setStatusText ("Checking services...");
					services.forEach (s -> HTTPUtils.testSingleService (s));
					frame.setStatusText ("Services have been checked. ");

					// Update the table based on the new results
					updateTable (frame);

					// Trye to sleep
					sleep ();

					// Pause period if button was pressed before or during sleeping phase.
					maybePausePeriod ();
				}
			}

			// Beacuse this thread is not on EDT - we need to tell swing to update it when it has time on EDT
			private void updateTable (ServiceFrame frame) {
				SwingUtilities.invokeLater (new Runnable () {

					@Override public void run () {
						frame.getServicePanel ().updateTable ();
					}
				});
			}

			private void sleep () {
				try {
					Thread.sleep (THREAD_SLEEP_DURATION);
				} catch (InterruptedException e) {
					e.printStackTrace ();
				}
			}

			// In order to pause automatic checking - we just trap the thread in an infinite loop
			private void maybePausePeriod () {
				while (frame.getServicePanel ().periodPaused ()) {
					frame.setStatusText ("Periodic testing disabled");
				}
			}
		});
	}

}
