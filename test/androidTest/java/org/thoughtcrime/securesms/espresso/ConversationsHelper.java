package org.thoughtcrime.securesms.espresso;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.matcher.RootMatchers;

import org.thoughtcrime.securesms.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class ConversationsHelper extends BaseHelper<ConversationsHelper> {
    public ConversationsHelper(HelperSecret s) {}

    /* NAVIGATION */

    public ConversationHelper goConversation() {
        onView(withId(R.id.fab))
            .perform(click());
        onView(withId(R.id.search_view))
            .perform(typeText(phoneNumber));
        onView(withId(R.id.name))
            .perform(click());

        return new ConversationHelper(new HelperSecret());
    }

    public ConversationHelper goGroup() {
        try {
            this.assertText(groupName);
            onView(withText(groupName))
                .perform(click());
        } catch (NoMatchingViewException e) {
            newGroup();
        } finally {
            return new ConversationHelper(new HelperSecret());
        }
    }

    public ConversationHelper goGroup(String groupname) {
        try {
            this.assertText(groupname);
            onView(withText(groupname))
                .perform(click());
        } catch (NoMatchingViewException e) {
            newGroup();
        } finally {
            return new ConversationHelper(new HelperSecret());
        }
    }

   private void newGroup() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());

        onView(withText(R.string.text_secure_normal__menu_new_group))
            .perform(click());
        onView(withId(R.id.group_name))
            .perform(typeText(groupName));
        onView(withId(R.id.recipients_text))
            .perform(click());
        onView(withId(R.id.recipients_text))
            .perform(typeText(phoneNumber));

        onView(withText(phoneNumber))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click());
        onView(withId(R.id.recipients_text))
            .perform(typeText(phoneNumber2));

        onView(withText(phoneNumber2))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click());
        onView(withId(R.id.menu_create_group))
            .perform(click());
    }
}
