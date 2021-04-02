import java.sql.Timestamp;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class ServiceTableModel extends AbstractTableModel {

	// Serial version is in format date-time to certify uniqueness
	// I won't actually serialize it, but JFrames implements serializable so I have to declare
	private static final long serialVersionUID = 2021_04_02_13_44L;

	String[] columnNames;
	List<Service> services;

	public ServiceTableModel (List<Service> services, String[] columnNames) {
		this.services = services;
		this.columnNames = columnNames;
	}

	public Service getService (int row) {
		return services.get (row);
	}

	public void AddService (Service service) {
		services.add (service);
		System.out.println (services);
	}

	@Override public Object getValueAt (int row, int column) {
		var tmp = services.get (row);

		//This is ugly, but it works and it's easy to code... Sorry...
		switch (column) {
			case 0:
				return tmp.getName ();
			case 1:
				return handleTimeStamp (tmp.getLastModifiedTimeStamp ());
			case 2:
				var response = tmp.getLastResponseOk ();
				if (response.isPresent ())
					return response.get ();
				return "-";
			case 3:
				return handleTimeStamp (tmp.getCreatedTimeStamp ());
			case 4:
				return tmp.getUrl ();
			default:
				return "-";
		}
	}

	private Object handleTimeStamp (Timestamp time) {
		if (time == null)
			return "-";

		//TODO - format this to look pretty... Maybe even convert to Date or some such
		return time;
	}

	// None of the cells should be editable...
	@Override public boolean isCellEditable (int row, int column) {
		return false;
	}

	// Columnnames are as defined by the input data
	@Override public String getColumnName (int column) {
		return columnNames[column];
	}

	@Override public int getRowCount () {
		return services.size ();
	}

	@Override public int getColumnCount () {
		return columnNames.length;
	}
}
