package org.rb.goodspriceandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

/**
 * Created by raitis on 20-Feb-17.
 */

public abstract  class ChoiceListDialog {

    private AlertDialog dlg;

    protected String selectedItem;
    protected int selectedIdx=-1;

    public ChoiceListDialog(final Activity cntx, String title, final String[] list){
        AlertDialog.Builder builder = new AlertDialog.Builder(cntx);

        // Get the layout inflater
         //LayoutInflater inflater =cntx.getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        //builder.setView(inflater.inflate(R.layout.add_modify_dialog_1, null));

        builder.setTitle(title);
        //builder.setMessage(dispTxt);
        builder.setSingleChoiceItems(list,-1,new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {
                //System.out.println("Item selected: "+which);
                selectedItem= list[which];
                selectedIdx= which;
            }
        });
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
                onConfirmClicked(selectedItem,selectedIdx);
                //dlg.dismiss();
                Log.i("InputDialog","dismiss...");

            }


        });
        dlg= builder.create();
       dlg.show();
    }


    public abstract void onConfirmClicked(String inputTxt,int selectedIdx) ;

}
