package com.example.bookshelf;

import android.app.Activity;
import android.view.View;
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
import org.junit.rules.ExpectedException;

public class GetLocationTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<CreateAccountActivity> rule =
            new ActivityTestRule<CreateAccountActivity>(CreateAccountActivity.class,
                    true, true);

    // Rule for exception testing
    @Rule
    public ExpectedException exception= ExpectedException.none();


    /**
     * Runs before all tests to create instance of solo
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }


    /**
     * Helper function to delete the user that was just created after testing is finished. Will be
     * called in Tear Down function
     * @throws Exception
     */
    public void deleteUser() throws Exception {
        String testFullname = "firstname lastname";
        String testusername = "username";
        final String testEmail = "firstname.lastname@gmail.com";
        String testPhone = "7803403052";
        final String testPassword = "password123";

        //Initialize firebase instance
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Initialize Firebase authentication instance
        final FirebaseUser testuser = FirebaseAuth.getInstance().getCurrentUser();

        //Asset user in database and current user isn't empty
        //Then delete user created from the database and the user authentication
        db.collection("users").whereEqualTo("username", testusername).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Assert.assertNotNull(testuser);
                            Assert.assertFalse(task.getResult().getDocuments().isEmpty());
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String docId = document.getId();
                                db.collection("users").document(docId).delete();
                                testuser.delete();
                                FirebaseAuth authenticatedUser = FirebaseAuth.getInstance();
                                authenticatedUser.signOut();
                            }
                        }
                    }
                });
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
    public void checkRequestDetails() throws Exception {
        solo.assertCurrentActivity("Wrong Activity", CreateAccountActivity.class);

        //Test inputs
        String testFullname = "firstname lastname";
        String testusername = "username";
        final String testEmail = "firstname.lastname@gmail.com";
        String testPhone = "7803403052";
        final String testPassword = "password123";

        //Enter valid field inputs
        solo.enterText((EditText) solo.getView(R.id.create_account_full_name), testFullname);
        solo.enterText((EditText) solo.getView(R.id.create_account_user_name), testusername);
        solo.enterText((EditText) solo.getView(R.id.create_account_user_email), testEmail);
        solo.enterText((EditText) solo.getView(R.id.create_account_phone_number), testPhone);
        solo.enterText((EditText) solo.getView(R.id.create_account_user_pwd), testPassword);
        solo.clickOnButton("Create Account");

        //Wait for profile page activity to open
        solo.waitForActivity(UserProfileActivity.class);

        View maps = solo.getView(R.id.maps_page);
        solo.clickOnView(maps);
        solo.waitForActivity(RequestDetailsActivity.class);
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

        deleteUser();
    }

}
