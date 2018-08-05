package kr.pe.kaijer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Cho, Wonsik on 2018-08-05.
 */

public class MemoList {
    private JPanel jPanel;
    private static JFrame jFrame = new JFrame("WS Notebook");

    private JLabel lb_UserID;

    private JComboBox cb_searchType;
    private JTextField tf_SearchMemo;
    private JTable table_Memo;

    private JButton btn_ChangePW;
    private JButton btn_Logout;
    private JButton btn_SearchMemo;
    private JButton btn_AddMemo;

    private static String user_id;

    public MemoList() {
        lb_UserID.setText(user_id);

        btn_ChangePW.addActionListener(new ChangePWButtonClicked());
        btn_Logout.addActionListener(new LogoutButtonClicked());
        btn_SearchMemo.addActionListener(new SearchMemoButtonClicked());
        btn_AddMemo.addActionListener(new AddMemoButtonClicked());
    }

    public static void memoList(String id) {
        user_id = id;

        jFrame.setContentPane(new MemoList().jPanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();

        Dimension frameSize = jFrame.getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        jFrame.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
        jFrame.setVisible(true);
    }

    // PW 변경 버튼 클릭 이벤트
    private class ChangePWButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("ChangePW Button Clicked!");
        }
    }

    // 로그아웃 버튼 클릭 이벤트
    private class LogoutButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            jFrame.dispose();
            Main.main(null);
        }
    }

    // 메모 검색 버튼 클릭 이벤트
    private class SearchMemoButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("SearchMemo Button Clicked!");
        }
    }

    // 메모 추가 버튼 클릭 이벤트
    private class AddMemoButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("AddMemo Button Clicked!");
        }
    }
}