/*
 * ChangePWController.java
 *
 * Created by Cho, Wonsik on 2018-08-23.
 */

package kr.pe.kaijer.wsnotebook.view.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import kr.pe.kaijer.wsnotebook.model.User;
import kr.pe.kaijer.wsnotebook.model.UserDAO;
import kr.pe.kaijer.wsnotebook.util.DialogUtil;

public class ChangePWController {
    private Stage modalStage;
    private String userID;

    @FXML private PasswordField pfCurrentPW;
    @FXML private PasswordField pfNewPW;
    @FXML private PasswordField pfNewPWRe;

    @FXML private Button btnChange;
    @FXML private Button btnCancel;

    @FXML
    void initialize() {
        btnChange.setOnAction(event -> handleBtnChangeAction(event));
        btnCancel.setOnAction(event -> handleBtnCancelAction(event));
    }

    public void setModalStage(Stage stage) {
        this.modalStage = stage;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * 비밀번호 변경 버튼 클릭 이벤트
     */
    private void handleBtnChangeAction(ActionEvent event) {
        String currentPW, newPW, newPWRe;

        currentPW = pfCurrentPW.getText();
        newPW = pfNewPW.getText();
        newPWRe = pfNewPWRe.getText();

        if (currentPW.equals("")) {
            DialogUtil.infoDialog("비밀번호를 입력하세요.");
        } else if (newPW.equals("") || !newPW.equals(newPWRe)) {
            DialogUtil.infoDialog("변경할 비밀번호가 일치하지 않습니다.");
        } else {
            User user = new User();
            user.setId(userID);
            user.setPw(currentPW);

            if (UserDAO.changePW(user, newPW)) {
                modalStage.close();
            }
        }
    }

    private void handleBtnCancelAction(ActionEvent event) {
        modalStage.close();
    }
}
