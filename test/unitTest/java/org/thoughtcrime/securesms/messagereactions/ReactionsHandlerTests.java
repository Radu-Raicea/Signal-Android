package org.thoughtcrime.securesms.messagereactions;

import org.junit.Before;
import org.junit.Test;
import org.thoughtcrime.securesms.ReactionsHandler;
import org.thoughtcrime.securesms.ReactionsMocks;
import org.thoughtcrime.securesms.database.DatabaseFactory;

import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class ReactionsHandlerTests extends ReactionsMocks {
    private ReactionsHandler reactionsHandler;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        super.setUpTestReactions();
        super.setUpGetMessageReactions();
        super.setUpStaticMessageReactionDatabase();
        super.setUpParcel();
        super.setUpCursor();
        reactionsHandler = new ReactionsHandler(context);
        reactionsHandler.setMessageReactionDatabase(DatabaseFactory.getMessageReactionDatabase(context));
    }

    @Test
    public void testAddReactionToSenderDB() {
        reactionsHandler.addReactionToSenderDB(messageRecord, reactions[0], reactionDate);
        verify(messageReactionDatabase).reactToMessage(messageRecord, reactions[0], reactionDate);
    }

    @Test
    public void testAddReactionToReceiverDB() {
        reactionsHandler.addReactionToReceiverDB(messageHash, reactions[0], reactionDate, reactorId, threadId);
        verify(messageReactionDatabase).reactToMessage(messageHash, reactions[0], reactionDate, reactorId, threadId);
    }

    @Test
    public void testGetMessageReactions() {
        List<ReactionsHandler.Reaction> results = reactionsHandler.getMessageReactions(messageRecord);

        assertEquals(results.size(), 3);

        assertEquals(results.get(0).getReaction(), ":)");
        assertEquals(results.get(1).getReaction(), ":(");
        assertEquals(results.get(2).getReaction(), ":)");

        assertEquals(results.get(0).getReactor().toString(), "reactor id");
        assertEquals(results.get(0).getReactionDate(), (Long) 1234L);
        assertEquals(results.get(0).getReactionId(), "reaction id");
        assertEquals(results.get(0).getMessageHash(), "message hash");
    }

    @Test
    public void testGetReactionCounts() {
        List<ReactionsHandler.Reaction> results = reactionsHandler.getMessageReactions(messageRecord);
        Map<String, Integer> reactionCounts = reactionsHandler.getReactionCounts(results);
        assertEquals(reactionCounts.get(":)"), (Integer) 2);
        assertEquals(reactionCounts.get(":("), (Integer) 1);
    }
}
