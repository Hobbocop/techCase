
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to interface with the database, will use SQLite database
 *
 * Author: Filip Bark
 */
public class DataBaseUtils {

	private static final String URL = "jdbc:sqlite:db/best.db";

	public static List<User> selectAllUsers () {
		List<User> ret = new ArrayList<> ();

		String sql = "select * from users";

		try (Connection conn = connect ();
		     Statement stmt = conn.createStatement ();
		     ResultSet rs = stmt.executeQuery (sql)) {
			while (rs.next ())
				ret.add (new User (rs.getString ("name"), rs.getString ("pwd"),
				                   rs.getInt ("admin") == 1));

		} catch (SQLException e) {
			System.out.println (e.getMessage ());
		}

		return ret;
	}

	public static List<Service> selectAllServices () {
		List<Service> ret = new ArrayList<> ();

		String sql = "select * from services";

		try (Connection conn = connect ();
		     Statement stmt = conn.createStatement ();
		     ResultSet rs = stmt.executeQuery (sql)) {
			while (rs.next ())
				ret.add (new Service (rs.getString ("name"), rs.getString ("url"),
				                      rs.getString ("created_by"), rs.getString ("created_date"),
				                      rs.getString ("last_modified_date")));

		} catch (SQLException e) {
			System.out.println (e.getMessage ());
		}

		return ret;
	}

	public static int getMaxServiceId () {
		return 0;
	}

	public static void storeNewService (Service service) {
		String sql = "insert into services(name, url, created_date, last_modified_date, created_by)" +
		" VALUES(?,?,?,?,?)";

		try (Connection conn = connect ();
		     PreparedStatement pstmt = conn.prepareStatement (sql)) {
			pstmt.setString (1, service.getName ());
			pstmt.setString (2, service.getUrl ());
			pstmt.setString (3, service.getCreatedTime ());
			pstmt.setString (4, service.getLastModifiedTime ());
			pstmt.setString (5, service.getCreatedBy ());
			pstmt.executeUpdate ();
		} catch (SQLException e) {
			System.out.println (e.getMessage ());
		}

	}

	public static void updateService (Service service) {
		// TODO - add a method to update Services... This requires some sort of ID for each service that won't get modified...
	}

	public static void removeService (Service service) {
		// TODO - add a method to remove Services... This requires some sort of ID for each service that won't get modified...
	}

	public static int getMaxUserId () {
		return 0;
	}

	private static Connection connect () {
		Connection conn = null;

		try {
			conn = DriverManager.getConnection (URL);
			System.out.println ("Successfully connected to database");

		} catch (SQLException e) {
			e.printStackTrace ();
		}

		return conn;
	}
}
