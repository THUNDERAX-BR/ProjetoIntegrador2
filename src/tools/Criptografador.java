package tools;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.JOptionPane;

public class Criptografador {

    public static String md5(String senha) {
        try {
            MessageDigest messageDIgest = MessageDigest.getInstance("MD5");
            byte[] messageDigest = messageDIgest.digest(senha.getBytes());
            BigInteger integer = new BigInteger(1, messageDigest);
            String hashtext = integer.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao criptografar.");
            return null;
        }
    }
}
