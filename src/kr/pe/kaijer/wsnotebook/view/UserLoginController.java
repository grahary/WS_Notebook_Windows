/*
 * UserLoginController.java
 *
 * Created by Cho, Wonsik on 2018-08-18.
 */

package kr.pe.kaijer.wsnotebook.view;

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
import kr.pe.kaijer.wsnotebook.util.DBUtil;
import kr.pe.kaijer.wsnotebook.util.EncUtil;
import static kr.pe.kaijer.wsnotebook.util.DialogUtil.infoDialog;

public class UserLoginController extends AnchorPane {
    @FXML private TextField tfID;
    @FXML private PasswordField pfPW;

    @FXML private Button btnLogin;
    @FXML private Button btnRegister;
    @FXML private Button btnSearchPW;

    private MainApp mainApp;

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

        String query = "SELECT pw FROM users WHERE id = \"" + id + "\";";

        if (id.equals("")) {
            infoDialog("ID를 입력해 주세요!");
        } else if (pw.equals("")) {
            infoDialog("PW를 입력해 주세요!");
        } else {
            ResultSet rs = DBUtil.dbExecuteQuery(query);

            try {
                if (rs.next()) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(rs.getBinaryStream("pw")));
                    String userPW = DBUtil.blobToSting(br);

                    if (userPW.equals(encodedPW)) {
                        mainApp.showMemoListView(id);
                    } else {
                        infoDialog("PW를 잘못 입력하셨습니다!");
                    }
                } else {
                    infoDialog("존재하지 않는 ID 입니다!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /** 회원가입 버튼 클릭시 이벤트 */
    private void handleBtnRegisterAction(ActionEvent event) {

    }

    /** 비밀번호 찾기 버튼 클릭시 이벤트 */
    private void handleBtnSearchPWAction(ActionEvent event) {

    }
}
