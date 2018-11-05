package org.rb.goodspriceandroid;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by raitis on 14-Feb-17.
 */

public class InfoDialog extends DialogFragment {
    public final static int INFO = 1;
    public final static int ERROR = 2;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args= getArguments();
        String title = args.getString("title");
        String msg = args.getString("message");
        int type = args.getInt("type");
        final Context ctx = getActivity().getApplicationContext();
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if(type == INFO){
            builder.setTitle("Info");
            builder.setIcon(R.drawable.ic_info_outline_black_24dp);
        }
        if(type == ERROR){
            builder.setTitle("ERROR");
            builder.setIcon(R.drawable.ic_error_black_24dp);
        }
        if(msg !=null) {
            builder.setMessage(msg);
        }
        if(title !=null){
            builder.setTitle(title);
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
                //Toast.makeText(ctx, "Fire clicked", Toast.LENGTH_LONG).show();
            }
        });

        //builder.setCancelable(false);
        // Create the AlertDialog object and return it
        return builder.create();

    }


}
