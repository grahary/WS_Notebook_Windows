package kr.pe.kaijer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Created by Cho, Wonsik on 2018-08-01.
 */

public class Login {
    private JPanel jPanel;
    private static JFrame jFrame = new JFrame("WS Notebook");

    private JLabel lb_AppName;
    private JLabel lb_ID;
    private JLabel lb_PW;

    private JTextField tf_ID;       // ID Field
    private JPasswordField pf_PW;   // PW Field

    private JButton btn_Login;
    private JButton btn_Register;
    private JButton btn_SearchPW;

    private static String dbDriver, dbURL, dbID, dbPW;

    public Login() {
        btn_Login.addActionListener(new LoginButtonClicked());
        btn_Register.addActionListener(new RegisterButtonClicked());
        btn_SearchPW.addActionListener(new SearchPWButtonClicked());
    }

    public static void login() {
        jFrame.setContentPane(new Login().jPanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    // 로그인 버튼 클릭 이벤트
    private class LoginButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Connection connection = null;
            Statement statement = null;

            String id, pw, encoded_pw;
            String query;

            id = tf_ID.getText();
            pw = new String(pf_PW.getPassword());
            encoded_pw = Encrypt.encode(id, pw, "SHA-512");

            query = "SELECT pw FROM users WHERE id = \"" + id + "\";";

            if (id.equals("")) {
                JOptionPane.showMessageDialog(null, "ID를 입력하세요...");
            } else if (pw.equals("")) {
                JOptionPane.showMessageDialog(null, "PW를 입력하세요...");
            } else {
                try {
                    Class.forName(dbDriver);
                    connection = DriverManager.getConnection(dbURL, dbID, dbPW);
                    statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);

                    if (resultSet.next()) {
                        pw = resultSet.getString("pw");

                        if (pw.equals(encoded_pw)) {
                            JOptionPane.showMessageDialog(null, "로그인 성공!");
                            jFrame.dispose();
                            MemoList.memoList(id);
                        } else {
                            JOptionPane.showMessageDialog(null, "비밀번호를 잘못 입력하셨습니다.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "존재하지 않는 ID 입니다.");
                    }
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
            }
        }
    }

    // 회원가입 버튼 클릭 이벤트
    private class RegisterButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Register.register();
            jFrame.dispose();
        }
    }

    // PW 찾기 버튼 클릭 이벤트
    private class SearchPWButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("SearchPW Button Clicked!");
        }
    }
}
