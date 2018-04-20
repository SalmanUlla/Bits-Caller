package c.bit.bitscaller;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import c.bit.bitscaller.Adapters.PhoneRecyclerAdapter;
import c.bit.bitscaller.Adapters.SMSRecyclerAdapter;

public class sms extends AppCompatActivity  implements ConnectivityReceiver.ConnectivityReceiverListener {

    Toolbar toolbar;

    private AppCompatActivity activity = sms.this;
    private RecyclerView recyclerViewSMS;
    private ArrayList<Pojo> list;
    private SMSRecyclerAdapter smsRecyclerAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("SMS Blacklist");

        initandfill();

    }

    public void initandfill() {
        recyclerViewSMS = findViewById(R.id.recyclersms);
        list = new ArrayList<>();
        smsRecyclerAdapter = new SMSRecyclerAdapter(list, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(sms.this);
        recyclerViewSMS.setLayoutManager(mLayoutManager);
        recyclerViewSMS.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSMS.setHasFixedSize(true);
        recyclerViewSMS.setAdapter(smsRecyclerAdapter);
        databaseHelper = new DatabaseHelper(activity);
        getDataFromSQLite();
    }

    @SuppressLint("StaticFieldLeak")
    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                list.clear();
                list.addAll(databaseHelper.getAllSMSList(sms.this));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                smsRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sms_menu,menu);
        return true;

        }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =item.getItemId();
        if(id==R.id.sms_clearlog)
        {
            databaseHelper.clearlogsmsblacklist();
           smsRecyclerAdapter.notifyDataSetChanged();
            getDataFromSQLite();
        }
       else if(id==R.id.rr)
        {
            smsRecyclerAdapter.notifyDataSetChanged();
            getDataFromSQLite();
        }


        return super.onOptionsItemSelected(item);
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
                    .make(findViewById(R.id.sms), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }


    }
}
