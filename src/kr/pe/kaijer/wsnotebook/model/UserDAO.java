/*
 * UserDAO.java
 *
 * Created by Cho, Wonsik on 2018-08-22.
 */

package kr.pe.kaijer.wsnotebook.model;

import kr.pe.kaijer.wsnotebook.util.DBUtil;
import kr.pe.kaijer.wsnotebook.util.DialogUtil;
import kr.pe.kaijer.wsnotebook.util.EncUtil;
import kr.pe.kaijer.wsnotebook.util.UserUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;

import static kr.pe.kaijer.wsnotebook.util.DialogUtil.infoDialog;

public class UserDAO {
    public static boolean login(User user) {
        String query = "SELECT pw FROM users WHERE id = \"" + user.getId() + "\";";

        ResultSet rs = DBUtil.dbExecuteQuery(query);

        try {
            if (rs.next()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(rs.getBinaryStream("pw")));
                String userPW = DBUtil.blobToSting(br);

                if (userPW.equals(user.getPw())) {
                    return true;
                } else {
                    infoDialog("PW를 잘못 입력하셨습니다!");
                }
            } else {
                infoDialog("존재하지 않는 ID 입니다!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void register(User user) {
        String query = "INSERT INTO users VALUES (\"" + user.getId() + "\", \"" + user.getEmail() + "\", \"" + user.getPw() + "\", " + user.getHintQuestion() + ", \"" + user.getHintAnswer() +"\")";

        DBUtil.dbExecuteUpdate(query);
    }

    public static Boolean searchPW(User user) {
        String query = "SELECT * FROM users WHERE id = \""+ user.getId() + "\" AND email = \"" + user.getEmail() + "\";";

        try {
            ResultSet rs = DBUtil.dbExecuteQuery(query);

            if (rs.next()) {
                if (rs.getInt("hintq") == user.getHintQuestion() && rs.getString("hinta").equals(user.getHintAnswer())) {
                    String newPW = UserUtil.createNewPW(user.getId());
                    String pw = EncUtil.encode(user.getId(), newPW, "SHA-512");

                    DBUtil.dbExecuteUpdate("UPDATE users SET pw = \"" + pw + "\" WHERE id = \"" + user.getId() + "\";");

                    System.out.println(newPW);

                    DialogUtil.changePWDialog(newPW);

                    return true;
                } else {
                    infoDialog("비밀번호 힌트가 틀렸습니다.");

                    return false;
                }
            } else {
                infoDialog("ID 또는 E-Mail이 존재하지 않습니다.");

                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static Boolean changePW(User user, String newPW) {
        String encCurPW = EncUtil.encode(user.getId(), user.getPw(), "SHA-512");
        String encNewPW = EncUtil.encode(user.getId(), newPW, "SHA-512");

        String query = "SELECT * FROM users WHERE id = \"" + user.getId() + "\";";

        try {
            ResultSet rs = DBUtil.dbExecuteQuery(query);

            if (rs.next()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(rs.getBinaryStream("pw")));
                String userPW = DBUtil.blobToSting(br);

                if (encCurPW.equals(userPW)) {
                    DBUtil.dbExecuteUpdate("UPDATE users SET pw = \"" + encNewPW + "\" WHERE id = \"" + user.getId() + "\";");

                    infoDialog("비밀번호가 변경되었습니다.");

                    return true;
                } else {
                    infoDialog("비밀번호를 잘못 입력했습니다..");

                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
