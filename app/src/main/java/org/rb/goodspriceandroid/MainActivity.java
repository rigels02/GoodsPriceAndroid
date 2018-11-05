package org.rb.goodspriceandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.rb.goodspriceandroid.good.GoodContent;
import org.rb.goodspriceandroid.impoexpo.CSV;
import org.rb.goodspriceandroid.model.Good;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements AddModDialog_1.IAddModifyDialogListener,
        GoodFragment.OnListFragmentInteractionListener,
        ConfirmDialog.IConfirmDialogListener{

    private static final int REST_CLIENT_REQUEST = 101;

    private TextView infoTxt;
    private int selectedItemIdx;
    private GoodFragment listFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //!!IMPORTANT: For android I have to use App's private directory, but not a root
        //Let's try to get local diretory
        String localDir = getFilesDir().getPath();
        CoreLayer.getInstance().InitCore(localDir);
        //InitStatic(localDir);
        Log.i("Info",getClass().getName()+" OnCreate");

        setContentView(R.layout.activity_main_no_fragment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        infoTxt= findViewById(R.id.info);
        FloatingActionButton fab = findViewById(R.id.fabDelete);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Delete Item?", Snackbar.LENGTH_LONG)
                        .setAction("DELETE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //showMyDialog();
                                showConfirmDialog();
                            }
                        }).show();
            }
        });
        FloatingActionButton fab1 = findViewById(R.id.fabAdd);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add new item here", Snackbar.LENGTH_LONG)
                        .setAction("ADD", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //showMActivity();
                                showAddDialog();
                            }
                        }).show();
            }
        });

        FloatingActionButton fab2 = findViewById(R.id.fabMod);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Modify selected Item", Snackbar.LENGTH_LONG)
                        .setAction("MODIFY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showModifyDialog();
                            }
                        }).show();
            }
        });
        /***
        FloatingActionButton fab3 =(FloatingActionButton) findViewById(R.id.fabReport);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Report information", Snackbar.LENGTH_LONG)
                        .setAction("REPORT", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showReportActivity();
                            }
                        }).show();
            }
        });
         ***/
        //Fragments
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if( findViewById(R.id.fragment_container) != null){
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

        }
        try {
            List<Good> goods = CoreLayer.getInstance().getControl().getList();
            GoodContent.populateGoodItems(goods);
        } catch (IOException | ClassNotFoundException e ) {
            Log.e("GetList:",Log.getStackTraceString(e));
        }
        listFragment = GoodFragment.newInstance(1);
        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        //listFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,listFragment)
                .commit();

    }

    private void showReportActivity() {
        Intent mi = new Intent(this, ReportActivity.class);
        List<Good> goods = CoreLayer.getInstance().getControl().getListFromDataControlMemory();
        ArrayList<Good> tgoods= (ArrayList<Good>) goods;

        Bundle args = new Bundle();
        // args.putString("html","Test string");

        args.putParcelableArrayList("List",tgoods);
        mi.putExtras(args);
        startActivity(mi);
    }

    private void showModifyDialog() {

        Good good;
        selectedItemIdx = listFragment.getSelectedPosition();
        if(selectedItemIdx != -1) {
            //good = GoodContent.getGood(selectedItemIdx);
            try {
                good = CoreLayer.getInstance().getControl().getItem(selectedItemIdx);
            }catch (Exception ex){
                showInfoDialog(InfoDialog.ERROR,ex.getMessage());
                return;
            }
        }else{
            return;
        }
        AddModDialog_1 dlg = new AddModDialog_1();
        dlg.passParams(good,AddModDialog_1.MODIFY);

        dlg.setTitle("Modify");
        dlg.setCancelable(false);
        dlg.show(getFragmentManager(), "AddModDialog_1");

    }



    private void showAddDialog() {
        AddModDialog_1 dlgBig = new AddModDialog_1();
        Good good= new Good();

        dlgBig.passParams(good,AddModDialog_1.ADD);
        dlgBig.setTitle("Add Info");
        dlgBig.setCancelable(false);
        dlgBig.show(getFragmentManager(), "AddModDialog_1");

    }
    private void showInfoDialog(int type,String msg){
        InfoDialog dialog = new InfoDialog();
        Bundle args = new Bundle();
        //args.putString("title","Error");
        args.putString("message",msg);
        args.putInt("type",type);
        dialog.setArguments(args);
        //dialog.setTitle("");
        //dialog.setMsg("Test message !");
        //dialog.setType(InfoDialog.Type.ERROR);
        dialog.setCancelable(false);
        dialog.show(getFragmentManager(), "InfoDialog");

    }
    private void showConfirmDialog(){
        Good good;
        selectedItemIdx = listFragment.getSelectedPosition();
        if(selectedItemIdx != -1) {
            //good = GoodContent.getGood(selectedItemIdx);
            try {
                good = CoreLayer.getInstance().getControl().getItem(selectedItemIdx);
            }catch (Exception ex){
                showInfoDialog(InfoDialog.ERROR,ex.getMessage());
                return;
            }
        }else {
            return;
        }
        ConfirmDialog dlg = new ConfirmDialog();
        dlg.passArguments("Confirm","Delete item:\n"+good.toString());
        dlg.setCancelable(false);
        dlg.show(getFragmentManager(),"Confirm");

    }

    private void updateGoodContentAndSelectedIdx( Good good) {
        List<Good> goods = CoreLayer.getInstance().getControl().getListFromDataControlMemory();
        GoodContent.populateGoodItems(goods);
        //goods are sorted by date find position of good
        if(goods.contains(good)){
            selectedItemIdx = goods.indexOf(good);
        }else{
            selectedItemIdx = -1;
        }
    }
    private void updateGoodContentAndSelectedIdxForDeleted( int deletedIdx) {
        List<Good> goods = CoreLayer.getInstance().getControl().getListFromDataControlMemory();
        GoodContent.populateGoodItems(goods);
        if(deletedIdx-1>=0) {
            selectedItemIdx= deletedIdx-1;
            return;
        }
        if(deletedIdx< goods.size()) {
            selectedItemIdx = deletedIdx;
            return;
        }
        selectedItemIdx= -1;

    }

    private void processSharingCSV() {
        List<Good> goods = CoreLayer.getInstance().getControl().getListFromDataControlMemory();
        String toSend = CSV.makeCSVString(goods);
        //Toast.makeText(this,toSend,Toast.LENGTH_LONG).show();
        //--------------//
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, toSend);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
        // Create intent to show chooser
        Intent chooser = Intent.createChooser(sendIntent, "Share CSV with:");
        // Verify the intent will resolve to at least one activity
        if (sendIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity_no, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if(id == R.id.id_shareCSV){
            processSharingCSV();
            return true;
        }
        if(id == R.id.id_showreport){
            showReportActivity();
            return true;
        }
        if(id == R.id.id_tcpClient){
            Intent mi = new Intent(this, TCPClientActivity.class);
            startActivity(mi);
            return true;
        }
        if(id == R.id.id_restfulclient){
            Intent serv = new Intent(this,RestfulClientActivity.class);

            startActivityForResult(serv,REST_CLIENT_REQUEST);
            return true;
        }
//        if(id == R.id.idTestImpo){
//            Intent mi = new Intent(this, ImportGoodsCSV.class);
//            startActivity(mi);
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REST_CLIENT_REQUEST && resultCode == RESULT_OK){

            updateGoodContentForAllDataReloaded();
            listFragment.refreshListView();
            listFragment.setSelectedItem(selectedItemIdx);
            listFragment.scrollToPosition(listFragment.getSelectedPosition());
        }

    }

    private void updateGoodContentForAllDataReloaded() {
        List<Good> goods = CoreLayer.getInstance().getControl().getListFromDataControlMemory();
        GoodContent.populateGoodItems(goods);
        //goods are sorted by date find position of good
        if(!goods.isEmpty()){
            selectedItemIdx = 0;
        }else{
            selectedItemIdx= -1;
        }

    }

    @Override
    public void addModifyDialogModifyAccepted(Good good) {

        infoTxt.setText(good.toString());
        try {
            CoreLayer.getInstance().getControl().edit(selectedItemIdx,good);
        } catch (IOException e) {
            showInfoDialog(InfoDialog.ERROR,e.getMessage());
            return;
        }
        updateGoodContentAndSelectedIdx(good);

        //GoodContent.updateItem(selectedItemIdx,good);
        listFragment.refreshListView();
        listFragment.setSelectedItem(selectedItemIdx);
        listFragment.scrollToPosition(listFragment.getSelectedPosition());
        Toast.makeText(this, "Modified", Toast.LENGTH_SHORT).show();
        //
    }



    @Override
    public void addModifyDialogAddAccepted(Good good) {
        try {
            //persist data
            CoreLayer.getInstance().getControl().add(good);
        } catch (IOException e) {
            showInfoDialog(InfoDialog.ERROR,e.getMessage());
            return;
        }
        updateGoodContentAndSelectedIdx(good);
        listFragment.refreshListView();
        listFragment.setSelectedItem(selectedItemIdx);
        Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
        listFragment.scrollToPosition(listFragment.getSelectedPosition());
    }


    @Override
    public void onListFragmentInteraction(int selectedPostion) {
        selectedItemIdx= selectedPostion;
        Toast.makeText(this,"Item Selected: "+GoodContent.getGood(selectedPostion).toString()
                ,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListFragmentResume(GoodFragment listFragment) {
        this.listFragment= listFragment;
        this.selectedItemIdx= listFragment.getSelectedPosition();
    }

    @Override
    public void onConfirmResult(int id) {
        // Toast.makeText(this,"Buton pressed: "+id,Toast.LENGTH_SHORT).show();
        //AlertDialog.BUTTON_NEGATIVE = -2
        //AlertDialog.BUTTON_POSITIVE = -1
        if(id== AlertDialog.BUTTON_POSITIVE){

            try {
                CoreLayer.getInstance().getControl().del(selectedItemIdx);
            } catch (IOException e) {
                showInfoDialog(InfoDialog.ERROR,e.getMessage());
                return;
            }

        }else{
            return;
        }
        updateGoodContentAndSelectedIdxForDeleted(selectedItemIdx);
        listFragment.refreshListView();
        listFragment.setSelectedItem(selectedItemIdx);
        listFragment.scrollToPosition(listFragment.getSelectedPosition());
        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("Info",getClass().getName()+" onSaveInstanceState");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Info",getClass().getName()+" onResume");
        Log.d("Control", CoreLayer.getInstance().getControl().toString());
        //TODO: has to be checked if data import was done. If yes, then
        //Goods content and view has to be updated
        //It might happen that new data was imported, but this activity
        //was resumed without onCreate. As result, the old data view is still presented.
        //We have to check this condition and update Goods content(GoodContent class) and view
        CoreLayer core = CoreLayer.getInstance();
        if (core.isNewDataImported()){
            List<Good> goods = core.getControl().getListFromDataControlMemory();
            GoodContent.populateGoodItems(goods);
            core.setNewDataImported(false);
            listFragment.refreshListView();
            listFragment.setSelectedItem(goods.size()-1);
            listFragment.scrollToPosition(listFragment.getSelectedPosition());
            Toast.makeText(this, "New data has been imported!", Toast.LENGTH_LONG).show();

        }

    }
}
