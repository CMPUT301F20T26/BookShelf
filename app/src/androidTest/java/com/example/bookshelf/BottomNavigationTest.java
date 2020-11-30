package com.example.bookshelf;

import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

/**
 * Testing Using the Bottom Navigation menu
 */
public class BottomNavigationTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<CreateAccountActivity> rule =
            new ActivityTestRule<CreateAccountActivity>(CreateAccountActivity.class,
                    true, true);


    /**
     * Runs before all tests to create instance of solo
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
        String testFullname = "firstnames lastnames";
        String testusername = "usernames";
        String testEmail = "firstnames.lastnames@gmail.com";
        String testPhone = "7801234567";
        String testPassword = "password1234";

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
                        if(task.isSuccessful()){
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
     * adds in field for a new user and tests if that user exits in the database.
     * Then deletes the tested user from the database.
     */
    @Test
    public void checkNavigation() throws Exception {
        solo.assertCurrentActivity("Wrong Activity", CreateAccountActivity.class);

        //Test inputs
        String testFullname = "firstnames lastnames";
        String testusername = "usernames";
        String testEmail = "firstnames.lastnames@gmail.com";
        String testPhone = "7801234567";
        String testPassword = "password1234";

        //Enter valid field inputs
        solo.enterText((EditText) solo.getView(R.id.create_account_full_name), testFullname);
        solo.enterText((EditText) solo.getView(R.id.create_account_user_name), testusername);
        solo.enterText((EditText) solo.getView(R.id.create_account_user_email), testEmail);
        solo.enterText((EditText) solo.getView(R.id.create_account_phone_number), testPhone);
        solo.enterText((EditText) solo.getView(R.id.create_account_user_pwd), testPassword);
        solo.clickOnButton("Create Account");

        //Wait for profile page activity to open
        solo.waitForActivity(UserProfileActivity.class);

        //TODO: Check navigation
        //16 different cases for navigation

        // Check Notifications
        View notifications = solo.getView(R.id.notifications_page);
        solo.clickOnView(notifications);
        solo.waitForActivity(UserNotificationsActivity.class);
        solo.assertCurrentActivity("Wrong Activity", UserNotificationsActivity.class);

        // Check Books page
        View myBooks = solo.getView(R.id.books_page);
        solo.clickOnView(myBooks);
        solo.waitForActivity(UserBooksActivity.class);
        solo.assertCurrentActivity("Wrong Activity", UserBooksActivity.class);

        // Check Search Page
        View search = solo.getView(R.id.search_page);
        solo.clickOnView(search);
        solo.waitForActivity(SearchBooksActivity.class);
        solo.assertCurrentActivity("Wrong Activity", SearchBooksActivity.class);

        // Check Profile Page
        View profile = solo.getView(R.id.profile_page);
        solo.clickOnView(profile);
        solo.waitForActivity(UserProfileActivity.class);
        solo.assertCurrentActivity("Wrong Activity", UserProfileActivity.class);

        // Check Interim Maps Page
        View maps = solo.getView(R.id.maps_page);
        solo.clickOnView(maps);
        solo.waitForActivity(RequestDetailsActivity.class);
        solo.assertCurrentActivity("Wrong Activity", RequestDetailsActivity.class);

        deleteUser();

    }


}


