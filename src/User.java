package kry;

/**
 * Container class with details regaring users.
 *
 * Author: Filip Bark
 */
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
		this.id = id;
	}

	private static int generateNewId () {
		var tmp = DataBaseUtils.getMaxUserId () + 1;
		System.out.println ("Generating new userId: " + tmp);
		return tmp;
	}

	public String getUserName () {
		return userName;
	}

	public void updateUserName (String newName) {
		this.userName = newName;
	}

	public String getHashedPassword () {
		return hashedPassword;
	}

	public void updatePassword (String newPassword) {
		this.hashedPassword = MyStringUtils.hashPassword (newPassword);
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

	// For a user, if the id is the same - it's the same user!
	@Override public boolean equals (Object obj) {
		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (obj instanceof User)
			return id == ((User) obj).getId ();

		return false;
	}

	// Because the user is same if username is same, the hashcode should depend on username as well
	@Override public int hashCode () {
		// ints don't require hashing but can be consider a (very simple) hash by themselves
		return id;
	}

	@Override public String toString () {
		return userName;
	}

	public void setAdmin (boolean setAdmin) {
		this.isAdmin = setAdmin;
	}

}
