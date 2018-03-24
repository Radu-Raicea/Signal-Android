package org.thoughtcrime.securesms.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageHash {
    private String hash;

    public MessageHash(String sender, String datetime) {
        String text = sender + datetime;
        MessageDigest md;
        try {
             md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            this.hash = text;
            return;
        }
        md.update(text.getBytes());
        byte[] digest = md.digest();

        StringBuffer result = new StringBuffer();
        for (byte byt : digest) {
            result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        }

        this.hash = result.toString();
    }

    /**
     * @return 64 character hash
     */
    public String get() {
        return this.hash;
    }
}
