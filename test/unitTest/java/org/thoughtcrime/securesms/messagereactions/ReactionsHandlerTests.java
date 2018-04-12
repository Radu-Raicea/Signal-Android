package org.thoughtcrime.securesms.messagereactions;

import org.junit.Before;
import org.junit.Test;
import org.thoughtcrime.securesms.ReactionsHandler;
import org.thoughtcrime.securesms.ReactionsMocks;
import org.thoughtcrime.securesms.database.DatabaseFactory;

import static org.mockito.Mockito.verify;

public class ReactionsHandlerTests extends ReactionsMocks {
    private ReactionsHandler reactionsHandler;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        reactionsHandler = new ReactionsHandler(context);
        reactionsHandler.setMessageReactionDatabase(DatabaseFactory.getMessageReactionDatabase(context));
    }

    @Test
    public void testAddReactionToSenderDB() {
        reactionsHandler.addReactionToSenderDB(messageRecord, reaction, reactionDate);
        verify(messageReactionDatabase).reactToMessage(messageRecord, reaction, reactionDate);
    }

    @Test
    public void testAddReactionToReceiverDB() {
        reactionsHandler.addReactionToReceiverDB(messageHash, reaction, reactionDate, reactorId, threadId);
        verify(messageReactionDatabase).reactToMessage(messageHash, reaction, reactionDate, reactorId, threadId);
    }

    @Test
    public void testDeleteReaction() {
        reactionsHandler.deleteReaction(messageRecord);

    }

    @Test
    public void testGetMessageReactions() {

    }
}
