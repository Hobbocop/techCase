import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class TechCase {
	static User currentUser;
	static List<User> allUsers;
	static List<Service> services;

	public static void main (String[] args) {
		// [MAYBE]  TODO - add support for I18n handling
		// This would include adding a language setting (perhaps in the user?)
		// A new text file with entries for all different languages
		System.out.println ("Hello world!");

		init ();

		if (currentUser == null) {
			System.out.println ("No user found");
			return;
		}

		// TODO [In progress] - add GUI features 
		// TODO - add http request support to test services
		// TODO - add database support
		run ();
	}

	// Will initialize everything, load all data from database and also log in current user
	private static void init () {
		// TODO - Open up a "loading" dialog while loading stuff from server 
		allUsers = fetchAllUsers ();
		services = fetchAllServices ();
		currentUser = logIn ();
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

	private static List<Service> fetchAllServices () {
		// TODO - Currently just setting up a test-set of users
		// TODO - should instead load from database
		List<Service> tmp = new ArrayList<> ();
		tmp.add (new Service ("1", "1", null));
		tmp.add (new Service ("2", "2", allUsers.get (1)));
		tmp.add (new Service ("Test ok", "https://www.google.com", allUsers.get (0)));
		tmp.add (new Service ("Test not ok", "https://www.probablyNotAnAddressThatExists.com",
		                      allUsers.get (0)));
		return tmp;
	}

	private static User logIn () {
		// TODO - create a Swing dialog that asks for username and password (maybe not closing until logged in)
		String userName = "AdminFilip";
		String rawPassword = "123"; // TODO - should be hashed!!

		Optional<User> tmp = allUsers.stream ().filter (u -> u.verify (userName, rawPassword)).findAny ();
		if (tmp.isPresent ())
			return tmp.get ();
		return null;
	}

	private static void run () {
		// TODO - this should be moved to a separate class, maybe a KryFrame?
		// TODO - The frame should contain a panel, which in turns contains a Table (of all services) and buttons
		JFrame frame = new JFrame ("Kry tech case");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

		// Create a temporary label so we have something in the window...
		JLabel emptyLabel = new JLabel ("Logged in as: " + currentUser);
		emptyLabel.setPreferredSize (new Dimension (800, 800));
		frame.getContentPane ().add (emptyLabel, BorderLayout.CENTER);

		// Open/Show the frame
		frame.pack ();
		frame.setVisible (true);
	}

}
