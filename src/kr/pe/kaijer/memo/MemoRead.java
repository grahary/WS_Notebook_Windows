package kr.pe.kaijer.memo;

import kr.pe.kaijer.db.DBObject;
import kr.pe.kaijer.db.DBProcess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Cho, Wonsik on 2018-08-06.
 */

public class MemoRead {
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

    private JButton btn_Update;
    private JButton btn_Delete;
    private JButton btn_Back;

    private static DBObject dbObject;

    public MemoRead() {
        tf_Title.setText(dbObject.title);
        tf_WriteDate.setText(dbObject.write_date);
        ta_Content.setText(dbObject.content);
        tf_Tag.setText(dbObject.tag);

        btn_Update.addActionListener(new UpdateButtonClicked());
        btn_Delete.addActionListener(new DeleteButtonClicked());
        btn_Back.addActionListener(new BackButtonClicked());
    }

    public static void memoRead(DBObject object) {
        dbObject = DBProcess.dbSelect(object);

        jFrame.setContentPane(new MemoRead().jPanel);
        jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                jFrame.dispose();
                MemoList.memoList(dbObject.user_id);
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
            jFrame.dispose();
            MemoUpdate.memoUpdate(dbObject);
        }
    }

    // 삭제 버튼 클릭 이벤트
    private class DeleteButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            DBProcess.dbDelete(dbObject.idx);

            jFrame.dispose();
            MemoList.memoList(dbObject.user_id);
        }
    }

    // 리스트로 버튼 클릭 이벤트
    private class BackButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            jFrame.dispose();
            MemoList.memoList(dbObject.user_id);
        }
    }
}
