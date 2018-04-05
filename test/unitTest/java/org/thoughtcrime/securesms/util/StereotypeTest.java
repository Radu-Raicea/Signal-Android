package org.thoughtcrime.securesms.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StereotypeTest {
    private String JSON(String type) {
        return "{\"type\":\""+ type + "\"}";
    }

    @Test
    public void testReturnsUnknownWhenError() {
        assertEquals(
            Stereotype.fromBody("bad json"),
            Stereotype.UNKNOWN
        );
    }

    @Test
    public void testReturnsUnknownWhenUnkown() {
        assertEquals(
            Stereotype.fromBody(JSON("unkown type")),
            Stereotype.UNKNOWN
        );
    }


    @Test
    public void testCanParseReaction() {
        assertEquals(
            Stereotype.fromBody(JSON("reaction")),
            Stereotype.REACTION
        );
    }
}
