package com.seminario.services.auth.cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * User: fcatania
 * Date: 6/5/2019
 * Time: 10:16
 */
public class CipherEncrypter {

    private Cipher ecipher;
    private Cipher dcipher;
    private static final  String CIPHER = "AES/GCM/NoPadding";

    public CipherEncrypter(String passPhrase) {
        try {

            this.dcipher = Cipher.getInstance(CIPHER);
            this.ecipher = Cipher.getInstance(CIPHER);

            byte[] salt = new byte[]{(byte)-87, (byte)-101, (byte)-56, (byte)50, (byte)86, (byte)53, (byte)-29, (byte)3};

            int ivLen = this.dcipher.getBlockSize();
            byte[] ivBytes = new byte[ivLen];
            GCMParameterSpec ivspec = new GCMParameterSpec(ivLen * Byte.SIZE, ivBytes);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), salt, 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");


            this.ecipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            this.dcipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String str) {
        try {
            byte[] utf8 = str.getBytes("UTF8");
            byte[] enc = this.ecipher.doFinal(utf8);
            return (new BASE64Encoder()).encode(enc);
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String decrypt(String str) {
        try {
            byte[] dec = (new BASE64Decoder()).decodeBuffer(str);
            byte[] utf8 = this.dcipher.doFinal(dec);
            return new String(utf8, "UTF8");
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}