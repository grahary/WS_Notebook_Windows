/*
 * MemoAddController.java
 *
 * Created by Cho, Wonsik on 2018-08-19.
 */

package kr.pe.kaijer.wsnotebook.view;

import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

import kr.pe.kaijer.wsnotebook.MainApp;
import kr.pe.kaijer.wsnotebook.model.Memo;
import kr.pe.kaijer.wsnotebook.model.MemoDAO;
import kr.pe.kaijer.wsnotebook.util.EncUtil;
import static kr.pe.kaijer.wsnotebook.util.DialogUtil.infoDialog;

public class MemoAddController {
    private MainApp mainApp;
    private Stage modalStage;
    private String userID;

    @FXML private TextField tfTitle;
    @FXML private HTMLEditor editorContent;
    @FXML private TextField tfTag;
    @FXML private CheckBox ckIsEnc;
    @FXML private PasswordField pfEncPW;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    @FXML
    void initialize() {
        btnSave.setOnAction(event -> handleBtnSaveAction(event));
        btnCancel.setOnAction(event -> handleBtnCancelAction(event));
    }

    public void setModalStage(MainApp mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.modalStage = stage;
    }

    public void setUserID(String id) {
        this.userID = id;
    }

    /**
     * 저장 버튼 클릭 이벤트
     */
    private void handleBtnSaveAction(ActionEvent event) {
        Memo memo = new Memo();

        String title = tfTitle.getText();
        String content = editorContent.getHtmlText();
        String tag = tfTag.getText();

        String pw = pfEncPW.getText();
        String encodedPW = "";

        Boolean isEnc = false;
        if (ckIsEnc.isSelected()) {
            isEnc = true;
            encodedPW = EncUtil.encode(userID, pw, "SHA-512");
        }

        if (title.equals("")) {
            infoDialog("제목을 입력하세요.");
        } else if (ckIsEnc.isSelected() && pw.equals("")) {
            infoDialog("암호를 입력하세요.");
        } else {
            memo.setUserID(userID);
            memo.setTitle(title);
            memo.setContent(content);
            memo.setTag(tag);
            memo.setIsEncrypt(isEnc);
            memo.setEncryptPW(encodedPW);

            MemoDAO.addMemo(memo);

            mainApp.showMemoListView(memo.getUserID());
            mainApp.setMemo(memo);
            mainApp.showModalContent("MemoRead");
            modalStage.close();
        }
    }

    /**
     * 취소 버튼 클릭 이벤트
     */
    private void handleBtnCancelAction(ActionEvent event) {
        modalStage.close();
    }
}
