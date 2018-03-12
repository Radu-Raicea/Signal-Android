package org.thoughtcrime.securesms.espresso;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import org.hamcrest.Matcher;
import org.thoughtcrime.securesms.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class PreferencesHelper extends BaseHelper<PreferencesHelper>{
    public PreferencesHelper(HelperSecret s) {}

    public PreferencesHelper resetNickname() {
        onView(withId(android.R.id.content))
            .perform(swipeUp());
        onView(withText(R.string.RecipientPreferenceActivity_resetnickname))
            .perform(click());

        return new PreferencesHelper(new HelperSecret());
    }

    public PreferencesHelper setNickname(String message) {
        onView(withId(android.R.id.content))
            .perform(swipeUp());

        onView(isRoot()).perform(waitFor(1000));
        onView(withText(R.string.RecipientPreferenceActivity_setnickname))
            .perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(android.R.id.edit))
            .perform(replaceText(message));
        onView(withId(android.R.id.button1))
            .perform(click());

        return new PreferencesHelper(new HelperSecret());
    }

    /* NAVIGATION */

    public ConversationHelper goConversation() {
        pressBack();

        return new ConversationHelper(new HelperSecret());
    }


    public PreferencesHelper scrolTOButtom() {
        onView(withText("Chat settings")).perform(swipeUp());
        return new PreferencesHelper(new HelperSecret());
    }

    public static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }
}
