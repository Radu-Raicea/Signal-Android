package org.thoughtcrime.securesms.pinmessages;

import org.junit.Before;
import org.junit.Test;

import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.MessagingDatabase;
import org.thoughtcrime.securesms.database.MmsDatabase;
import org.thoughtcrime.securesms.database.model.MessageRecord;
import org.thoughtcrime.securesms.database.SmsDatabase;
import org.thoughtcrime.securesms.PinnedMessageHandler;
import org.thoughtcrime.securesms.PinnedMessageMocks;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PinnedMesssageHandlerTests extends PinnedMessageMocks {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        super.databaseFactory      = mock(DatabaseFactory.class);
        super.messagingDatabase    = mock(MessagingDatabase.class);
        super.messageRecordSms     = mock(MessageRecord.class);
        super.messageRecordMms     = mock(MessageRecord.class);
        super.mmsDatabase          = mock(MmsDatabase.class);
        super.smsDatabase          = mock(SmsDatabase.class);

        super.setUpPinMethod();
        super.setUpUnpinMethod();
        super.setUpMessageRecord();
        super.setUpStaticMessagingDatabase();
    }

    @Test
    public void testPinHandleGetAppropriateDatabase() {
        PinnedMessageHandler handler = new PinnedMessageHandler(context);
        assertEquals(handler.getAppropriateDatabase(messageRecordSms), smsDatabase);
        assertEquals(handler.getAppropriateDatabase(messageRecordMms), mmsDatabase);
    }

    @Test
    public void testHandlerPinMessagesSmsSuccess() {
        PinnedMessageHandler handler = new PinnedMessageHandler(context);
        boolean result = handler.handlePinMessage(messageRecordSms, smsDatabase);

        verify(smsDatabase).pinMessage(1);
        assertEquals(result, true);
    }

    @Test
    public void testHandlerPinMessagesMmsSuccess() {
        PinnedMessageHandler handler = new PinnedMessageHandler(context);
        boolean result = handler.handlePinMessage(messageRecordMms, mmsDatabase);

        verify(mmsDatabase).pinMessage(1);
        assertEquals(result, true);
    }

    @Test
    public void testHandlerPinMessageSmsFailureOnAlreadyPinned() {
        setMockGetIdReturnValue(2);

        PinnedMessageHandler handler = new PinnedMessageHandler(context);
        boolean result = handler.handlePinMessage(messageRecordSms, smsDatabase);

        verify(smsDatabase).pinMessage(2);
        assertEquals(result, false);
    }

    @Test
    public void testHandlerPinMessageMmsFailureOnAlreadyPinned() {
        setMockGetIdReturnValue(2);

        PinnedMessageHandler handler = new PinnedMessageHandler(context);
        boolean result = handler.handlePinMessage(messageRecordMms, mmsDatabase);

        verify(mmsDatabase).pinMessage(2);
        assertEquals(result, false);
    }

    @Test
    public void testHandlerPinMessageSmsOnNotExist() {
        setMockGetIdReturnValue(3);

        PinnedMessageHandler handler = new PinnedMessageHandler(context);
        boolean result = handler.handlePinMessage(messageRecordSms, smsDatabase);

        verify(smsDatabase).pinMessage(3);
        assertEquals(result, false);
    }

    @Test
    public void testHandlerPinMessageMmsOnNotExist() {
        setMockGetIdReturnValue(3);

        PinnedMessageHandler handler = new PinnedMessageHandler(context);
        boolean result = handler.handlePinMessage(messageRecordMms, mmsDatabase);

        verify(mmsDatabase).pinMessage(3);
        assertEquals(result, false);
    }

    @Test
    public void testHandlerUnpinMethodSmsMessagedAlreadyPinned() {
        setMockGetIdReturnValue(1);

        PinnedMessageHandler handler = new PinnedMessageHandler(context);
        boolean result = handler.handleUnpinMessage(messageRecordSms, smsDatabase);

        verify(smsDatabase).unpinMessage(1);
        assertEquals(result, false);
    }

    @Test
    public void testHandlerUnpinMethodMmsMessagedAlreadyPinned() {
        setMockGetIdReturnValue(1);

        PinnedMessageHandler handler = new PinnedMessageHandler(context);
        boolean result = handler.handleUnpinMessage(messageRecordMms, mmsDatabase);

        verify(mmsDatabase).unpinMessage(1);
        assertEquals(result, false);
    }

    @Test
    public void testHandlerUnpinMethodSmsSuccess() {
        setMockGetIdReturnValue(2);

        PinnedMessageHandler handler = new PinnedMessageHandler(context);
        boolean result = handler.handleUnpinMessage(messageRecordSms, smsDatabase);

        verify(smsDatabase).unpinMessage(2);
        assertEquals(result, true);
    }

    @Test
    public void testHandlerUnpinMethodMmsSuccess() {
        setMockGetIdReturnValue(2);

        PinnedMessageHandler handler = new PinnedMessageHandler(context);
        boolean result = handler.handleUnpinMessage(messageRecordMms, mmsDatabase);

        verify(mmsDatabase).unpinMessage(2);
        assertEquals(result, true);
    }
}
