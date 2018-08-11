package kr.pe.kaijer.memo;

import kr.pe.kaijer.db.DBObject;
import kr.pe.kaijer.Encrypt;
import kr.pe.kaijer.db.GetJDBCProp;
import kr.pe.kaijer.Main;
import kr.pe.kaijer.user.ChangePW;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

    private static DBObject[] dbObjects;

    public MemoList() {
        lb_UserID.setText(user_id);

        String colNames[] = {"제목", "작성일", "태그"};
        model = new DefaultTableModel(colNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table_Memo.setModel(model);

        table_Memo.addMouseListener(new tableItemClicked());
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

        select("none", null);
    }

    // 테이블 아이템 클릭 이벤트
    private class tableItemClicked extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int rowNum = table_Memo.getSelectedRow();

            if (e.getClickCount() == 2) {
                if (dbObjects[rowNum].is_enc) {
                    JPasswordField pf_EncPW = new JPasswordField();
                    int okcxl = JOptionPane.showConfirmDialog(null, pf_EncPW, "이 글은 잠겨있습니다.\n암호를 입력하세요.", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (okcxl == JOptionPane.OK_OPTION) {
                        String pw = new String(pf_EncPW.getPassword());
                        String encoded_pw = Encrypt.encode(user_id, pw, "SHA-512");

                        if (encoded_pw.equals(dbObjects[rowNum].enc_pw)) {
                            jFrame.dispose();
                            MemoRead.memoRead(dbObjects[rowNum]);
                        } else if(pw == null) {

                        } else {
                            JOptionPane.showMessageDialog(null, "암호가 틀렸습니다.");
                        }
                    }
                } else {
                    jFrame.dispose();
                    MemoRead.memoRead(dbObjects[rowNum]);
                }
            }
        }
    }

    // PW 변경 버튼 클릭 이벤트
    private class ChangePWButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            jFrame.dispose();
            ChangePW.changePW(user_id);
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
                select("none", null);
            }
        }
    }

    // 메모 추가 버튼 클릭 이벤트
    private class AddMemoButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            jFrame.dispose();
            MemoAdd.memoAdd(user_id);
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

        String query = null;
        model.setNumRows(0);

        switch (condition) {
            case "none":
                query = "SELECT * FROM notebook WHERE user_id = \"" + user_id + "\";";
                break;
            case "title":
                query = "SELECT * FROM notebook WHERE user_id = \"" + user_id + "\" && title LIKE \"%" + searchText + "%\";";
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

            int rowCnt = 0;
            int cnt = 0;

            if (resultSet.last()) {
                rowCnt = resultSet.getRow();
                resultSet.beforeFirst();
            }

            dbObjects = new DBObject[rowCnt];

            for (int i = 0; i < dbObjects.length; i++) {
                dbObjects[i] = new DBObject();
            }

            while (resultSet.next()) {
                model.addRow(new Object[]{resultSet.getString("title"), resultSet.getString("write_date"), resultSet.getString("tag")});

                dbObjects[cnt].rowNum = cnt;
                dbObjects[cnt].idx = resultSet.getInt("idx");
                dbObjects[cnt].title = resultSet.getString("title");
                dbObjects[cnt].is_enc = resultSet.getBoolean("is_enc");
                dbObjects[cnt].enc_pw = resultSet.getString("enc_pw");

                cnt++;
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