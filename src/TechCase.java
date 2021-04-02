import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
		// [MAYBE]  TODO - add support for I18n handling
		// This would include adding a language setting (perhaps in the user?)
		// A new text file with entries for all different languages
		System.out.println ("Hello world!");

		// Initialize and load all data
		List<User> allUsers = fetchAllUsers ();
		User currentUser = logIn (allUsers);
		List<Service> services = fetchAllServices (allUsers, currentUser);

		// If we were unable to log in - just close the program. This shouldn't really happen...
		if (currentUser == null) {
			System.out.println ("No user found");
			return;
		}

		// TODO - add database support

		// Start the program!
		run (services, currentUser);
	}

	// Load all stored users from database
	private static List<User> fetchAllUsers () {
		// TODO - Currently just setting up a test-set of users
		// TODO - should instead load from database
		List<User> allUsers = new ArrayList<> ();
		allUsers.add (new User ("AdminFilip", "123", true));
		allUsers.add (new User ("NonAdminFilip", "321", false));
		allUsers.add (new User ("1", "1", true));
		return allUsers;
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
			if (tmp.isPresent ())
				return tmp.get ();

			DialogUtils.showMessageDialog (null, "Username or password was incorrect, try again", "log in");
			System.out.println ("wat " + currentUser);
		}

		return currentUser;
	}

	private static List<Service> fetchAllServices (List<User> allUsers, User user) {
		// TODO - Currently just setting up a test-set of users
		// TODO - should instead load from database
		List<Service> tmp = new ArrayList<> ();
		tmp.add (new Service ("1", "1", null));
		tmp.add (new Service ("2", "2", allUsers.get (1)));
		tmp.add (new Service ("Test ok", "https://www.google.com", allUsers.get (0)));
		tmp.add (new Service ("Test not ok", "https://www.probablyNotAnAddressThatExists.com",
		                      allUsers.get (0)));

		return tmp.stream ().filter (s -> s.shouldShowFor (user)).collect (Collectors.toList ());
	}

	private static void run (List<Service> services, User currentUser) {
		// TODO - this should be moved to a separate class, maybe a KryFrame?
		// TODO - The frame should contain a panel, which in turns contains a Table (of all services) and buttons
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
