import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.JFrame;

public class TechCase {

	public static void main (String[] args) {
		// [MAYBE]  TODO - add support for I18n handling
		// This would include adding a language setting (perhaps in the user?)
		// A new text file with entries for all different languages
		System.out.println ("Hello world!");

		// Initialize and load all data
		List<User> allUsers = fetchAllUsers ();  // TODO - Open up a "loading" dialog while loading stuff from server
		User currentUser = logIn (allUsers);
		List<Service> services = fetchAllServices (allUsers, currentUser);

		// If we were unable to log in - just close the program. This shouldn't really happen...
		if (currentUser == null) {
			System.out.println ("No user found");
			return;
		}

		// TODO [In progress] - add GUI features 
		// TODO - add http request support to test services
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
		return allUsers;
	}

	private static User logIn (List<User> allUsers) {
		// TODO - create a Swing dialog that asks for username and password (maybe not closing until logged in)
		String userName = "AdminFilip";
		String rawPassword = "123"; // TODO - should be hashed!!

		Optional<User> tmp = allUsers.stream ().filter (u -> u.verify (userName, rawPassword)).findAny ();
		if (tmp.isPresent ())
			return tmp.get ();
		return null;
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
		JFrame frame = new ServiceFrame ("Kry tech case", services, currentUser);

		// Open/Show the frame
		frame.setVisible (true);
	}

}
