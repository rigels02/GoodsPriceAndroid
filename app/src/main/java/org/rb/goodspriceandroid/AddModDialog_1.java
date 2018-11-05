package org.rb.goodspriceandroid;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import org.rb.goodspriceandroid.good.GoodContent;
import org.rb.goodspriceandroid.model.Good;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by raitis on 14-Feb-17.
 */

public class AddModDialog_1 extends DialogFragment {

    public static final int MODIFY = 2 ;
    public static final int ADD = 1 ;
    private EditText etDate;
    private View v;
    private Date date;
    private TextWatcher dateWacher;
    private IAddModifyDialogListener mCallback;
    private EditText etName;
    private EditText etShop;
    private EditText etPrice;
    private EditText etDiscount;
    private Good good;
    private int toDO;
    private AlertDialog adialog;
    private String title;
    private TextWatcher nameWacher;
    private TextWatcher shopWacher;
    private TextWatcher priceWacher;
    private TextWatcher discountWacher;
    private ImageButton iNameLstbtn;
    //private ChoiceListDialog choiceNameDlg;
    private ImageButton iShopLstbtn;
    //private ChoiceListDialog choiceShopDlg;


    public void passParams(Good good, int action) {
        if(action!=MODIFY && action != ADD) {
            throw new IllegalArgumentException("Incorrect action argument!");
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable("good",good);
        bundle.putInt("todo",action);
        this.setArguments(bundle);
    }


    public interface IAddModifyDialogListener {

        void addModifyDialogModifyAccepted(Good good);
        void addModifyDialogAddAccepted(Good good);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        good = (Good) getArguments().getParcelable("good");
        toDO = getArguments().getInt("todo");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context ctx = getActivity().getApplicationContext();
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v=inflater.inflate(R.layout.add_modify_dialog_1, null));
        etDate=(EditText) v.findViewById(R.id.eTxtDate);
        etName= (EditText)v.findViewById(R.id.eTxtName);
        etShop = (EditText)v.findViewById(R.id.eTxtShop);
        etPrice = (EditText)v.findViewById(R.id.eTxtPrice);
        etDiscount = (EditText)v.findViewById(R.id.eTxtDiscount);

        if(good != null){

            fillInFields(good);
        }

        setupWatchers();

        //builder.setMessage("Message");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
                //!!overwritten in new onShowListener , see below
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
               // Toast.makeText(ctx, "Cancel clicked", Toast.LENGTH_SHORT).show();
            }
        });

        setupNamesListImageButton();
        setupShopsListImageButton();

        //builder.setCancelable(false);
        // Create the AlertDialog object and return it
        adialog= builder.create();
        adialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {
                Button posButton = adialog.getButton(AlertDialog.BUTTON_POSITIVE);
                posButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //if(isValidDate(etDate.getText().toString())){
                            //Toast.makeText(ctx, "Valid date= "+etDate.getText().toString(), Toast.LENGTH_LONG).show();
                            Good ngood = new Good();
                            try {
                                fieldsToGoodFields(ngood);
                            }catch (Exception ex){
                                Toast.makeText(ctx,ex.getMessage(),Toast.LENGTH_LONG).show();
                                return;
                            }
                            dialog.dismiss();//??maybe last stm??
                            if(toDO == MODIFY) {
                                mCallback.addModifyDialogModifyAccepted(ngood);
                            }else{
                                mCallback.addModifyDialogAddAccepted(ngood);
                            }

                        //}
                    }
                });
            }
        });
        if(title!=null) {
            adialog.setTitle(title);

        }

        return adialog;

    }

    private void setupNamesListImageButton() {
        final Activity thCtx = getActivity();
        iNameLstbtn = (ImageButton) v.findViewById(R.id.name_list_btn);
        iNameLstbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> lst = getGoodsNamesList();
                String[] arrList = new String[lst.size()];
                lst.toArray(arrList);
                if(arrList.length==0){
                    Toast.makeText(thCtx,"Empty selection list",Toast.LENGTH_LONG).show();
                    return;
                }
                new ChoiceListDialog(thCtx, "Select Name", arrList) {
                    @Override
                    public void onConfirmClicked(String inputTxt, int selectedIdx) {
                        etName.setText(inputTxt);
                        etName.setError(null);
                    }
                };

            }
        });
    }

    private void setupShopsListImageButton(){
        final Activity thCtx = getActivity();
        iShopLstbtn = (ImageButton) v.findViewById(R.id.shop_list_btn);
        iShopLstbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> lst = getGoodsShopsList();
                String[] arrList = new String[lst.size()];
                lst.toArray(arrList);
                if(arrList.length==0){
                    Toast.makeText(thCtx,"Empty selection list",Toast.LENGTH_LONG).show();
                    return;
                }
                 new ChoiceListDialog(thCtx, "Select Name", arrList) {
                    @Override
                    public void onConfirmClicked(String inputTxt, int selectedIdx) {
                        etShop.setText(inputTxt);
                        etShop.setError(null);
                    }
                };

            }
        });
    }

    public void setTitle(String title){
          this.title = title;

    }
    private void enablePositiveButton(boolean b) {
        ((AlertDialog)adialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(b);
    }


    private boolean isValidDate(String value) {
        date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            etDate.setError("OPS!!: Enter a valid date: dd-MM-YYYY");
        }
        return date != null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (IAddModifyDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IAddModifyDialogListener");
        }
    }


    ///////////////TOOLS//////////////////////////////////////
    void setupWatchers(){
        dateWacher= new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if( !isValidDate(etDate.getText().toString())){
                    etDate.setError("Enter a valid date: dd-MM-YYYY");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        nameWacher= new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    checkOnValidName();
                }catch (Exception ex){
                    return;

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        shopWacher= new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    checkOnValidShop();
                }catch (Exception ex){
                    return;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        priceWacher= new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    checkOnValidPrice();
                }catch (Exception ex){
                    return;
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        discountWacher= new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    checkOnValidDiscount();
                }catch (Exception ex){
                    return;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        etName.addTextChangedListener(nameWacher);
        etShop.addTextChangedListener(shopWacher);
        etDate.addTextChangedListener(dateWacher);
        etPrice.addTextChangedListener(priceWacher);
        etDiscount.addTextChangedListener(discountWacher);

    }

    private void checkOnValidDiscount() throws Exception {
        String msg = "Discount must be > 0.0";
        if( etDiscount.getText().toString().isEmpty()){
            etDiscount.setError(msg);
            throw new Exception(msg);
        }
        double d;
        try {
            d = Double.parseDouble(etDiscount.getText().toString());

        }catch (Exception ex){
            etDiscount.setError(msg="Discount format error");
            throw new Exception(msg);
        }
        if(d < 0.0){
            etDiscount.setError(msg);
            throw new Exception(msg);
        }
    }

    private void checkOnValidPrice() throws Exception {
        String msg = "Price must be > 0.0";
        if( etPrice.getText().toString().isEmpty()){
            etPrice.setError(msg);
            throw new Exception(msg);
        }
        double d;
        try {
            d = Double.parseDouble(etPrice.getText().toString());

        }catch (Exception ex){
            etPrice.setError(msg="Price format error");
            throw new Exception(msg);
        }
        if(d <= 0.0){
            etPrice.setError(msg);
            throw new Exception(msg);
        }
    }

    private void checkOnValidShop() throws Exception {
        if( etShop.getText().toString().length()<1){
            String msg = "Empty Shop not allowed";
            etShop.setError(msg);
            throw new Exception(msg);
        }
    }

    private void checkOnValidName() throws Exception {
        if( etName.getText().toString().length()<1){
            String msg= "Empty Name not allowed";
            etName.setError(msg);
          throw new Exception(msg);
        }
    }

    void fillInFields(Good good) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String tdate = sdf.format(good.getCdate());
        etDate.setText(tdate);
        etName.setText(good.getName());
        etShop.setText(good.getShop());

            etPrice.setText(String.valueOf(good.getPrice()));
            etDiscount.setText(String.valueOf(good.getDiscount()));

        //setupNameCB(good.getName(), names);
        //setupShopsCB(good.getShop(), shops);

    }
    void fieldsToGoodFields(Good ngood)throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        if( ! isValidDate(etDate.getText().toString())){
            throw new Exception("Bad date format!");

        }

        checkOnValidName();
        checkOnValidShop();
        checkOnValidPrice();
        checkOnValidDiscount();
        ngood.setCdate(sdf.parse(etDate.getText().toString()));
        ngood.setName(etName.getText().toString());
        ngood.setShop(etShop.getText().toString());
        ngood.setPrice(Double.parseDouble(etPrice.getText().toString()));
        ngood.setDiscount(Double.parseDouble(etDiscount.getText().toString()));

    }

    /**
     * Build names list from GoodContent.
     * GoodContent must keep info about Good's Name and Shop fields
     * @return
     */
    private List<String> getGoodsNamesList(){

        return GoodContent.getNamesList();
    }

    /**
     * Build Shops list from GoodContent.
     * GoodContent must keep info about Good's Name and Shop fields
     * @return
     */
    private List<String> getGoodsShopsList(){

        return GoodContent.getShopsList();
    }
}
