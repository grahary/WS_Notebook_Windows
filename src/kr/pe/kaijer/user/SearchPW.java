package kr.pe.kaijer.user;

import kr.pe.kaijer.Encrypt;
import kr.pe.kaijer.db.GetJDBCProp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Cho, Wonsik on 2018-08-11.
 */

public class SearchPW {
    private JPanel jPanel;
    private static JFrame jFrame = new JFrame("WS Notebook");

    private JLabel lb_ID;
    private JLabel lb_Email;
    private JLabel lb_PWHint;

    private JTextField tf_ID;
    private JTextField tf_Email;
    private JComboBox cb_SelectEmail;
    private JComboBox cb_PWHint;
    private JTextField tf_PWHint;

    private JButton btn_SearchPW;
    private JButton btn_Cancel;

    private static String dbDriver, dbURL, dbID, dbPW;

    public SearchPW() {
        btn_SearchPW.addActionListener(new SearchPWButtonClicked());
        btn_Cancel.addActionListener(new CancelButtonClicked());
    }

    public static void searchPW() {
        jFrame.setContentPane(new SearchPW().jPanel);
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

    // 비밀번호 찾기 버튼 클릭 이벤트
    private class SearchPWButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Connection connection = null;
            Statement statement = null;

            String id, email, emailDomain, hintA;
            int hintQ;
            String query;

            id = tf_ID.getText();

            // E-Mail 도메인 선택시 도메인은 자동 입력
            emailDomain = cb_SelectEmail.getSelectedItem().toString();
            if (emailDomain.equals("직접 입력")) {
                email = tf_Email.getText();
            } else {
                email = tf_Email.getText() + emailDomain;
            }

            hintQ = cb_PWHint.getSelectedIndex();
            hintA = tf_PWHint.getText();

            query = "SELECT * FROM users WHERE id = \""+ id + "\" AND email = \"" + email + "\";";

            if (id.equals("")) {
                JOptionPane.showMessageDialog(null, "ID를 입력하세요...");
            } else if (!isValidEmailAddress(email)) {
                JOptionPane.showMessageDialog(null, "올바른 E-Mail 형식이 아닙니다...");
            } else if (hintA.equals("")) {
                JOptionPane.showMessageDialog(null, "비밀번호 힌트를 입력해주세요...");
            } else {
                try {
                    Class.forName(dbDriver);
                    connection = DriverManager.getConnection(dbURL, dbID, dbPW);
                    statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);

                    if (resultSet.next()) {
                        if (resultSet.getInt("hintq") == hintQ && resultSet.getString("hinta").equals(hintA)) {
                            String newPW = createNewPW(id);
                            String pw = Encrypt.encode(id, newPW, "SHA-512");

                            statement.executeUpdate("UPDATE users SET pw = \"" + pw + "\" WHERE id = \"" + id + "\";");

                            jFrame.dispose();
                            SearchPWResult.searchPWResult(id, newPW);
                        } else {
                            JOptionPane.showMessageDialog(null, "비밀번호 힌트가 틀렸습니다...");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "ID나 E-Mail이 존재하지않습니다...");
                    }


                    //jFrame.dispose();
                    //Login.login();
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

    // 새로운 비밀번호 생성
    public static String createNewPW(String id) {
        StringBuffer buffer = new StringBuffer();
        Random rand = new Random();

        for (int i = 0; i < 10; i++) {
            int idx = rand.nextInt(3);

            switch (idx) {
                case 0:
                    // a-z
                    buffer.append((char) ((int) (rand.nextInt(26)) + 97));
                    break;
                case 1:
                    // A-Z
                    buffer.append((char) ((int) (rand.nextInt(26)) + 65));
                    break;
                case 2:
                    // 0-9
                    buffer.append((rand.nextInt(10)));
                    break;
            }
        }

        return buffer.toString();
    }

    // E-Mail 주소 유효성 확인 [ 리턴 값이 참인 경우 유효한 E-Mail 주소 ]
    public static boolean isValidEmailAddress(String email) {
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}
