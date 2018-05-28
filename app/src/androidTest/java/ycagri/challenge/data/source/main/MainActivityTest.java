package ycagri.challenge.data.source.main;

import android.content.pm.ActivityInfo;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ycagri.challenge.R;
import ycagri.challenge.main.DetailFragment;
import ycagri.challenge.main.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by vayen01 on 1.12.2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    /**
     * {@link IntentsTestRule} is an {@link ActivityTestRule} which inits and releases Espresso
     * Intents before and after each test run.
     * <p>
     * <p>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Prepare your test fixture for this test. In this case we register an IdlingResources with
     * Espresso. IdlingResource resource is a great way to tell Espresso when your app is in an
     * idle state. This helps Espresso to synchronize your test actions, which makes tests significantly
     * more reliable.
     */
    @Before
    public void registerIdlingResource() {
        IdlingRegistry.getInstance().register(mMainActivityTestRule.getActivity().getCountingIdlingResource());
    }

    @Test
    public void checkFragmentsLandscape() {
        mMainActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        assertNotNull(mMainActivityTestRule.getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container));

        onView(withId(R.id.rv_master_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return null;
                    }

                    @Override
                    public String getDescription() {
                        return null;
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        view.performClick();
                    }
                }))
                .check((view, noViewFoundException) -> assertNotNull(mMainActivityTestRule.getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_detail_container)));
    }

    @Test
    public void checkFragmentsPortrait() {
        mMainActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        assertNotNull(mMainActivityTestRule.getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container));

        onView(withId(R.id.rv_master_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return null;
                    }

                    @Override
                    public String getDescription() {
                        return null;
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        view.performClick();
                    }
                }))
                .check((view, noViewFoundException) -> {
                    assertTrue(mMainActivityTestRule.getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof DetailFragment);
                });
    }

    @After
    public void clean() {
        IdlingRegistry.getInstance().unregister(mMainActivityTestRule.getActivity().getCountingIdlingResource());
    }
}
