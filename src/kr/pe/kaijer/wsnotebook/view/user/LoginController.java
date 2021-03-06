/*
 * LoginController.java
 *
 * Created by Cho, Wonsik on 2018-08-18.
 */

package kr.pe.kaijer.wsnotebook.view.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.pe.kaijer.wsnotebook.MainApp;
import kr.pe.kaijer.wsnotebook.model.User;
import kr.pe.kaijer.wsnotebook.model.UserDAO;
import kr.pe.kaijer.wsnotebook.util.DBUtil;
import kr.pe.kaijer.wsnotebook.util.EncUtil;
import static kr.pe.kaijer.wsnotebook.util.DialogUtil.infoDialog;

public class LoginController extends AnchorPane {
    private MainApp mainApp;

    @FXML private TextField tfID;
    @FXML private PasswordField pfPW;

    @FXML private Button btnLogin;
    @FXML private Button btnRegister;
    @FXML private Button btnSearchPW;
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    void initialize() {
        pfPW.setOnAction(event -> handleBtnLoginAction(event));
        btnLogin.setOnAction(event -> handleBtnLoginAction(event));
        btnRegister.setOnAction(event -> handleBtnRegisterAction(event));
        btnSearchPW.setOnAction(event -> handleBtnSearchPWAction(event));
    }

    /**
     * 로그인 버튼 클릭시 이벤트
     *
     * 입력받은 ID, PW를 검사해서 올바른 정보면 MemoList 로 이동.
     */
    private void handleBtnLoginAction(ActionEvent event) {
        String id = tfID.getText();
        String pw = pfPW.getText();
        String encodedPW = EncUtil.encode(id, pw, "SHA-512");

        if (id.equals("")) {
            infoDialog("ID를 입력해 주세요!");
        } else if (pw.equals("")) {
            infoDialog("PW를 입력해 주세요!");
        } else {
            User user = new User();
            user.setId(id);
            user.setPw(encodedPW);

            if (UserDAO.login(user)) {
                mainApp.showMemoListView(id);
            }
        }
    }

    /** 회원가입 버튼 클릭시 이벤트 */
    private void handleBtnRegisterAction(ActionEvent event) {
        mainApp.showModalContent("user/Register");
    }

    /** 비밀번호 찾기 버튼 클릭시 이벤트 */
    private void handleBtnSearchPWAction(ActionEvent event) {
        mainApp.showModalContent("user/SearchPW");
    }
}
