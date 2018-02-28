package org.thoughtcrime.securesms.database;

import org.junit.Before;
import org.junit.Test;
import org.thoughtcrime.securesms.NicknameMocks;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class NicknameTests extends NicknameMocks {

    @Before
    @Override
    public void setUp() throws Exception{
        super.setUp();
        super.setupRecipientObject();
        super.setupStaticRecipientDatabase();
        super.setupSetNicknameMethod();
    }

    @Test
    public void testSetNicknameDatabase() {
        assertEquals(recipientDatabase.setNickname(super.recipient, "test"), true);
        assertEquals(recipientDatabase.setNickname(super.recipient, "test"), false);

        verify(recipientDatabase, times(2)).setNickname(super.recipient, "test");
    }

    @Test
    public void testSetNicknameDatabaseExtended() {
        assertEquals(recipientDatabase.setNickname(super.recipient, "test"), true);
        assertEquals(recipientDatabase.setNickname(super.recipient, "test"), false);
        assertEquals(recipientDatabase.setNickname(super.recipient, "new test"), true);
        assertEquals(recipientDatabase.setNickname(super.recipient, "new test"), false);

        verify(recipientDatabase, times(2)).setNickname(super.recipient, "test");
        verify(recipientDatabase, times(2)).setNickname(super.recipient, "new test");
    }
}

