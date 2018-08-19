/*
 * MemoDAO.java
 *
 * Created by Cho, Wonsik on 2018-08-16.
 */

package kr.pe.kaijer.wsnotebook.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.apache.commons.text.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.pe.kaijer.wsnotebook.util.DBUtil;

public class MemoDAO {
    /**
     * 메모 리스트 출력을 위한 메소드
     *
     * @param userID     로그인한 사용자 ID
     * @param searchType     검색을 원하는 조건 [ 전체보기, 제목, 태그 ]
     * @param searchText     검색을 원하는 문자열
     * @return     Memo 객체 배열을 반환
     */
    public static ObservableList<Memo> searchMemo(String userID, String searchType, String searchText) {
        String query = null;

        switch (searchType) {
            case "전체보기":
                query = "SELECT * FROM notebook WHERE user_id = \"" + userID + "\";";
                break;
            case "제목":
                query = "SELECT * FROM notebook WHERE user_id = \"" + userID + "\" && title LIKE \"%" + searchText + "%\";";
                break;
            case "태그":
                query = "SELECT * FROM notebook WHERE user_id = \"" + userID + "\" && tag = \"" + searchText + "\";";
                break;
        }

        ResultSet rs = DBUtil.dbExecuteQuery(query);
        ObservableList<Memo> memoList = FXCollections.observableArrayList();

        try {
            while (rs.next()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(rs.getBinaryStream("enc_pw")));
                String encPW = DBUtil.blobToSting(br);

                Memo memo = new Memo();
                memo.setIdx(rs.getInt("idx"));
                memo.setTitle(rs.getString("title"));
                memo.setUserID(userID);
                memo.setWriteDate(rs.getString("write_date"));
                memo.setTag(rs.getString("tag"));
                memo.setIsEncrypt(rs.getBoolean("is_enc"));
                memo.setEncryptPW(encPW);
                memoList.add(memo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return memoList;
    }

    /**
     * 새로운 메모를 추가하는 메소드
     *
     * @param memo    데이터베이스에 추가할 메모 객체
     */
    public static void addMemo(Memo memo) {
        String content = StringEscapeUtils.escapeHtml4(memo.getContent());

        String query = "INSERT INTO notebook(title, user_id, content, tag, is_enc, enc_pw) " +
                "VALUES (\"" + memo.getTitle() + "\", \"" + memo.getUserID() + "\", AES_ENCRYPT(\"" + content + "\", SHA2(\"" + memo.getEncryptPW() + "\", 512)), \"" +
                memo.getTag() + "\", " + memo.getIsEncrypt() + ", \"" + memo.getEncryptPW() + "\");";

        DBUtil.dbExecuteUpdate(query);
    }

    /**
     * 사용자가 선택한 메모의 내용을 불러오기 위한 메소드
     *
     * @param memo     사용자가 선택한 메모 객체
     */
    public static void readMemo(Memo memo) {
        String query = "SELECT AES_DECRYPT(content, SHA2(\"" + memo.getEncryptPW() + "\", 512)) AS content FROM notebook WHERE idx = " + memo.getIdx() + ";";
        ResultSet rs = DBUtil.dbExecuteQuery(query);

        try {
            while (rs.next()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(rs.getBinaryStream("content")));
                String str = DBUtil.blobToSting(br);
                String content = StringEscapeUtils.unescapeHtml4(str);

                memo.setContent(content);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public static void updateMemo(Memo memo) {
        String content = StringEscapeUtils.escapeHtml4(memo.getContent());

        String query = "UPDATE notebook " +
                "SET title = \"" + memo.getTitle() + "\", content = AES_ENCRYPT(\"" + content + "\", SHA2(\"" + memo.getEncryptPW() + "\", 512)), tag = \"" + memo.getTag() +
                "\", is_enc = " + memo.getIsEncrypt() + ", enc_pw = \"" + memo.getEncryptPW() + "\" WHERE idx = " + memo.getIdx() + ";";

        DBUtil.dbExecuteUpdate(query);
    }

    /**
     *
     */
    public static void deleteMemo(int idx) {
        String query = "DELETE FROM notebook WHERE idx = " + idx + ";";

        DBUtil.dbExecuteUpdate(query);
    }
}
