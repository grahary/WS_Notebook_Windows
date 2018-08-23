/*
 * SearchPWController.java
 *
 * Created by Cho, Wonsik on 2018-08-22.
 */

package kr.pe.kaijer.wsnotebook.view.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import kr.pe.kaijer.wsnotebook.model.User;
import kr.pe.kaijer.wsnotebook.model.UserDAO;
import kr.pe.kaijer.wsnotebook.util.DialogUtil;
import kr.pe.kaijer.wsnotebook.util.UserUtil;

public class SearchPWController {
    private Stage modalStage;

    @FXML private TextField tfID;
    @FXML private TextField tfEmail;
    @FXML private ComboBox cbEmail;
    @FXML private ComboBox cbPWQuestion;
    @FXML private TextField tfPWAnswer;

    @FXML private Button btnSearch;
    @FXML private Button btnCancel;

    @FXML
    void initialize() {
        ObservableList<String> emailList = FXCollections.observableArrayList("직접 입력", "@gmail.com", "@hanmail.net", "@naver.com");
        ObservableList<String> questionList = FXCollections.observableArrayList("가장 좋아하는 것은?", "가장 싫어하는 것은?", "아버지 성함은?", "어머니 성함은?",
                "가장 재미있게 본 영화는?", "가장 재미없게 본 영화는?", "좋아하는 계절은?", "좋아하는 색은?");

        cbEmail.setItems(emailList);
        cbEmail.getSelectionModel().selectFirst();

        cbPWQuestion.setItems(questionList);
        cbPWQuestion.getSelectionModel().selectFirst();

        btnSearch.setOnAction(event -> handleBtnSearchAction(event));
        btnCancel.setOnAction(event -> handleBtnCancelAction(event));
    }

    public void setModalStage(Stage stage) {
        this.modalStage = stage;
    }

    /**
     * 비밀번호 찾기 버튼 클릭 이벤트
     */
    private void handleBtnSearchAction(ActionEvent event) {
        String id, email, emailDomain, hintAnswer;
        int hintQuestion;

        id = tfID.getText();

        emailDomain = cbEmail.getValue().toString();
        if (emailDomain.equals("직접 입력")) {
            email = tfEmail.getText();
        } else {
            email = tfEmail.getText() + emailDomain;
        }

        hintQuestion = cbPWQuestion.getSelectionModel().getSelectedIndex();
        hintAnswer = tfPWAnswer.getText();

        if (id.equals("")) {
            DialogUtil.infoDialog("ID를 입력하세요.");
        } else if (!UserUtil.isValidEmailAddress(email)) {
            DialogUtil.infoDialog("올바른 E-Mail 주소가 아닙니다.");
        } else if (hintAnswer.equals("")) {
            DialogUtil.infoDialog("비밀번호 힌트를 입력해주세요.");
        } else {
            User user = new User();
            user.setId(id);
            user.setEmail(email);
            user.setHintQuestion(hintQuestion);
            user.setHintAnswer(hintAnswer);

            if (UserDAO.searchPW(user)) {
                modalStage.close();
            }
        }
    }

    private void handleBtnCancelAction(ActionEvent event) {
        modalStage.close();
    }
}
