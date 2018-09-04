import java.io.*;
import java.sql.*;

public class Runner {
    static int year;
    static int month;

    public static void main(String[] args) throws IOException {

        doPayments();
//        doAdditionalPayments();
    }

    static void doPayments() throws IOException {
        year = 2018; // don't forget to change it when the new year comes;

        month = 1; // changes accordingly to the number of the current month
        Payment jan2018 = new Payment
                (year, month, 22394, 22657, 610, 614);
        jan2018.payForEverything(); // as the complete set of normal payments was done in this month
        PaymentsArrays.payments2018[month] = jan2018; //storing this months entry into the archive of payments

        month = 2; // changes accordingly to the number of the current month
        Payment feb2018 = new Payment
                (year, month, 22657, 22861, 614, 616);
        feb2018.payForEverything(); // as the complete set of normal payments was done in this month
        feb2018.paymentForHeating = 0; // didn't pay for heating
        feb2018.getTotal();
        PaymentsArrays.payments2018[month] = feb2018; //storing this months entry into the archive of payments

        month = 3; // changes accordingly to the number of the current month
        Payment mar2018 = new Payment
                (year, month, 22861, 23104, 616, 619);
        mar2018.payForEverything(); // as the complete set of normal payments was done in this month
        PaymentsArrays.payments2018[month] = mar2018; //storing this months entry into the archive of payments

        month = 4; // changes accordingly to the number of the current month
        Payment apr2018 = new Payment
                (year, month, 23104, 23411, 619, 622);
        apr2018.payForEverything(); // as the complete set of normal payments was done in this month
        apr2018.paymentForHeating = 0; // didn't pay for heating
        apr2018.getTotal();
        PaymentsArrays.payments2018[month] = apr2018; //storing this months entry into the archive of payments

        month = 5; // changes accordingly to the number of the current month
        Payment may2018 = new Payment
                (year, month, 23411, 23584, 622, 626);
        may2018.payForEverything(); // as the complete set of normal payments was done in this month
        PaymentsArrays.payments2018[month] = may2018; //storing this months entry into the archive of payments

        month = 6; // changes accordingly to the number of the current month
        Payment jun2018 = new Payment
                (year, month, 23584, 23816, 626, 629);
        jun2018.payForEverything(); // as the complete set of normal payments was done in this month
        PaymentsArrays.payments2018[month] = jun2018; //storing this months entry into the archive of payments

        month = 7; // changes accordingly to the number of the current month
        Payment jul2018 = new Payment
                (year, month, 23816, 24120, 629, 632);
        jul2018.payForEverything(); // as the complete set of normal payments was done in this month
        PaymentsArrays.payments2018[month] = jul2018; //storing this months entry into the archive of payments

        month = 8; // changes accordingly to the number of the current month
        Payment aug2018 = new PaymentSinceAugust2018
                (year, month, 24120, 24365, 632, 636);
        aug2018.payForEverything(); // as the complete set of normal payments was done in this month
        PaymentsArrays.payments2018[month] = jul2018; //storing this months entry into the archive of payments

        Payment current = aug2018;
        print(current);// print complete payment data to *.doc file. The file will be stored in the project directory
//        toDataBase(current, true);

        /* section of printing for info and curiosity: */
//        printToConsole(current);// additional printing to console just for info
//        print(current, "Trial"); // For curiosity
//        print(PaymentsArrays.payments2018[month], PaymentsArrays.payments2018[month].name+"_from_array");
//        print(PaymentsArrays.payments2018[month]); // For curiosity
//        print(PaymentsArrays.payments2018[month], PaymentsArrays.payments2018[month].name+"_from_array");
//        printFromArchive(PaymentsArrays.payments2018);
    }

