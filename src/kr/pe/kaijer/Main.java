package kr.pe.kaijer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Cho, Wonsik on 2018-08-01.
 */

public class Main {
    private JPanel jPanel;

    public static String user_id = "";

    public Main() {

    }

    public static void main(String args[]) {
        JFrame jFrame = new JFrame("WS Notebook");

        jFrame.setContentPane(new Main().jPanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();

        Dimension frameSize = jFrame.getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        jFrame.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);

        user_id = Login.login();

        if (!user_id.equals("")) {
            System.out.println(user_id);

            jFrame.setVisible(true);
        }
    }
}
