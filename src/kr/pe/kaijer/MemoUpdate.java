package kr.pe.kaijer;

import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Cho, Wonsik on 2018-08-07.
 */

public class MemoUpdate {
    private JPanel jPanel;
    private static JFrame jFrame = new JFrame("WS Notebook");

    private JLabel lb_Title;
    private JLabel lb_WriteDate;
    private JLabel lb_Tag;

    private JTextField tf_Title;
    private JTextField tf_WriteDate;
    private JTextArea ta_Content;
    private JScrollPane jScrollPane;
    private JTextField tf_Tag;
    private JCheckBox ck_isEncrypt;
    private JPasswordField pf_EncPW;

    private JButton btn_Update;
    private JButton btn_Cancel;

    private static DBObject dbObject;

    public MemoUpdate() {
        tf_Title.setText(dbObject.title);
        tf_WriteDate.setText(dbObject.write_date);
        ta_Content.setText(dbObject.content);
        tf_Tag.setText(dbObject.tag);

        if (dbObject.is_enc) {
            ck_isEncrypt.setSelected(true);
        } else {
            ck_isEncrypt.setSelected(false);
        }

        btn_Update.addActionListener(new UpdateButtonClicked());
        btn_Cancel.addActionListener(new CancelButtonClicked());
    }

    public static void memoUpdate(DBObject object) {
        dbObject = DBProcess.dbSelect(object);

        jFrame.setContentPane(new MemoUpdate().jPanel);
        jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jFrame.dispose();
                MemoRead.memoRead(dbObject);
            }
        });
        jFrame.pack();

        Dimension frameSize = jFrame.getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        jFrame.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
        jFrame.setVisible(true);
    }

    // 수정 버튼 클릭 이벤트
    private class UpdateButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String title = tf_Title.getText();
            String content = ta_Content.getText();
            String tag = tf_Tag.getText();

            String pw = new String(pf_EncPW.getPassword());
            String encoded_pw = "";

            Boolean isEncPWCheck = true;

            // 암호화 옵션, 암호 변경
            if (ck_isEncrypt.isSelected()) {
                if (dbObject.is_enc) {
                    if (!pw.equals("")) {
                        // 암호 변경
                        encoded_pw = Encrypt.encode(dbObject.user_id, pw, "SHA-512");
                        dbObject.enc_pw = encoded_pw;
                    }
                } else {
                    // false => true
                    if (pw.equals("")) {
                        JOptionPane.showMessageDialog(null, "암호를 입력하세요...");
                        isEncPWCheck = false;
                    } else {
                        encoded_pw = Encrypt.encode(dbObject.user_id, pw, "SHA-512");

                        dbObject.is_enc = true;
                        dbObject.enc_pw = encoded_pw;
                    }
                }
            } else {
                // true => false
                if (dbObject.is_enc) {
                    dbObject.is_enc = false;
                    dbObject.enc_pw = encoded_pw;
                }
            }

            if (isEncPWCheck) {
                if (title.equals("")) {
                    JOptionPane.showMessageDialog(null, "제목을 입력하세요...");
                } else {
                    dbObject.title = title;
                    dbObject.content = content;
                    dbObject.tag = tag;

                    DBProcess.dbUpdate(dbObject);

                    jFrame.dispose();
                    MemoRead.memoRead(dbObject);
                }
            }
        }
    }

    // 취소 버튼 클릭 이벤트
    private class CancelButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            jFrame.dispose();
            MemoRead.memoRead(dbObject);
        }
    }
}
