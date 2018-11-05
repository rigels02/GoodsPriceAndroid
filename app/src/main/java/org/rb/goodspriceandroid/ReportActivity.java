package org.rb.goodspriceandroid;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import org.rb.goodspriceandroid.model.Good;
import org.rb.goodspriceandroid.reports.HtmlFormater;
import org.rb.goodspriceandroid.reports.ReportUtils;
import org.rb.goodspriceandroid.reports.Reporter;

import java.util.ArrayList;
import java.util.Date;

public class ReportActivity extends AppCompatActivity implements DateSelectDialog.IDateSelected{

    private WebView webView;
    private ArrayList<Good> goods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args= getIntent().getExtras();
        //String toShow= args.getString("html");
        goods = args.getParcelableArrayList("List");
        setContentView(R.layout.activity_report);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab_report);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Change start and end dates", Snackbar.LENGTH_LONG)
                        .setAction("Set Dates", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                selectDates();
                            }
                        }).show();

            }
        });

        webView= findViewById(R.id.reportView);


        //---------------------------------------------//
        Date startDate;
        //By default make Start Date 1Jan for current year, the End date current date.
        int yy = ReportUtils.getYearPart(new Date());
        startDate= new Date(yy, 0, 1);


        createAndShowReportHtml(startDate, new Date());
    }


    private void createAndShowReportHtml(Date startDate, Date endDate){
        String reportTxt;

        reportTxt= new HtmlFormater()
                .printFmtStr_1(
                        new Reporter(goods)
                        .getTimeReportForAllShops(startDate, endDate)
                );

        webView.getSettings().setDefaultTextEncodingName("utf-8");
        //webView.loadData(summary,"text/html;charset=utf-8",null);
        webView.loadData(reportTxt,"text/html;charset=utf-8",null);

    }
    private void selectDates() {
        DateSelectDialog dialog = new DateSelectDialog();
        dialog.setTitle("Select dates");
        dialog.setCancelable(false);
        dialog.show(getFragmentManager(),"dateSelectDialog");
    }


    @Override
    public void dateSelectedCallBack(Date startDate, Date endDate) {
        createAndShowReportHtml(startDate,endDate);
    }
}
