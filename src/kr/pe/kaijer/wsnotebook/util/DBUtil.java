/*
 * DBUtil.java
 *
 * Created by Cho, Wonsik on 2018-08-15.
 */

package kr.pe.kaijer.wsnotebook.util;

import com.sun.rowset.CachedRowSetImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DBUtil {
    private static Connection conn = null;

    /**
     * 데이터베이스 연결
     */
    private static void dbConnect() {
        String dbDriver, dbURL, dbID, dbPW;

        try {
            Properties properties = new Properties();
            properties.load(new FileReader("src/jdbc.properties"));

            dbDriver = properties.getProperty("MySQL.Driver");
            dbURL = properties.getProperty("MySQL.URL");
            dbID = properties.getProperty("DB.ID");
            dbPW = properties.getProperty("DB.PW");

            Class.forName(dbDriver);

            conn = DriverManager.getConnection(dbURL, dbID, dbPW);
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 데이터베이스 연결 해제
     */
    private static void dbDisconnect() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 쿼리 실행 [ SELECT ]
     *
     * @param query    실행할 쿼리문
     * @return     쿼리 실행 결과를 ResultSet 으로 반환
     */
    public static ResultSet dbExecuteQuery(String query) {
        Statement stmt = null;
        ResultSet rs = null;
        CachedRowSetImpl crs = null;

        try {
            dbConnect();

            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            crs = new CachedRowSetImpl();
            crs.populate(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }

                dbDisconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return crs;
    }

    /**
     * 쿼리 실행 [INSERT, UPDATE, DELETE ]
     *
     * @param query     실행할 쿼리문
     */
    public static void dbExecuteUpdate(String query) {
        Statement stmt = null;

        try {
            dbConnect();

            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

                dbDisconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 쿼리를 실행해 반환받은 BLOB 데이터를 String 으로 변환
     *
     * @param bufferedReader     String 으로 변환할 BLOB 데이터
     * @return     변환된 String 객체
     */
    public static String blobToSting(BufferedReader bufferedReader) {
        try {
            StringBuffer strOut = new StringBuffer();
            String aux;

            while ((aux=bufferedReader.readLine())!=null) {
                strOut.append(aux);
            }

            return strOut.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
