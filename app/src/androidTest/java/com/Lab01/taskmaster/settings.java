package com.Lab01.taskmaster;


import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.Lab01.taskmaster.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class settings {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void settings() {
        ViewInteraction materialButton = onView(
allOf(withId(R.id.settingsButton), withText("Settings"),
childAtPosition(
childAtPosition(
withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
0),
3),
isDisplayed()));
        materialButton.perform(click());
        
        ViewInteraction appCompatEditText = onView(
allOf(withId(R.id.username),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
1),
isDisplayed()));
        appCompatEditText.perform(replaceText("farah"), closeSoftKeyboard());
        
        ViewInteraction materialButton2 = onView(
allOf(withId(R.id.button), withText("Save"),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
3),
isDisplayed()));
        materialButton2.perform(click());
        
        ViewInteraction materialButton3 = onView(
allOf(withId(R.id.settingsBackButton), withText("Back"),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
2),
isDisplayed()));
        materialButton3.perform(click());
        }
    
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }
