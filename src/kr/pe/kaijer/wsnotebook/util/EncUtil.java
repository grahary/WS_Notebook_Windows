/*
 * EncUtil.java
 *
 * Created by Cho, Wonsik on 2018-08-01.
 */

package kr.pe.kaijer.wsnotebook.util;

import java.security.MessageDigest;

public class EncUtil {
    /**
     * Hash 알고리즘으로 암호화하는 메소드
     *
     * @param id     사용자의 ID
     * @param pw     사용자의 PW
     * @param encodeType     사용할 Hash 알고리즘 선택 [ MD2, MD4, MD5, SHA-1, SHA-256, SHA-512 ]
     * @return     암호화된 텍스트
     */
    public static String encode(String id, String pw, String encodeType) {
        MessageDigest messageDigest;

        String salting = "ws_notebook@" + id + "@" + pw;
        String encodedPW = "";

        try {
            messageDigest = MessageDigest.getInstance(encodeType);
            messageDigest.update(salting.getBytes());
            byte[] bytes = messageDigest.digest();

            for(int i = 0; i < bytes.length; i++) {
                byte temp = bytes[i];
                String str = Integer.toHexString(new Byte(temp));

                while (str.length() < 2) {
                    str = "0" + str;
                }

                str = str.substring(str.length() - 2);
                encodedPW += str;
            }
        } catch (Exception e) {
            return encodedPW;
        }

        return encodedPW;
    }
}