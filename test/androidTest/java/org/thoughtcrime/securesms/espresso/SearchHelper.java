package org.thoughtcrime.securesms.espresso;

import org.thoughtcrime.securesms.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class SearchHelper extends BaseRecyclerHelper<ConversationHelper> {
    SearchHelper(HelperSecret s) {}

    public SearchHelper search(String message) {
        onView(withId(R.id.custom_search))
            .perform(typeText(message), pressImeActionButton());

        return this;
    }

    public SearchHelper up() {
        onView(withId(R.id.search_arrow_up))
            .perform(click());

        return this;
    }

    public SearchHelper down() {
        onView(withId(R.id.search_arrow_down))
            .perform(click());

        return this;
    }

    /* NAVIGATION */

    public ConversationHelper goConversation() {
        pressBack();
        pressBack();

        return new ConversationHelper(new HelperSecret());
    }
}
