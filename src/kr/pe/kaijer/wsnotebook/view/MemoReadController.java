/*
 * MemoReadController.java
 *
 * Created by Cho, Wonsik on 2018-08-18.
 */

package kr.pe.kaijer.wsnotebook.view;

import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import kr.pe.kaijer.wsnotebook.model.Memo;

public class MemoReadController {
    private Stage modalStage;
    private Memo memo;

    @FXML private TextField tfTitle;
    @FXML private TextField tfWriteDate;
    @FXML private WebView wvContent;
    @FXML private TextField tfTag;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClose;

    @FXML
    void initialize() {
        btnUpdate.setOnAction(event -> handleBtnUpdateAction(event));
        btnDelete.setOnAction(event -> handleBtnDeleteAction(event));
        btnClose.setOnAction(event -> handleBtnCloseAction(event));
    }

    public void setModalStage(Stage stage) {
        this.modalStage = stage;
    }

    /**
     * MemoList 클레스에서 사용자가 선택한 메모 객체를 가져와 초기화
     *
     * @param memo     사용자가 선택한 Memo 객체
     */
    public void setMemoReadView(Memo memo) {
        this.memo = memo;

        tfTitle.setText(memo.getTitle());
        tfWriteDate.setText(memo.getWriteDate());
        tfTag.setText(memo.getTag());

        WebEngine webEngine = wvContent.getEngine();
        webEngine.loadContent(memo.getContent(), "text/html");
    }

    private void handleBtnUpdateAction(ActionEvent event) {

    }

    private void handleBtnDeleteAction(ActionEvent event) {

    }

    private void handleBtnCloseAction(ActionEvent event) {
        modalStage.close();
    }
}
