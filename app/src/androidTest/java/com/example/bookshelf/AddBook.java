package com.example.bookshelf;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
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
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Testing Searching for books and users
 */
public class AddBook {
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
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
        deleteUser();
    }

    /**
     * Test out the add book, edit book and delete book functionalities
     *
     * @throws Exception
     */
    @Test
    public void changeBook() throws Exception {
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

        View books = solo.getView(R.id.books_page);
        solo.clickOnView(books);
        solo.waitForActivity(UserBooksActivity.class);

        // Testing out the add book functionality
        solo.clickOnButton("Add");
        solo.enterText((EditText) solo.getView(R.id.Title_add), "Sample");
        solo.enterText((EditText) solo.getView(R.id.Author_add), "Sample, Sample");
        solo.enterText((EditText) solo.getView(R.id.ISBN_add), "1231231231233");
        solo.enterText((EditText) solo.getView(R.id.Description_add), "Hey this is a sample book");
        solo.clickOnButton("OK");

        ListView results = (ListView) solo.getView(R.id.Book_list);
        ArrayList<TextView> resultDetails = solo.clickInList(0,0);

        Assert.assertEquals("Sample", resultDetails.get(0).getText().toString());
        Assert.assertEquals("Sample, Sample", resultDetails.get(1).getText().toString());
        Assert.assertEquals("username", resultDetails.get(2).getText().toString());
        Assert.assertEquals("Hey this is a sample book", resultDetails.get(3).getText().toString());

        // Testing out the edit book functionality
        solo.clickInList(0,0);
        solo.clickOnButton("Edit");
        solo.clearEditText((EditText) solo.getView(R.id.Title_add));
        solo.enterText((EditText) solo.getView(R.id.Title_add), "Sample1");
        solo.clearEditText((EditText) solo.getView(R.id.Author_add));
        solo.enterText((EditText) solo.getView(R.id.Author_add), "Sample1, Sample1");
        solo.clearEditText((EditText) solo.getView(R.id.Description_add));
        solo.enterText((EditText) solo.getView(R.id.Description_add), "Hey this is an edited sample book");
        solo.clickOnButton("OK");

        resultDetails = solo.clickInList(0,0);
        Assert.assertEquals("Sample1", resultDetails.get(0).getText().toString());
        Assert.assertEquals("Sample1, Sample1", resultDetails.get(1).getText().toString());
        Assert.assertEquals("username", resultDetails.get(2).getText().toString());
        Assert.assertEquals("Hey this is an edited sample book", resultDetails.get(3).getText().toString());

        // Testing out the delete book functionality
        solo.clickInList(0,0);
        solo.clickOnButton("Delete");
        results = (ListView) solo.getView(R.id.Book_list);
        Assert.assertEquals(1, ((ListView) solo.getView(R.id.Book_list)).getAdapter().getCount());
        //Assert.assertEquals(Book.class, results.getAdapter().getItem(0).getClass());

        // Tell the test to expect an exception to be thrown because of the next statement
        //exception.expect(IndexOutOfBoundsException.class);

        // Try to access a book that has already been deleted through the array adapter
        // , causing the adapter to throw an IndexOutOfBounds exception which is expected by the
        // previous statement, hence test passes
        //Assert.assertEquals(" ", results.getAdapter().getItem(0).toString());

//        deleteUser();

    }
}
