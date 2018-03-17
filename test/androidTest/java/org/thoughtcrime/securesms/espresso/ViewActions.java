package org.thoughtcrime.securesms.espresso;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.MotionEvents;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;

public class ViewActions {
    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }

    public static ViewAction longClickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performLongClick();
            }
        };
    }

    public static ViewAction clickPercent(final float pctX, final float pctY) {
        return new GeneralClickAction(Tap.SINGLE, (View view) -> {
            final int[] screenPos = new int[2];
            view.getLocationOnScreen(screenPos);
            int w = view.getWidth();
            int h = view.getHeight();

            float x = w * pctX;
            float y = h * pctY;

            final float screenX = screenPos[0] + x;
            final float screenY = screenPos[1] + y;

            return new float[]{screenX, screenY};
        }, Press.FINGER);
    }

    public static ViewAction longClickPercent(final float pctX, final float pctY) {
        return new GeneralClickAction(Tap.LONG, (View view) -> {
            final int[] screenPos = new int[2];
            view.getLocationOnScreen(screenPos);
            int w = view.getWidth();
            int h = view.getHeight();

            float x = w * pctX;
            float y = h * pctY;

            final float screenX = screenPos[0] + x;
            final float screenY = screenPos[1] + y;

            return new float[]{screenX, screenY};
        }, Press.FINGER);
    }

    public static ViewAction getTextFromView(String[] holder) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView textView = (TextView)view;
                holder[0] = textView.getText().toString();
            }
        };
    }

    public static ViewAction clickParent(final int levels) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "clicking on parent view";
            }

            @Override
            public void perform(UiController uiController, View view) {
                for (int i = 0; i < levels; i++) {
                    view = (View)view.getParent();
                }
                view.performClick();
            }
        };
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

    public static ViewAction pressAndHoldAction() {
        return PressAndReleaseHandler.pressAndHoldAction();
    }

    public static ViewAction releaseAction() {
        return PressAndReleaseHandler.releaseAction();
    }

    private static class PressAndReleaseHandler {
        static MotionEvent sMotionEventDownHeldView = null;

        public static ViewAction pressAndHoldAction() {
            return new ViewAction(){
                @Override
                public Matcher<View> getConstraints() {
                    return isDisplayingAtLeast(90); // Like GeneralClickAction
                }

                @Override
                public String getDescription() {
                    return "Press and hold action";
                }

                @Override
                public void perform(final UiController uiController, final View view) {
                    if (sMotionEventDownHeldView != null) {
                        throw new AssertionError("Only one view can be held at a time");
                    }

                    float[] precision = Press.FINGER.describePrecision();
                    float[] coords = GeneralLocation.CENTER.calculateCoordinates(view);
                    sMotionEventDownHeldView = MotionEvents.sendDown(uiController, coords, precision).down;
                }
            };
        }

        public static ViewAction releaseAction() {
            return new  ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return isDisplayingAtLeast(90);  // Like GeneralClickAction
                }

                @Override
                public String getDescription() {
                    return "Release action";
                }

                @Override
                public void perform(final UiController uiController, final View view) {
                    if (sMotionEventDownHeldView == null) {
                        throw new AssertionError("Before calling release(), you must call pressAndHold() on a view");
                    }

                    float[] coords = GeneralLocation.CENTER.calculateCoordinates(view);
                    MotionEvents.sendUp(uiController, sMotionEventDownHeldView, coords);
                    sMotionEventDownHeldView = null;
                }
            };
        }
    }
}
