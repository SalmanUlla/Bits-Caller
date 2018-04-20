package c.bit.bitscaller;

import android.Manifest;
import android.annotation.SuppressLint;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.annotation.Nullable;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

import c.bit.bitscaller.Adapters.PhoneRecyclerAdapter;

public class phone extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private static final int RESTORE_REQUEST_CODE = 0;

    private boolean bb;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    private AppCompatActivity activity = phone.this;
    private RecyclerView recyclerViewPhone;
    private ArrayList<Pojo> list;
    private PhoneRecyclerAdapter phoneRecyclerAdapter;
    private DatabaseHelper databaseHelper;
    Button add;
    Button cancel;
    EditText numbertext;
    String number = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.INTERNET, Manifest.permission.READ_SMS, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CALENDAR, Manifest.permission.INTERNET, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();


        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CALL BLACKLIST");


        initAndFillData();

        navigationView = (NavigationView) findViewById(R.id.navigation_menu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {


                    case R.id.phoneblacklist:
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.smsblacklist:
                        Intent intent1 = new Intent(phone.this, sms.class);
                        startActivity(intent1);
                        break;

                    case R.id.spam_mails:
                        Intent intent2 = new Intent(phone.this, news.class);
                        startActivity(intent2);
                        break;
                    case R.id.phonelist:
                        Intent intent3 = new Intent(phone.this, managephonelist.class);
                        startActivity(intent3);
                        break;

                    case R.id.smslist:
                        Intent intent4 = new Intent(phone.this, managesmslist.class);
                        startActivity(intent4);
                        break;

                    case R.id.about:
                        Intent intent5 = new Intent(phone.this, about.class);
                        startActivity(intent5);
                        break;


                    case R.id.bitswebsite:
                        Intent intent6 = new Intent(phone.this,TOI.class);
                        startActivity(intent6);
                        break;



                }
                return false;
            }
        });

        setUpToolbar();


    }


    public void initAndFillData() {
        recyclerViewPhone = findViewById(R.id.recyclerphone);
        list = new ArrayList<>();
        phoneRecyclerAdapter = new PhoneRecyclerAdapter(list, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(phone.this);
        recyclerViewPhone.setLayoutManager(mLayoutManager);
        recyclerViewPhone.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPhone.setHasFixedSize(true);
        recyclerViewPhone.setAdapter(phoneRecyclerAdapter);
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
                list.addAll(databaseHelper.getAllPhoneList(phone.this));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                phoneRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    private void setUpToolbar() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Call Blacklist");
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.phone_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.phone_clearlog) {
            databaseHelper.clearlogphoneblacklist();
            phoneRecyclerAdapter.notifyDataSetChanged();
            getDataFromSQLite();
        } else if (id == R.id.backup) {
            bakup();
        } else if (id == R.id.restore) {
            restore();
        }else if (id == R.id.addtoblack) {
            addblacklist();
        }

        else if (id == R.id.Refresh) {
            phoneRecyclerAdapter.notifyDataSetChanged();
            getDataFromSQLite();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == RESTORE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // get String data from Intent
                String filename = data.getStringExtra("filename");
                databaseHelper.restore(this, filename);
            }
        }
    }

    @Override
    //pressing back button twice to exit
    public void onBackPressed() {
        if (!bb) {
            final Toast toast = Toast.makeText(getApplicationContext(), "press back again to exit", Toast.LENGTH_SHORT);
            toast.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, 800);
            bb = true;
        } else {
            super.onBackPressed();
        }
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                bb = false;
            }
        }.start();
    }


    @Override
    protected void onResume() {
        super.onResume();

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();
        registerReceiver(connectivityReceiver, intentFilter);

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
                    .make(findViewById(R.id.drawerlayout), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }
    }

    public void bakup() {
        databaseHelper.backup(phone.this);
    }

    public void restore() {
        Intent intent = new Intent(phone.this, Restore.class);
        startActivityForResult(intent, RESTORE_REQUEST_CODE);
    }

    public void addblacklist() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_blackllist, null);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setView(dialogView);
        add = dialogView.findViewById(R.id.addblack);
        cancel = dialogView.findViewById(R.id.canceladd);
        numbertext = dialogView.findViewById(R.id.blacknumber);
        final AlertDialog ad = dialogBuilder.show();
        add.setOnClickListener(v -> {
            number = numbertext.getText().toString();
            if (number == "" || number.length() != 10) {
                Toast.makeText(phone.this, "Please Check The Number You Entered", Toast.LENGTH_LONG).show();
            } else {
                String tmpnum = "91" + number;
                databaseHelper.insertphone(tmpnum, 0, 0, "d", "", 1);
                databaseHelper.insertsms(tmpnum, "", 1, "d", "");
                ad.dismiss();
            }

        });
        cancel.setOnClickListener(v -> ad.dismiss());
    }
}
