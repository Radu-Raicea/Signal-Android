package org.thoughtcrime.securesms;

import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.thoughtcrime.securesms.database.model.DisplayRecord;
import org.thoughtcrime.securesms.database.model.MessageRecord;

import java.util.LinkedList;

import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class SearchConversationMocks extends BaseUnitTest {

    protected LinkedList<MessageRecord> messageRecordList;
    protected MessageRecord             messageRecordOne;
    protected MessageRecord             messageRecordTwo;
    protected MessageRecord             messageRecordThree;
    protected MessageRecord             messageRecordFour;
    protected MessageRecord             messageRecordFive;

    public void setUpMessageRecords() {
        when(messageRecordOne.getBody()).thenReturn(new DisplayRecord.Body("Hello World", true));
        when(messageRecordTwo.getBody()).thenReturn(new DisplayRecord.Body("Goodbye World", true));
        when(messageRecordThree.getBody()).thenReturn(new DisplayRecord.Body("hello", true));
        when(messageRecordFour.getBody()).thenReturn(new DisplayRecord.Body("goodbye", true));
        when(messageRecordFive.getBody()).thenReturn(new DisplayRecord.Body("hello again", true));

        setUpMessageRecordIds();
    }

    public void setUpMessageRecordIds() {
        when(messageRecordOne.getId()).thenReturn(1L);
        when(messageRecordTwo.getId()).thenReturn(2L);
        when(messageRecordThree.getId()).thenReturn(3L);
        when(messageRecordFour.getId()).thenReturn(4L);
        when(messageRecordFive.getId()).thenReturn(5L);
    }

    public void setUpMessageRecordList() {
        messageRecordList.add(messageRecordOne);
        messageRecordList.add(messageRecordTwo);
        messageRecordList.add(messageRecordThree);
        messageRecordList.add(messageRecordFour);
    }

}
