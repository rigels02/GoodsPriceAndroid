package org.rb.goodspriceandroid;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.rb.goodspriceandroid.control.DataControl;
import org.rb.goodspriceandroid.model.Good;
import org.rb.goodspriceandroid.tcp.Status;
import org.rb.goodspriceandroid.tcp.TcpClient;
import org.rb.goodspriceandroid.tcp.TcpServer;

import java.io.IOException;
import java.util.List;


public class TCPClientActivity extends AppCompatActivity {

    private TextView tv;
    private FloatingActionButton fabCancel;
    private FloatingActionButton fabSend;
    private FloatingActionButton fabReceive;
    private List<Good> goods;
    private String host_IP;
    private int host_Port;
    private EditText etIp;
    private EditText etPort;
    private ProgressBar progress;
    private TcpClient tcpSender;
    private TcpClient tcpReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp__client);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        progress = (ProgressBar) findViewById(R.id.progressBar);
        etIp = (EditText) findViewById(R.id.etIP);
        etPort = (EditText) findViewById(R.id.etPort);
        tv = (TextView) findViewById(R.id.tvStatus);
        tv.setText("Please input Server's host/IP and port number.");
        tv.setMovementMethod(new ScrollingMovementMethod());
        fabSend = (FloatingActionButton) findViewById(R.id.fab_tcp_send);
        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Send data to server", Snackbar.LENGTH_LONG)
                        .setAction("Send data", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (inputsValid()) {
                                    tcpClientSend();
                                }
                            }
                        }).show();
            }
        });

        fabReceive = (FloatingActionButton) findViewById(R.id.fab_tcp_receive);
        fabReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Receive data from server", Snackbar.LENGTH_LONG)
                        .setAction("Receive data", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (inputsValid()) {
                                    tcpClientReceive();
                                }
                            }
                        }).show();
            }
        });
        fabCancel = (FloatingActionButton) findViewById(R.id.fab_tcp_cancel);
        fabCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Cancel TCP process", Snackbar.LENGTH_LONG)
                        .setAction("Cancel", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tcpClientCancel();
                            }
                        }).show();
            }
        });
        fabCancel.setEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private boolean inputsValid() {

        if (etIp.getText().toString().isEmpty() || etPort.getText().toString().isEmpty()) {
            new SimpleInfoDialog(this, SimpleInfoDialog.ERROR, "Error", "Some field is empty! ");
            return false;
        }
        host_IP = etIp.getText().toString();
        int port;
        try {
            port = Integer.valueOf(etPort.getText().toString());
        } catch (NumberFormatException ex) {
            new SimpleInfoDialog(this, SimpleInfoDialog.ERROR, "Error", ex.getMessage());
            return false;
        }
        if (!TcpServer.isAcceptablePort(port)) {
            new SimpleInfoDialog(this, SimpleInfoDialog.ERROR, "Error", "Not Acceptable port number!");
            return false;
        }
        host_Port = port;
        return true;
    }

    private void resetClientStatus() {
        fabCancel.setEnabled(false);
        fabReceive.setEnabled(true);
        fabSend.setEnabled(true);
        tcpSender = null;

    }

    private void tcpClientCancel() {
        try {
            if(tcpReceiver!=null){
                tcpReceiver.getSocket().close();
            }
            if(tcpSender !=null) {
                tcpSender.getSocket().close();
            }
        } catch (NullPointerException | IOException e) {
            progress.setVisibility(View.VISIBLE);
            if (!(e instanceof NullPointerException))
                new SimpleInfoDialog(this, SimpleInfoDialog.ERROR, "Error", e.getMessage());
        }
        resetClientStatus();
        tv.setText(tv.getText().toString() + "\nCanceled...");
        Toast.makeText(this, "Canceled!", Toast.LENGTH_SHORT).show();
    }

    private void tcpClientSend() {
        fabSend.setEnabled(false);
        fabCancel.setEnabled(true);
        Toast.makeText(this, "Sending ....", Toast.LENGTH_LONG).show();
        DataControl control = CoreLayer.getInstance().getControl();
        if (control == null) {
            CoreLayer.getInstance().InitCore(getFilesDir().toString());
        }
        goods = CoreLayer.getInstance().getControl().getListFromDataControlMemory();
        setTitle("Client Sender");
        String txt;
        String msg = "Please, run server receiver on other side.";

        txt = String.format("\nClient %s port %d waiting for acceptance...\n%s",
                host_IP, host_Port, msg);
        tv.setText(tv.getText().toString() + txt);
        progress.setVisibility(View.VISIBLE);
        new TASK_TcpClient_Send().execute();

    }

    private void tcpClientReceive() {
        fabSend.setEnabled(false);
        fabCancel.setEnabled(true);
        Toast.makeText(this, "Receiving ....", Toast.LENGTH_LONG).show();
        DataControl control = CoreLayer.getInstance().getControl();
        if (control == null) {
            CoreLayer.getInstance().InitCore(getFilesDir().toString());
        }
        //goods = CoreLayer.getInstance().getControl().getListFromDataControlMemory();
        setTitle("Client Receiver");
        String txt;
        String msg = "Please, run server sender on other side.";

        txt = String.format("\nClient %s port %d waiting for acceptance...\n%s",
                host_IP, host_Port, msg);
        tv.setText(tv.getText().toString() + txt);
        progress.setVisibility(View.VISIBLE);
        new TASK_TcpClient_Receive().execute();

    }


    private class TASK_TcpClient_Send extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... args) {
            tcpSender = new TcpClient(host_IP, host_Port, org.rb.goodspriceandroid.tcp.Status.SENDER);
            tcpSender.setGoods(goods);
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            tcpSender.run();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            //super.onPostExecute(s);
            progress.setVisibility(View.INVISIBLE);
            //canceled
            if (tcpSender == null) return;
            if (tcpSender.getStatus() == org.rb.goodspriceandroid.tcp.Status.ERROR) {
                tv.setText(tv.getText().toString() + "\nError: " + tcpSender.getError());
                new SimpleInfoDialog(TCPClientActivity.this, SimpleInfoDialog.ERROR, "Error",tcpSender.getError());

            } else {
                tv.setText(tv.getText() + "\nData sending is completed.");
                new SimpleInfoDialog(TCPClientActivity.this, SimpleInfoDialog.INFO, "Info", "Data sending is completed!");
            }
            resetClientStatus();
        }

        @Override
        protected void onCancelled() {
            //super.onCancelled();
            Toast.makeText(TCPClientActivity.this, "Sending canceled!", Toast.LENGTH_LONG).show();

        }
    }

    private class TASK_TcpClient_Receive extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            tcpReceiver = new TcpClient(host_IP, host_Port, org.rb.goodspriceandroid.tcp.Status.RECEIVER);
            tcpReceiver.run();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
             /*
                Warning!
                tcpClient.run does not throw an exception (all exceptions are catched
                internally). So, in done() the checking for an error must be done carefully.
                Status.ERROR and Status.COMPLETED must be used to be sure is Done()
                completed  successfully or not.
                */
            progress.setVisibility(View.INVISIBLE);
            //canceled
            if (tcpReceiver == null) {
                return;
            }
            if (tcpReceiver.getStatus() == org.rb.goodspriceandroid.tcp.Status.ERROR) {
                tv.setText(tv.getText().toString() + "\nError: " + tcpReceiver.getError());
                new SimpleInfoDialog(TCPClientActivity.this, SimpleInfoDialog.ERROR, "Error",tcpReceiver.getError());

            } else if (tcpReceiver.getStatus() == org.rb.goodspriceandroid.tcp.Status.COMPLETED) {

                try {
                    CoreLayer.getInstance().getControl().importData(tcpReceiver.getGoods());
                } catch (IOException e) {
                    new SimpleInfoDialog(TCPClientActivity.this, SimpleInfoDialog.ERROR, "Error", e.getMessage());
                    resetClientStatus();
                    return;
                }
                tv.setText(tv.getText() + "\nData receiving is completed.");
                new SimpleInfoDialog(TCPClientActivity.this, SimpleInfoDialog.INFO, "Info", "Data receiving is completed!");
            }
            resetClientStatus();

        }
    }
}
