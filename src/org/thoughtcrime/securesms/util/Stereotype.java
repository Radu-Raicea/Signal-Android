package org.thoughtcrime.securesms.util;

import android.icu.text.SymbolTable;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class Stereotype {
    public static final String REACTION = "reaction";
    public static final String COMMENT = "comment";
    public static final String UNKNOWN = "";

    private Stereotype() {}

    public static String fromBody(String body) {
        String type;
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> m = mapper.readValue(body, Map.class);
            type = m.get("type");
        } catch (IOException e) {
            return UNKNOWN;
        }

        switch(type) {
            case REACTION:
            case COMMENT:
                return type;
            default:
                return UNKNOWN;
        }
    }
}
