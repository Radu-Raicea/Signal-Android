package org.thoughtcrime.securesms.espresso;

import org.thoughtcrime.securesms.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class ChatsAndMediaSettingsHelper extends BaseHelper<ChatsAndMediaSettingsHelper> {
    public ChatsAndMediaSettingsHelper(HelperSecret s) {};

    public ChatsAndMediaSettingsHelper toggleCompressImages() {
        onView(withText(R.string.preferences_chats__compression))
            .perform(click());
        onView(withText(R.string.arrays__images))
            .perform(click());
        onView(withId(android.R.id.button1))
                .perform(click());

        return this;
    };

    public ChatsAndMediaSettingsHelper toggleCompressVideo() {
        onView(withText(R.string.preferences_chats__compression))
            .perform(click());
        onView(withText(R.string.arrays__video))
            .perform(click());
        onView(withId(android.R.id.button1))
            .perform(click());

        return this;
    };

    public ChatsAndMediaSettingsHelper toggleCompressGif() {
        onView(withText(R.string.preferences_chats__compression))
            .perform(click());
        onView(withText(R.string.arrays__gif))
            .perform(click());
        onView(withId(android.R.id.button1))
            .perform(click());

        return this;
    };

    /* NAVIGATION */

    public SettingsHelper goSettings() {
        pressBack();

        return new SettingsHelper(new HelperSecret());
    }
}
