package kr.pe.kaijer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

/**
 * Created by Cho, Wonsik on 2018-08-02.
 */

public class Register {
    private JPanel jPanel;
    private static JFrame jFrame = new JFrame("WS Notebook");

    private JLabel lb_Register;
    private JLabel lb_ID;
    private JLabel lb_Email;
    private JLabel lb_at;
    private JLabel lb_PW;

    private JTextField tf_ID;
    private JTextField tf_EmailName;
    private JTextField tf_EmailDomain;
    private JPasswordField pf_PW;

    private JButton btn_Check;
    private JButton btn_Register;
    private JButton btn_Cancel;

    private static String dbDriver, dbURL, dbID, dbPW;

    private static boolean isIDCheck = false;

    public Register() {
        btn_Check.addActionListener(new CheckButtonClicked());
        btn_Register.addActionListener(new RegisterButtonClicked());
        btn_Cancel.addActionListener(new CancelButtonClicked());
    }

    public static void register() {
        jFrame.setContentPane(new Register().jPanel);
        jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jFrame.dispose();
                Login.login();
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

    // 중복확인 버튼 클릭 이벤트
    private class CheckButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Connection connection = null;
            Statement statement = null;

            String query = "SELECT id FROM users WHERE id = \"" + tf_ID.getText() + "\";";

            try {
                Class.forName(dbDriver);
                connection = DriverManager.getConnection(dbURL, dbID, dbPW);
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(null, "이미 존재하는 ID 입니다.\n다른 ID를 사용해 주세요.");
                } else {
                    JOptionPane.showMessageDialog(null, "사용 가능한 ID입니다.\n계속 진행해 주세요.");

                    isIDCheck = true;
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

    // 가입 버튼 클릭 이벤트
    private class RegisterButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Connection connection = null;
            Statement statement = null;

            String id, email, pw, encoded_pw;
            String query;

            id = tf_ID.getText();
            email = tf_EmailName.getText() + "@" + tf_EmailDomain;
            pw = new String(pf_PW.getPassword());
            encoded_pw = Encrypt.encode(id, pw, "SHA-512");

            query = "";

            if (id.equals("")) {
                JOptionPane.showMessageDialog(null, "ID를 입력하세요...");
            } else if (!isIDCheck) {
                JOptionPane.showMessageDialog(null, "ID 중복확인을 해주세요...");
            } else if (!isValidEmailAddress(email)) {
                JOptionPane.showMessageDialog(null, "올바른 E-Mail 형식이 아닙니다...");
            } else if (pw.equals("")) {
                JOptionPane.showMessageDialog(null, "PW를 입력하세요...");
            } else {
                System.out.println("회원가입");
            }
        }
    }

    // 취소 버튼 클릭 이벤트
    private class CancelButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            jFrame.dispose();
            Login.login();
        }
    }

    public static boolean isValidEmailAddress(String email) {
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        if (email.equals(regex)) {
            return true;
        } else {
            return false;
        }
    }
}
