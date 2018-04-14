package org.thoughtcrime.securesms.espresso;

import android.support.test.espresso.AmbiguousViewMatcherException;
import android.support.test.espresso.NoMatchingViewException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.thoughtcrime.securesms.espresso.ViewMatchers.atPosition;
import static org.thoughtcrime.securesms.espresso.ViewMatchers.visibleIdAtPosition;

class BaseRecyclerHelper<T> extends BaseHelper<T> {

    /* ASSERTIONS */

    public T assertVisibleIdAt(int id, int position) {
        try {
            onView(withId(android.R.id.list))
                .perform(scrollToPosition(position))
                .check(matches(visibleIdAtPosition(position, id)));
        } catch (AmbiguousViewMatcherException e) {}

        return (T)this;
    }

    public T assertNoVisibleIdAt(int id, int position) {
        try {
            onView(withId(android.R.id.list))
                .perform(scrollToPosition(position))
                .check(matches(visibleIdAtPosition(position, id)));

            throw new Error("Helper.assertNoVisibleIdAt: View found matching " + id + " at child #" + position);
        } catch (VisibleErr e) {}

        return (T)this;
    }

    public T assertTextAt(String text, int position) {
        try {
            onView(withId(android.R.id.list))
                .perform(scrollToPosition(position))
                .check(matches(atPosition(position, hasDescendant(withText(text)))));
        } catch (AmbiguousViewMatcherException e) {}

        return (T)this;
    }

    public T assertNoTextAt(String text, int position) {
        try {
            onView(withId(android.R.id.list))
                .perform(scrollToPosition(position))
                .check(matches(atPosition(position, hasDescendant(withText(text)))));

            throw new Error("Helper.assertNoTextAt: View found matching \"" + text + "\" at child #" + position);
        } catch (NoMatchingViewException e) {}

        return (T)this;
    }

    public T assertEmojiAt(int emoji, int position) {
        String[] emojis = new String[]{
                "\uD83D\uDE00",
                "\uD83D\uDE01",
                "\uD83D\uDE02",
                "\uD83E\uDD23",
                "\uD83D\uDE03",
                "\uD83D\uDE04",
                "\uD83D\uDE05",
                "\uD83D\uDE06",
                "\uD83D\uDE09",
                "\uD83D\uDE0A",
                "\uD83D\uDE0B",
                "\uD83D\uDE0E",
                "\uD83D\uDE0D",
                "\uD83D\uDE18",
                "\uD83D\uDE17",
                "\uD83D\uDE19",
        };

        return this.assertTextAt(emojis[emoji], position);
    }
}
