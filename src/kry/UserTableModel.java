package kry;

import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * Table model used in the table that displays users.
 *
 * Author: Filip Bark
*/
public class UserTableModel extends AbstractTableModel {

        // Serial version is in format date-time to certify uniqueness
        // I won't actually serialize it, but JFrames implements serializable so I have to declare
        private static final long serialVersionUID = 2021_04_03_10_45L;

        private static final Object NO_DATA = "-";

        private String[] columnNames;
        private List<User> allUsers;

        public UserTableModel (String[] columnNames, List<User> allUsers) {
                this.columnNames = columnNames;
                this.allUsers = allUsers;
        }

        public User getUser (int row) {
                return allUsers.get (row);
        }

        public void addUser (User user) {
                allUsers.add (user);
        }

        public void removeUser (User user) {
                allUsers.remove (user);
        }

        @Override public String getColumnName (int column) {
                return columnNames[column];
        }

        @Override public int getRowCount () {
                return allUsers.size ();
        }

        @Override public int getColumnCount () {
                return columnNames.length;
        }

        @Override public Object getValueAt (int rowIndex, int columnIndex) {
                var tmp = allUsers.get (rowIndex);

		// This isn't the prettiest method of converting a User into a bunch of columns, but I can't figure
		// out a better method right now.
		switch (columnIndex) {
			case 0:
				return tmp.getUserName ();
			case 1:
				return tmp.isAdmin ();
                        default: 
                                return NO_DATA;
                }
        }

}
