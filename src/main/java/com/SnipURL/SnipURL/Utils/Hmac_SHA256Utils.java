package com.SnipURL.SnipURL.Utils;

import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
@Component
public class Hmac_SHA256Utils {
    public static String generateHmacSHA256(String data, String secretKey) {
        try {
             Mac mac = Mac.getInstance("HmacSHA256"); // or HmacSHA512
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hmacBytes = mac.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(hmacBytes); // or use Hex if preferred
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate HMAC", e);
        }
    }
}
