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

/**
 * Edit Profile fragment for editing user information.
 */
public class EditProfileFragment extends DialogFragment {
    //Layout Variables
    private EditText fullnameEt;
    private EditText phoneEt;
    private OnFragmentInteractionListener listener;

    //User class instantiation
    private UserInfo userInfo;

    /**
     * Instantiates a new Edit profile fragment with a UserInfo Object
     * @param user the user
     */
    public EditProfileFragment(UserInfo user){
        this.userInfo = user;
    }

    /**
     * The interface On fragment interaction listener.
     */
    public interface OnFragmentInteractionListener {
        /**
         * On ok pressed.
         * @param userInfo the user info
         */
        void onOkPressed(UserInfo userInfo);
    }

    /**
     * Attaching the context to the fragment.
     * @param context
     */
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

    /**
     * Create Dialog box.
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //Setting the view
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_profile_fragment, null);

        //Assigning Layout variables
        fullnameEt = view.findViewById(R.id.et_fullname);
        phoneEt = view.findViewById(R.id.et_phone);

        //Build Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        //Setting layout text so user knows where to edit.
        fullnameEt.setText(userInfo.getFullname());
        phoneEt.setText(userInfo.getPhone());

        return builder
                .setView(view)
                .setTitle("Edit Profile")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //on ok pressed we want to go back to profile page after updating firebase
                        String editedFullname = fullnameEt.getText().toString();
                        String editedPhone = phoneEt.getText().toString();
                        //TODO: Add some checks for data here.

                        //Set user info data
                        userInfo.setFullname(editedFullname);
                        userInfo.setPhone(editedPhone);

                        //Call on Pressed on UserProfileActivity
                        listener.onOkPressed(userInfo);
                    }
                }).create();
    }
}