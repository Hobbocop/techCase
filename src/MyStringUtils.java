import java.text.SimpleDateFormat;
import java.util.Date;

public class MyStringUtils {

	private static final String DATE_FORMAT = "HH:mm:ss yyyy-MM-dd";

	public static String getNow () {

		Date tmp = new Date ();
		SimpleDateFormat formatter = new SimpleDateFormat (DATE_FORMAT);
		return formatter.format (tmp);
	}

	// TODO - add a HashMethod for password handling!

}
