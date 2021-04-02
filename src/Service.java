import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Optional;

/**
 * Class used to represent a service - with some minor utility methods.
 *
 * It will verify that url entered (when updating url) is a valid url.
 *
 *  Author: Filip Bark
 */
public class Service {
	private String serviceName;
	private String url;
	private User user;
	private Timestamp lastModified;
	private Boolean lastResponseOk;
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
		this.lastResponseOk = null;
	}

	public String getName () {
		return serviceName;
	}

	public void updateName (String newName) {
		this.serviceName = newName;
	}

	public String getUrl () {
		return url;
	}

	public void updateUrl (String newUrl) throws MalformedURLException, URISyntaxException {
		// TODO - for the actual release, should verity email bfore updating!!!
		// this.url = verify (newUrl);
		this.url = newUrl;
	}

	private String verify (String newUrl) throws MalformedURLException, URISyntaxException {
		URL u = new URL (url);
		return u.toURI ().toString ();
	}

	public void setLastResponse (boolean response) {
		lastResponseOk = response;
		lastModified = new Timestamp (System.currentTimeMillis ());
	}

	public Optional<Boolean> getLastResponseOk () {
		return Optional.ofNullable (lastResponseOk);
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
