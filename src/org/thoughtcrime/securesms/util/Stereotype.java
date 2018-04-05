package org.thoughtcrime.securesms.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class Stereotype {
    public static final String REACTION = "reaction";
    public static final String INVALID = "";

    private Stereotype() {}

    public static String fromBody(String body) {
        String type;
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> m = mapper.readValue(body, Map.class);
            type = m.get("type");
        } catch (IOException e) {
            return INVALID;
        }

        switch(type) {
            case REACTION:
         // case OTHERONE:
                return type;
            default:
                return INVALID;
        }
    }
}
