package org.rb.goodspriceandroid;

import android.content.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.rb.goodspriceandroid.model.Good;
import org.rb.goodspriceandroid.model.GoodR;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Note 1:
 * ------
 *  The screen orientation for this activity is disabled and set to portrait
 *  android:screenOrientation="portrait"
 *  Otherwise to keep activity status and properly update fields I have to save instance status
 *  and overwrite onPause , onResume methods with register and unregister broadcast receiver.
 *  A broadcast message for the receiver must be sent by onPostExecute method of AsyncTask.
 * Note 2:
 * ------
 *   - If during data receiving task the Back button is called (doInBackground is still working)
 *     the onPostExecute will not update data later on.(task completion is canceled)
 *   - If during data sending task the Back button is called then there is no influence on task completion.
 *     The sending onPostExecute sends broadcast message to current active RestClientActivity instance.
 * Note 3:
 * -------
 *   - For simplicity, in case of any AsyncTask is running the Back button is disabled for this time.
 *     See onBackPressed() method. The RestClientActivity is still able to run properly even if Back button
 *     is enabled.
 */
public class RestfulClientActivity extends AppCompatActivity {

    private static final String URL_IS_REQUIRED = "URL is required!";
    private static final String PROGRESS_BAR_STATE = "progressBarState";
    private static final String TV_RESULT_TXT = "tvResultTxt";
    private static final String URL_TXT = "UrlTxt";
    private static final String HTTP_TASK_RESULT = "HttpTaskResult";
    private static final String HTTP_TASK_INTENT = "HTTP_Task_Intent";
    private static final String CHK_PATH = "/chk";
    private static final String TIME_STAMP = "/date";
    private static final String PREF_URL = "URL";

    private static boolean sendTaskRunning = false;
    private static String etUrl_txt;
    private static String tvResult_txt;
    private boolean receiveTaskRunning = false;
    private boolean compareTimeTaskRunning= false;

    private EditText etUrl;
    private TextView tvResult;
    private ProgressBar progressBar;
    private FloatingActionButton fabReceive;
    private FloatingActionButton fabModTime;
    private FloatingActionButton fabSend;

    private boolean active = false;


