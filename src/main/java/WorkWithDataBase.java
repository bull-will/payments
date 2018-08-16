import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class WorkWithDataBase {
    public static void main(String[] args) {
        deleteByMonthAndYear(7, 2018);
    }

    /*This method makes a condition part for a statement for deleting entries with
    specified month and year from the database*/
    private static void deleteByMonthAndYear(int month, int year) {

        String conditionForStatement = "month = " + month + " AND year = " + year;
        deleteByConditions(conditionForStatement);
    }

    /*This method makes the full statements for deleting entries from the database tables by condition*/
    private static void deleteByConditions(String condition) {

        /*Statement for deleting entries with specified condition from all tables
        It doesn't work though. I failed at writing it*/
        String deleteEntryFromAllTablesStatement = "DELETE FROM payments.electricity, " +
                " payments.heating, payments.water, payments.flat, payments.garbage, payments.total\n" +
                "WHERE " + condition + ";";
//        execute(deleteEntryFromAllTablesStatement);


        /*Statements for deleting entries with specified condition from separate tables*/
        String deleteElectricityEntryStatement = "DELETE FROM payments.electricity\n" +
                "WHERE " + condition + ";";

        String deleteHeatingEntryStatement = "DELETE FROM payments.heating\n" +
                "WHERE " + condition + ";";

        String deleteWaterEntryStatement = "DELETE FROM payments.water\n" +
                "WHERE " + condition + ";";

        String deleteFlatEntryStatement = "DELETE FROM payments.flat\n" +
                "WHERE " + condition + ";";

        String deleteGarbageEntryStatement = "DELETE FROM payments.garbage\n" +
                "WHERE " + condition + ";";

        String deleteTotalEntryStatement = "DELETE FROM payments.total\n" +
                "WHERE " + condition + ";";

        /* Deleting entries with specified condition from separate tables*/
        execute(deleteElectricityEntryStatement);
        execute(deleteHeatingEntryStatement);
        execute(deleteWaterEntryStatement);
        execute(deleteFlatEntryStatement);
        execute(deleteGarbageEntryStatement);
        execute(deleteTotalEntryStatement);
    }

    /* This method at last executes the query by the received statement*/
    static void execute(String statement) {
        String url = "jdbc:mysql://localhost:3306/payments?useSSL=false&useUnicode=true&" +
                "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String user = "root";
        String password = "admin";

        Connection con = null;
        Statement stmt = null;

        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);

            // getting Statement object to execute query
            stmt = con.createStatement();

            // executing  query
            stmt.execute(statement);
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
}
