/*
 * MemoUpdateController.java
 *
 * Created by Cho, Wonsik on 2018-08-19.
 */

package kr.pe.kaijer.wsnotebook.view.memo;

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
import kr.pe.kaijer.wsnotebook.util.DialogUtil;
import kr.pe.kaijer.wsnotebook.util.EncUtil;

import static kr.pe.kaijer.wsnotebook.util.DialogUtil.infoDialog;

public class MemoUpdateController {
    private MainApp mainApp;
    private Stage modalStage;
    private Memo memo;

    @FXML private TextField tfTitle;
    @FXML private HTMLEditor editorContent;
    @FXML private TextField tfTag;
    @FXML private CheckBox ckIsEnc;
    @FXML private PasswordField pfEncPW;
    @FXML private Button btnUpdate;
    @FXML private Button btnCancel;

    @FXML
    void initialize() {
        btnUpdate.setOnAction(event -> handleBtnUpdateAction(event));
        btnCancel.setOnAction(event -> handleBtnCancelAction(event));
    }

    public void setModalStage(MainApp mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.modalStage = stage;
    }

    public void setMemo(Memo memo) {
        this.memo = memo;

        tfTitle.setText(memo.getTitle());
        editorContent.setHtmlText(memo.getContent());
        tfTag.setText(memo.getTag());
        ckIsEnc.setSelected(memo.getIsEncrypt());
    }

    /**
     * 수정 버튼 클릭 이벤트
     */
    private void handleBtnUpdateAction(ActionEvent event) {
        String title = tfTitle.getText();
        String content = editorContent.getHtmlText();
        String tag = tfTag.getText();

        String pw = pfEncPW.getText();
        String encodedPW = "";

        Boolean encOptionCheck = true; // 암호화가 안된 메모를 암호화 시킬 경우 PasswordField 입력 여부 체크

        if (memo.getIsEncrypt()) {
            if (ckIsEnc.isSelected() && !pw.equals("")) {
                // 암호화 true => true and 암호 변경
                encodedPW = EncUtil.encode(memo.getUserID(), pw, "SHA-512");
                memo.setEncryptPW(encodedPW);
            } else if (!ckIsEnc.isSelected()) {
                // 암호화 true => false
                memo.setIsEncrypt(false);
                memo.setEncryptPW(encodedPW);
            }
        } else {
            if (ckIsEnc.isSelected()) {
                // 암호화 false => true but PasswordField 가 비어있는 경우
                if (pw.equals("")) {
                    DialogUtil.infoDialog("암호를 입력하세요");
                    encOptionCheck = false;
                } else {
                    encodedPW = EncUtil.encode(memo.getUserID(), pw, "SHA-512");
                    memo.setIsEncrypt(true);
                    memo.setEncryptPW(encodedPW);
                }
            }
        }

        if (title.equals("")) {
            infoDialog("제목을 입력하세요.");
        } else if (encOptionCheck){
            memo.setTitle(title);
            memo.setContent(content);
            memo.setTag(tag);

            MemoDAO.updateMemo(memo);

            mainApp.showModalContent("memo/MemoRead");
            modalStage.close();
        }
    }

    /**
     * 취소 버튼 클릭 이벤트
     */
    private void handleBtnCancelAction(ActionEvent event) {
        mainApp.showModalContent("memo/MemoRead");
        modalStage.close();
    }
}
