package com.example.bookshelf;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.Text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SearchBooksActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<SearchBooksActivity> rule =
            new ActivityTestRule<>(SearchBooksActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * starts SearchBooksActivity for testing purposes
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * Assumes Alone Forever: The Singles Collection is the highest in alphabetical order, and checks for its presence by searching with the keyword "The".
     * Then, clicks on the item and verifies book info, and further verifies owner info after clicking on the owner username.
     */
    @Test
    public void checkSearch(){
        solo.assertCurrentActivity("Wrong Activity", SearchBooksActivity.class);
        solo.enterText((EditText) solo.getView(R.id.search_bar), "The");
        solo.clickOnButton("GO");
        assertTrue(solo.waitForText("Alone Forever: The Singles Collection")); // assumes this is the top of the list, which it is in the original database
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity", BookActivity.class);
        assertTrue(solo.waitForText("cvijovic")); //for this specific item we see the username, and click on it to pull up the profile
        solo.clickOnView(solo.getView(R.id.owner_text));
        solo.assertCurrentActivity("Wrong Activity", UserActivity.class);
        assertTrue(solo.waitForText("Petar")); //for the given username we have the first name displayed on the user profile page.

    }

}
