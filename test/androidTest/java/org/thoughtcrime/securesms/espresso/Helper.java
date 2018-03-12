package org.thoughtcrime.securesms.espresso;

import android.support.test.rule.ActivityTestRule;

import org.thoughtcrime.securesms.ConversationListActivity;

public class Helper extends BaseHelper<Helper> {
    public Helper(ActivityTestRule<ConversationListActivity> activityRule) {
        super(activityRule);
    }

    /* NAVIGATION */

    public ConversationsHelper goConversations() {
        return new ConversationsHelper(new HelperSecret());
    }
}
