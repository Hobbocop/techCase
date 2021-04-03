# techCase
Testcase for Kry,

author: Filip Bark (fbark@kth.se)

# How to run:
- Download all files as the same structure here
- Run tc.jar via 
```
java -jar tc.jar
```
- There is currently only one user available with username and password both being 1
- Once logged in you can add more users (and also decide if they're admins) by opening user administration tools from the top menu: File->Edit Users...
- Services will be periodically checked (every 5 seconds) if they're running or not, a satus message at the bottom of the window will say when it the background thread is currently checking all services.
- Services will be represented as either Online or Offline depending on if we got an aswer or not.
- Admins can see/add/delete/edit all services, but normal users can only see and manage the services that they've added.
- Admins can add/delete/edit all users (not delete themselves), including giving admin privileges. Normal users can only change their own data (not give admin priveleges)


# Code structure
The main class is called TechCase.java. It will load information from database and also ask user to log in.
Once logged in, it will open the ServiceFrame and also start a daemon Thread that will periodically check all services with HTTPUtils.

The ServiceFrame has a menu with an option for editing users, and also a ServiceTablePanel that contains all the services (as well as buttons to handle services). The panel has a big table where all the data is stored in a ServiceTableModel.

Both users and services (the only 2 data structures) are stored in their separate struct-like classes with some helper methods.

The user editor, EditUserPanel, is handled basically just like the ServiceTablePanel. It has buttons and a table containing data in a UserTableModel.

Additionally, there are 4 util classes:
- HttpUtils - contains one method to check a service via a HTTP get request
- DialogUtils - contains methods for opening small dialogs
- DataBaseUtils - contains all methods that access the database.
- MyStringUitls - contains methods for parsing/hashing/checking strings
