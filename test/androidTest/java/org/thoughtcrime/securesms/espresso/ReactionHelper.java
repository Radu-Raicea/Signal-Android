package org.thoughtcrime.securesms.espresso;

import static android.support.test.espresso.Espresso.pressBack;



public class ReactionHelper extends BaseRecyclerHelper<ReactionHelper> {
    ReactionHelper(HelperSecret s) {}

    private Boolean reactionMode = false;

    public ConversationHelper goConversation() {
        pressBack();

        return new ConversationHelper(new HelperSecret());
    }
}
