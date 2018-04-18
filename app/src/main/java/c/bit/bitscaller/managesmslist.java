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
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import c.bit.bitscaller.Adapters.ManagePhoneAdapter;
import c.bit.bitscaller.Adapters.ManageSMSAdapter;

public class managesmslist extends AppCompatActivity  implements ConnectivityReceiver.ConnectivityReceiverListener {

    Toolbar toolbar;

    private AppCompatActivity activity = managesmslist.this;
    private RecyclerView recyclerviewmanagesms;
    private ArrayList<Pojo> list;
    private ManageSMSAdapter smsrecycleradapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managesmslist);
        toolbar = (Toolbar) findViewById(R.id.toolbar6);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Manage SMS List");
        initAndFillData();
    }

    public void initAndFillData() {
        recyclerviewmanagesms = findViewById(R.id.recyclermanagesms);
        list = new ArrayList<>();
        smsrecycleradapter = new ManageSMSAdapter(list, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(managesmslist.this);
        recyclerviewmanagesms.setLayoutManager(mLayoutManager);
        recyclerviewmanagesms.setItemAnimator(new DefaultItemAnimator());
        recyclerviewmanagesms.setHasFixedSize(true);
        recyclerviewmanagesms.setAdapter(smsrecycleradapter);
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
                list.addAll(databaseHelper.getAllManageSMSList(managesmslist.this));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                smsrecycleradapter.notifyDataSetChanged();
            }
        }.execute();
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
                    .make(findViewById(R.id.managesms), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }


    }
}

