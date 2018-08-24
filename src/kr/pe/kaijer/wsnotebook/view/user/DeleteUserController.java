/*
 * DeleteUserController.java
 *
 * Created by Cho, Wonsik on 2018-08-24.
 */

package kr.pe.kaijer.wsnotebook.view.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import kr.pe.kaijer.wsnotebook.MainApp;
import kr.pe.kaijer.wsnotebook.model.User;
import kr.pe.kaijer.wsnotebook.model.UserDAO;
import kr.pe.kaijer.wsnotebook.util.DialogUtil;
import kr.pe.kaijer.wsnotebook.util.EncUtil;

public class DeleteUserController {
    private MainApp mainApp;
    private Stage modalStage;
    private String userID;

    @FXML private TextField tfID;
    @FXML private PasswordField pfPW;
    @FXML private Button btnDeleteUser;
    @FXML private Button btnCancel;

    @FXML
    void initialize() {
        btnDeleteUser.setOnAction(event -> handleBtnDeleteUserAction(event));
        btnCancel.setOnAction(event -> handleBtnCancelAction(event));
    }

    public void setModalStage(MainApp mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.modalStage = stage;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    private void handleBtnDeleteUserAction(ActionEvent event) {
        String id = tfID.getText();
        String pw = pfPW.getText();

        if (id.equals("")) {
            DialogUtil.infoDialog("ID를 입력해주세요.");
        } else if (pw.equals("")) {
            DialogUtil.infoDialog("비밀번호를 입력해주세요.");
        } else if (!id.equals(userID)) {
            DialogUtil.infoDialog("ID를 잘못 입력했습니다.");
        } else {
            User user = new User();
            user.setId(userID);
            user.setPw(EncUtil.encode(id, pw, "SHA-512"));

            if (UserDAO.deleteUser(user)) {
                modalStage.close();
                mainApp.showLoginView();
            }
        }
    }

    private void handleBtnCancelAction(ActionEvent event) {
        modalStage.close();
    }
}
