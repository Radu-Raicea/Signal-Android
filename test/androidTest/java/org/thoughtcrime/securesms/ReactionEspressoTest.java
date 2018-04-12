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
public class ReactionEspressoTest {
    @Rule
    public ActivityTestRule<ConversationListActivity> mainActivityRule =
            new ActivityTestRule(ConversationListActivity.class, true, false);

    @Test
    public void buttonExists() {
        Helper helper = new Helper(mainActivityRule);

        helper
            .goConversations()
            .goConversation()
                .sendMessage("Testing")
                .selectMessage(0)
                .assertId(R.id.menu_context_emoji);
    }

    @Test
    public void canSendReaction() {
        Helper helper = new Helper(mainActivityRule);

        helper
            .goConversations()
            .goConversation()
                .sendMessage("Testing")
                .sendReaction(0, 1);
    }

    @Test
    public void reactionIsVisibleInUI() {
        Helper helper = new Helper(mainActivityRule);

        helper
            .goConversations()
            .goConversation()
                .sendMessage("Testing")
                .sendReaction(0, 2)
                .assertEmojiAt(2, 0);
    }

    @Test
    public void multipleReactionsAreVisibleInUI() {
        Helper helper = new Helper(mainActivityRule);

        helper
            .goConversations()
            .goConversation()
                .sendMessage("Testing")
                .sendReaction(0, 3)
                .sendReaction(0, 4)
                .assertEmojiAt(4, 0)
                .assertEmojiAt(3, 0);
    }
}

