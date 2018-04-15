package org.thoughtcrime.securesms.espresso;

import org.thoughtcrime.securesms.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class SettingsHelper extends BaseHelper<SettingsHelper> {
    public SettingsHelper(HelperSecret s) {}

    /* NAVIGATION */

    public ChatsAndMediaSettingsHelper goChatsAndMediaSettings() {
        onView(withText(R.string.preferences__chats))
            .perform(click());

        return new ChatsAndMediaSettingsHelper(new HelperSecret());
    }

    public ConversationsHelper goConversations() {
        pressBack();

        return new ConversationsHelper(new HelperSecret());
    }
}
