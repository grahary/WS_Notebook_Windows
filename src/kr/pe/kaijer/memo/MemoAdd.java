package kr.pe.kaijer.memo;

import kr.pe.kaijer.db.DBObject;
import kr.pe.kaijer.db.DBProcess;
import kr.pe.kaijer.Encrypt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

    private static DBObject dbObject = new DBObject();

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
    }

    // 저장 버튼 클릭 이벤트
    private class SaveButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String title = tf_Title.getText();
            String content = ta_Content.getText();
            String tag = tf_Tag.getText();

            String pw = new String(pf_EncPW.getPassword());
            String encoded_pw = "";

            Boolean is_enc = false;
            if (ck_isEncrypt.isSelected()) {
                is_enc = true;
                encoded_pw = Encrypt.encode(user_id, pw, "SHA-512");
            }

            if (title.equals("")) {
                JOptionPane.showMessageDialog(null, "제목을 입력하세요...");
            } else if (ck_isEncrypt.isSelected() && pw.equals("")) {
                JOptionPane.showMessageDialog(null, "암호를 입력하세요...");
            } else {
                dbObject.user_id = user_id;
                dbObject.title = title;
                dbObject.content = content;
                dbObject.tag = tag;
                dbObject.is_enc = is_enc;
                dbObject.enc_pw = encoded_pw;

                DBProcess.dbInsert(dbObject);

                jFrame.dispose();
                MemoRead.memoRead(dbObject);
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
