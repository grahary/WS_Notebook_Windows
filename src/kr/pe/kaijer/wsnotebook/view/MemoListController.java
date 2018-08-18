/*
 * MemoListController.java
 *
 * Created by Cho, Wonsik on 2018-08-18.
 */

package kr.pe.kaijer.wsnotebook.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import kr.pe.kaijer.wsnotebook.MainApp;
import kr.pe.kaijer.wsnotebook.model.Memo;
import kr.pe.kaijer.wsnotebook.model.MemoDAO;
import kr.pe.kaijer.wsnotebook.util.EncUtil;
import static kr.pe.kaijer.wsnotebook.util.DialogUtil.InfoDialog;
import static kr.pe.kaijer.wsnotebook.util.DialogUtil.PWInputDialog;

public class MemoListController extends AnchorPane {
    @FXML private Label lbUserID;
    @FXML private ComboBox<String> cbSearchType;
    @FXML private TextField tfSearchText;
    @FXML private Button btnSearch;
    @FXML private Button btnLogout;

    @FXML private TableView<Memo> tableMemoList;
    @FXML private TableColumn<Memo, String> colTitle;
    @FXML private TableColumn<Memo, String> colWriteDate;
    @FXML private TableColumn<Memo, String> colTag;

    private MainApp mainApp;
    private String userID;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * 로그인한 사용자의 ID 가져오기 및 테이블 초기화
     *
     * @param id     로그인한 사용자 ID
     */
    public void userLogged(String id) {
        userID = id;
        lbUserID.setText("Welcome " + id + "!");

        ObservableList<String> list = FXCollections.observableArrayList("전체보기", "제목", "태그");
        cbSearchType.setItems(list);
        cbSearchType.getSelectionModel().selectFirst();

        ObservableList<Memo> memoList = MemoDAO.searchMemo(userID, "전체보기", null);
        tableMemoList.setItems(memoList);
    }

    @FXML
    void initialize() {
        colTitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        colWriteDate.setCellValueFactory(cellData -> cellData.getValue().writeDateProperty());
        colTag.setCellValueFactory(cellData -> cellData.getValue().tagProperty());

        tableMemoList.setOnMouseClicked(event -> handleTableItemClicked(event));
        btnSearch.setOnAction(event -> handleSearchAction(event));
        btnLogout.setOnAction(event -> handleLogoutAction(event));
    }

    /**
     * 테이블 클릭 이벤트
     *
     * 사용자가 클릭한 메모 객체를 MemoRead 로 이동해 해당 메모의 내용 출력
     */
    private void handleTableItemClicked(MouseEvent event) {
        int rowNum = tableMemoList.getSelectionModel().getSelectedIndex();
        Memo memo = tableMemoList.getSelectionModel().getSelectedItem();

        if (event.getClickCount() == 2 && rowNum >= 0) {
            if (memo.getIsEncrypt()) {
                String pw = PWInputDialog();
                String encoded_pw = EncUtil.encode(userID, pw, "SHA-512");

                if (encoded_pw.equals(memo.getEncryptPW())) {
                    MemoDAO.readMemo(memo);

                    try {
                        mainApp.setMemo(memo);
                        mainApp.showModalContent("MemoRead");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (!encoded_pw.equals(memo.getEncryptPW()) && pw != null) {
                    InfoDialog("비밀번호가 틀림");
                }
            } else {
                MemoDAO.readMemo(memo);

                try {
                    mainApp.setMemo(memo);
                    mainApp.showModalContent("MemoRead");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 검색 버튼 클릭 이벤트
     *
     * 검색 조건과 텍스트로 쿼리를 실행해 해당하는 메모를 테이블에 출력
     */
    private void handleSearchAction(ActionEvent event) {
        String searchType, searchText;

        searchType = cbSearchType.getValue();
        searchText = tfSearchText.getText();

        ObservableList<Memo> memoList = MemoDAO.searchMemo(userID, searchType, searchText);
        tableMemoList.setItems(memoList);
    }

    private void handleLogoutAction(ActionEvent event) {
        mainApp.showLoginView();
    }
}