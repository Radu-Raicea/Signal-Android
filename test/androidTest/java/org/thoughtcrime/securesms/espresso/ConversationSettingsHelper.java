package org.thoughtcrime.securesms.espresso;

import junit.framework.AssertionFailedError;

import org.thoughtcrime.securesms.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.thoughtcrime.securesms.espresso.ViewActions.waitFor;

public class ConversationSettingsHelper extends BaseHelper<ConversationSettingsHelper> {
    public ConversationSettingsHelper(HelperSecret s) {}

    public ConversationSettingsHelper resetNickname() {
        try {
            onView(withText(R.string.RecipientPreferenceActivity_resetnickname))
                .check(matches(isDisplayed()));
        } catch (AssertionFailedError e) {
            onView(withId(android.R.id.content))
                .perform(swipeUp());
            onView(isRoot())
                .perform(waitFor(500));
        } finally {
            onView(withText(R.string.RecipientPreferenceActivity_resetnickname))
                .perform(click());
        }

        return new ConversationSettingsHelper(new HelperSecret());
    }

    public ConversationSettingsHelper setNickname(String message) {
        try {
            onView(withText(R.string.RecipientPreferenceActivity_setnickname))
                .check(matches(isDisplayed()));
        } catch (AssertionFailedError e) {
            onView(withId(android.R.id.content))
                .perform(swipeUp());
            onView(isRoot())
                .perform(waitFor(500));
        } finally {
            onView(withText(R.string.RecipientPreferenceActivity_setnickname))
                .perform(click());
            onView(withId(android.R.id.edit))
                .perform(replaceText(message));
            onView(withId(android.R.id.button1))
                .perform(click());
        }

        return new ConversationSettingsHelper(new HelperSecret());
    }

    /* NAVIGATION */

    public ConversationHelper goConversation() {
        pressBack();

        return new ConversationHelper(new HelperSecret());
    }
}
