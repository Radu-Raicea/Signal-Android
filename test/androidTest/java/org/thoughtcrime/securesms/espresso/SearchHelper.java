package org.thoughtcrime.securesms.espresso;

import org.thoughtcrime.securesms.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class SearchHelper extends BaseRecyclerHelper<ConversationHelper> {
    SearchHelper(HelperSecret s) {}

    public SearchHelper search(String message) {
        onView(withId(R.id.search_src_text))
            .perform(typeText(message));
        // TODO doesn't find the search field

        return this;
    }

    public SearchHelper next() {
        // TODO press arrow up
        return this;
    }

    public SearchHelper prev() {
        // TODO press arrow down
        return this;
    }

    /* NAVIGATION */

    public ConversationHelper goConversation() {
        // TODO go back to conversation (pressBack?)
        return new ConversationHelper(new HelperSecret());
    }
}
