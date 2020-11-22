package com.example.bookshelf;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.text.RandomStringGenerator;

import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

/**
 * The type Firebase helper.
 *
 * Contains methods for common firebase operations.
 */
public class FirebaseHelper {
    private final FirebaseFirestore db;

    public interface IHelper {
        void onSuccess(Object o);
        void onFailure(Object o);
    }

    /**
     * Instantiates a new Firebase helper.
     */
    FirebaseHelper()
    {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Gets a document snapshot from Firebase, and returns it through the listener interface.
     *
     * @param collectionPath the collection path
     * @param docID          the doc id
     */
    void get(String collectionPath, String docID, final IHelper listener){
        final DocumentSnapshot[] result = new DocumentSnapshot[1];
        db.collection(collectionPath).document(docID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            result[0] = documentSnapshot;
                            listener.onSuccess(result[0]);
                        }}});
    }

    /**
     * Add a new document to a collection, returning the document ID.
     *
     * @param collectionPath the collection path
     * @param obj            the document's data
     */
    String add(String collectionPath, Map<String, Object> obj) {
        // first randomly generate a document ID
        // we do this offline so the method can return quickly
        final String docID = RandomStringUtils.randomAlphanumeric(20);

        // set the fields of the document in Firebase
        db.collection(collectionPath).document(docID).set(obj);

        // return the randomly generated ID
        return docID;
    }

    /**
     * Edit a firebase document field.
     *
     * @param collectionPath the collection path
     * @param docID          the doc id
     * @param fieldID        the field id
     * @param data           the data
     */
    void edit(String collectionPath, String docID, String fieldID, Object data) {
        DocumentReference doc = db.collection(collectionPath).document(docID); // TODO: doc not found
        doc.update(fieldID, data);
    }

    /**
     * Append values to an array field in a firebase document.
     *
     * @param collectionPath the collection path
     * @param docID          the doc id
     * @param fieldID        the field id
     * @param data           the data
     */
    void append(String collectionPath, String docID, String fieldID, Object[] data) {
        DocumentReference doc = db.collection(collectionPath).document(docID); // TODO: doc not found
        doc.update(fieldID, FieldValue.arrayUnion(data));
    }


    /**
     * Gets user id String from username, passing the result through an IHelper listener.
     *
     * @param username the username
     * @param listener the callback listener
     */
    void getUserID(String username, final IHelper listener) {
        final String[] uid = new String[1];
        db.collection("users").whereEqualTo("username", username).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                uid[0] = document.getId();
                            }
                            listener.onSuccess(uid[0]);
                        }
                        // todo: on failure
                    }
                });
    }


    /**
     * Gets app user id.
     *
     * @return the app user id
     */
    String getAppUserID() {
        FirebaseAuth userAuth = FirebaseAuth.getInstance();
        FirebaseUser authenticatedUser = userAuth.getCurrentUser();
        return authenticatedUser.getUid();
    }


}
