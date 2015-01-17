package rmg.pdrtracker.login.utils;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryption {

    private static final String SecretKey = "9081726354fabced";
    private static final String Salt = "03xy9z52twq8r4s1uv67";
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5padding";
    private static final String KEY_ALGORITHM = "AES";
    private static final String SECRET_KEY_ALGORITHM = "PBEWithSHA256And256BitAES-CBC-BC";
    private static final String RANDOM_ALGORITHM = "SHA-512";
    private static final int PBE_ITERATION_COUNT = 1024;
    private static final int PBE_KEY_LENGTH = 256;
    private static final int IV_LENGTH = 16;
    private Cipher cipher;
    private Random randomInt;

    public AESEncryption() {
        try {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        }
        catch (NoSuchAlgorithmException e) {
            cipher = null;
        }
        catch (NoSuchPaddingException e) {
            cipher = null;
        }
        randomInt = new Random();
    }

    public SecretKey getSecretKey(String password) {
        try {

            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), Salt.getBytes("UTF-8"), PBE_ITERATION_COUNT, PBE_KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
            SecretKey tmp = factory.generateSecret(pbeKeySpec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), KEY_ALGORITHM);
            return secret;
        }
        catch (Exception e) {
            return null;
        }
    }

    private byte[] generateIV() {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
           // SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            byte[] iv = new byte[IV_LENGTH];
            random.nextBytes(iv);
            return iv;
        }
        catch (Exception e) {
            Log.e("pdrtracker", e.getMessage());
            return null;
        }
    }

    /*public static String bytesToHex(byte[] data) {
        String HEXES = "0123456789ABCDEF";
        if (data == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2*data.length);
        for (final byte b : data) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }*/


    /*public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        }
        else if (str.length() < 2) {
            return null;
        }
        else {
            int len = str.length()/2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i*2,i*2+2),16);
            }
            return buffer;
        }
    }*/

    public static String decryptString(String textToDecrypt) {
        byte[] rawKey = new byte[32];
        java.util.Arrays.fill(rawKey, (byte) 0);

        byte[] keyOk = hmacSha1(Salt, SecretKey);
        for (int i = 0; i < keyOk.length; i++) {
            rawKey[i] = keyOk[i];
        }

        SecretKeySpec skeySpec = new SecretKeySpec(hmacSha1(Salt, SecretKey), "AES");

        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encryptedData = cipher.doFinal(Base64.decode(textToDecrypt, Base64.NO_CLOSE));

            if (encryptedData == null) return null;

            return new String(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String encryptString(String clearText) {

        byte[] rawKey = new byte[32];
        java.util.Arrays.fill(rawKey, (byte) 0);

        byte[] keyOk = hmacSha1(Salt, SecretKey);
        for (int i = 0; i < keyOk.length; i++) {
            rawKey[i] = keyOk[i];
        }

        SecretKeySpec skeySpec = new SecretKeySpec(hmacSha1(Salt, SecretKey), KEY_ALGORITHM);

        try {
            byte[] iv = generateIV();
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
           // cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(SecretKey), ivspec);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(SecretKey));
            byte[] encryptedData = cipher.doFinal(clearText.getBytes("UTF-8"));

            if (encryptedData == null) return null;

            return Base64.encodeToString(encryptedData, Base64.NO_CLOSE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }


    public static byte[] hmacSha1(String salt, String key) {

        SecretKeyFactory factory = null;
        Key keyByte = null;

        try {
            factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
            KeySpec keyspec = new PBEKeySpec(key.toCharArray(), salt.getBytes(), PBE_ITERATION_COUNT, PBE_KEY_LENGTH);
            keyByte = factory.generateSecret(keyspec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return keyByte.getEncoded();
    }
}
