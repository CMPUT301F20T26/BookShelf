package com.example.bookshelf;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class EditProfileFragment extends DialogFragment {
    //Layout Variables
    private EditText fullnameEt;
    private EditText emailEt;
    private EditText phoneEt;
    private OnFragmentInteractionListener listener;

    //User class instantiation
    private User userInfo;
    public EditProfileFragment(UserInfo user){
        this.userInfo = user;
    }

    public interface OnFragmentInteractionListener {
        void onOkPressed(String fullname, String email, String phone);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_profile_fragment, null);

        fullnameEt = view.findViewById(R.id.et_fullname);
        emailEt = view.findViewById(R.id.et_phone);
        phoneEt = view.findViewById(R.id.et_email);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        fullnameEt.setText(this.userInfo.getFullname());
        emailEt.setText(this.userInfo.getEmail());
        phoneEt.setText(this.userInfo.getPhone());

        return builder
                .setView(view)
                .setTitle("Edit Profile")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //on ok pressed we want to go back to profile page after updating firebase
                        String editedFullname = fullnameEt.getText().toString();
                        String editedEmail = emailEt.getText().toString();
                        String editedPhone = phoneEt.getText().toString();
                        //TODO: Add some checks for data here.

                        listener.onOkPressed(this.userInfo);
                    }
                }).create();
    }
}