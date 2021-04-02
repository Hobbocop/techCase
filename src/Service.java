import java.sql.Timestamp;
import java.util.Optional;

public class Service {
	private String serviceName;
	private String url;
	private User user;
	private Timestamp lastModified;
	private boolean lastResponseOk;

	public Service (String name, String url, User user) {
		this.serviceName = name;
		this.url = url;
		this.user = user;
	}

	public String getName () {
		return serviceName;
	}

	public String getUrl () {
		return url;
	}

	// Users should only be able to see their own services, admins should be able to see all!
	public boolean shouldShowFor (User currentUser) {
		// TODO - Admins should be able to see all, add this specific 
		return user == null || user.isAdmin () || user.equals(currentUser);
	}

	public Optional<Timestamp> getLastModified () {
		return Optional.of (lastModified);
	}

	public Optional<Boolean> getLastResponseOk () {
		return Optional.of (lastResponseOk);
	}

	public void setLastResponse (boolean response) {
		lastResponseOk = response;
		lastModified = new Timestamp (System.currentTimeMillis ());
	}

}
