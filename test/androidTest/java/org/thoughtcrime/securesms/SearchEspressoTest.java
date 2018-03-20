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
public class SearchEspressoTest {
    private String wholeScreenMessage = "@\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n@";

    @Rule
    public ActivityTestRule<ConversationListActivity> mainActivityRule =
            new ActivityTestRule(ConversationListActivity.class, true, false);

    @Test
    public void pageExists() {
        Helper helper = new Helper(mainActivityRule);

        helper
            .goConversations()
            .goConversation()
            .goSearch();
    }

    @Test
    public void canSearchForMessages() {
        Helper helper = new Helper(mainActivityRule);

        String testString = helper.randString();

        helper
            .goConversations()
            .goConversation()
                .sendMessage(testString)
                .sendMessage(this.wholeScreenMessage)
            .goSearch()
                .search(testString)
                .assertText(testString);
    }

    @Test
    public void canMatchMixedCase() {
        Helper helper = new Helper(mainActivityRule);

        String testString = helper.randString();

        helper
            .goConversations()
            .goConversation()
                .sendMessage(testString)
                .sendMessage(this.wholeScreenMessage)
            .goSearch()
                .search(testString.toUpperCase())
                .assertText(testString);
    }

    @Test
    public void canNavigateResults() {
        Helper helper = new Helper(mainActivityRule);

        String testString1 = helper.randString();
        String testString2 = helper.randString();

        helper
            .goConversations()
            .goConversation()
                .sendMessage(testString1)
                .sendMessage(this.wholeScreenMessage)
                .sendMessage(testString2)
                .sendMessage(this.wholeScreenMessage)
            .goSearch()
                .search("-")
                .up()
                .assertText(testString1)
                .down()
                .assertText(testString2);
    }
}
