package kry;

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
				                   rs.getInt ("admin") == 1, rs.getInt ("id")));

		} catch (SQLException e) {
			e.printStackTrace ();
		}

		return ret;
	}

	public static int getMaxUserId () {
		String sql = "select * from users order by id desc limit 1";
		return getMaxId (sql);
	}

	private static int getMaxId (String sql) {
		int maxId = -1;

		try (Connection conn = connect ();
		     Statement stmt = conn.createStatement ();
		     ResultSet rs = stmt.executeQuery (sql)) {
			while (rs.next ()) {
				int tmpId = rs.getInt ("id");
				if (tmpId > maxId)
					maxId = tmpId;
			}
		} catch (SQLException e) {
			e.printStackTrace ();
		}
		return maxId;
	}

	public static void storeNewUser (User newUser) {
		String sql = "insert into users (name, pwd, admin, id) values (?,?,?,?)";

		try (Connection conn = connect ();
		     PreparedStatement pstmt = conn.prepareStatement (sql);) {
			pstmt.setString (1, newUser.getUserName ());
			pstmt.setString (2, newUser.getHashedPassword ());
			pstmt.setInt (3, newUser.isAdmin () ? 1 : 0);
			pstmt.setInt (4, newUser.getId ());
			pstmt.executeUpdate ();
		} catch (SQLException e) {
			e.printStackTrace ();
		}
	}

	public static void updateUser (User user) {
		String sql = "update users set name = ?, pwd = ?, admin = ? where id = ?";

		try (Connection conn = connect ();
		     PreparedStatement pstmt = conn.prepareStatement (sql)) {
			pstmt.setString (1, user.getUserName ());
			pstmt.setString (2, user.getHashedPassword ());
			pstmt.setInt (3, user.isAdmin () ? 1 : 0);
			pstmt.setInt (4, user.getId ());
			pstmt.executeUpdate ();
		} catch (SQLException e) {
			e.printStackTrace ();
		}
	}

	public static void removeUser (User user) {
		String sql = "delete from users where id = ?";

		try (Connection conn = connect ();
		     PreparedStatement pstmt = conn.prepareStatement (sql)) {
			pstmt.setInt (1, user.getId ());
			pstmt.executeUpdate ();
		} catch (SQLException e) {
			e.printStackTrace ();
		}

		// I considered also deleting all the services that this users services.
		// But since admins can see (and delete) all services anyhow it didn't seem all that important...
	}

	public static List<Service> selectAllServices () {
		List<Service> ret = new ArrayList<> ();

		String sql = "select * from services";

		try (Connection conn = connect ();
		     Statement stmt = conn.createStatement ();
		     ResultSet rs = stmt.executeQuery (sql)) {
			while (rs.next ())
				ret.add (new Service (rs.getString ("name"), rs.getString ("url"),
				                      rs.getInt ("created_by"), rs.getString ("created_date"),
				                      rs.getString ("last_modified_date"), rs.getInt ("id")));
		} catch (SQLException e) {
			e.printStackTrace ();
		}

		return ret;
	}

	public static void storeNewService (Service service) {
		String sql = "insert into services(name, url, created_date, last_modified_date, created_by, id)" +
			     " VALUES(?,?,?,?,?,?)";

		try (Connection conn = connect ();
		     PreparedStatement pstmt = conn.prepareStatement (sql);) {
			pstmt.setString (1, service.getName ());
			pstmt.setString (2, service.getUrl ());
			pstmt.setString (3, service.getCreatedTime ());
			pstmt.setString (4, service.getLastModifiedTime ());
			pstmt.setInt (5, service.getCreatedBy ());
			pstmt.setInt (6, service.getId ());
			pstmt.executeUpdate ();
		} catch (SQLException e) {
			e.printStackTrace ();
		}
	}

	public static int getMaxServiceId () {
		String sql = "select * from services order by id desc limit 1";
		return getMaxId (sql);
	}

	public static void updateService (Service service) {
		String sql = "update services set name = ?, url = ?, last_modified_date = ? where id = ?";

		try (Connection conn = connect ();
		     PreparedStatement pstmt = conn.prepareStatement (sql)) {
			pstmt.setString (1, service.getName ());
			pstmt.setString (2, service.getUrl ());
			pstmt.setString (3, service.getLastModifiedTime ());
			pstmt.setInt (4, service.getId ());
			pstmt.executeUpdate ();
		} catch (SQLException e) {
			e.printStackTrace ();
		}
	}

	public static void removeService (Service service) {
		String sql = "delete from services where id = ?";

		try (Connection conn = connect ();
		     PreparedStatement pstmt = conn.prepareStatement (sql)) {
			pstmt.setInt (1, service.getId ());
			pstmt.executeUpdate ();
		} catch (SQLException e) {
			e.printStackTrace ();
		}
	}

	private static Connection connect () {
		Connection conn = null;

		try {
			conn = DriverManager.getConnection (URL);

		} catch (SQLException e) {
			e.printStackTrace ();
		}

		return conn;
	}
}
