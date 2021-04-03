package kry.junit; // Not an actual junit test perhaps, but it will work somewhat similarily

public class TestTechCase {

        public static void main (String[] args){
                System.out.println ("Hello world, let's test the TechCase and see if it works!");

                int totalTestPassed = 0;
                int dbTests = 6; // TODO - update this to final amount of tests
                System.out.println ("Testing database:");
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
                int passedTests = 0;
                passedTests += testUsers ();
                passedTests += testServices ();

                return passedTests;
        }

        private static int testUsers () {
                int passedTests = 0;
                passedTests += testCreateUser ();
                passedTests += testEditUser ();
                passedTests += testRemoveUser (); 

                return passedTests;
        }

        private static int testCreateUser () {
                return 0;
        }

        private static int testEditUser () {
                return 0;
        }

        private static int testRemoveUser () {
                return 0;
        }

        private static int testServices () {
                int passedTests = 0;
                passedTests += testCreateService ();
                passedTests += testEditService ();
                passedTests += testRemoveService ();
                return passedTests;
        }

        private static int testCreateService () {
                return 0;
        }

        private static int testEditService () {
                return 0;
        }

        private static int testRemoveService () {
                return 0;
        }

        private static int testUtils () {
                int passedTests = 0;
                passedTests += testStringUtils ();
                passedTests += testHttpUtils ();
                return passedTests;
        }

        private static int testStringUtils () {
                return 0;
        }

        private static int testHttpUtils () {
                return 0;
        }

}