    static void doAdditionalPayments() throws IOException {
        /* This is for making additional/extra payments.
        Look how it's done on an example of an additional payment for heating for the july 2018
        */

        Payment jul2018extraHeating = new Payment
                (2018, 7, 0, 0, 0, 0);
        /* As the payments database requires the month and the year of the payment to be unique,
        the month number will be changed. For january it will be 101, for february 201, for november 1101.
        // TODO: automatically increment the number of month for the next payment for this month,
        // i.e. 1102 for the second additional payment in november, 1103 for the third november payment etc.
        // This is going to need creating a List of Payment type next to array of payments in the archive.
         */
        jul2018extraHeating.month *= 100;
        jul2018extraHeating.month += 1;

        // Filling the needed field with numbers
        jul2018extraHeating.paymentForHeating = 500;
        jul2018extraHeating.getTotal();

        // Add something to the end of the payment name
        String additionToName = " дополнительный платеж за отопление";
        jul2018extraHeating.name += additionToName;

        // Now for making the .doc file for history and putting this payment to the database.
        print(jul2018extraHeating);
        toDataBase(jul2018extraHeating,true);

    }

    static void toDataBase(Payment payment, boolean updateIfExists) {
        String url = "jdbc:mysql://localhost:3306/payments?useSSL=false&useUnicode=true&" +
                "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String user = "root";
        String password = "admin";

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        /* variables for collecting flags if payments entries already exist in the database tables
        (0 by default, 1 if exists)*/
        int electricityPaid = 0;
        int heatingPaid = 0;
        int waterPaid = 0;
        int flatPaid = 0;
        int garbagePaid = 0;
        int totalPaid = 0;

        // Statements for inserting a new payment data into the database tables
        String insertElectricityStatement = "INSERT INTO payments.electricity\n" +
                "VALUES (" + payment.month + ", " + payment.year + ", " +
                payment.electro_start + ", " + payment.electro_end + ", " +
                payment.kWattConsumed + ", " +
                payment.electroTariff1 + ", " + payment.electroLimit1 + ", " +
                payment.electroTariff2 + ", " + payment.electroLimit2 + ", " +
                payment.electroTariff3 + ", " + payment.electroLimit3 + ", " +
                payment.electroTariff4 + ", " + payment.paymentForElectricity + ");";

        String insertHeatingStatement = "INSERT INTO payments.heating\n" +
                "VALUES (" + payment.month + ", " + payment.year + ", " +
                payment.heatingTariff + ", " + payment.paymentForHeating + ");";


        String insertWaterStatement = "INSERT INTO payments.water\n" +
                "VALUES (" + payment.month + ", " + payment.year + ", " +
                payment.water_start + ", " + payment.water_end + ", " +
                payment.m3consumed + ", " +
                payment.waterTariff + ", " + payment.paymentForWater + ");";

        String insertFlatStatement = "INSERT INTO payments.flat\n" +
                "VALUES (" + payment.month + ", " + payment.year + ", " +
                payment.flatTariff + ", " + payment.paymentForFlat + ");";

        String insertGarbageStatement = "INSERT INTO payments.garbage\n" +
                "VALUES (" + payment.month + ", " + payment.year + ", " +
                payment.garbageTariff + ", " + payment.paymentForGarbage + ");";

        String insertTotalStatement = "INSERT INTO payments.total\n" +
                "VALUES (" + payment.month + ", " + payment.year +
                ", " + payment.electro_start + ", " + payment.electro_end +
                ", " + payment.kWattConsumed + ", " + payment.paymentForElectricity +
                ", " + payment.water_start + ", " + payment.water_end +
                ", " + payment.m3consumed + ", " + payment.paymentForWater +
                ", " + payment.paymentForHeating + ", " + payment.paymentForFlat +
                ", " + payment.paymentForGarbage + ", " + payment.total + ");";

        // Statements for updating the payment data existing in the database tables
        String updateElectricityStatement = "UPDATE payments.electricity\n" +
                "SET start_readings = " + payment.electro_start + ", end_readings = "
                + payment.electro_end + ", kWatt = " + payment.kWattConsumed + ", tariff1 = " +
                payment.electroTariff1 + ", limit1 = " + payment.electroLimit1 + ", tariff2 = " +
                payment.electroTariff2 + ", limit2 = " + payment.electroLimit2 + ", tariff3 = " +
                payment.electroTariff3 + ", limit3 = " + payment.electroLimit3 + ", tariff4 = " +
                payment.electroTariff4 + ", payment = " + payment.paymentForElectricity + "\n" +
                "WHERE month = " + payment.month + " AND year = " + payment.year + ";";

        String updateHeatingStatement = "UPDATE payments.heating\n" +
                "SET tariff = " + payment.heatingTariff + ", payment = " + payment.paymentForHeating + "\n" +
                "WHERE month = " + payment.month + " AND year = " + payment.year + ";";

        String updateWaterStatement = "UPDATE payments.water\n" +
                "SET start_readings = " + payment.water_start + ", end_readings = " + payment.water_end +
                ", m3 = " + payment.m3consumed + ", tariff = " + payment.waterTariff +
                ", payment = " + payment.paymentForWater + "\n" +
                "WHERE month = " + payment.month + " AND year = " + payment.year + ";";

        String updateFlatStatement = "UPDATE payments.flat\n" +
                "SET tariff = " + payment.flatTariff + ", payment = " + payment.paymentForFlat + "\n" +
                "WHERE month = " + payment.month + " AND year = " + payment.year + ";";

        String updateGarbageStatement = "UPDATE payments.garbage\n" +
                "SET tariff = " + payment.garbageTariff + ", payment = " + payment.paymentForGarbage + "\n" +
                "WHERE month = " + payment.month + " AND year = " + payment.year + ";";

        String updateTotalStatement = "UPDATE payments.total\n" +
                "SET electro_start = " + +payment.electro_start + ", electro_end = " + payment.electro_end +
                ", kWatt = " + payment.kWattConsumed + ", electro_payment = " + payment.paymentForElectricity +
                ", water_start = " + payment.water_start + ", water_end = " + payment.water_end +
                ", m3 = " + payment.m3consumed + ", water_payment = " + payment.paymentForWater +
                ", heating = " + payment.paymentForHeating + ", flat = " + payment.paymentForFlat +
                ", garbage = " + payment.paymentForGarbage + ", total = " + payment.total + "\n" +
                "WHERE month = " + payment.month + " AND year = " + payment.year + ";";


        // Statements for checking if payments for the exact month already exist in the database
        String checkElectricity = "SELECT EXISTS (SELECT * FROM payments.electricity\n" +
                "WHERE month = " + payment.month + " AND year = " + payment.year + ");";
        String checkHeating = "SELECT EXISTS (SELECT * FROM payments.heating\n" +
                "WHERE month = " + payment.month + " AND year = " + payment.year + ");";
        String checkWater = "SELECT EXISTS (SELECT * FROM payments.water\n" +
                "WHERE month = " + payment.month + " AND year = " + payment.year + ");";
        String checkFlat = "SELECT EXISTS (SELECT * FROM payments.flat\n" +
                "WHERE month = " + payment.month + " AND year = " + payment.year + ");";
        String checkGarbage = "SELECT EXISTS (SELECT * FROM payments.garbage\n" +
                "WHERE month = " + payment.month + " AND year = " + payment.year + ");";
        String checkTotal = "SELECT EXISTS (SELECT * FROM payments.total\n" +
                "WHERE month = " + payment.month + " AND year = " + payment.year + ");";

        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);

