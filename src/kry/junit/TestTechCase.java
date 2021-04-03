package kry.junit; // Not an actual junit test perhaps, but it will work somewhat similarily

import java.util.List;
import java.util.stream.Collectors;

import kry.DataBaseUtils;
import kry.HTTPUtils;
import kry.Service;
import kry.User;

/**
 * Faux junit test for TechCase. Because I've never written a junit framework this test isn't perfect.
 * Mainly that the changes made in the database will be reflected in the database once the tests are finished.
 * So if things aren't cleaned out of the databse, they will start cluttering the database.<br><br>
 *
 * Ideally, there should be a rollback in the database once all tests are done...
 * But that's beyond my abilities right now.<br><br>
 *
 * Author: Filip Bark
 * </pre>
*/

public class TestTechCase {

        public static void main (String[] args) {
                System.out.println ("Hello world, let's test the TechCase and see if it works!");

                int totalTestPassed = 0;
                int dbTests = 6; // TODO - update this to final amount of tests
                int dbTestPassed = testDatabase ();
                if (dbTestPassed != dbTests)
                        System.out.println ("ERROR: Not all db tests passed!");

                int utilTests = 4; // TODO - update this to the final amount of tests;
                int utilsTestPassed = testUtils ();
                if (utilsTestPassed != utilTests)
                        System.out.println ("ERROR: Not all util tests passed!");

                int totalTests = dbTests + utilTests;
                if (totalTestPassed == totalTests)
                        System.out.println ("All tests successfully passed!");
                else
                        System.out.println ("ERROR: Some test failed!");
        }

        private static int testDatabase () {
                System.out.println ("Testing database...");
                int passedTests = 0;
                passedTests += testUsers ();
                passedTests += testServices ();

                return passedTests;
        }

        private static int testUsers () {
                int passedTests = 0;
                var allUsersPreTest = DataBaseUtils.selectAllUsers ();
                var testUser = new User ("Test", "Test", false);
                passedTests += testCreateUser (allUsersPreTest, testUser);
                passedTests += testEditUser (testUser, passedTests);
                passedTests += testRemoveUser (testUser); 
                System.out.println (passedTests + "/3 user tests passed");

                return passedTests;
        }

        private static int testCreateUser (List<User> allUsersPreTest, User testUser) {
                
                DataBaseUtils.storeNewUser (testUser);

                // The idea is that the new user should be the new max id - if it is, then the test has passed!
                var maxIdPostTest = DataBaseUtils.getMaxUserId ();
                var allUsersPostTest = DataBaseUtils.selectAllUsers ();

                // The max id should have become the new user, this means user got an incremental id and this is now
                // the largest id available.
                if (maxIdPostTest != testUser.getId ())
                        return 0;

                // The size of all users should have increased by at least one now that we've added a user.
                if (allUsersPostTest.size () - allUsersPreTest.size () <= 0)
                        return 0;

                return 1;

        }

        private static int testEditUser (User testUser, int passedTests) {
                // If we didn't pass the first test -there is no point doing this test either, as the user wasn't stored
                if (passedTests == 0)
                        return 0;
                String newName = "newName";
                // Small change in edituser
                testUser.updateUserName (newName);

                // We can directly call edit user because the user was created in previous test (which we know succeeded)
                DataBaseUtils.updateUser (testUser);

                // Verifying this is a bit harder, we once again have to fetch all users and find this particular user
                var tmpAllUsers = DataBaseUtils.selectAllUsers ();
                var matchingUsers = tmpAllUsers.stream ()
                                               .filter (u -> u.equals (testUser))
                                               .collect (Collectors.toList ());

                // If there's more than one user with the same id, this doesn't work.
                if (matchingUsers.size () != 1)
                        return 0;

                // If the name of the matchin user in database is the new name, the update was successful.
                if (matchingUsers.get (0).getUserName ().equals (newName))
                        return 1;

                return 0;
        }

        private static int testRemoveUser (User testUser) {
                var allUsersPreTest = DataBaseUtils.selectAllUsers ();
                DataBaseUtils.removeUser (testUser);
                var allUsersPostTest = DataBaseUtils.selectAllUsers ();

                // There should be one user less after the test
                if (allUsersPreTest.size () - allUsersPostTest.size () == 1)
                        return 1;

                return 0;
        }

        private static int testServices () {
                int passedTests = 0;

                var allServicesPreTest = DataBaseUtils.selectAllServices ();
                var testUserId = DataBaseUtils.getMaxUserId () + 1;
                var testService = new Service ("test", "www.google.com", testUserId);

                passedTests += testCreateService (allServicesPreTest, testService);
                passedTests += testEditService (testService, passedTests);
                passedTests += testRemoveService (testService);
                System.out.println (passedTests + "/3 service tests passed");
                return passedTests;
        }

        private static int testCreateService (List<Service> allServicesPreTest, Service testService) {
                // See corresponding user test for comments...
                DataBaseUtils.storeNewService (testService);

                var maxIdPostTest = DataBaseUtils.getMaxServiceId ();
                var allServicesPostTest = DataBaseUtils.selectAllServices ();

                if (maxIdPostTest != testService.getId ())
                        return 0;

                if (allServicesPostTest.size () - allServicesPreTest.size () <= 0)
                        return 0;

                return 1;
        }

        private static int testEditService (Service testService, int passedTests) {
                // For code comments, see corresponding user test
                if (passedTests == 0)
                        return 0;
        
                String newName = "newName";

                testService.updateName (newName);

                DataBaseUtils.updateService (testService);

                var tmpAllSerivces = DataBaseUtils.selectAllServices ();
                var matchingServices = tmpAllSerivces.stream ()
                                                     .filter (s -> s.getId () == testService.getId ())
                                                     .collect (Collectors.toList ());

                if (matchingServices.size () != 1)
                        return 0;

                if (matchingServices.get (0).getName ().equals (newName))
                        return 1;

                return 0;
        }

        private static int testRemoveService (Service testService) {
                var allServicesPreTest = DataBaseUtils.selectAllServices ();
                DataBaseUtils.removeService (testService);
                var allServicesPostTest = DataBaseUtils.selectAllServices ();

                if (allServicesPreTest.size () - allServicesPostTest.size () == 1)
                        return 1;

                return 0;
        }

        private static int testUtils () {
                System.out.println ("Testing utils...");
                int passedTests = 0;
                passedTests += testStringUtils ();
                passedTests += testHttpUtils ();
                System.out.println (passedTests + "/4 util tests passed");
                return passedTests;
        }

        private static int testStringUtils () {
                return 0;
        }

        private static int testHttpUtils () {
                int passedTests = 0;
                // one service should be ok, the other should not be ok
                Service okService = new Service ("n1", "https://www.google.com", 0);
                Service notOkService = new Service ("n2", "https.//www.notGoogle.com", 0);

                HTTPUtils.testSingleService (okService);
                HTTPUtils.testSingleService (notOkService);

                var ob1 = okService.getLastResponseOk ();
                var ob2 = notOkService.getLastResponseOk ();

                // Check that first service has gotten a response and that it's ok
                if (ob1.isPresent () && ob1.get ())
                        passedTests++;

                // Check that second service has gotten a response and that it's not ok
                if (ob2.isPresent () && !ob2.get ())
                        passedTests++;

                return passedTests;
        }

}
