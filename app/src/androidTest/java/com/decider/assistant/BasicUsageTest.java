package com.decider.assistant;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.DataInteraction;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

// GOOD EXAMPLES HERE: https://github.com/android/testing-samples/blob/a1c7572299741697ca9ad4e4d285f158e5fd7562/ui/espresso/CustomMatcherSample/app/src/sharedTest/java/com/example/android/testing/espresso/CustomMatcherSample/HintMatchersTest.java

@RunWith(AndroidJUnit4.class)
public class BasicUsageTest {
    private static final String LOG_TAG = "BasicUsageTest";

    private static DataInteraction optionElementAt(int position) {
        Matcher<View> optionsList = ViewMatchers.withId(R.id.options_list);
        return Espresso.onData(CoreMatchers.anything()).inAdapterView(optionsList).atPosition(position);
    }

    private static DataInteraction optionTextAt(int position) {
        return optionElementAt(position).onChildView(ViewMatchers.withClassName(CoreMatchers.containsString("EditText")));

    }
    private static DataInteraction optionRemoveButtonAt(int position) {
        return optionElementAt(position).onChildView(ViewMatchers.withId(R.id.delete_option_button));
    }

    private static ViewInteraction decideButton() {
        return Espresso.onView(ViewMatchers.withId(R.id.decider_button));
    }

    private static ViewInteraction resultsText() {
        return Espresso.onView(ViewMatchers.withId(R.id.resultsText));
    }

    private void showAppState() {
        ActivityScenario<MainActivity> scenario = rule.getScenario();
        scenario.onActivity(activity -> Log.i(
            LOG_TAG,
            String.format("Current state: %s", activity.extractAppState())
        ));
    }

    @Before
    public void setup() {
        Log.i(LOG_TAG, "Setting up test");
        AppState appState = new AppState();
        ActivityScenario<MainActivity> scenario = rule.getScenario();
        scenario.onActivity(activity -> {
            activity.applyAppState(appState);
            activity.showOptionsListView();
        });
    }

    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testMainFlow() {
        optionTextAt(0).perform(ViewActions.typeText("TEST"));
        optionRemoveButtonAt(1).perform(ViewActions.click());
        decideButton().perform(ViewActions.click());
        resultsText().check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString("TEST"))));
    }

    // TODO: write a test that confirms data can be reconstructed when starting back up
    // TODO: tests for handling corrupted preferences

}
