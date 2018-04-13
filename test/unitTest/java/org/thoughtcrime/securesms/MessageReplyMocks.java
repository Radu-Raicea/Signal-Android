package org.thoughtcrime.securesms;

import android.database.Cursor;
import android.os.Parcel;

import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.MessageReplyDatabase;
import org.thoughtcrime.securesms.database.model.MessageRecord;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DatabaseFactory.class, Parcel.class})
public class MessageReplyMocks extends BaseUnitTest {
    protected DatabaseFactory      databaseFactory;
    protected MessageReplyDatabase messageReplyDatabase;
    protected MessageRecord        messageRecord;
    protected Cursor               cursor;
    protected Parcel               parcel;
    protected String[]             replies;
    protected String               replyId;
    protected String               messageHash;
    protected String               replierId;
    protected String               messageType;
    protected Long                 time;
    protected Long                 threadId;

    @Override
    public void setUp() throws Exception {
        databaseFactory      = mock(DatabaseFactory.class);
        messageReplyDatabase = mock(MessageReplyDatabase.class);
        messageRecord        = mock(MessageRecord.class);
        cursor               = mock(Cursor.class);
        parcel               = mock(Parcel.class);
        replies              = new String[3];
        replyId              = "reply id";
        messageHash          = "message hash";
        replierId            = "replier id";
        messageType          = "SMS";
        time                 = 1234L;
        threadId             = 1L;
    }

    public void setUpTestReplies() {
        replies[0] = "reply 1";
        replies[1] = "reply 2";
        replies[2] = "reply 3";
    }

    public void setUpGetMessageReplies() {
        when(messageReplyDatabase.getMessageReplies(messageRecord)).thenReturn(cursor);
    }

    public void setUpStaticMessageReplyDatabase() throws Exception {
        PowerMockito.mockStatic(DatabaseFactory.class);
        BDDMockito.given(DatabaseFactory.getInstance(context)).willReturn(databaseFactory);
        BDDMockito.given(DatabaseFactory.getMessageReplyDatabase(context)).willReturn(messageReplyDatabase);
    }

    public void setUpParcel() {
        PowerMockito.mockStatic(Parcel.class);
        when(Parcel.obtain()).thenReturn(parcel);
        when(parcel.readString()).thenReturn(replierId);
    }

    public void setUpCursor() {
        when(cursor.moveToNext())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);

        when(cursor.getLong(cursor.getColumnIndexOrThrow(Mockito.anyString())))
                .thenReturn(time);

        List<String> replyAttributes = new ArrayList<>();

        for (String reply : replies) {
            replyAttributes.add(replierId);
            replyAttributes.add(reply);
            replyAttributes.add(replyId);
            replyAttributes.add(messageType);
            replyAttributes.add(messageHash);
        }

        when(cursor.getString(cursor.getColumnIndexOrThrow(Mockito.anyString())))
                .thenAnswer(AdditionalAnswers.returnsElementsOf(replyAttributes));
    }
}