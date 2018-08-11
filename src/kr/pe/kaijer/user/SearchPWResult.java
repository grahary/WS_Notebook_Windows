package kr.pe.kaijer.user;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SearchPWResult extends JDialog {
    private JPanel jPanel;
    private JButton btn_OK;
    private JLabel lb_text1;
    private JLabel lb_text2;
    private JTextField tf_NewPW;

    private static String user_id, newPW;

    public SearchPWResult() {
        setContentPane(jPanel);
        setModal(true);

        lb_text1.setText(user_id + "님의 비밀번호가 다음과 같이 변경되었습니다. ");
        tf_NewPW.setText(newPW);

        getRootPane().setDefaultButton(btn_OK);

        btn_OK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
    }

    private void onOK() {
        dispose();
        Login.login();
    }

    public static void searchPWResult(String id, String pw) {
        user_id = id;
        newPW = pw;

        SearchPWResult dialog = new SearchPWResult();
        dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dialog.dispose();
                Login.login();
            }
        });
        dialog.pack();

        Dimension frameSize = dialog.getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        dialog.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
        dialog.setVisible(true);
    }
}
