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
        String testSend = helper.randString();

        helper
                .goConversations()
                .goConversation()
                .sendMessage(testSend)
                .selectMessage(0)
                .assertId(R.id.menu_context_reply);

    }

    @Test
    public void canSendReply() {
        Helper helper = new Helper(mainActivityRule);
        String testSend = helper.randString();
        String testReply = helper.randString();

        helper
                .goConversations()
                .goConversation()
                .sendMessage(testSend)
                .sendReply(0, testReply)
                .assertText(testReply);
    }

    @Test
    public void replyIsVisibleInUI() {
        Helper helper = new Helper(mainActivityRule);

        String testSend = helper.randString();
        String testReply = helper.randString();

        helper
                .goConversations()
                .goConversation()
                .sendMessage(testSend)
                .sendReply(0, testReply)
                .assertText(testReply);
    }

    @Test
    public void multipleRepliesAreVisibleInUI() {
        Helper helper = new Helper(mainActivityRule);

        String testSend1 = helper.randString();
        String testReply1 = helper.randString();
        String testSend2 = helper.randString();
        String testReply2 = helper.randString();

        helper
                .goConversations()
                .goConversation()
                .sendMessage(testSend1)
                .sendReply(0, testReply1)
                .assertText(testReply1)
                .sendMessage(testSend2)
                .sendReply(0, testReply2)
                .assertText(testReply2);

    }
}
