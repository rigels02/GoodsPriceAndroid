package org.rb.goodspriceandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by raitis on 20-Feb-17.
 */

public   class SimpleInfoDialog {

    public final static int INFO= 1;
    public final static int ERROR= 1;

    private AlertDialog.Builder builder;

    private AlertDialog dlg;


    public SimpleInfoDialog(final Activity activity, int type, String title, String msg){
        builder = new AlertDialog.Builder(activity);

        // Get the layout inflater
         //LayoutInflater inflater =cntx.getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        //builder.setView(inflater.inflate(R.layout.add_modify_dialog_1, null));
        if(type == ERROR){
            builder.setIcon(R.drawable.ic_error_black_24dp);
        }else {
            builder.setIcon(R.drawable.ic_info_outline_black_24dp);
        }
        builder.setTitle(title);
        builder.setMessage(msg);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dlg.dismiss();

            }
        });

        dlg=builder.create();
       dlg.show();
    }



}
