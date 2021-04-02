import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.List;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class ServiceTablePanel extends JPanel {

	// Serial version is in format date-time to certify uniqueness
	// I won't actually serialize it, but JFrames implements serializable so I have to declare
	private static final long serialVersionUID = 2021_04_02_13_24L;

	private JTable serviceTable;
	private boolean periodicTestingEnabled;

	public ServiceTablePanel (List<Service> services, User currentUser) {
		super (new BorderLayout ());
		setPreferredSize (new Dimension (800, 800));
		periodicTestingEnabled = true;

		serviceTable = createTable (services);
		add (new JScrollPane (serviceTable), BorderLayout.CENTER);

		JPanel buttonPanel = createButtonPanel ();
		add (buttonPanel, BorderLayout.SOUTH);
	}

	private JTable createTable (List<Service> services) {
		// Hard coded for ease of use, should be changed later on for scalability
		String[] columnNames = {"Name", "Checked", "Status", "Added", "Url"};
		TableModel serviceTableModel = new ServiceTableModel (services, columnNames);
		JTable table = new JTable (serviceTableModel);
		table.setFillsViewportHeight (true);
		table.setRowSelectionAllowed (true);
		table.setAutoResizeMode (JTable.AUTO_RESIZE_LAST_COLUMN);
		return table;
	}

	private JPanel createButtonPanel () {
		JPanel buttonPanel = new JPanel ();

		JButton editServiceButton = new JButton ("Edit selected service");
		editServiceButton.addActionListener (e -> editSelectedService ());
		buttonPanel.add (editServiceButton);

		JButton addServiceButton = new JButton ("Add new service");
		addServiceButton.addActionListener (e -> addNewService ());
		buttonPanel.add (addServiceButton);

		JButton testServiceButton = new JButton ("Manually test service");
		testServiceButton.addActionListener (e -> manuallyTestSelectedService ());
		buttonPanel.add (testServiceButton);

		JButton toggleAutomaticTesting = new JButton ("Pause automatic testing");
		toggleAutomaticTesting.addActionListener (e -> toggleAutomaticTesting (toggleAutomaticTesting));
		buttonPanel.add (toggleAutomaticTesting);

		return buttonPanel;

	}

	private Object editSelectedService () {
		// TODO - open dialog for editing selected service
		System.out.println ("Button 1 has been pressed! Table row " + serviceTable.getSelectedRow ()
		                    + " is selected");
		return null;
	}

	private Object addNewService () {
		// TODO - open dialog to add new service and add it to the tableModel
		System.out.println ("Add new service!");
		return null;
	}

	private Object manuallyTestSelectedService () {
		// TODO - manually perform a http test for the selected service
		System.out.println ("Test service in row " + serviceTable.getSelectedRow ());
		return null;
	}

	private Object toggleAutomaticTesting (JButton srcButton) {
		if (periodicTestingEnabled) {
			periodicTestingEnabled = false;
			System.out.println ("Let's pause the testing!");
			srcButton.setText ("Resume automatic testing");
		} else {
			periodicTestingEnabled = true;
			System.out.println ("Let's resume the testing!");
			srcButton.setText ("Pause automatic testing");
		}

		return true;
	}
}
