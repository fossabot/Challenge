package ycagri.challenge;

import android.content.pm.ActivityInfo;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ycagri.challenge.activity.MainActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

/**
 * Test code for {@link MainActivity}.
 *
 * @author ycagri
 * @since 05.04.2017
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Test
    public void checkContainers() {
        if (mActivityRule.getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
            onView(withId(R.id.fragment_detail_container)).check(matches(isDisplayed()));
        } else if (mActivityRule.getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
            onView(withId(R.id.fragment_detail_container)).check(doesNotExist());
        }
    }

    @Test
    public void checkListView() {
        onData(anything()).inAdapterView(withId(R.id.lv_master_list)).atPosition(0).perform(click());
        onView(withId(R.id.map_fragment_container)).check(matches(isDisplayed()));
        onView(withId(R.id.vp_venue_photos)).check(matches(isDisplayed()));
    }
}
