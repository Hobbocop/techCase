
public class User {
	String userName;
	// Todo - should obviously never store the user password in clear text... Always use and store hashed versions
	String hashedPassword;
	// Normally, this shouldn't be a boolean - but probably an enum saying which level of rights this user has
	// but in this small system there are only super users and normal users
	boolean isAdmin;

	public User (String userName, String rawPassword, boolean isAdmin) {
		this.userName = userName;
		// TODO - hash password somehow
		this.hashedPassword = rawPassword;
		this.isAdmin = isAdmin;
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

	boolean verify (String enteredUserName, String rawPassword) {
		boolean verified = userName.equals (enteredUserName);

		// TODO - use the hashing algorithm to hash the entered password, potentionally not here...
		// TODO - enteredPassword = hash (rawPassword);

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
