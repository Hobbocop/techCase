package kry;

import java.awt.Dimension;
import java.util.List;
import java.awt.*;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/** 
 * Panel used to edit users
 *
 * Author: Filip Bark
*/
public class EditUserPanel extends JPanel {

        // Serial version is in format date-time to certify uniqueness
        // I won't actually serialize it, but JFrames implements serializable so I have to declare
        private static final long serialVersionUID = 2021_04_03_10_31L;

        private static final String EDIT_USER_DIALOG_TITLE = "Edit selected user";
        private static final String ADD_USER_DIALOG_TITLE = "Add new user";
        private static final String REMOVE_USER_TITLE = "Remove selected user";
        private static final String NO_USER_MESSAGE = "No user selected";
        private static final String INVALID_RIGHTS_MESSAGE = "You do not have the rights to edit this user";
        private static final String CANT_DELETE_MESSAGE = "You can't delete your own user that you're logged into";

        private JTable userTable;
        private UserTableModel userTableModel;

        public EditUserPanel (User currentUser, List<User> allUsers) {
                super (new BorderLayout ());
                setPreferredSize (new Dimension (500, 400));
                JLabel text = new JLabel ("Currently logged in as: " + currentUser);
                add (text, BorderLayout.NORTH);

                userTable = createTable (allUsers);
                add (new JScrollPane (userTable), BorderLayout.CENTER);

                JPanel buttonPanel = createButtonPanel (currentUser);
                add (buttonPanel, BorderLayout.SOUTH);
	}

        private JTable createTable (List<User> allUsers) {
               String[] columnNames = {"User name", "Admin"};

		userTableModel = new UserTableModel (columnNames, allUsers);

		JTable table = new JTable (userTableModel);
		table.setFillsViewportHeight (true);
		table.setRowSelectionAllowed (true);
		table.setAutoResizeMode (JTable.AUTO_RESIZE_LAST_COLUMN);
                return table;
        }

        private JPanel createButtonPanel (User currentUser) {
                JPanel buttonPanel = new JPanel ();

                JButton editUserButton = new JButton (EDIT_USER_DIALOG_TITLE);
                editUserButton.addActionListener (e -> editSelectedUser (currentUser));
                buttonPanel.add (editUserButton);

                // Only admins are allowed to add and remove users
                if (currentUser.isAdmin ()) {
                        JButton addUserButton = new JButton (ADD_USER_DIALOG_TITLE);
                        addUserButton.addActionListener (e -> addNewUser (currentUser));
                        buttonPanel.add (addUserButton);

                        JButton deleteUserButton = new JButton (REMOVE_USER_TITLE);
                        deleteUserButton.addActionListener (e -> removeSelectedUser (currentUser));
                        buttonPanel.add (deleteUserButton);
                }
                
                return buttonPanel;
        }

        private void editSelectedUser (User currentUser) {
                
                if (userTable.getSelectedRow () == -1) {
                        DialogUtils.showMessageDialog (this, NO_USER_MESSAGE, EDIT_USER_DIALOG_TITLE);
                        return;
                }

                User user = userTableModel.getUser (userTable.getSelectedRow ());

                // Need to be your own user or admin to edit users
                if (!currentUser.isAdmin () && !user.equals (currentUser)) {
                        DialogUtils.showMessageDialog (this, INVALID_RIGHTS_MESSAGE, EDIT_USER_DIALOG_TITLE);
                        return;
                }

                var choice = DialogUtils.showEditUserDialog (this, user, currentUser.isAdmin (), EDIT_USER_DIALOG_TITLE);

                if (DialogUtils.choiceNotOk (choice))
                        return;

                updateServiceInModel (user);
        }

        private void updateServiceInModel (User user) {
                DataBaseUtils.updateUser (user);
                updateTable ();
                
        }

        private void updateTable () {
                userTable.updateUI ();
        }

        private void addNewUser (User currentUser) {
                User newUser = new User ("", "", false);
                var choice = DialogUtils.showEditUserDialog (this, newUser, currentUser.isAdmin (),
                                                             ADD_USER_DIALOG_TITLE);

                if (DialogUtils.choiceNotOk (choice))
                        return;

                addNewServiceToModel (newUser);
        }

        private void addNewServiceToModel (User newUser) {
                userTableModel.addUser (newUser);
                DataBaseUtils.storeNewUser (newUser);
                updateTable ();
        }

         private void removeSelectedUser (User currentUser) {
                if (userTable.getSelectedRow () == -1) {
                        DialogUtils.showMessageDialog (this, NO_USER_MESSAGE, REMOVE_USER_TITLE);
                        return;
                }

                var user = userTableModel.getUser (userTable.getSelectedRow ());

                if (user.equals (currentUser)) {
                        DialogUtils.showMessageDialog (this, CANT_DELETE_MESSAGE, REMOVE_USER_TITLE);
                }

                userTableModel.removeUser (user);
                DataBaseUtils.removeUser (user);
                updateTable ();
        }

}
