import java.awt.Dimension;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

import java.awt.*;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * The main panel for displaying buttons and table. Includes the action listeners for the buttons.
 *
 * Author: Filip Bark
 */
public class ServiceTablePanel extends JPanel {

	// Serial version is in format date-time to certify uniqueness
	// I won't actually serialize it, but JFrames implements serializable so I have to declare
	private static final long serialVersionUID = 2021_04_02_13_24L;
	private JTable serviceTable;
	private boolean periodicTestingEnabled;
	private User currentUser;

	private ServiceTableModel serviceTableModel;

	public ServiceTablePanel (List<Service> services, User currentUser) {
		super (new BorderLayout ());

		setPreferredSize (new Dimension (800, 800));
		periodicTestingEnabled = true;
		this.currentUser = currentUser;

		serviceTable = createTable (services);
		add (new JScrollPane (serviceTable), BorderLayout.CENTER);

		JPanel buttonPanel = createButtonPanel ();
		add (buttonPanel, BorderLayout.SOUTH);
	}

	private JTable createTable (List<Service> services) {
		// Hard coded for ease of use, should be changed later on for scalability
		String[] columnNames = {"Name", "Checked", "Status", "Added", "Url"};

		serviceTableModel = new ServiceTableModel (services, columnNames);

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
		var service = serviceTableModel.getService (serviceTable.getSelectedRow ());

		var st = new StringTuple (service.getName (), service.getUrl ());

		DialogUtils.showEditServiceDialog (this, st, "Edit service");

		try {
			service.updateName (st.s1);
			service.updateUrl (st.s2);
		} catch (MalformedURLException | URISyntaxException e) {
			handleUrlException (st.s2);
		}

		updateServiceInModel (service);
		return null;
	}

	private void updateServiceInModel (Service service) {
		// TODO - update service in database...
		serviceTable.updateUI ();
	}

	private void handleUrlException (String url) {
		System.out.println ("Invalid URL: " + url);
		JOptionPane.showMessageDialog (this, "Invalid URL: " + url, "Edit thing? ", JOptionPane.ERROR_MESSAGE);
	}

	private Object addNewService () {
		var sd = new StringTuple ("", "");

		DialogUtils.showEditServiceDialog (this, sd, "Create new service");

		if (sd.s1.equals ("") || sd.s2.equals (""))
			return null;

		var newService = new Service (sd.s1, "", currentUser);
		try {
			newService.updateUrl (sd.s2);
		} catch (MalformedURLException | URISyntaxException e) {
			handleUrlException (sd.s2);
			return null;
		}

		addNewServiceToModel (newService);

		return null;
	}

	private void addNewServiceToModel (Service newService) {
		// TODO - also add service to Database!
		serviceTableModel.AddService (newService);
		serviceTable.updateUI ();
	}

	private Object manuallyTestSelectedService () {
		// TODO - manually perform a http test for the selected service
		System.out.println ("Test service in row " + serviceTable.getSelectedRow ());

		var service = serviceTableModel.getService (serviceTable.getSelectedRow ());

		service.updateLastResponse (HTTPUtils.testSingleService (service));
		serviceTable.updateUI ();
		return null;
	}

	private Object toggleAutomaticTesting (JButton srcButton) {
		if (periodicTestingEnabled) {
			periodicTestingEnabled = false;
			srcButton.setText ("Resume automatic testing");
		} else {
			periodicTestingEnabled = true;
			srcButton.setText ("Pause automatic testing");
		}

		return true;
	}
}
