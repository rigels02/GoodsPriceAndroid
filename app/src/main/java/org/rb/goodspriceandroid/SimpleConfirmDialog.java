package org.rb.goodspriceandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by raitis on 20-Feb-17.
 */

public abstract  class SimpleConfirmDialog {

    private AlertDialog.Builder builder;

    private AlertDialog dlg;


    protected int selectedIdx=-1;

    public SimpleConfirmDialog(final Activity activity, String title, String msg){
        builder = new AlertDialog.Builder(activity);

        // Get the layout inflater
         //LayoutInflater inflater =cntx.getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        //builder.setView(inflater.inflate(R.layout.add_modify_dialog_1, null));
        builder.setIcon(R.drawable.ic_info_outline_black_24dp);
        builder.setTitle(title);
        builder.setMessage(msg);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dlg.dismiss();

            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //String txt="";
                onConfirmClicked();


            }


        });
        dlg=builder.create();
       dlg.show();
    }


    public abstract void onConfirmClicked() ;

}
