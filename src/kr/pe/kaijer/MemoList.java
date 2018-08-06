package kr.pe.kaijer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

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
    private JScrollPane jScrollPane;

    private static DefaultTableModel model;

    private JButton btn_ChangePW;
    private JButton btn_Logout;
    private JButton btn_SearchMemo;
    private JButton btn_AddMemo;

    private static String user_id;
    private static String dbDriver, dbURL, dbID, dbPW;

    public MemoList() {
        lb_UserID.setText(user_id);

        String colNames[] = {"제목", "작성일", "태그"};
        model = new DefaultTableModel(colNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table_Memo.setModel(model);

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

        // JDBC 로그인 정보 받아오기
        GetJDBCProp.getJDBCProp();

        dbDriver = GetJDBCProp.dbDriver;
        dbURL = GetJDBCProp.dbURL;
        dbID = GetJDBCProp.dbID;
        dbPW = GetJDBCProp.dbPW;

        select("x", null);
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
            String str = cb_searchType.getSelectedItem().toString();
            if (str.equals("제목")) {
                select("title", tf_SearchMemo.getText());
            } else if (str.equals("태그")) {
                select("tag", tf_SearchMemo.getText());
            } else {
                select("x", null);
            }
        }
    }

    // 메모 추가 버튼 클릭 이벤트
    private class AddMemoButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("AddMemo Button Clicked!");
        }
    }

    /**
     *
     * @param condition     검색 조건 선택 [x: 모두 검색, title: 제목 검색, tag: 태그 검색]
     * @param searchText    검색할 텍스트
     */
    private static void select(String condition, String searchText) {
        Connection connection = null;
        Statement statement = null;

        String query = "";
        model.setNumRows(0);

        switch (condition) {
            case "x":
                query = "SELECT * FROM notebook WHERE user_id = \"" + user_id + "\";";
                break;
            case "title":
                query = "SELECT * FROM notebook WHERE user_id = \"" + user_id + "\" && title = \"" + searchText + "\";";
                break;
            case "tag":
                query = "SELECT * FROM notebook WHERE user_id = \"" + user_id + "\" && tag = \"" + searchText + "\";";
                break;
        }

        try {
            Class.forName(dbDriver);
            connection = DriverManager.getConnection(dbURL, dbID, dbPW);
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                model.addRow(new Object[]{resultSet.getString("title"), resultSet.getString("write_date"), resultSet.getString("tag")});
            }

            connection.close();
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