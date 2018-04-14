package org.thoughtcrime.securesms;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.thoughtcrime.securesms.espresso.Helper;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RepliesEspressoTest {
    @Rule
    public ActivityTestRule<ConversationListActivity> mainActivityRule =
            new ActivityTestRule(ConversationListActivity.class, true, false);

    @Test
    public void buttonExists(){
        Helper helper = new Helper(mainActivityRule);

        helper
                .goConversations()
                .goConversation()
                .sendMessage("Testing")
                .selectMessage(0)
                .assertId(R.id.menu_context_reply);

    }

    @Test
    public void canSendReply() {
        Helper helper = new Helper(mainActivityRule);

        helper
                .goConversations()
                .goConversation()
                .sendMessage("Testing")
                .sendReply(0, "Reply test");
    }

    @Test
    public void replyIsVisibleInUI() {
        Helper helper = new Helper(mainActivityRule);

        helper
                .goConversations()
                .goConversation()
                .sendMessage("Testing")
                .sendReply(0, "test")
                .assertText("test");
    }

    @Test
    public void multipleRepliesAreVisibleInUI() {
        Helper helper = new Helper(mainActivityRule);

        helper
                .goConversations()
                .goConversation()
                .sendMessage("Testing1")
                .sendReply(0, "test1")
                .assertText("test1")
                .sendMessage("Testing2")
                .sendReply(0, "test2")
                .assertText("test2");

    }
}
