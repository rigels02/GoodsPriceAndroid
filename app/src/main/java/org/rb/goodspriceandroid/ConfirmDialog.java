package org.rb.goodspriceandroid;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by raitis on 15-Feb-17.
 */

public class ConfirmDialog extends DialogFragment {
    private AlertDialog dialog;
    private IConfirmDialogListener mCallback;

    interface IConfirmDialogListener {
        void onConfirmResult(int id);

    }

    public void passArguments(String title,String message){
        Bundle args= new Bundle();
        args.putString("title",title);
        args.putString("message",message);
        this.setArguments(args);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (IConfirmDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IConfirmDialogListener");
        }
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        String title = args.getString("title");
        String msg = args.getString("message");
        final Context ctx = getActivity().getApplicationContext();
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setIcon(android.R.drawable.alert_light_frame);
        builder.setIcon(R.drawable.ic_help_outline_black_24dp);
        if(title !=null){
            builder.setTitle(title);
        }
        if(msg != null){
            builder.setMessage(msg);
        }
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
                //Toast.makeText(ctx, "Ok clicked", Toast.LENGTH_LONG).show();
                mCallback.onConfirmResult(id);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                //Toast.makeText(ctx, "Cancel clicked", Toast.LENGTH_LONG).show();
                mCallback.onConfirmResult(id);
            }
        });
        //builder.setCancelable(false);
        // Create the AlertDialog object and return it


        return builder.create();

    }

    }
