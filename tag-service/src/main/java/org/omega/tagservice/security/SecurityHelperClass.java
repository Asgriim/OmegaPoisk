package org.omega.tagservice.security;

import org.omega.common.security.SecurityHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class SecurityHelperClass implements SecurityHelper {
    @Value("${keys.publicKey}")
    private String publicKey;

    @Override
    public PublicKey getJwtValidationKey() {
        byte[] keyBytes = Base64.getDecoder().decode(
                publicKey
                        .replace("\n", "")
                        .replace("\r", "")
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
