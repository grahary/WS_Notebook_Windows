package kr.pe.kaijer;

import java.sql.*;

/**
 * Created by Cho, Wonsik on 2018-08-07.
 */

public class DBProcess {
    private static String dbDriver, dbURL, dbID, dbPW;
    private static Connection connection = null;
    private static Statement statement = null;

    private static DBObject dbObject;

    public static void getDBProp() {
        // JDBC 로그인 정보 받아오기
        GetJDBCProp.getJDBCProp();

        dbDriver = GetJDBCProp.dbDriver;
        dbURL = GetJDBCProp.dbURL;
        dbID = GetJDBCProp.dbID;
        dbPW = GetJDBCProp.dbPW;
    }

    public static void dbCUD(String query) {
        getDBProp();

        try {
            Class.forName(dbDriver);
            connection = DriverManager.getConnection(dbURL, dbID, dbPW);
            statement = connection.createStatement();

            statement.executeUpdate(query);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("해당 클래스를 찾을 수 없습니다: " + cnfe.getMessage());
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        } finally {
            try {
                statement.close();
            } catch (Exception ignored) {

            }
            try {
                connection.close();
            } catch (Exception ignored) {

            }
        }
    }

    public static void dbInsert(DBObject object) {
        dbObject = object;

        String query = "INSERT INTO notebook(title, user_id, content, tag, is_enc, enc_pw) " +
                "VALUES (\"" + dbObject.title + "\", \"" + dbObject.user_id + "\", AES_ENCRYPT(\"" + dbObject.content + "\", SHA2(\"" + dbObject.enc_pw + "\", 512)), \"" +
                dbObject.tag + "\", " + dbObject.is_enc + ", \"" + dbObject.enc_pw + "\");";

        dbCUD(query);
    }

    public static DBObject dbSelect(DBObject object) {
        dbObject = object;
        getDBProp();

        String query = "SELECT user_id, write_date, AES_DECRYPT(content, SHA2(\"" + dbObject.enc_pw + "\", 512)) AS content, tag FROM notebook WHERE idx = " + dbObject.idx + ";";

        try {
            Class.forName(dbDriver);
            connection = DriverManager.getConnection(dbURL, dbID, dbPW);
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                dbObject.user_id = resultSet.getString("user_id");
                dbObject.write_date = resultSet.getString("write_date");
                dbObject.content = resultSet.getString("content");
                dbObject.tag = resultSet.getString("tag");
            }

            return dbObject;
        } catch (ClassNotFoundException cnfe) {
            System.out.println("해당 클래스를 찾을 수 없습니다: " + cnfe.getMessage());
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        } finally {
            try {
                statement.close();
            } catch (Exception ignored) {

            } try {
                connection.close();
            } catch (Exception ignored) {

            }
        }

        return null;
    }

    public static void dbUpdate(DBObject object) {
        dbObject = object;
        getDBProp();

        String query = "UPDATE notebook " +
                "SET title = \"" + dbObject.title + "\", content = AES_ENCRYPT(\"" + dbObject.content + "\", SHA2(\"" + dbObject.enc_pw + "\", 512)), tag = \"" + dbObject.tag +
                "\", is_enc = " + dbObject.is_enc + ", enc_pw = \"" + dbObject.enc_pw + "\" WHERE idx = " + dbObject.idx + ";";

        dbCUD(query);
    }

    public static void dbDelete(int idx) {
        String query = "DELETE FROM notebook WHERE idx = " + idx + ";";

        dbCUD(query);
    }
}
