package kry;

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
	private int userId;
	private String lastModified; // Normally this would be Dates/Timestamps/Instants, but sqlite doesn't handle dates
	private String created; // Normally this would be Dates/Timestamps/Instants, but sqlite doesn't handle dates
	private Boolean lastResponseOk;
	private final int id; // Id can't be null, so using int instead of Int

	// Constructor for newly created Services, creating a time-stamp when they are created
	public Service (String name, String url, int userId) {
		this (name, url, userId, MyStringUtils.getNow (), null, generateNewId ());
	}

	// Constructor for stored Services that already have a timestamp
	public Service (String name, String url, int userId, String created, String lastModified, int id) {
		this.serviceName = name;
		this.url = url;
		this.userId = userId;
		this.created = created;
		this.lastModified = lastModified;
		lastResponseOk = null;
		this.id = id;
	}

	private static int generateNewId () {
		return DataBaseUtils.getMaxServiceId () + 1;
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

	public int getCreatedBy () {
		return userId;
	}

	public int getId () {
		return id;
	}

	public boolean updateUrl (String newUrl) {
		if (newUrl == null || !MyStringUtils.verifyUrl (newUrl))
			return false;

		this.url = newUrl;
		lastModified = MyStringUtils.getNow ();
		return true;
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
		return userId == -1 || currentUser.isAdmin () || userId == currentUser.getId ();
	}

}
