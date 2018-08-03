package kr.pe.kaijer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Cho, Wonsik on 2018-08-02.
 */

public class Register {
    private JPanel jPanel;
    private static JFrame jFrame = new JFrame("WS Notebook");

    private JLabel lb_Register;
    private JLabel lb_ID;
    private JLabel lb_Email;
    private JLabel lb_PW;

    private JTextField tf_ID;
    private JTextField tf_EmailAddress;
    private JPasswordField pf_PW;

    private JButton btn_Register;
    private JButton btn_Cancel;

    private static String dbDriver, dbURL, dbID, dbPW;

    public Register() {
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

    // 가입 버튼 클릭 이벤트
    private class RegisterButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Connection connection = null;
            Statement statement = null;

            String id, email, pw, encoded_pw;
            String query;

            id = tf_ID.getText();
            email = tf_EmailAddress.getText();
            pw = new String(pf_PW.getPassword());
            encoded_pw = Encrypt.encode(id, pw, "SHA-512");

            query = "INSERT INTO users VALUES (\"" + id + "\", \"" + email + "\", \"" + encoded_pw + "\")";

            if (id.equals("")) {
                JOptionPane.showMessageDialog(null, "ID를 입력하세요...");
            } else if (!isValidEmailAddress(email)) {
                JOptionPane.showMessageDialog(null, "올바른 E-Mail 형식이 아닙니다...");
            } else if (pw.equals("")) {
                JOptionPane.showMessageDialog(null, "PW를 입력하세요...");
            } else if (isDuplicateID(id)) {
                JOptionPane.showMessageDialog(null, "이미 사용중인 ID 입니다.\n다른 ID를 사용해 주세요.");
            }  else {
                try {
                    Class.forName(dbDriver);
                    connection = DriverManager.getConnection(dbURL, dbID, dbPW);
                    statement = connection.createStatement();

                    statement.executeUpdate(query);

                    JOptionPane.showMessageDialog(null, "회원가입이 완료 되었습니다!");

                    jFrame.dispose();
                    Login.login();
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
            Login.login();
        }
    }

    // ID 중복 확인 [ 리턴 값이 거짓인 경우 사용 가능한 ID ]
    public static boolean isDuplicateID(String id) {
        Connection connection = null;
        Statement statement = null;

        String query = "SELECT id FROM users WHERE id = \"" + id + "\";";

        try {
            Class.forName(dbDriver);
            connection = DriverManager.getConnection(dbURL, dbID, dbPW);
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                return true;
            } else {
                return false;
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

        return true;
    }

    // E-Mail 주소 유효성 확인 [ 리턴 값이 참인 경우 유효한 E-Mail 주소 ]
    public static boolean isValidEmailAddress(String email) {
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}
