package org.thoughtcrime.securesms.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StereotypeTest {
    public String JSON(String type) {
        return "{\"type\":\""+ type + "\"}";
    }

    @Test
    public void testReturnsInvalidWhenError() {
        assertEquals(
            Stereotype.fromBody("bad json"),
            Stereotype.INVALID
        );
    }

    @Test
    public void testReturnsInvalidWhenUnkown() {
        assertEquals(
            Stereotype.fromBody(JSON("unkown type")),
            Stereotype.INVALID
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
