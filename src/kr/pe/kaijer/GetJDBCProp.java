package kr.pe.kaijer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class GetJDBCProp {
    public static String dbDriver, dbURL, dbID, dbPW;

    public static void getJDBCProp() {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader("src/jdbc.properties"));

            dbDriver = properties.getProperty("MySQL.Driver");
            dbURL = properties.getProperty("MySQL.URL");
            dbID = properties.getProperty("DB.ID");
            dbPW = properties.getProperty("DB.PW");
        } catch (FileNotFoundException fnfe) {
            fnfe.getMessage();
        } catch (IOException ioe) {
            ioe.getMessage();
        }
    }
}
