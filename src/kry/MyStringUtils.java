package kry;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for handling strings
 *
 * Author: Filip Bark
 */
public class MyStringUtils {

	private static final String DATE_FORMAT = "HH:mm:ss yyyy-MM-dd";

	public static String getNow () {

		Date tmp = new Date ();
		SimpleDateFormat formatter = new SimpleDateFormat (DATE_FORMAT);
		return formatter.format (tmp);
	}

	public static String hashPassword (String rawPassword) {
		// Just a symbolic hash. Would normally do something more impressive...
		return String.valueOf (rawPassword.hashCode ());
	}

}
