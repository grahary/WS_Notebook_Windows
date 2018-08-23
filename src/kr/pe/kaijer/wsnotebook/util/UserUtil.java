package kr.pe.kaijer.wsnotebook.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserUtil {
    /**
     * ID 중복 확인
     *
     * @param id     중복 확인 할 ID
     * @return     리턴 값이 거짓인 경우 사용 가능한 ID
     */
    public static boolean isDuplicateID(String id) {
        String query = "SELECT id FROM users WHERE id = \"" + id + "\";";

        ResultSet rs = DBUtil.dbExecuteQuery(query);

        try {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * E-Mail 주소 유효성 확인
     *
     * @param email     확인활 E-Mail 주소
     * @return     리턴 값이 참인 경우 유효한 E-Mail 주소
     */
    public static boolean isValidEmailAddress(String email) {
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    /**
     * 비밀번호 찾기 기능 사용시 새로운 비밀번호 생성
     *
     * @param id     로그인한 사용자의 ID
     * @return     새로운 PW
     */
    public static String createNewPW(String id) {
        StringBuffer buffer = new StringBuffer();
        Random rand = new Random();

        for (int i = 0; i < 10; i++) {
            int idx = rand.nextInt(3);

            switch (idx) {
                case 0:
                    // a-z
                    buffer.append((char) ((int) (rand.nextInt(26)) + 97));
                    break;
                case 1:
                    // A-Z
                    buffer.append((char) ((int) (rand.nextInt(26)) + 65));
                    break;
                case 2:
                    // 0-9
                    buffer.append((rand.nextInt(10)));
                    break;
            }
        }

        return buffer.toString();
    }
}
