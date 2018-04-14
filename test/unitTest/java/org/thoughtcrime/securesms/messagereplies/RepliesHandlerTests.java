package org.thoughtcrime.securesms.messagereplies;

import org.junit.Before;
import org.junit.Test;
import org.thoughtcrime.securesms.MessageReplyMocks;
import org.thoughtcrime.securesms.RepliesHandler;
import org.thoughtcrime.securesms.database.DatabaseFactory;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class RepliesHandlerTests extends MessageReplyMocks {
    private RepliesHandler repliesHandler;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        super.setUpTestReplies();
        super.setUpGetMessageReplies();
        super.setUpStaticMessageReplyDatabase();
        super.setUpParcel();
        super.setUpCursor();
        repliesHandler = new RepliesHandler(context);
        repliesHandler.setMessageReplyDatabase(DatabaseFactory.getMessageReplyDatabase(context));
    }

    @Test
    public void testReplyToMessageBySender() {
        repliesHandler.replyToMessageBySender(messageRecordWithReplies, replies[0], time);
        verify(messageReplyDatabase).replyToMessage(messageRecordWithReplies, replies[0], time);
    }

    @Test
    public void testReceiveReply() {
        repliesHandler.receiveReply(messageHash, replies[0], time, replierId, threadId);
        verify(messageReplyDatabase).replyToMessage(messageHash, replies[0], time, replierId, threadId);
    }

    @Test
    public void testGetMessageReplies() {
        List<RepliesHandler.Reply> results = repliesHandler.getMessageReplies(messageRecordWithReplies);

        assertEquals(results.size(), 3);

        assertEquals(results.get(0).getReply(), "reply 1");
        assertEquals(results.get(1).getReply(), "reply 2");
        assertEquals(results.get(2).getReply(), "reply 3");

        assertEquals(results.get(0).getReplierAddress().toString(), "replier id");
        assertEquals(results.get(0).getReplyDate(), (Long) 1234L);
        assertEquals(results.get(0).getReplyId(), "reply id");
        assertEquals(results.get(0).getMessageHash(), "message hash");
    }

    @Test
    public void testGetMessageRepliesWithNoReplies() {
        List<RepliesHandler.Reply> results = repliesHandler.getMessageReplies(messageRecordWithoutReplies);
        assertEquals(results.size(), 0);
    }
}
