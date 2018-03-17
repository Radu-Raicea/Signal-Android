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
public class NicknameEspressoTest {
    @Rule
    public ActivityTestRule<ConversationListActivity> mainActivityRule =
            new ActivityTestRule(ConversationListActivity.class, true, false);

    @Test
    public void pageExists() {
        Helper otherHelper = new Helper(mainActivityRule);

        otherHelper
            .goConversations()
            .goConversation()
            .goSettings();
    }

    @Test
    public void canSetNickname() {
        Helper helper = new Helper(mainActivityRule);

        String testString = helper.randString();

        helper
            .goConversations()
            .goConversation()
            .goSettings()
                .setNickname(testString)
            .goConversation()
            .goConversations()
                .assertText(testString)
            .goConversation()
            .goSettings()
                .resetNickname()
            .goConversation()
            .goConversations()
                .assertNoText(testString);
    }

    @Test
    public void canSetGroupNickname() {
        Helper helper = new Helper(mainActivityRule);

        String testString = helper.randString();

        helper
            .goConversations()
            .goGroup()
            .goSettings()
                .setNickname(testString)
            .goConversation()
            .goConversations()
                .assertText(testString)
            .goGroup(testString)
            .goSettings()
                .resetNickname()
            .goConversation()
            .goConversations()
                .assertNoText(testString);
    }

    @Test
    public void canResetNickname() {
        Helper helper = new Helper(mainActivityRule);

        String testString = helper.randString();

        helper
            .goConversations()
            .goConversation()
            .goSettings()
                .setNickname(testString)
                .resetNickname()
            .goConversation()
                .assertNoText(testString);
    }
}
