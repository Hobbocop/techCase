package kry;

import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * Underlying table model to enable Services to be reprensted easily in a table format.
 * Includes some additional methods to add and remove services.
 *
 * Author: Filip Bark
 */
public class ServiceTableModel extends AbstractTableModel {

	private static final String NO_DATA = "-";
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

	public void addService (Service service) {
		services.add (service);
	}

	public void removeService (Service service) {
		services.remove (service);
	}

	// This method is called by the JTable to figure out what should be written in each cell.
	// This is the method that's used to explain how a list of services becomes a bunch of cells
	@Override public Object getValueAt (int row, int column) {
		var tmp = services.get (row);

		// This isn't the prettiest method of converting a Service into a bunch of columns, but I can't figure
		// out a better method right now.
		switch (column) {
			case 0:
				return tmp.getName ();
			case 1:
				var response = tmp.getLastResponseOk ();
				if (response.isPresent ())
					return formatResponse (response.get ());
				return NO_DATA;
			case 2:
				return handleTime (tmp.getCreatedTime ());
			case 3:
				return handleTime (tmp.getLastModifiedTime ());
			case 4:
				return tmp.getUrl ();
			default:
				return NO_DATA;
		}
	}


	// Timestamps are the underlying database type, they shouldn't be shown to the user.
	// This method [will] format them to look nicer.
	private Object handleTime (String time) {
		if (time == null)
			return NO_DATA;

		return time;
	}

	private Object formatResponse (Boolean response) {
		if (response)
			return "Online";
		return "Offline";
	}

	// None of the cells should be editable...
	@Override public boolean isCellEditable (int row, int column) {
		return false;
	}

	// Columnnames are as defined by the input data
	@Override public String getColumnName (int column) {
		return columnNames[column];
	}

	// There will always be one row per service
	@Override public int getRowCount () {
		return services.size ();
	}

	// Columncount is defined by columnNames
	@Override public int getColumnCount () {
		return columnNames.length;
	}
}
