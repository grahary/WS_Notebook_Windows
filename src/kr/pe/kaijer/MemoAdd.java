package kr.pe.kaijer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Cho, Wonsik on 2018-08-06.
 */

public class MemoAdd {
    private JPanel jPanel;
    private static JFrame jFrame = new JFrame("WS Notebook");

    private JLabel lb_Title;
    private JLabel lb_Tag;

    private JTextField tf_Title;
    private JTextArea ta_Content;
    private JScrollPane jScrollPane;
    private JTextField tf_Tag;
    private JCheckBox ck_isEncrypt;
    private JPasswordField pf_EncPW;

    private JButton btn_Save;
    private JButton btn_Cancel;

    private static String user_id;
    private static String dbDriver, dbURL, dbID, dbPW;

    public MemoAdd() {
        btn_Save.addActionListener(new SaveButtonClicked());
        btn_Cancel.addActionListener(new CancelButtonClicked());
    }

    public static void memoAdd(String id) {
        user_id = id;

        jFrame.setContentPane(new MemoAdd().jPanel);
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

    // 저장 버튼 클릭 이벤트
    private class SaveButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Connection connection = null;
            Statement statement = null;

            String title = tf_Title.getText();
            String content = ta_Content.getText();
            String tag = tf_Tag.getText();

            String pw = new String(pf_EncPW.getPassword());
            String encoded_pw = "";

            Boolean is_enc = false;
            if (ck_isEncrypt.isSelected()) {
                is_enc = true;
                encoded_pw = Encrypt.encode(title, pw, "SHA-512");
            }

            String query = "INSERT INTO notebook(title, user_id, content, tag, is_enc, enc_pw) " +
                    "VALUES (\"" + title + "\", \"" + user_id + "\", AES_ENCRYPT(\"" + content + "\", SHA2(\"" + encoded_pw + "\", 512)), \"" + tag + "\", " + is_enc + ", \"" + encoded_pw + "\");";

            if (title.equals("")) {
                JOptionPane.showMessageDialog(null, "제목을 입력하세요...");
            } else if (ck_isEncrypt.isSelected() && pw.equals("")) {
                JOptionPane.showMessageDialog(null, "암호를 입력하세요...");
            } else {
                try {
                    Class.forName(dbDriver);
                    connection = DriverManager.getConnection(dbURL, dbID, dbPW);
                    statement = connection.createStatement();

                    statement.executeUpdate(query);

                    jFrame.dispose();
                    MemoList.memoList(user_id);
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
