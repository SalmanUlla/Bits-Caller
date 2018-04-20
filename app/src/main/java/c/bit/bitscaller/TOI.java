package c.bit.bitscaller;

import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

public class TOI extends AppCompatActivity  implements ConnectivityReceiver.ConnectivityReceiverListener{
    private WebView webview;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toi);

        toolbar = (Toolbar) findViewById(R.id.toolbar7);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Times of India");

        boolean isConnected = ConnectivityReceiver.isConnected();
        if(!isConnected)
        {
            Toast.makeText(this, "Turn On Internet To Access TOI website", Toast.LENGTH_LONG).show();
            finish();
        }


        webview =(WebView)findViewById(R.id.webView);

        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webview.loadUrl("http://m.timesofindia.com/");
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


