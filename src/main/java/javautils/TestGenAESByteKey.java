package javautils;

/**
 * Created by yuanbo on 2019-03-26.
 */
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import sun.misc.BASE64Encoder;

public class TestGenAESByteKey{

    /**
     * @param args
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        KeyGenerator keygen;
        SecretKey desKey;
        Cipher c;
        byte[] cByte;
        // 注册机
        keygen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed("eefbf3cca60830313a95283c368687e3".getBytes());
        keygen.init(128, secureRandom);
        //keygen.init(128, new SecureRandom(strKey.getBytes()));
        desKey = keygen.generateKey();
        byte[] enCodeFormat = desKey.getEncoded();
        BASE64Encoder coder = new BASE64Encoder();

        System.out.println(coder.encode(enCodeFormat));
    }

}