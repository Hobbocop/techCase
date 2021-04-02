import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Utility class to handle all HTTP requests, that will test the Services
 * Author: Filip Bark
 */
public class HTTPUtils {

	public static boolean testSingleService (Service service) {
		int status = -1;
		HttpURLConnection con = null;
		try {
			URL url = new URL (service.getUrl ());
			con = (HttpURLConnection) url.openConnection ();
			con.setRequestMethod ("GET");
			con.setReadTimeout (5000);
			con.connect ();

			status = con.getResponseCode ();
			System.out.println ("We got " + status);
		} catch (IOException e) {
			System.out.print ("Uh oh");
			return false;
		} finally {
			if (con != null)
				con.disconnect ();
		}

		if (status == HttpURLConnection.HTTP_OK)
			return true;

		return false;
	}

}
