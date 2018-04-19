package c.bit.bitscaller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

public class IntentServiceActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {



    Toolbar toolbar;

    private MyWebRequestReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_service);

        toolbar = (Toolbar) findViewById(R.id.toolbar7);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Bits Website");

        boolean isConnected = ConnectivityReceiver.isConnected();
        if(!isConnected)
        {
            Toast.makeText(this, "Turn On Internet To Access Bits Website", Toast.LENGTH_LONG).show();
            finish();
        }





        IntentFilter filter = new IntentFilter(MyWebRequestReceiver.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyWebRequestReceiver();
        registerReceiver(receiver, filter);

        Intent msgIntent = new Intent(IntentServiceActivity.this, MyWebRequestService.class);

        msgIntent.putExtra(MyWebRequestService.REQUEST_STRING, "http://www.bits-pilani.ac.in/Overview");
        startService(msgIntent);


    }

    @Override
    public void onDestroy() {
        this.unregisterReceiver(receiver);
        super.onDestroy();
    }

    public class MyWebRequestReceiver extends BroadcastReceiver{

        public static final String PROCESS_RESPONSE = "c.bit.webservice.intent.action.PROCESS_RESPONSE";

        @Override
        public void onReceive(Context context, Intent intent) {

            String reponseMessage = intent.getStringExtra(MyWebRequestService.RESPONSE_MESSAGE);


            WebView myWebView = (WebView) findViewById(R.id.mywebview);
            myWebView.getSettings().setJavaScriptEnabled(true);
            try {
                myWebView.loadData(URLEncoder.encode(reponseMessage,"utf-8").replaceAll("\\+"," "), "text/html", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();
        registerReceiver(connectivityReceiver, intentFilter);

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }




    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (!isConnected) {
            message = "Sorry! Not Connected To Internet";
            color = Color.RED;
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.web), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }


    }
}



