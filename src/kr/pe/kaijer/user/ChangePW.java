package kr.pe.kaijer.user;

import kr.pe.kaijer.Encrypt;
import kr.pe.kaijer.db.GetJDBCProp;
import kr.pe.kaijer.memo.MemoList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

/**
 * Created by Cho, Wonsik on 2018-08-08.
 */

public class ChangePW {
    private JPanel jPanel;
    private static JFrame jFrame = new JFrame("WS Notebook");

    private JLabel lb_OriginPW;
    private JLabel lb_ChangePW;
    private JLabel lb_ChangePWRe;

    private JPasswordField pf_OriginPW;
    private JPasswordField pf_ChangePW;
    private JPasswordField pf_ChangePWRe;

    private JButton btn_Change;
    private JButton btn_Cancel;

    private static String user_id;
    private static String dbDriver, dbURL, dbID, dbPW;

    public ChangePW() {
        btn_Change.addActionListener(new ChangeButtonClicked());
        btn_Cancel.addActionListener(new CancelButtonClicked());
    }

    public static void changePW(String id) {
        user_id = id;

        jFrame.setContentPane(new ChangePW().jPanel);
        jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jFrame.dispose();
                MemoList.memoList(user_id);
            }
        });
        jFrame.pack();

        Dimension frameSize = jFrame.getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        jFrame.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
        jFrame.setVisible(true);

        // JDBC 로그인 정보 받아오기
        GetJDBCProp.getJDBCProp();

        dbDriver = GetJDBCProp.dbDriver;
        dbURL = GetJDBCProp.dbURL;
        dbID = GetJDBCProp.dbID;
        dbPW = GetJDBCProp.dbPW;
    }

    // PW 변경 버튼 클릭 이벤트
    private class ChangeButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Connection connection = null;
            Statement statement = null;

            String originPW = new String(pf_OriginPW.getPassword());
            String newPW = new String(pf_ChangePW.getPassword());
            String newPWRe = new String(pf_ChangePWRe.getPassword());

            String encOriPW = Encrypt.encode(user_id, originPW, "SHA-512");
            String encNewPW = Encrypt.encode(user_id, newPW, "SHA-512");

            String sqlSelect = "SELECT pw FROM users WHERE id = \"" + user_id + "\";";
            String sqlUpdate = "UPDATE users SET pw = \"" + encNewPW + "\" WHERE id = \"" + user_id + "\";";

            if (originPW.equals("")) {
                JOptionPane.showMessageDialog(null, "현재 비밀번호를 입력하세요.");
            } else if (newPW.equals("")) {
                JOptionPane.showMessageDialog(null, "변경할 비밀번호를 입력하세요.");
            } else if (newPWRe.equals("")) {
                JOptionPane.showMessageDialog(null, "변경할 비밀번호를 한 번 더 입력하세요.");
            } else if (!newPW.equals(newPWRe)) {
                JOptionPane.showMessageDialog(null, "변경할 비밀번호가 일치하지 않습니다.");
            } else {
                try {
                    Class.forName(dbDriver);
                    connection = DriverManager.getConnection(dbURL, dbID, dbPW);
                    statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sqlSelect);

                    if (resultSet.next()) {
                        String pw = resultSet.getString("pw");

                        if (pw.equals(encOriPW)) {
                            statement.executeUpdate(sqlUpdate);

                            jFrame.dispose();
                            MemoList.memoList(user_id);
                        } else {
                            JOptionPane.showMessageDialog(null, "비밀번호를 잘못 입력하셨습니다.");
                        }
                    }
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
        }
    }

    // 취소 버튼 클릭 이벤트
    private class CancelButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            jFrame.dispose();
            MemoList.memoList(user_id);
        }
    }
}
