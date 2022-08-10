package com.decider.assistant;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.onData;
import androidx.test.espresso.DataInteraction;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.action.ViewActions.typeText;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

// TODO: linting, maybe something that comes with IDE

// GOOD EXAPMLES HERE: https://github.com/android/testing-samples/blob/a1c7572299741697ca9ad4e4d285f158e5fd7562/ui/espresso/CustomMatcherSample/app/src/sharedTest/java/com/example/android/testing/espresso/CustomMatcherSample/HintMatchersTest.java

@RunWith(AndroidJUnit4.class)
public class BasicUsageTest {
    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    private DataInteraction optionElementAt(int position) {
        return onData(anything()).inAdapterView(withId(R.id.options_list)).atPosition(position);
    }

    private DataInteraction optionTextAt(int position) {
        return optionElementAt(position).onChildView(withClassName(containsString("EditText")));

    }
    private DataInteraction optionRemoveButtonAt(int position) {
        return optionElementAt(position).onChildView(withId(R.id.delete_option_button));
    }

    private ViewInteraction decideButton() {
        return onView(withId(R.id.decider_button));
    }

    private ViewInteraction resultsText() {
        return onView(withId(R.id.resultsText));
    }

    @Test
    public void testMainFlow() {
        optionTextAt(0).perform(typeText("TEST"));
        optionRemoveButtonAt(1).perform(click());
        decideButton().perform(click());
        resultsText().check(matches(withText(containsString("TEST"))));
    }
}
