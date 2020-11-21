package com.example.bookshelf;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Testing Searching for books and users
 */
public class SearchTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<CreateAccountActivity> rule =
            new ActivityTestRule<CreateAccountActivity>(CreateAccountActivity.class,
                    true, true);


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
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Search for a book by searching for the owners username
     *
     * @throws Exception
     */
    @Test
    public void searchBookByOwner() throws Exception{
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

        View search = solo.getView(R.id.search_page);
        solo.clickOnView(search);
        solo.waitForActivity(SearchBooksActivity.class);

        solo.enterText((EditText) solo.getView(R.id.search_bar), "adekunle");
        solo.clickOnView((Button) solo.getView(R.id.search_button));

        ListView results = (ListView) solo.getView(R.id.search_results);
        ArrayList<TextView> resultDetails = solo.clickInList(0,0);

        Assert.assertEquals("Beach Safari", resultDetails.get(0).getText().toString());
        Assert.assertEquals("Witzel, Mawil", resultDetails.get(1).getText().toString());
        Assert.assertEquals("adekunle", resultDetails.get(2).getText().toString());
        deleteUser();
    }

    /**
     * Search for a book by searching for the owners username
     *
     * @throws Exception
     */
    @Test
    public void searchBookByName() throws Exception {
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

        solo.assertCurrentActivity("Wrong Activity", UserProfileActivity.class);

        // Navigate to the search page
        View search = solo.getView(R.id.search_page);
        solo.clickOnView(search);
        solo.waitForActivity(SearchBooksActivity.class);

        // Search for Beach Safari book and click on it to get the array list of its details
        solo.enterText((EditText) solo.getView(R.id.search_bar), "Beach Safari");
        solo.clickOnView((Button) solo.getView(R.id.search_button));

        ListView results = (ListView) solo.getView(R.id.search_results);
        ArrayList<TextView> resultDetails = solo.clickInList(0, 0);

        // Ensure details are correct
        Assert.assertEquals("Beach Safari", resultDetails.get(0).getText().toString());
        Assert.assertEquals("Witzel, Mawil", resultDetails.get(1).getText().toString());
        Assert.assertEquals("adekunle", resultDetails.get(2).getText().toString());
        deleteUser();
    }

}
