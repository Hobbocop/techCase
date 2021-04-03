package kry;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Utility class to handle all HTTP requests, that will test the Services
 * Author: Filip Bark
 */
public class HTTPUtils {

	public static void testSingleService (Service service) {
		synchronized (service) {
			int status = -1;
			HttpURLConnection con = null;
			try {
				URL url = new URL (service.getUrl ());
				con = (HttpURLConnection) url.openConnection ();
				con.setRequestMethod ("GET");
				con.setReadTimeout (5000);
				con.connect ();

				status = con.getResponseCode ();

			} catch (IOException e) {
				service.updateLastResponse (false);
				return;
			} finally {
				if (con != null)
					con.disconnect ();
			}

			service.updateLastResponse (status == HttpURLConnection.HTTP_OK);
		}
	}

}
