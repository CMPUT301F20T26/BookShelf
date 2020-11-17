package com.example.bookshelf;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/**
 * The type Firebase helper.
 *
 * Contains methods for common firebase operations.
 */
public class FirebaseHelper {
    private final FirebaseFirestore db;

    /**
     * Instantiates a new Firebase helper.
     */
    FirebaseHelper()
    {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Get a document snapshot from Firebase.
     *
     * @param collectionPath the collection path
     * @param docID          the doc id
     * @return the document snapshot
     */
    DocumentSnapshot get(String collectionPath, String docID){
        final DocumentSnapshot[] result = new DocumentSnapshot[1];
        db.collection(collectionPath).document(docID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            result[0] = documentSnapshot;
                        }}});
        return result[0];
    }

    /**
     * Add a new document to a collection.
     *
     * @param collectionPath the collection path
     * @param newDocID       the new doc id
     * @param obj            the document's data
     */
    void add(String collectionPath, String newDocID, Map<String, Object> obj) {
        db.collection(collectionPath).document(newDocID).set(obj);
        // TODO: listeners
    }

    /**
     * Edit a firebase document field.
     *
     * @param collectionPath the collection path
     * @param docID          the doc id
     * @param fieldID        the field id
     * @param data           the data
     */
    void edit(String collectionPath, String docID, String fieldID, Object[] data) {
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
}
