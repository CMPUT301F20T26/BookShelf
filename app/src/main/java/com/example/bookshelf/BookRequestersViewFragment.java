package com.example.bookshelf;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * This fragment acts to receive confirmation from the user to delete a book.
 */

public class BookRequestersViewFragment extends DialogFragment {
    private DialogListener listener;

    //Layout Variables
    private ListView reqestsLv;
    private ArrayAdapter<String> requestsAdapter;
    private ArrayList<String> requestsList;

    //Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notificationCollection = db.collection("notifications");

    String bookId ;


    BookRequestersViewFragment(String bookId) {
        this.bookId = bookId;
    }

    public interface DialogListener {
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof DialogListener) {
            listener = (DialogListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_requesters_view, null);

        reqestsLv = view.findViewById(R.id.requesters_list);

        requestsList = new ArrayList<String>();
        requestsAdapter = new ArrayAdapter<>(getContext(), R.layout.content, requestsList);
        reqestsLv.setAdapter(requestsAdapter);

        notificationCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots){
                    if(snapshot.getData().get("bookID").toString().equals(bookId)
                            && RequestStatus.valueOf(snapshot.getData().get("status").toString()).equals(RequestStatus.PENDING)){
                        String notificationId = snapshot.getId();
                        db.collection("users").document(snapshot.getData().get("requesterID").toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                requestsAdapter.add(task.getResult().getData().get("username").toString());

                                //Clicking the username of someone who's requested a book.
                                reqestsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        //Start a new intent
                                        //Pass in the variable called notificationId to the new intent
                                    }
                                });
                            }
                        });
                    }
                }
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Requests: ").create();
    }
}