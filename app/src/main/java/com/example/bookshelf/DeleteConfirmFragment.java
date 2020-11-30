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
import android.widget.DatePicker;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * This fragment acts to receive confirmation from the user to delete a book.
 */

public class DeleteConfirmFragment extends DialogFragment {
    private DialogListener listener;

    private Integer position;

    DeleteConfirmFragment(int position) {
        this.position = position;
    }

    public interface DialogListener {
        void deleteLongPress(Integer position);
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

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.make_request_fragment_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Are you sure you want to delete this book?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete the current book on long press
                        listener.deleteLongPress(position);
                    }
                }).create();
    }
}