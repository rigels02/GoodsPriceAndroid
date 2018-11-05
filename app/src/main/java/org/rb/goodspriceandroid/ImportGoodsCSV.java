package org.rb.goodspriceandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.rb.goodspriceandroid.impoexpo.CSV;
import org.rb.goodspriceandroid.model.Good;

import java.util.List;

/**
 * For info:
 * Activity is the base class of all other activities,
 * Activity <- FragmentActivity <- AppCompatActivity <- ActionBarActivity
 * <p>
 * '<-' means inheritance here.
 * So basically, using AppCompatActivity is always the right choise. The differences between them:
 * <p>
 * Activity is the basic one.
 * Based on Activity, FragmentActivity provides the ability to use Fragment.
 * Based on FragmentActivity, AppCompatActivity provides features to ActionBar
 */
public class ImportGoodsCSV extends AppCompatActivity {

    private TextView textForImpo;
    private Activity activ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impo_shared_csv);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textForImpo = findViewById(R.id.textImpo);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Current data will be overwritten!", Snackbar.LENGTH_LONG)
                        .setAction("Import CSV?!", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                /**/
                                new SimpleConfirmDialog(ImportGoodsCSV.this,
                                        "Confirm!", "Are you sure?\nThis action will overwrite old data?") {
                                    @Override
                                    public void onConfirmClicked() {
                                        processImportCSV();
                                    }
                                };
                                /**/
                            }
                        }).show();
            }


        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //--------------------------//
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        }
    }

    private void handleSendText(Intent intent) {
        String sharedTextForMe = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedTextForMe != null) {
            //textForImpo = (TextView) findViewById(R.id.textImpo);
            Toast.makeText(this, "Shared CSV text! Please check it! ", Toast.LENGTH_LONG).show();
            textForImpo.setText(sharedTextForMe);
        } else {
            Toast.makeText(this, "Nothing to import", Toast.LENGTH_LONG).show();
        }

    }

    private void processImportCSV() {

        Toast.makeText(this, "Import is proccesing....", Toast.LENGTH_LONG).show();

        String txtImpo = this.textForImpo.getText().toString();

        if (txtImpo.isEmpty())
            return;
        //String localDir = getFilesDir().getPath().toString();

        List<Good> goods;
        try {
            goods = CSV.makeListFromCSVString(txtImpo);
            if(CoreLayer.getInstance().getControl()==null){
                String localDir = getFilesDir().getPath();
                CoreLayer.getInstance().InitCore(localDir);
            }
            CoreLayer.getInstance().getControl().importData(goods);
            CoreLayer.getInstance().setNewDataImported(true);
            Toast.makeText(this,"Data Import Completed!",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            showInfoDialog(InfoDialog.ERROR, e.getMessage());
        }

    }

    private void showInfoDialog(int type, String msg) {
        InfoDialog dialog = new InfoDialog();
        Bundle args = new Bundle();
        //args.putString("title","Error");
        args.putString("message", msg);
        args.putInt("type", type);
        dialog.setArguments(args);
        //dialog.setTitle("");
        //dialog.setMsg("Test message !");
        //dialog.setType(InfoDialog.Type.ERROR);
        dialog.setCancelable(false);
        dialog.show(getFragmentManager(), "InfoDialog");

    }


}
