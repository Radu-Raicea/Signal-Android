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
import org.thoughtcrime.securesms.database.MessageReactionDatabase;
import org.thoughtcrime.securesms.database.model.MessageRecord;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DatabaseFactory.class, Parcel.class})
public class ReactionsMocks extends BaseUnitTest {
    protected DatabaseFactory         databaseFactory;
    protected MessageReactionDatabase messageReactionDatabase;
    protected MessageRecord           messageRecordWithReactions;
    protected MessageRecord           messageRecordWithoutReactions;
    protected Cursor                  cursorWithReactions;
    protected Cursor                  cursorWithoutReactions;
    protected Parcel                  parcel;
    protected String[]                reactions;
    protected String                  reactionId;
    protected String                  messageHash;
    protected String                  reactorId;
    protected String                  messageType;
    protected Long                    reactionDate;
    protected Long                    threadId;

    @Override
    public void setUp() throws Exception {
        databaseFactory            = mock(DatabaseFactory.class);
        messageReactionDatabase    = mock(MessageReactionDatabase.class);
        messageRecordWithReactions = mock(MessageRecord.class);
        cursorWithReactions        = mock(Cursor.class);
        cursorWithoutReactions     = mock(Cursor.class);
        parcel                     = mock(Parcel.class);
        reactions                  = new String[3];
        reactionId                 = "reaction id";
        messageHash                = "message hash";
        reactorId                  = "reactor id";
        messageType                = "SMS";
        reactionDate               = 1234L;
        threadId                   = 1L;
    }

    public void setUpTestReactions() {
        reactions[0] = ":)";
        reactions[1] = ":(";
        reactions[2] = ":)";
    }

    public void setUpGetMessageReactions() {
        when(messageReactionDatabase.getMessageReaction(messageRecordWithReactions)).thenReturn(cursorWithReactions);
        when(messageReactionDatabase.getMessageReaction(messageRecordWithoutReactions)).thenReturn(cursorWithoutReactions);
    }

    public void setUpStaticMessageReactionDatabase() throws Exception {
        PowerMockito.mockStatic(DatabaseFactory.class);
        BDDMockito.given(DatabaseFactory.getInstance(context)).willReturn(databaseFactory);
        BDDMockito.given(DatabaseFactory.getMessageReactionDatabase(context)).willReturn(messageReactionDatabase);
    }

    public void setUpParcel() {
        PowerMockito.mockStatic(Parcel.class);
        when(Parcel.obtain()).thenReturn(parcel);
        when(parcel.readString()).thenReturn(reactorId);
    }

    public void setUpCursor() {
        when(cursorWithReactions.moveToNext())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);

        when(cursorWithoutReactions.moveToNext()).thenReturn(false);

        when(cursorWithReactions.getLong(cursorWithReactions.getColumnIndexOrThrow(Mockito.anyString())))
                .thenReturn(reactionDate);

        List<String> reactionAttributes = new ArrayList<>();

        for (String reaction : reactions) {
            reactionAttributes.add(reactorId);
            reactionAttributes.add(reaction);
            reactionAttributes.add(reactionId);
            reactionAttributes.add(messageType);
            reactionAttributes.add(messageHash);
        }

        when(cursorWithReactions.getString(cursorWithReactions.getColumnIndexOrThrow(Mockito.anyString())))
                .thenAnswer(AdditionalAnswers.returnsElementsOf(reactionAttributes));
    }
}
