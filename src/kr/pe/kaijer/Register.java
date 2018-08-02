package kr.pe.kaijer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Cho, Wonsik on 2018-08-02.
 */

public class Register {
    private JPanel jPanel;

    private JLabel lb_Register;
    private JLabel lb_ID;
    private JLabel lb_EMail;
    private JLabel lb_PW;

    private JTextField tf_ID;
    private JTextField tf_EMail;
    private JPasswordField pf_PW;

    private JButton btn_Register;
    private JButton btn_Cancel;

    public Register() {
        btn_Register.addActionListener(new RegisterButtonClicked());
        btn_Cancel.addActionListener(new CancelButtonClicked());
    }

    public static void register() {
        JFrame jFrame = new JFrame("WS Notebook");

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
    }

    // 가입 버튼 클릭 이벤트
    private class RegisterButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Register Button Clicked!");
        }
    }

    // 취소 버튼 클릭 이벤트
    private class CancelButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Cancel Button Clicked!");
        }
    }
}
