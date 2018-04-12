package org.thoughtcrime.securesms;

import android.database.Cursor;

import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.MessageReactionDatabase;
import org.thoughtcrime.securesms.database.model.MessageRecord;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest({DatabaseFactory.class})
public class ReactionsMocks extends BaseUnitTest {
    protected DatabaseFactory databaseFactory;
    protected MessageReactionDatabase messageReactionDatabase;
    protected MessageRecord messageRecord;
    protected String reaction;
    protected String messageHash;
    protected String reactorId;
    protected Long reactionDate;
    protected Long threadId;
    protected Cursor cursor;

    @Override
    public void setUp() throws Exception {
        databaseFactory = mock(DatabaseFactory.class);
        messageReactionDatabase = mock(MessageReactionDatabase.class);
        messageRecord = mock(MessageRecord.class);
        cursor = mock(Cursor.class);
    }

    public void setUpReactToMessageSender() {

    }

    public void setUpReactToMessageReceiver() {

    }

    public void setUpDeleteReaction() {
        when(messageReactionDatabase.removeReaction(messageRecord)).thenReturn(1);
    }

    public void setUpGetMessageReactions() {
        when(messageReactionDatabase.getMessageReaction(messageRecord)).thenReturn(cursor);
    }

    public void setUpStaticMessageReactionDatabase() {
        PowerMockito.mockStatic(DatabaseFactory.class);
        BDDMockito.given(DatabaseFactory.getInstance(context)).willReturn(databaseFactory);
        BDDMockito.given(DatabaseFactory.getMessageReactionDatabase(context)).willReturn(messageReactionDatabase);
    }

}
