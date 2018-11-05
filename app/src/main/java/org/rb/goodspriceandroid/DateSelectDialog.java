package org.rb.goodspriceandroid;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by raitis on 14-Feb-17.
 */

public class DateSelectDialog extends DialogFragment {

    private View v;
    private Date startDate;
    private Date endDate;
    //private TextWatcher dateWacher;
    //private IAddModifyDialogListener mCallback;
    private AlertDialog adialog;
    private String title;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private IDateSelected mCallback;
    private TextView tv1;
    private TextView tv2;


    interface IDateSelected {

        void  dateSelectedCallBack(Date startDate, Date endDate);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context ctx = getActivity().getApplicationContext();
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v;
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v=inflater.inflate(R.layout.date_select_dialog, null));
        startDatePicker = v.findViewById(R.id.datePickerStart);
        startDatePicker.setCalendarViewShown(false);
        endDatePicker = v.findViewById(R.id.datePickerEnd);
        endDatePicker.setCalendarViewShown(false);
        tv1= v.findViewById(R.id.textView);
        tv2= v.findViewById(R.id.textView2);
        Button accBtn = v.findViewById(R.id.btnAccepted);
        Button cBtn = v.findViewById(R.id.btnCancel);
        cBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adialog.dismiss();
            }
        });
        accBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int dd=startDatePicker.getDayOfMonth();
                int mm= startDatePicker.getMonth();
                int yy= startDatePicker.getYear()-1900;
                startDate= new Date(yy,mm,dd);
                dd=endDatePicker.getDayOfMonth();
                mm= endDatePicker.getMonth();
                yy= endDatePicker.getYear()-1900;
                endDate= new Date(yy,mm,dd);
                if(startDate.after(endDate)){
                    tv1.setError("startDate > endDate!");
                    return;
                }
                adialog.dismiss();
                mCallback.dateSelectedCallBack(startDate,endDate);
            }
        });

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        final String tv1Promt= tv1.getText().toString();
        final String tv2Promt= tv2.getText().toString();
        startDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                //Log.d("Date", "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth);
                tv1.setText(tv1Promt+dayOfMonth+"-"+(month+1)+"-"+year);
            }
        });
        endDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                //Log.d("Date", "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth);
                tv2.setText(tv2Promt+dayOfMonth+"-"+(month+1)+"-"+year);
            }
        });
        //builder.setCancelable(false);
        // Create the AlertDialog object and return it
        adialog= builder.create();

        if(title!=null) {
            adialog.setTitle(title);

        }
        return adialog;

    }

    public void setTitle(String title){
          this.title = title;

    }


    private String getDateAsString(int dd,int mm,int yy){
        Date date;
        String sDate=null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            date = new Date(yy,mm,dd);
            sDate = sdf.format(date);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sDate;
    }
    private boolean isValidDate(String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            //etDate.setError("Enter a valid date: dd-MM-YYYY");
        }
        return date != null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (IDateSelected) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IDateSelected");
        }
    }


}
