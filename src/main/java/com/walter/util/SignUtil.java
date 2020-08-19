package com.walter.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUtil {

    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    /**
     * 获取私钥。
     *
     * @param pkFile 私钥文件  (required)
     * @return 私钥对象
     */
    public static PrivateKey getPrivateKey(File pkFile) throws IOException {

        String content = new String(Files.readAllBytes(Paths.get(pkFile.getAbsolutePath())), "utf-8");
        try {
            String privateKey = content.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(
                    new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持RSA", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("无效的密钥格式");
        }
    }

    /**
     * 获取私钥。
     *
     * @param pkContent 私钥文件内容  (required)
     * @return 私钥对象
     */
    public static PrivateKey getPrivateKey(String pkContent) throws IOException {

        try {
            String privateKey = pkContent.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(
                    new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持RSA", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("无效的密钥格式");
        }
    }

    /**
     * 签名
     * @param pkFile	私钥文件
     * @param requestData	请求参数
     * @return
     */
    public static String sign(File pkFile, String requestData){
        String signature = null;
        byte[] signed;
        try {
            PrivateKey privateKey = getPrivateKey(pkFile);

            Signature Sign = Signature.getInstance(SIGNATURE_ALGORITHM);
            Sign.initSign(privateKey);
            Sign.update(requestData.getBytes());
            signed = Sign.sign();

            signature = Base64.getEncoder().encodeToString(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return signature;
    }

    /**
     * 签名
     * @param pkContent	私钥文件内容
     * @param requestData	请求参数
     * @return
     */
    public static String sign(String pkContent, String requestData){
        String signature = null;
        byte[] signed;
        try {
            PrivateKey privateKey = getPrivateKey(pkContent);

            Signature Sign = Signature.getInstance(SIGNATURE_ALGORITHM);
            Sign.initSign(privateKey);
            Sign.update(requestData.getBytes());
            signed = Sign.sign();

            signature = Base64.getEncoder().encodeToString(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return signature;
    }

    /**
     * 获取证书。
     *
     * @param filename 证书文件路径  (required)
     * @return X509证书
     */
    public static X509Certificate getCertificate(String filename) throws Exception {
        InputStream fis = new FileInputStream(filename);
        BufferedInputStream bis = new BufferedInputStream(fis);

        try {
            CertificateFactory cf = CertificateFactory.getInstance("X509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(bis);
            cert.checkValidity();
            return cert;
        } catch (CertificateExpiredException e) {
            throw new RuntimeException("证书已过期", e);
        } catch (CertificateNotYetValidException e) {
            throw new RuntimeException("证书尚未生效", e);
        } catch (CertificateException e) {
            throw new RuntimeException("无效的证书文件", e);
        } finally {
            bis.close();
        }
    }
}
