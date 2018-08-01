package kr.pe.kaijer;

import java.security.MessageDigest;

/**
 * Created by Cho, Wonsik on 2018-08-01.
 */

public class Encrypt {
    /**
     *
     * @param id            사용자의 ID
     * @param pw            사용자의 PW
     * @param enc_type     사용할 Hash 알고리즘 선택 [ MD2, MD4, MD5, SHA-1, SHA-256, SHA-512 ]
     * @return
     */
    public static String encode(String id, String pw, String enc_type) {
        MessageDigest messageDigest;

        String salting = "ws_notebook@" + id + "@" + pw;
        String encoded_pw = "";

        try {
            messageDigest = MessageDigest.getInstance(enc_type);
            messageDigest.update(salting.getBytes());
            byte[] bytes = messageDigest.digest();

            for(int i = 0; i < bytes.length; i++) {
                byte temp = bytes[i];
                String str = Integer.toHexString(new Byte(temp));

                while (str.length() < 2) {
                    str = "0" + str;
                }

                str = str.substring(str.length() - 2);
                encoded_pw += str;
            }
        } catch (Exception e) {
            return encoded_pw;
        }

        return encoded_pw;
    }
}
