package org.omega.comon.core.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class SecurityHelperClass implements SecurityHelper {
    private final String publicKey = " MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1tm6iFcR2RR65oYbJS8A\n" +
            "    Vb3Y0BXVX+lvN+IWW283aeTjnQXCnUi1cIoOyAxD7bxglHutQEgXRxiYkBeyUFHU\n" +
            "    kLV+QfqyLRXYxXSUIkQ7v36NNl5cK3E8g2jTb3x1OYsrdLVw6ZyhZVAtWe55/r+7\n" +
            "    +GmPogVzqoRLDp3cDa1TVcoE9VndH2GK6TlJNkDdU5Bc9CRTxiAxcktk6DJ46HYe\n" +
            "    UH7c7NGtZDwFdPp/j+ibvxFlaToyDmw+eSFfDZj0YzoeNAXXK3h+I7kRV59naXS3\n" +
            "    xbM482ZB/Iw/gYiix5SV24VrcF83cOltvLHksjlC86WUA5RTZmb61kS5bvQlCQ5n\n" +
            "    pQIDAQAB";

    @Override
    public PublicKey getJwtValidationKey() {
        byte[] keyBytes = Base64.getDecoder().decode(
                publicKey
                        .replace("\n", "")
                        .replace("\r", "")
                        .replace(" ", "")
                        .trim()
        );
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Invalid key");
        }
    }
}
