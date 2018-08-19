/*
 * DialogUtil.java
 *
 * Created by Cho, Wonsik on 2018-08-16.
 */

package kr.pe.kaijer.wsnotebook.util;

import javafx.scene.control.Alert;

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
