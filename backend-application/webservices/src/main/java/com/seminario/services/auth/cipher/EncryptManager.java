package com.seminario.services.auth.cipher;

/**
 * User: fcatania
 * Date: 6/5/2019
 * Time: 10:24
 */

public class EncryptManager {

    public static void main(String[] args) {
        System.out.println(EncryptManager.encryptWord("admin"));
        System.out.println(EncryptManager.decryptWord("Wpc3yPWn/85ZzK9FIiL36VpNSIqg"));
    }

    public static String decryptWord(String encrypt) {
        CipherEncrypter objEncrypt = new CipherEncrypter("CompEncryptedDataSourceFactory");
        return objEncrypt.decrypt(encrypt);
    }
    public static String encryptWord(String decrypt) {
        CipherEncrypter objEncrypt = new CipherEncrypter("CompEncryptedDataSourceFactory");
        return objEncrypt.encrypt(decrypt);
    }

}

