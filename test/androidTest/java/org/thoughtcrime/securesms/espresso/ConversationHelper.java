package org.thoughtcrime.securesms.espresso;

import android.support.test.InstrumentationRegistry;
import android.view.KeyEvent;

import org.thoughtcrime.securesms.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.thoughtcrime.securesms.espresso.ViewActions.longClickPercent;
import static org.thoughtcrime.securesms.espresso.ViewActions.pressAndHoldAction;
import static org.thoughtcrime.securesms.espresso.ViewActions.releaseAction;
import static org.thoughtcrime.securesms.espresso.ViewActions.waitFor;
import static org.thoughtcrime.securesms.espresso.ViewMatchers.nthChildOf;

public class ConversationHelper extends BaseRecyclerHelper<ConversationHelper> {
    ConversationHelper(HelperSecret s) {}

    private Boolean messageSelected = false;

    public ConversationHelper sendMessage(String message) {
        onView(withId(R.id.embedded_text_editor))
            .perform(typeText(message));
        onView(withId(R.id.send_button))
            .perform(click());

        return this;
    }

    public ConversationHelper sendImage(String message) {
        onView(withId(R.id.quick_camera_toggle))
            .perform(click());
        onView(isRoot())
            .perform(waitFor(2000));
        onView(withId(R.id.shutter_button))
            .perform(click());
        onView(isRoot())
            .perform(waitFor(2000));
        onView(withId(R.id.embedded_text_editor))
            .perform(typeText(message));
        onView(withId(R.id.send_button))
            .perform(click());

        return this;
    }

    public ConversationHelper sendAudio() {
        onView(withId(R.id.recorder_view))
            .perform(pressAndHoldAction());
        onView(isRoot())
            .perform(waitFor(2000));
        onView(withId(R.id.recorder_view))
            .perform(releaseAction());
        onView(isRoot())
            .perform(waitFor(1000));

        return this;
    }

    public ConversationHelper sendReaction(int position, int emoji) {
        this.unselectMessage();

        this.selectMessage(position);

        int index = emoji % 16;

        onView(withId(R.id.menu_context_emoji))
            .perform(click());
        onView(
            nthChildOf(nthChildOf(
                withId(R.id.tabs), 0),1))
            .perform(click());
        onView(
            nthChildOf(nthChildOf(nthChildOf(nthChildOf(
                withId(R.id.emoji_pager),1),0),0), index))
            .perform(click());

        this.messageSelected = false;

        pressBack();

        return this;
    }

    public ConversationHelper sendReply(int position, String message) {

        this.unselectMessage();
        this.selectMessage(position);

        onView(withId(R.id.menu_context_reply))
                .perform(click());
        onView(withId(R.id.lin_reply))
                .perform(click());
        onView(withId(R.id.custom_reply))
                .perform(typeText(message));
        onView(withId(R.id.custom_reply)).perform(android.support.test.espresso.action.ViewActions.pressKey(KeyEvent.KEYCODE_ENTER));

        return this;
    }


    public ConversationHelper selectMessage(int position) {
        this.messageSelected = true;

        onView(withId(android.R.id.list))
            .perform(scrollToPosition(position))
            .perform(actionOnItemAtPosition(position, longClickPercent(0f,0.9f)));

        return this;
    }

    public ConversationHelper unselectMessage() {
        if (!this.messageSelected) {
            return this;
        }

        this.messageSelected = false;
        pressBack();

        return this;
    }

    public ConversationHelper pinMessage(int position) {
        this.unselectMessage();

        this.selectMessage(position);

        onView(withId(R.id.menu_context_pin_message))
            .perform(click());

        this.messageSelected = false;

        return this;
    }

    public ConversationHelper unpinMessage(int position) {
        this.unselectMessage();

        this.selectMessage(position);

        onView(withId(R.id.menu_context_unpin_message))
            .perform(click());

        this.messageSelected = false;

        return this;
    }

    /* NAVIGATION */

    public ConversationsHelper goConversations() {
        if (this.messageSelected) {
            pressBack();
        }

        pressBack();

        return new ConversationsHelper(new HelperSecret());
    }

    public PinnedHelper goPinned() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());

        onView(withText(R.string.conversation__menu_view_pinned_messages))
            .perform(click());

        return new PinnedHelper(new HelperSecret());
    }

    public PreferencesHelper goSettings() {
        onView(withId(R.id.contact_photo_image))
            .perform(click());

        return new PreferencesHelper(new HelperSecret());
    }

    public SearchHelper goSearch() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());

        onView(withText(R.string.Conversation__menu_search_conversation))
            .perform(click());

        return new SearchHelper(new HelperSecret());
    }
}

