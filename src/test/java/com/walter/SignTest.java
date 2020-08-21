package com.walter;

import com.walter.util.SignUtil;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import java.io.File;
import java.io.IOException;

public class SignTest {

    @Test
    public void signWithPrivateKeyFile() {
        File pkf = new File("D:/apiclient_key.pem");
        String data = "GET\n/v3/certificates\n1554208460\n593BEC0C930BF1AFEB40B4A08C8FB242\n\n";
        System.out.println(SignUtil.sign(pkf, data));
    }

    @Test
    public void signWithPrivateKeyContent() {
        String pkContent = "content in D:/apiclient_key.pem";
        String data = "GET\n/v3/certificates\n1554208460\n593BEC0C930BF1AFEB40B4A08C8FB242\n\n";
        System.out.println(SignUtil.sign(pkContent, data));
    }

    @Test
    public void serialNumber() throws Exception {
        String pcf = "D:/apiclient_cert.pem";
        System.out.println(SignUtil.getCertificate(pcf).getSerialNumber().toString(16).toUpperCase());
    }

    @Test
    public void rsaEncryptOAEP() throws Exception {
        String filename = "D:/apiclient_cert.pem";
        String message = "HelloWorld";
        System.out.println(SignUtil.rsaEncryptOAEP(message, filename));
    }

    @Test
    public void rsaDecryptOAEP() throws BadPaddingException, IOException {
        File pkFile = new File("D:/apiclient_key.pem");
        String message = "TtWy6vO0HPGJsPo5tZYdEMfTUMJ0FGrYGgQhjRe2gw5S62IrINAEQfnuDtipDaLb34xJmycxrztWrrQvYTkxNziCbWpthGNLQGVNAseIA3GJOHcCd6euH4jFPP7ZF7D25DEXBo6Y5EB4F6FYWb7z29NnIQ2CqmrYxbQZbYlSNP9taqiPOQ7YBfLTWHr3y8k/BLyQwUZCZ6TNt6/CRJUAXrAWOYejLFtTqB0QABmoqzcPUX1QT33uMYTdsPjKwbn1zxb7vJtNmqEmzvG69QM58AoCeykv8oaAmiBJHiVwkGHe0QQePxGBZHjhR7R2ep212EotZBhwYbBNdEtTlTrj2w==";
        System.out.println(SignUtil.rsaDecryptOAEP(message, pkFile));
    }
}
