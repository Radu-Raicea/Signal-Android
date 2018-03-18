package org.thoughtcrime.securesms.espresso;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.v7.widget.AppCompatImageButton;

import org.thoughtcrime.securesms.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.thoughtcrime.securesms.espresso.ViewActions.clickPercent;
import static org.thoughtcrime.securesms.espresso.ViewMatchers.first;

public class ConversationsHelper extends BaseHelper<ConversationsHelper> {
    public ConversationsHelper(HelperSecret s) {}

   private void createGroup() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());

        onView(withText(R.string.text_secure_normal__menu_new_group))
            .perform(click());

        onView(withId(R.id.group_name))
            .perform(typeText(Helper.groupName));

        String[] numbers = {
            Helper.phoneNumber1,
            Helper.phoneNumber2
        };

       for(String number : numbers) {
           onView(withId(R.id.contacts_button))
               .perform(click());
           onView(withId(R.id.search_view))
               .perform(typeText(number));
           onView(withId(R.id.recycler_view))
               .perform(clickPercent(0.5f, 0.1f));
           onView(allOf(
               withParent(withId(R.id.toolbar)),
               withClassName(is(AppCompatImageButton.class.getName()))
           )).perform(click());
       }

       onView(withId(R.id.menu_create_group))
           .perform(click());
    }

    /* NAVIGATION */

    public ConversationHelper goConversation() {
        onView(withId(R.id.fab))
            .perform(click());
        onView(withId(R.id.search_view))
            .perform(typeText(phoneNumber1));
        onView(withId(R.id.name))
            .perform(click());

        return new ConversationHelper(new HelperSecret());
    }

    public ConversationHelper goGroup() {
        try {
            onView(first(anyOf(
                withText(Helper.groupName),
                withText("null")
            ))).perform(click());
        } catch (NoMatchingViewException e) {
            this.createGroup();
        }

        return new ConversationHelper(new HelperSecret());
    }
}
