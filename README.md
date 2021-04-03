# techCase
Testcase for Kry,

Dependencies: Java 11, sqlite (only if you want to manually check the database)

author: Filip Bark (fbark@kth.se)

# How to run:
- Download all files as the same structure here
- Run tc.jar via 
```
java -jar tc.jar
```
- Or manually compile and run (instructions further down)
- There is currently only one user available with username and password both being "1" (This user is an Admin, and I would recommend you don't delete or change this users priveleges)
- Once logged in you can add more users (and also decide if they're admins) by opening user administration tools from the top menu: File->Edit Users...
- Services will be periodically checked (every 5 seconds) if they're running or not, a satus message at the bottom of the window will say when it the background thread is currently checking all services.
- Services will be represented as either Online or Offline depending on if we got an aswer or not.
- Admins can see/add/delete/edit all services, but normal users can only see and manage the services that they've added.
- Admins can add/delete/edit all users (not delete themselves), including giving admin privileges. Normal users can only change their own data (not give admin priveleges)

# How to compile and run manually:
First download all files then in the main directory run
```
javac -d bin src/kry/*.java
```
Once the files have been compiled, start the programm manually from the main directory by running:
```
java -classpath ".:bin:lib/sqlite-jdbc-3.32.3.2.jar" kry.TechCase
```

# How to test
First you need to compile the test files this is not an actual junit test, just a separate java class that will test all files:
```
javac -d bin src/kry/junit/*.java src/kry/*.java
```
The test can then be run in terminal (for the output, sorry) with:
```
java -classpath ".:bin:lib/sqlite-jdbc-3.32.3.2.jar" kry.junit.TestTechCase
```
If no "Error" prints are present - this means all tests passed!

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

# Database
The database has 2 tables:
- Users with 4 fields:
  - Name (string) - name of the user
  - Pwd (hashed string) - For security reasons we're not storing password as the user entered it.
  - Admin (int, 0 or 1) - Sqlite doesn't handle booleans, but we can just check whether it's 1 or not.
  - id (int) - a uniqiue identifier for a user, every new user will get max(id)+1. (Id's will not be reused when delete users, just keep using the largest+1)

- Services with 6 fields:
  - Name (string) - Name of the service
  - Url (string) - Url for the service, there is a small check when this is added so that most nonsensical urls won't get added.
  - created_date (string) - Sqlite doesn't handle dates, so the timestamps get formatted to strings. When was the service created?
  - last_modified_date (string) - Sqlite doesn't handle dates, so the timestamps get formatted to strings. When was the service last modified?
  - created_by (int) - The id of the User that created this service. (Services can only be seen by their owners or Admins)
  - id (int) - a uniqiue identifier for this service, every new service will get max(id)+1.
