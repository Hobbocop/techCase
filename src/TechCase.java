import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TechCase {
	// TODO - these shouldn't really be a global variable in here...
	static User currentUser;
	static List<Service> services;

	public static void main (String[] args) {
		System.out.println ("Hello world!");

		// TODO - add a hashing helper class to hash passwords
		User admin = new User ("AdminFilip", "123", true);
		User nonAdmin = new User ("NonAdminFilip", "321", false);
		currentUser = logIn ();
		if (currentUser == null) {
			System.out.println ("No user found");
			return;
		}

		// TODO - Just adding temporary items here, probably shouldn't be handled here
		List<Service> tmp = new ArrayList<> ();
		tmp.add (new Service ("1", "1", null));
		tmp.add (new Service ("2", "2", nonAdmin));
		tmp.add (new Service ("Test ok", "https://www.google.com", admin));
		tmp.add (new Service ("Test not ok", "https://www.probablyNotAnAddressThatExists.com", admin));

	}

	// TODO - should fetch 
	private static User logIn () {
		List<User> allStoredUsers = fetchAllUsers ();
		String userName = "AdminFilip";
		String rawPassword = "123"; // TODO - should be hashed!!

		Optional<User> tmp = allStoredUsers.stream ().filter (u -> u.verify (userName, rawPassword)).findAny ();
		if (tmp.isPresent ())
			return tmp.get ();
		return null;
	}

	// TODO - Should actually fetch users from Database, ask for user credentials and then return the correct user
	private static List<User> fetchAllUsers () {
		List<User> allUsers = new ArrayList<> ();
		allUsers.add (new User ("AdminFilip", "123", true));
		allUsers.add (new User ("NonAdminFilip", "321", false));
		return allUsers;
	}
}