    private BroadcastReceiver httpClientTaskBroadcastReceiver =
            new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Bundle bundle = intent.getBundleExtra(HTTP_TASK_RESULT);
                    if(bundle != null){
                        String txt = bundle.getString(TV_RESULT_TXT);
                        tvResult.append(txt);
                        progressBar.setVisibility(View.GONE);
                        enableControls(true);
                    }
                }
            };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restful_client);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       

        etUrl =  findViewById(R.id.etUrl);
        etUrl.setText(getFromPreferences());
        tvResult = findViewById(R.id.tvResult);
        tvResult.setMovementMethod(new ScrollingMovementMethod());
        progressBar = findViewById(R.id.progressBar);
        // Check whether we're recreating a previously destroyed instance
        if(savedInstanceState != null){
            progressBar.setVisibility(savedInstanceState.getInt(PROGRESS_BAR_STATE));
            etUrl.setText(savedInstanceState.getString(URL_TXT));
            tvResult.setText(savedInstanceState.getString(TV_RESULT_TXT));
        }
        fabSend =  findViewById(R.id.fab_tcp_send);
        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Send data to server", Snackbar.LENGTH_LONG)
                        .setAction("Send All data", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                              
                                sendData();
                            }
                        }).show();
            }
        });

        fabReceive = findViewById(R.id.fab_tcp_receive);
        fabReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Receive data from server", Snackbar.LENGTH_LONG)
                        .setAction("Receive data", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                
                                confirmReceiveData("Confirm to receive data!\nThe local data will be overwritten!");
                            }
                        }).show();
            }
        });

        fabModTime = findViewById(R.id.fab_get_time);
        fabModTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Compare Local data with remote", Snackbar.LENGTH_LONG)
                        .setAction("Compare time Stamps", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                compareTimeStamps();
                            }
                        }).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private String getFromPreferences() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        return prefs.getString(PREF_URL,"");
    }
    private void saveInPreferences(){
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        prefs.edit()
                .putString(PREF_URL,etUrl.getText().toString())
                .commit();

    }
    private void compareTimeStamps() {
        Date localTime = CoreLayer.getInstance().getControl().getTimeStamp();
        progressBar.setVisibility(View.VISIBLE);
        new CompareTimeStampsHttpTask().execute(localTime);
        compareTimeTaskRunning = true;
        enableControls(false);
    }

    /***
    private void cancelCurrentTask() {
        if(task!=null){
            task.cancel(true);
        }
    }
    ***/

    public EditText getEtUrl() {
        return etUrl;
    }

    public TextView getTvResult() {
        return tvResult;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }



    private String getUrl(){
        String url = etUrl.getText().toString();
        if(url.isEmpty()){
            etUrl.setError(URL_IS_REQUIRED);
            return null;
        }
        return url;
    }
    private void enableControls(boolean status){
        fabReceive.setEnabled(status);
        fabSend.setEnabled(status);
        etUrl.setEnabled(status);
        fabModTime.setEnabled(status);
    }
    private void receiveData() {

        progressBar.setVisibility(View.VISIBLE);
        new ReceiveDataHttpTask().execute();
        receiveTaskRunning = true;
        enableControls(false);
    }

    private void sendData() {
        List<Good> goods;

        try {
            goods = CoreLayer.getInstance().getControl().getList();

        }catch (IOException | ClassNotFoundException ex){
            showInfoDialog(InfoDialog.ERROR,ex.getMessage());
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        sendTaskRunning = true;

        new SendDataHttpTask().execute(goods);
        enableControls(false);
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
    private void confirmReceiveData(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                receiveData();
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        })
        .create().show();

    }


    private String chkRemoteSite(String reqUrl){
            String msg= "Connection problem to remote server!\n" +
                    "Check is it up and running\n" +
                    "Check URL correctness.";
            RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory httpFact =
                (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
        httpFact.setConnectTimeout(10000);

            //restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<String> response;
            try {
                response =
                        restTemplate.getForEntity(reqUrl + CHK_PATH, String.class);
            }catch (Exception ex){
                return "Connection problem: "+ex.getMessage();
            }
            if(response.getStatusCode() != HttpStatus.OK){
                return msg;
            }
            return null;
        }


    class ReceiveDataHttpTask extends AsyncTask<Void,String,List<Good>>{


        private String errMsg;



        @Override
        protected List<Good> doInBackground(Void... voids) {

            List<Good> goods = new ArrayList<>();
            String url = getUrl();
            publishProgress("Connecting...");
            String message = chkRemoteSite(url);
            if(message != null){
                errMsg = message;
                return goods; //null
            }
            publishProgress("Receiving data...");
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<GoodR[]> response=
                    restTemplate.getForEntity(url,GoodR[].class);
            if(response.getStatusCode()== HttpStatus.OK){
                GoodR[] goods_r = response.getBody();
                for(GoodR el: goods_r){
                    try {
                        goods.add(el.makeGood());
                    }catch (CloneNotSupportedException ex){
                        Log.e("RESTCLN",ex.getMessage());
                    }
                }
            }else {
                errMsg="Http Error code: "+response.getStatusCode();
            }
            return goods;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            //super.onProgressUpdate(values);
            tvResult.append("\n"+values[0]);
        }


        @Override
        protected void onPostExecute(List<Good> goods) {
            receiveTaskRunning = false;
            //Check on the activity of instance who started this task. is it still active?
            if(! active )
                return;
            progressBar.setVisibility(View.GONE);
            enableControls(true);

            if( errMsg!=null){
                tvResult.append("\nReceiving Error: "+errMsg);
                setResult(RESULT_CANCELED);
                return;
            }
            if(goods == null){
                tvResult.append("\nNothing received...");
                setResult(RESULT_CANCELED);
                return;
            }

            try {
                CoreLayer.getInstance().getControl().importData(goods);
            }catch (IOException ex){
                tvResult.append("\nError during data saving locally:\n"+ex.getMessage());
                setResult(RESULT_CANCELED);
                return;
            }
            tvResult.append("\nReceived and saved "+goods.size()+" records...");
            tvResult.append("\nReceiving Completed!");
            setResult(RESULT_OK);
        }

    }

    /**
     * Send Data using REST API to url (for.ex., http://localhost:8080/goods )
     */
    class SendDataHttpTask extends AsyncTask<List<Good>,Void,String> {


        @SafeVarargs
        @Override
        protected final String doInBackground(List<Good>... lists) {
            if(lists[0]==null){
                return "Nothing to send...";
            }
            String url = getUrl();
            String message = chkRemoteSite(url);
            if(message != null){
                return message;
            }
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            //Delete Old remote data
            ResponseEntity<Void> responseDelete =
                    restTemplate.exchange(url + "/all", HttpMethod.DELETE, null, Void.class);
            if(responseDelete.getStatusCode() != HttpStatus.OK){
                return "SendData...\nError on deleting remote data...";
            }
            //Send local data to remote server
            for(int i=0; i< lists[0].size(); i++) {
                Good good = lists[0].get(i);
                GoodR good_r = new GoodR(good);
                ResponseEntity<GoodR> response = restTemplate.postForEntity(url, good_r, GoodR.class);
                if(response.getStatusCode() != HttpStatus.CREATED){
                    return "SendData Error:"+response.getStatusCode();
                }
            }

            return "All Data has been sent.\n"+lists[0].size()+" record(s).";
        }


        @Override
        protected void onPostExecute(String message) {
            sendTaskRunning =false;
            enableControls(true);
            //Check on the activity who started this task is still active
            //if( !active )
            //    return;

            StringBuilder sb = new StringBuilder();
            if (message != null)
                sb.append("\n").append(message);

            sb.append("\nSending Completed!");
            setResult(RESULT_CANCELED);
            Intent intent = new Intent(HTTP_TASK_INTENT);
            Bundle bundle = new Bundle();
            bundle.putString(TV_RESULT_TXT,sb.toString());
            intent.putExtra(HTTP_TASK_RESULT, bundle);
            //intent.putExtra(HTTP_TASK_RESULT,sb.toString());
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        }
    }

    private class CompareTimeStampsHttpTask extends AsyncTask<Date,Void,String>{



        @Override
        protected String doInBackground(Date... localTimes) {
            if(localTimes[0]==null){
                return "Local Time Stamp == NULL !";
            }
            active = true;
            String url = getUrl();

            RestTemplate restTemplate = new RestTemplate();
            ((SimpleClientHttpRequestFactory)restTemplate.getRequestFactory()).setConnectTimeout(10000);
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Date> responseTime;
            try {
                responseTime = restTemplate.getForEntity(url + TIME_STAMP, Date.class);
            }catch (Exception ex){
                return "Connection error: "+ex.getMessage();
            }
            if(responseTime.getStatusCode() != HttpStatus.OK){
                return "Can not get Remote TimeStamp!\nCode= "+responseTime.getStatusCode();
            }
            String pfx = "Comparing Time Stamps...\n";
            if (responseTime.getBody().after(localTimes[0]))
                return pfx+"Remote Data are newer then local.";
            if(responseTime.getBody().before(localTimes[0]))
                return pfx+"Local Data are newer then Remote.";
            return pfx+"Local and Remote data seems to be equal.";
        }

        @Override
        protected void onPostExecute(String msg) {
            compareTimeTaskRunning = false;
            //Check on the activity of instance who started this task. is it still active?
            if(! active )
                return;
            progressBar.setVisibility(View.GONE);
            enableControls(true);
            setResult(RESULT_CANCELED);
            tvResult.append("\n"+msg);

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(PROGRESS_BAR_STATE,progressBar.getVisibility());
        outState.putString(URL_TXT,etUrl.getText().toString());
        outState.putString(TV_RESULT_TXT,tvResult.getText().toString());
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
        if(sendTaskRunning)
            Toast.makeText(this,"The sending task is continue running",Toast.LENGTH_SHORT).show();
        if(receiveTaskRunning)
            Toast.makeText(this,"The receiving task is canceled",Toast.LENGTH_SHORT).show();
        saveInPreferences();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.httpClientTaskBroadcastReceiver);
        etUrl_txt = etUrl.getText().toString();
        tvResult_txt = tvResult.getText().toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(this.httpClientTaskBroadcastReceiver,new IntentFilter(HTTP_TASK_INTENT));
        if(sendTaskRunning) {
            progressBar.setVisibility(View.VISIBLE);
            etUrl.setText(etUrl_txt);
            tvResult.setText(tvResult_txt);
            enableControls(false);
        }
    }

    @Override
    public void onBackPressed() {
        if(anyTaskRunning())
            return;
        super.onBackPressed();
    }

    private boolean anyTaskRunning() {
        return (sendTaskRunning || receiveTaskRunning || compareTimeTaskRunning);

    }


}
