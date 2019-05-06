package com.seminario.services.auth.cipher;

/**
 * User: fcatania
 * Date: 6/5/2019
 * Time: 10:24
 */

public class EncryptManager {

    public static String decryptWord(String encrypt) throws Exception{
        CipherEncrypter objEncrypt = new CipherEncrypter("CompEncryptedDataSourceFactory");
        return objEncrypt.decrypt(encrypt);
    }
    public static String encryptWord(String decrypt) throws Exception{
        CipherEncrypter objEncrypt = new CipherEncrypter("CompEncryptedDataSourceFactory");
        return objEncrypt.encrypt(decrypt);
    }

}