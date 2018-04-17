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

import java.util.ArrayList;

import c.bit.bitscaller.Adapters.ManagePhoneAdapter;
import c.bit.bitscaller.Adapters.PhoneRecyclerAdapter;

public class managephonelist extends AppCompatActivity {

    Toolbar toolbar;

    private AppCompatActivity activity = managephonelist.this;
    private RecyclerView recyclerViewManagePhone;
    private ArrayList<Pojo> list;
    private ManagePhoneAdapter phoneRecyclerAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managephonelist);

        toolbar = findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("MANAGE PHONE LIST");
        initAndFillData();
    }

    public void initAndFillData() {
        recyclerViewManagePhone = findViewById(R.id.recyclermanagephone);
        list = new ArrayList<>();
        phoneRecyclerAdapter = new ManagePhoneAdapter(list, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(managephonelist.this);
        recyclerViewManagePhone.setLayoutManager(mLayoutManager);
        recyclerViewManagePhone.setItemAnimator(new DefaultItemAnimator());
        recyclerViewManagePhone.setHasFixedSize(true);
        recyclerViewManagePhone.setAdapter(phoneRecyclerAdapter);
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
                list.addAll(databaseHelper.getAllManagePhoneList(managephonelist.this));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                phoneRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
