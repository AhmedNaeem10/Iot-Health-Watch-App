package com.example.iotwatch;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SasGenerator {
    public static String generateSasToken(String resourceUri, String key, int expiryTimeInSeconds) throws Exception {
        long epoch = System.currentTimeMillis() / 1000L;
        long expiry = epoch + expiryTimeInSeconds;

        String encodedUri = URLEncoder.encode(resourceUri, "UTF-8").toLowerCase();
        String toSign = encodedUri + "\n" + expiry;

        String signature = getHmac256(key, toSign);
        String encodedSignature = URLEncoder.encode(signature, "UTF-8");

        return "SharedAccessSignature sr=" + encodedUri + "&sig=" + encodedSignature + "&se=" + expiry;
    }

    private static String getHmac256(String key, String input) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        byte[] decodedKey = Base64.getDecoder().decode(key.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec secretKey = new SecretKeySpec(decodedKey, "HmacSHA256");
        mac.init(secretKey);
        byte[] hmacBytes = mac.doFinal(input.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hmacBytes);
    }
}
