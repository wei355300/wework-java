package com.qc.wework.chatdata.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.util.Base64;

public class RSA {

    private static final Logger logger = LoggerFactory.getLogger(RSA.class);

    public static final String PRI_KEY = ""+
            "MIIEowIBAAKCAQEAz4di/rHvy+1KgfAjA6+GvLOnxdMi1zU9rUH1oKc5Mgfx424w"+
            "j+8fX/29/Z+Jclb6gV8isMFj0dZNtztvA5rhhWgr4MwaHxLfuw7YT+IEjCitS9on"+
            "Yp1YRAL8zVua+rdhtQ/G2aIzCZXW6Ov5xJvGZSiidrxD4XIOd193iFUnOQ84atcS"+
            "5kBKZUV+XV/FGkfoyICZg73P3UsVMx1eCvOk+YYHa5ilVoteHoR6erG+ktVlfLDN"+
            "HJSefpcLqZibFKaomz4h/K0DBU22N0273ubCTnjEetdFTYYUcDweGIbBs1OTDzBT"+
            "9XlKLzDLJuJ8OzAGxw+4ptM+PizO0szNCaRiZwIDAQABAoIBAHWxUzwcT2E4dovQ"+
            "Uxsw/VmhDWHMhrLD9/F9Ob7ddztfdBfGEvZd5046Cfh+9KUKnNCn0nWph2GmhraG"+
            "l0PVH1bYKCI5vgqnWoisN5K+HFQccYNV+gYcM8WGaDZSYEcVnuzfQKeGLCmgd5vq"+
            "z+Bnc5FFBTYPOPPvCjM6ygBgQnlBmVm7yTzPjaJBuv4BFknlc0fxokgIe6a4mnfP"+
            "dDh1S/a5A9pQlShRHIhhwUQ2peyfJcc/6S+HIEcHBp9KlhoPCkwa9wXk+q5PjIuf"+
            "7erCL/eKFsNWY6Lg5vMhR3j8A/7lWV4s8ZHcvWUQubVoszGHwIzVRyP41F1Pmzr6"+
            "rpu4vPECgYEA+BC+wPOnJkOMX+s7meYrjVYhUexelgvTZ/WZcKYQHdsKbDditnvj"+
            "1MPkIi8SHiYqab85qfjWM4t3/ZnmaG5mA8TwpxmaIibUK//s8c/EKaqNBZ6xFWze"+
            "zh4pBQafWRnmPte1ne/Xfml7mRXVJxwMy77z6EjlS6pbvcztoFpPKt8CgYEA1iq2"+
            "cblXUynD/ju6FJPABk23OpNZ4hDcf94HpmUbCnL/JWYsCC5t5LCsjXS1xSRDNWN1"+
            "FOnJl1c4Ve8DC2Qn6gR6E+2+M8vpFRnxb9gu7WdiI20dxTCqZyeYhJgTVRm5bIUv"+
            "ifuIRll29zBCDgDRWPZPyijE+DQsJb6QCwj5wXkCgYA/Fisc8RoyPf2GK1HGCuZK"+
            "lHv21LEdYbaK+sfiASmaHimhadh95jYD1ym+k8/8wOIKcKpekr0O8Mo8QZZ6OpFR"+
            "JhW5uWYch1lwMy70NE2D9y7UHg6VD1H+g2cVMyD1TQPkOYGjv+pC6NqZDRdfUd/j"+
            "NRXcniVKP/IC7AoGvNDjawKBgG5YrY4k58hyoYhRb8wAYJUe38RvfYVMGMBNPvXN"+
            "7EPTAoFbhl5kxbxqB04PD0TXLUXJy96Noo+2odEtCA2LekFKMzgna7O2YY68wjhV"+
            "U8CAXwFPzGH4dCQYM65FyNg1G+O9L8RRdGKC4Bf2pmIeq77BklTf8rTL9N8xs3fs"+
            "FPtJAoGBALlQq9lu3SnbfIZdYvizl4aicNx4rOWdNYdSDQFB/ldriMATNrtGcO9G"+
            "RaV5/j37Wg9Z1Sd71WXTeY4l/Dhe4sxAR+LxrC3M3YrgEjKHt+NVK3PRq7kfIlMu"+
            "eK5yqaITz4cDbnYGsV9hTnUR1DivNVghz/J8QOhSvbKRr2MUg9Qw"+
            "";

    public static final String PUB_KEY = "-----BEGIN PUBLIC KEY-----" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAz4di/rHvy+1KgfAjA6+G" +
            "vLOnxdMi1zU9rUH1oKc5Mgfx424wj+8fX/29/Z+Jclb6gV8isMFj0dZNtztvA5rh" +
            "hWgr4MwaHxLfuw7YT+IEjCitS9onYp1YRAL8zVua+rdhtQ/G2aIzCZXW6Ov5xJvG" +
            "ZSiidrxD4XIOd193iFUnOQ84atcS5kBKZUV+XV/FGkfoyICZg73P3UsVMx1eCvOk" +
            "+YYHa5ilVoteHoR6erG+ktVlfLDNHJSefpcLqZibFKaomz4h/K0DBU22N0273ubC" +
            "TnjEetdFTYYUcDweGIbBs1OTDzBT9XlKLzDLJuJ8OzAGxw+4ptM+PizO0szNCaRi" +
            "ZwIDAQAB" +
            "-----END PUBLIC KEY-----";

    static PrivateKey privateKey;

    static {
        try {
            privateKey = getPrivateKey(PRI_KEY);
        } catch (Exception e) {
            logger.error("", e);
        }
    }


    /**
     * RSA pkcs1 2048bit 解密工具,
     * 获取私钥PrivateKey
     *
     * @param privKeyPEM 2048bit pkcs1格式,base64编码后的RSA字符串
     * @return PrivateKey, 用于解密 decryptRSA
     * @throws IOException              异常
     * @throws NoSuchAlgorithmException 异常
     * @throws InvalidKeySpecException  异常
     */
    public static PrivateKey getPrivateKey(String privKeyPEM) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] bytes = java.util.Base64.getDecoder().decode(privKeyPEM);
        DerInputStream derReader = new DerInputStream(bytes);
        DerValue[] seq = derReader.getSequence(0);
        BigInteger modulus = seq[1].getBigInteger();
        BigInteger publicExp = seq[2].getBigInteger();
        BigInteger privateExp = seq[3].getBigInteger();
        BigInteger prime1 = seq[4].getBigInteger();
        BigInteger prime2 = seq[5].getBigInteger();
        BigInteger exp1 = seq[6].getBigInteger();
        BigInteger exp2 = seq[7].getBigInteger();
        BigInteger crtCoef = seq[8].getBigInteger();
        RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp, prime1, prime2, exp1, exp2, crtCoef);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * RSA pkcs1 2048bit 解密工具,
     *
     * @param str 被解密的字符串
     *            //     * @param privateKey 私钥对象 从 getPrivateKey 获取
     * @return 解密后数据
     * @throws NoSuchPaddingException    异常
     * @throws NoSuchAlgorithmException  异常
     * @throws InvalidKeyException       异常
     * @throws BadPaddingException       异常
     * @throws IllegalBlockSizeException 异常
     */
    public static String decryptRSA(String str) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] randomkeybyte = Base64.getDecoder().decode(str);
        byte[] finalrandomkeybyte = cipher.doFinal(randomkeybyte);
        return new String(finalrandomkeybyte);
    }
}
