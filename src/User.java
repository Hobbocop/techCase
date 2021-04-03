
public class User {
	private String userName;
	private String hashedPassword;
	// Normally, this shouldn't be a boolean - but probably an enum saying which level of rights this user has
	// but in this small system there are only super users and normal users
	private boolean isAdmin;
	private int id;

	public User (String userName, String rawPassword, boolean isAdmin) {
		this (userName, MyStringUtils.hashPassword (rawPassword), isAdmin, generateNewId ());
	}

	public User (String userName, String hashedPassword, boolean isAdmin, int id) {
		this.userName = userName;
		this.hashedPassword = hashedPassword;
		this.isAdmin = isAdmin;
	}

	private static int generateNewId () {
		var tmp = DataBaseUtils.getMaxUserId ();
		System.out.println ("Generating new userId: " + tmp);
		return tmp;
	}

	public String getUserName () {
		return userName;
	}

	public String getHashedPassword () {
		return hashedPassword;
	}

	public boolean isAdmin () {
		return isAdmin;
	}

	public int getId () {
		return id;
	}

	boolean verify (String enteredUserName, String rawPassword) {
		boolean verified = userName.equals (enteredUserName);
		verified &= hashedPassword.equals (rawPassword);

		return verified;
	}

	// For a user, if the username is the same - it's the same user!
	@Override public boolean equals (Object obj) {
		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (obj instanceof User)
			return userName.equals (((User) obj).getUserName ());

		return false;
	}

	// Because the user is same if username is same, the hashcode should depend on username as well
	@Override public int hashCode () {
		return userName.hashCode ();
	}

	@Override public String toString () {
		return userName;
	}

}
