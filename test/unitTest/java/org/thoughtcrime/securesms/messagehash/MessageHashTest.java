package org.thoughtcrime.securesms.messagehash;

import org.junit.Before;
import org.junit.Test;
import org.thoughtcrime.securesms.BaseUnitTest;
import org.thoughtcrime.securesms.util.MessageHash;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class MessageHashTest extends BaseUnitTest {
    private int    iterationMax = 10000;
    private String testAddress  = "+15140009999";
    private String timestamp    = "1231242143";
    private String expectedHash = "";


    @Before
    public void setup() {
        this.expectedHash = MessageHash.generateFrom(this.testAddress, this.timestamp);
    }

    @Test
    public void testMessageHashGeneratorConsistency() {
        String actualHash = MessageHash.generateFrom(this.testAddress, this.timestamp);

        assertEquals(actualHash, this.expectedHash);
    }

    @Test
    public void testMessageHashGeneratorConsistencyAfterMultipleTries() {
        int iteration = 0;
        String[] generatedHashes = new String[this.iterationMax];
        for( ; iteration != this.iterationMax; iteration++) {
            generatedHashes[iteration] = MessageHash.generateFrom(this.testAddress, this.timestamp);
        }

        // Check if all hashes are identical
        for (String generatedHash : generatedHashes) {
            assertEquals(generatedHash, this.expectedHash);
        }
    }

    @Test
    public void testMessageHashGeneratingHashesWithNoCollusion() {
        HashMap<String, String> generatedHashes = new HashMap<>();

        Long testTimestamp = 221232131l;
        int iteration = 0;

        for( ; iteration != this.iterationMax; iteration++) {
            String generatedHash = MessageHash.generateFrom(this.testAddress, (testTimestamp++).toString());
            // check that no such hash exist
            assertNull(generatedHashes.get(generatedHash));
            generatedHashes.put(generatedHash, generatedHash);
        }
        assertEquals(generatedHashes.size(), this.iterationMax);
    }
}
