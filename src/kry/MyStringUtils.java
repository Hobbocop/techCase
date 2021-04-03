package kry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for handling strings
 *
 * Author: Filip Bark
 */
public class MyStringUtils {

	private static final String DATE_FORMAT = "HH:mm:ss yyyy-MM-dd";
	private static final String URL_REGEX = "((http|https)://)?(www.)?"
						+ "[a-zA-Z0-9 @:%._\\+~#?&//=]{2,256}\\.[a-z]"
						+ "{2,6}\\b([-a-zA-Z0-9@:%._\\+~#?&//=]*)";

	public static String getNow () {

		Date tmp = new Date ();
		SimpleDateFormat formatter = new SimpleDateFormat (DATE_FORMAT);
		return formatter.format (tmp);
	}

	public static String hashPassword (String rawPassword) {
		// Just a symbolic hash. Would normally do something more impressive...
		return String.valueOf (rawPassword.hashCode ());
	}

	public static boolean verifyUrl (String newUrl) {
		Pattern p = Pattern.compile (URL_REGEX);
		Matcher m = p.matcher (newUrl);
		System.out.println (newUrl + " -> " + m.matches ());
		return m.matches ();
	}

}
