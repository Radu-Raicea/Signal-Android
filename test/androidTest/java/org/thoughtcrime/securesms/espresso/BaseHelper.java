package org.thoughtcrime.securesms.espresso;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.AmbiguousViewMatcherException;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import org.thoughtcrime.securesms.ConversationListActivity;
import org.thoughtcrime.securesms.R;

import java.util.UUID;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

abstract class BaseHelper<T> {
    // TODO temporary solution until pinned images sent to self finish loading (bug)
    static String groupName    = "TESTTESTTEST";
    static String phoneNumber  = "+15148136314";
    static String phoneNumber2 = "+15149709566";


    BaseHelper() {}

    public BaseHelper(ActivityTestRule<ConversationListActivity> activityRule) {
        activityRule.launchActivity(new Intent());

        if (BaseHelper.phoneNumber == null) {
            openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());

            onView(ViewMatchers.withText(R.string.text_secure_normal__menu_settings))
                .perform(click());

            final String[] stringHolder = {null};
            onView(withId(R.id.number)).perform(ViewActions.getTextFromView(stringHolder));
            BaseHelper.phoneNumber = stringHolder[0];

            pressBack();
        }
    }

    public String getPhoneNumber() {
        return BaseHelper.phoneNumber;
    }

    /* ASSERTIONS */

    public T assertId(int id) {
        try {
            onView(withId(id))
                .check(matches(isDisplayed()));
        } catch (AmbiguousViewMatcherException e) {}

        return (T)this;
    }

    public T assertText(String text) {
        try {
            onView(withText(text))
                .check(matches(isDisplayed()));
        } catch (AmbiguousViewMatcherException e) {}

        return (T)this;
    }

    public T assertNoText(String text) {
        try {
            onView(withText(text))
                .check(matches(isDisplayed()));

            throw new Error("Helper.assertNoText: View found matching \"" + text + "\"");
        } catch (NoMatchingViewException e) {}

        return (T)this;
    }

    /* MISC */

    public String randString() {
        return UUID.randomUUID().toString().substring(0, 16);
    }
}
