package com.example.bookshelf;

import android.app.Activity;
import android.widget.EditText;

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
import com.robotium.solo.Solo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Testing create account activity
 */
public class SignInTest {
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
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
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
    public void signInWithtestCredentials(){
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

        //Initialize firebase instance
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Initialize Firebase authentication instance
        final FirebaseUser testCreateuser = FirebaseAuth.getInstance().getCurrentUser();

        //Assert user in database
        db.collection("users").whereEqualTo("username", testusername).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Assert.assertTrue(testCreateuser!=null);
                            Assert.assertFalse(task.getResult().isEmpty());
                        }
                    }
                });

        //Log out
        solo.clickOnButton("Sign Out");

        //LogIn with credentials
        solo.waitForActivity(SignInActivity.class);

        solo.enterText((EditText) solo.getView(R.id.signin_user_email), testEmail);
        TextInputLayout testPasswordLayout = (TextInputLayout) solo.getView(R.id.signin_password);
        solo.enterText((EditText) testPasswordLayout.getEditText() , testPassword);
        solo.clickOnButton("Sign In");

        solo.waitForActivity(UserProfileActivity.class);
        final FirebaseUser testSignInUser = FirebaseAuth.getInstance().getCurrentUser();
        Assert.assertTrue(testSignInUser!= null);


        //Delete test credentials from auth and firestore
        db.collection("users").whereEqualTo("username", testusername).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String docId = document.getId();

                            db.collection("users").document(docId).delete();
                            testCreateuser.delete();
                        }
                    }
                });
    }


}
