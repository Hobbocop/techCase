import java.sql.Timestamp;
import java.util.Optional;

public class Service {
	private String serviceName;
	private String url;
	private User user;
	private Timestamp lastModified;
	private boolean lastResponseOk;
	private Timestamp created;

	// Constructor for newly created Services, creating a time-stamp when they are created
	public Service (String name, String url, User user) {
		this (name, url, user, new Timestamp (System.currentTimeMillis ()));
	}

	// Constructor for stored Services that already have a timestamp
	public Service (String name, String url, User user, Timestamp created) {
		this.serviceName = name;
		this.url = url;
		this.user = user;
		this.created = created;
	}

	public String getName () {
		return serviceName;
	}

	public String getUrl () {
		return url;
	}

	public void setLastResponse (boolean response) {
		lastResponseOk = response;
		lastModified = new Timestamp (System.currentTimeMillis ());
	}

	public Optional<Boolean> getLastResponseOk () {
		return Optional.of (lastResponseOk);
	}

	public Timestamp getCreatedTimeStamp () {
		return created;
	}

	public Timestamp getLastModifiedTimeStamp () {
		return lastModified;
	}
	// Users should only be able to see their own services, admins should be able to see all!
	public boolean shouldShowFor (User currentUser) {
		return user == null || user.isAdmin () || user.equals (currentUser);
	}

}
