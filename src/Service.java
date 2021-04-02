import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
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
	private String user;
	private String lastModified;
	private String created;
	private Boolean lastResponseOk;

	// Constructor for newly created Services, creating a time-stamp when they are created
	public Service (String name, String url, String user) {
		this (name, url, user, MyStringUtils.getNow (), null);
	}

	// Constructor for stored Services that already have a timestamp
	public Service (String name, String url, String user, String created, String lastModified) {
		this.serviceName = name;
		this.url = url;
		this.user = user;
		this.created = created;
		this.lastModified = lastModified;
		lastResponseOk = null;
	}

	public String getName () {
		return serviceName;
	}

	public void updateName (String newName) {
		this.serviceName = newName;
		lastModified = MyStringUtils.getNow ();
	}

	public String getUrl () {
		return url;
	}

	public String getCreatedBy () {
		return user;
	}

	public void updateUrl (String newUrl) throws MalformedURLException, URISyntaxException {
		// TODO - for the actual release, should verity email bfore updating!!!
		// this.url = verify (newUrl);
		this.url = newUrl;
		lastModified = MyStringUtils.getNow ();
	}

	private String verify (String newUrl) throws MalformedURLException, URISyntaxException {
		URL u = new URL (url);
		return u.toURI ().toString ();
	}

	public synchronized void updateLastResponse (boolean response) {
		lastResponseOk = response;
	}

	public synchronized Optional<Boolean> getLastResponseOk () {
		return Optional.ofNullable (lastResponseOk);
	}

	public synchronized String getCreatedTime () {
		return created;
	}

	public synchronized String getLastModifiedTime () {
		return lastModified;
	}

	// Users should only be able to see their own services, admins should be able to see all!
	public boolean shouldShowFor (User currentUser) {
		return user == null || currentUser.isAdmin || user.equals (currentUser.getUserName ());
	}

}
