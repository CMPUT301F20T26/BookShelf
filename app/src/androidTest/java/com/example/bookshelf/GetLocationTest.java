package com.example.bookshelf;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class GetLocationTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<RequestDetailsActivity> rule =
            new ActivityTestRule<RequestDetailsActivity>(RequestDetailsActivity.class,
                    true, true);


    /**
     * Runs before all tests to create instance of solo
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Runs after all tests are finished to ensure all opened activities are closed and finished
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    /**
     * Gets the current activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * Test to confirm the location put in to the map fragment is correct
     */
    @Test
    public void checkRequestDetails() {
        solo.assertCurrentActivity("Wrong Activity", RequestDetailsActivity.class);

        String lat = "53.528329";
        String  lng = "113.528916";

        solo.enterText((EditText) solo.getView(R.id.Lat), lat);
        solo.enterText((EditText) solo.getView(R.id.Long), lng);
        solo.clickOnButton("Get Location");

        EditText Lat = (EditText) solo.getView(R.id.Lat);
        EditText Lng = (EditText) solo.getView(R.id.Long);

        Assert.assertEquals(Lat.getText().toString(), lat);
        Assert.assertEquals(Lng.getText().toString(), lng);

    }

}
