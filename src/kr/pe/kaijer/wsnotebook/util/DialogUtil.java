/*
 * DialogUtil.java
 *
 * Created by Cho, Wonsik on 2018-08-16.
 */

package kr.pe.kaijer.wsnotebook.util;

import javafx.scene.control.*;

import java.util.Optional;

public class DialogUtil {
    /**
     * 정보 출력을 위한 다이얼로그
     *
     * @param text     다이얼로그에 표시될 텍스트
     */
    public static void infoDialog(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("WS Notebook");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }

    public static void changePWDialog(String newPW) {
        Dialog dialog = new Dialog();
        dialog.setTitle("WS Notebook");
        dialog.setHeaderText("비밀번호가 다음과 같이 변경되었습니다.\n로그인 후에 반드시 비밀번호를 변경해주세요.");

        ButtonType okButton = new ButtonType("확인", ButtonBar.ButtonData.OK_DONE);

        TextField tfNewPW = new TextField();
        tfNewPW.setText(newPW);
        tfNewPW.setEditable(false);

        dialog.getDialogPane().getButtonTypes().add(okButton);
        dialog.getDialogPane().setContent(tfNewPW);

        dialog.showAndWait();
    }

    public static Boolean confirmDialog(String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("WS Notebook");
        alert.setHeaderText(null);
        alert.setContentText(text);

        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK;
    }

    /**
     * 비밀번호를 입력 받기 위한 다이얼로그
     *
     * @return     입력받은 비밀번호
     */
    public static String pwInputDialog() {
        PasswordDialog dialog = new PasswordDialog();
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            return result.get();
        }

        return null;
    }
}
