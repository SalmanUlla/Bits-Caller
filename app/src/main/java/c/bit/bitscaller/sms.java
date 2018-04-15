package c.bit.bitscaller;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import c.bit.bitscaller.Adapters.PhoneRecyclerAdapter;
import c.bit.bitscaller.Adapters.SMSRecyclerAdapter;

public class sms extends AppCompatActivity {

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
        getSupportActionBar().setTitle("SMS BLACKLIST");

        initandfill();

    }

    public void initandfill() {
        recyclerViewSMS = findViewById(R.id.recyclersms);
        list = new ArrayList<>();
        Log.e("Error1", "Reached Here 1");
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
        return super.onOptionsItemSelected(item);
    }
}
