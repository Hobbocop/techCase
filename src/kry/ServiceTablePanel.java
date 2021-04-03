package kry;

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

	private static final String EDIT_SERVICE_DIALOG_TITLE = "Edit selected service";
	private static final String ADD_SERVICE_DIALOG_TITLE = "Add new service";
	private static final String REMOVE_SERVICE_DIALOG_TITLE = "Remove selected service";
	private static final String NO_SERVICE_MESSAGE = "No service selected";
	private static final String MANUAL_TEST_MESSAGE = "Manually test selected service";
	private static final String PAUSE_AUTO_TEST_MESSAGE = "Pause automatic testing";
	private static final String RESUME_AUTO_TEST_MESSAGE = "Resume automatic testing";

	// Serial version is in format date-time to certify uniqueness
	// I won't actually serialize it, but JFrames implements serializable so I have to declare
	private static final long serialVersionUID = 2021_04_02_13_24L;
	private JTable serviceTable;
	private boolean periodicTestingPaused;
	private User currentUser;

	private ServiceTableModel serviceTableModel;

	public ServiceTablePanel (List<Service> services, User currentUser) {
		super (new BorderLayout ());

		setPreferredSize (new Dimension (900, 500));
		periodicTestingPaused = false;
		this.currentUser = currentUser;

		serviceTable = createTable (services);
		add (new JScrollPane (serviceTable), BorderLayout.CENTER);

		JPanel buttonPanel = createButtonPanel ();
		add (buttonPanel, BorderLayout.SOUTH);
	}

	private JTable createTable (List<Service> services) {
		// Hard coded for ease of use, should be changed later on for scalability
		String[] columnNames = {"Service name", "Service status", "Service added", "Service last modeified",
					"Service Url"};

		serviceTableModel = new ServiceTableModel (services, columnNames);

		JTable table = new JTable (serviceTableModel);
		table.setFillsViewportHeight (true);
		table.setRowSelectionAllowed (true);
		table.setAutoResizeMode (JTable.AUTO_RESIZE_LAST_COLUMN);

		return table;
	}

	private JPanel createButtonPanel () {
		JPanel buttonPanel = new JPanel ();

		JButton editServiceButton = new JButton (EDIT_SERVICE_DIALOG_TITLE);
		editServiceButton.addActionListener (e -> editSelectedService ());
		buttonPanel.add (editServiceButton);

		JButton addServiceButton = new JButton (ADD_SERVICE_DIALOG_TITLE);
		addServiceButton.addActionListener (e -> addNewService ());
		buttonPanel.add (addServiceButton);

		JButton deleteServiceButton = new JButton (REMOVE_SERVICE_DIALOG_TITLE);
		deleteServiceButton.addActionListener (e -> removeSelectedService ());
		buttonPanel.add (deleteServiceButton);

		JButton testServiceButton = new JButton (MANUAL_TEST_MESSAGE);
		testServiceButton.addActionListener (e -> manuallyTestSelectedService ());
		buttonPanel.add (testServiceButton);

		JButton toggleAutomaticTesting = new JButton (PAUSE_AUTO_TEST_MESSAGE);
		toggleAutomaticTesting.addActionListener (e -> toggleAutomaticTesting (toggleAutomaticTesting));
		buttonPanel.add (toggleAutomaticTesting);

		return buttonPanel;
	}

	private void editSelectedService () {
		if (serviceTable.getSelectedRow () == -1) {
			DialogUtils.showMessageDialog (this, NO_SERVICE_MESSAGE, EDIT_SERVICE_DIALOG_TITLE);
			return;
		}

		var service = serviceTableModel.getService (serviceTable.getSelectedRow ());

		var st = new StringTuple (service.getName (), service.getUrl ());

		DialogUtils.showEditServiceDialog (this, st, EDIT_SERVICE_DIALOG_TITLE);

		try {
			service.updateName (st.s1);
			service.updateUrl (st.s2);
		} catch (MalformedURLException | URISyntaxException e) {
			handleUrlException (st.s2);
		}

		synchronized (service) {
			updateServiceInModel (service);
		}
	}

	private void updateServiceInModel (Service service) {
		DataBaseUtils.updateService (service);
		updateTable ();
	}

	private void handleUrlException (String url) {
		JOptionPane.showMessageDialog (this, "Invalid URL: " + url, EDIT_SERVICE_DIALOG_TITLE,
					       JOptionPane.ERROR_MESSAGE);
	}

	private void addNewService () {
		var sd = new StringTuple ("", "");

		DialogUtils.showEditServiceDialog (this, sd, ADD_SERVICE_DIALOG_TITLE);

		if (sd.s1.equals ("") || sd.s2.equals (""))
			return;

		var newService = new Service (sd.s1, "", currentUser.getId ());
		try {
			newService.updateUrl (sd.s2);
		} catch (MalformedURLException | URISyntaxException e) {
			handleUrlException (sd.s2);
			return;
		}

		addNewServiceToModel (newService);
	}

	private void addNewServiceToModel (Service newService) {
		DataBaseUtils.storeNewService (newService);
		serviceTableModel.addService (newService);
		updateTable ();
	}

	private void removeSelectedService () {
		if (serviceTable.getSelectedRow () == -1) {
			DialogUtils.showMessageDialog (this, NO_SERVICE_MESSAGE, REMOVE_SERVICE_DIALOG_TITLE);
			return;
		}

		var service = serviceTableModel.getService (serviceTable.getSelectedRow ());
		serviceTableModel.removeService (service);
		DataBaseUtils.removeService (service);
		updateTable ();
	}

	private Object manuallyTestSelectedService () {
		var service = serviceTableModel.getService (serviceTable.getSelectedRow ());

		HTTPUtils.testSingleService (service);
		updateTable ();
		return null;
	}

	private Object toggleAutomaticTesting (JButton srcButton) {
		if (!periodicTestingPaused) {
			periodicTestingPaused = true;
			srcButton.setText (RESUME_AUTO_TEST_MESSAGE);
		} else {
			periodicTestingPaused = false;
			srcButton.setText (PAUSE_AUTO_TEST_MESSAGE);
		}

		return true;
	}

	public boolean periodPaused () {
		return periodicTestingPaused;
	}

	public void updateTable () {
		serviceTable.updateUI ();
	}

}
