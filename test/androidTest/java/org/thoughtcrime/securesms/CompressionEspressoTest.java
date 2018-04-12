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
public class CompressionEspressoTest {
    @Rule
    public ActivityTestRule<ConversationListActivity> mainActivityRule =
            new ActivityTestRule(ConversationListActivity.class, true, false);

    @Test
    public void settingExists() {
        Helper helper = new Helper(mainActivityRule);

        helper
            .goConversations()
            .goSettings()
            .goChatsAndMediaSettings()
                .assertText("Image / Video Compression");
    }

    @Test
    public void canToggleAllMediaTypes() {
        Helper helper = new Helper(mainActivityRule);

        helper
            .goConversations()
            .goSettings()
            .goChatsAndMediaSettings()
                .toggleCompressImages()
                .toggleCompressVideo()
                .toggleCompressGif();
    }
}
