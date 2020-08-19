package com.walter;

import com.walter.util.SignUtil;
import org.junit.Test;

import java.io.File;

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
        System.out.println(SignUtil.getCertificate(pcf).getSerialNumber());
    }
}
