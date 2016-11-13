package com.task.krith.taskreminder;

import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.task.krith.taskreminder.Activity.TaskList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CreateTaskTest {
    @Rule
    public final ActivityTestRule<TaskList> taskListActivityTestRule = new ActivityTestRule<>(TaskList.class);

    @Test
    public void shouldBeAbleToAddTasksAndHaveThemVisibleOnTheRecyclerView() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.task_name)).perform(click());
        onView(withId(R.id.task_name)).perform(typeText("Task 5"));
        onView(withId(R.id.description)).perform(typeText("Sample description 5"), closeSoftKeyboard());
        onView(withId(R.id.save_btn)).perform(click());
    }
}