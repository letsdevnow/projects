//Program with command line interface.
//Using H2 database for storing data.

package carsharing;

import carsharing.common.CarSharing;

public class Main {
    private static String dbName = "defaultDB";
    private static final String driverAndPath = "jdbc:h2:file:.\\db\\";

    public static void main(String[] args) {

        //obtaining db filename in case of -databaseFileName parameter existence
        if (args.length > 1) {
            if ("-databaseFileName".equals(args[0])) {
                dbName=args[1];
            }
        }

        final String connectionString = driverAndPath + dbName;
        CarSharing carSharing = new CarSharing(connectionString);
        carSharing.runMainMenu();
    }

    public static void println(String str) {
        System.out.println(str);
    }

    public static void println(int n) {
        System.out.println(n);
    }
}

