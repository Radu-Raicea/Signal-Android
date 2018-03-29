package org.thoughtcrime.securesms.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageHash {
    private static MessageHash instance;

    public static MessageHash getInstance() {
        if (MessageHash.instance != null) {
            return MessageHash.instance;
        }

        MessageHash.instance = new MessageHash();
        return MessageHash.instance;
    }

    /**
     * @return generated 64 character hash
     */
    public static String generateFrom(String sender, String datetime) {
        String text = sender + datetime;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
        md.update(text.getBytes());
        byte[] digest = md.digest();

        StringBuffer result = new StringBuffer();
        for (byte byt : digest) {
            result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        }

        return result.toString();
    }
}