            // getting Statement object to execute query
            stmt = con.createStatement();

            // checking and setting flags for existence of the entriws in the tables
            rs = stmt.executeQuery(checkElectricity);
            rs.next();
            electricityPaid = rs.getInt(1);

            rs = stmt.executeQuery(checkHeating);
            rs.next();
            heatingPaid = rs.getInt(1);

            rs = stmt.executeQuery(checkWater);
            rs.next();
            waterPaid = rs.getInt(1);

            rs = stmt.executeQuery(checkFlat);
            rs.next();
            flatPaid = rs.getInt(1);

            rs = stmt.executeQuery(checkGarbage);
            rs.next();
            garbagePaid = rs.getInt(1);

            rs = stmt.executeQuery(checkTotal);
            rs.next();
            totalPaid = rs.getInt(1);

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            //close connection ,stmt and resultset here
            try {
                con.close();
            } catch (SQLException se) { /*can't do anything */ }
            try {
                stmt.close();
            } catch (SQLException se) { /*can't do anything */ }
            try {
                rs.close();
            } catch (SQLException se) { /*can't do anything */ }
        }

        /* Updating database tables or inserting new entries into tables accordingly
        to whether entries for current month exist */
        if (electricityPaid == 0) {
            insertIntoDataBase(payment, insertElectricityStatement);
        } else if (updateIfExists) {
            updateDataBase(payment, updateElectricityStatement);
        }
        if (heatingPaid == 0) {
            insertIntoDataBase(payment, insertHeatingStatement);
        } else if (updateIfExists) {
            updateDataBase(payment, updateHeatingStatement);
        }
        if (waterPaid == 0) {
            insertIntoDataBase(payment, insertWaterStatement);
        } else if (updateIfExists) {
            updateDataBase(payment, updateWaterStatement);
        }
        if (flatPaid == 0) {
            insertIntoDataBase(payment, insertFlatStatement);
        } else if (updateIfExists) {
            updateDataBase(payment, updateFlatStatement);
        }
        if (garbagePaid == 0) {
            insertIntoDataBase(payment, insertGarbageStatement);
        } else if (updateIfExists) {
            updateDataBase(payment, updateGarbageStatement);
        }
        if (totalPaid == 0) {
            insertIntoDataBase(payment, insertTotalStatement);
        } else if (updateIfExists) {
            updateDataBase(payment, updateTotalStatement);
        }

    }

    static void updateDataBase(Payment payment, String statement) {
        String url = "jdbc:mysql://localhost:3306/payments?useSSL=false&useUnicode=true&" +
                "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String user = "root";
        String password = "admin";

        Connection con = null;
        Statement stmt = null;

        String updatePaymentQuery = statement;

        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);

            // getting Statement object to execute query
            stmt = con.createStatement();

            // executing  query
            stmt.execute(updatePaymentQuery);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            //close connection ,stmt and resultset here
            try {
                con.close();
            } catch (SQLException se) { /*can't do anything */ }
            try {
                stmt.close();
            } catch (SQLException se) { /*can't do anything */ }
        }
    }

    static void insertIntoDataBase(Payment payment, String statement) {

        String url = "jdbc:mysql://localhost:3306/payments?useSSL=false&useUnicode=true&" +
                "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String user = "root";
        String password = "admin";

        Connection con = null;
        Statement stmt = null;

        String insertPaymentQuery = statement;

        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);

            // getting Statement object to execute query
            stmt = con.createStatement();

            // executing  query
            stmt.execute(insertPaymentQuery);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            //close connection ,stmt and resultset here
            try {
                con.close();
            } catch (SQLException se) { /*can't do anything */ }
            try {
                stmt.close();
            } catch (SQLException se) { /*can't do anything */ }
        }
    }

    static void print(Payment payment) throws FileNotFoundException, IOException {
        /*
        At first this method sets the name for the .doc file. The name consists of the year and the month.
        If a file with such name already exists, the digit "1" will be added to the and of the new file name
        so previous file will not be replaced.
        */
        File outPutFileToPrint = makeOutPutFile(payment.name);

        /* Now this method makes output stream writer for the .doc file */
        OutputStreamWriter dataOut = makeWriter(outPutFileToPrint);

        /* And now let's fill the file with contents */
        writeLinesToFile(payment, dataOut);
    }

    static void printWithSpeacilName(Payment payment, String specialName) throws FileNotFoundException, IOException {
        /*
        This method sets the .doc file the name you specify in parameters you call it with.
        If a file with such name already exists, the digit "1" will be added to the and of the new file name
        so previous file will not be replaced.
        */

        File outPutFileToPrint = makeOutPutFile(specialName);

        /* Now this method makes output stream writer for the .doc file */
        OutputStreamWriter dataOut = makeWriter(outPutFileToPrint);

        /* And now let's fill the file with contents */
        dataOut.write(specialName + "\n"); // printing this line into the file could be useful for the notice
        writeLinesToFile(payment, dataOut);
    }

    static void printFromArchive(Payment[] yearArrayOfPayments) throws IOException {
        /* printing all the contents of the year payments archive */
        for (Payment monthPayment : yearArrayOfPayments) {
            if (monthPayment != null) {
                printWithSpeacilName(monthPayment, monthPayment.name + "from archive");
            }
        }
    }

    static void writeLinesToFile(Payment payment, OutputStreamWriter dataOut) throws IOException {
        // let's write the data to the .doc file!
        try {
            dataOut.write(payment.name + ":\n\n"); // The year and the months are written at the first line
            dataOut.write("Квартплата\t\t\tТариф " + (int) payment.flatTariff + " руб.\n");
            dataOut.write("\t\t\t\tПлатеж " + payment.paymentForFlat + " руб.\n\n");
            dataOut.write("Электричество\n");
            dataOut.write("(Нач. пок. счетчика:\t" + payment.electro_start + ")\n");
            dataOut.write("(Конеч. пок. счетчика:\t" + payment.electro_end + ")\n");
            dataOut.write("(Потреблено кВт:\t\t" + payment.kWattConsumed + ")\n");
            dataOut.write("\t\t\t\tПлатеж " + payment.paymentForElectricity + " руб.\n\n");
            dataOut.write("Вода\t\t\t\tТариф " + (int) payment.waterTariff + " руб.\n");
            dataOut.write("(Нач. пок. счетчика:\t" + payment.water_start + ")\n");
            dataOut.write("(Конеч. пок. счетчика:\t" + payment.water_end + ")\n");
            dataOut.write("(Потреблено куб.м:\t" + payment.m3consumed + ")\n");
            dataOut.write("\t\t\t\tПлатеж " + payment.paymentForWater + " руб.\n\n");
            dataOut.write("Отопление\t\t\tТариф " + (int) payment.heatingTariff + " руб.\n");
            dataOut.write("\t\t\t\tПлатеж " + payment.paymentForHeating + " руб.\n\n");
            dataOut.write("Вывоз мусора\t\tТариф " + (int) payment.garbageTariff + " руб.\n");
            dataOut.write("\t\t\t\tПлатеж " + payment.paymentForGarbage + " руб.\n\n");
            dataOut.write("\t\t\t\tВсего: " + payment.total + " руб.\n");
            dataOut.flush();
        } catch (IOException e) {
            System.out.println("There was an IOException during the writing into the output file");
        } finally {
            dataOut.close();
        }
    }

    static File makeOutPutFile(String paymentName) throws IOException {
        /* This method sets a directory, a name and an extension for output file */
        String workingDir = System.getProperty("user.dir"); // Files will be stored in the project directory
        String dirForGeneratedFiles = workingDir + "\\Платежи\\"; // setting a directory for generated files
        if (!new File(dirForGeneratedFiles).exists()) {
            new File(dirForGeneratedFiles).mkdir();
        }
        String outPutFileName = dirForGeneratedFiles + paymentName;
        String extension = ".doc"; // set the extension of the output file

        // the following section changes the file name if a file with this name already exists:
        int counter = 0; // has to be given to the next method
        if (new File(outPutFileName + extension).exists())
            outPutFileName = changeOutPutFileName(outPutFileName, extension, counter);
        return new File(outPutFileName + extension);
    }

    static String changeOutPutFileName(String outPutFileName, String extension, int counter) {
        counter++;
        if (new File(outPutFileName + "_" + counter + extension).exists()) {
            return changeOutPutFileName(outPutFileName, extension, counter);
        } else {
            return outPutFileName + "_" + counter;
        }
    }

    static OutputStreamWriter makeWriter(File outPutFileToPrint) throws FileNotFoundException {
        BufferedOutputStream outputStream
                = new BufferedOutputStream(
                new FileOutputStream(
                        outPutFileToPrint));
        return new OutputStreamWriter(outputStream);
    }

    static void printToConsole(Payment current) {
        // print to console just for info, doesn't affect creating .doc files
        System.out.println(current.name + ":");
        System.out.println("flat\t" + current.paymentForFlat);
        System.out.println("electr\t" + current.paymentForElectricity);
        System.out.println("water\t" + current.paymentForWater);
        System.out.println("heating\t" + current.paymentForHeating);
        System.out.println("garbage\t" + current.paymentForGarbage);
        System.out.println("total\t" + current.total);
    }

}
