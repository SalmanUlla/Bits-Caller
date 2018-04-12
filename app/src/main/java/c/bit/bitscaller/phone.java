package c.bit.bitscaller;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

public class phone extends AppCompatActivity {

    private boolean bb;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("PHONE BLACKLIST");


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
                        Intent intent2 = new Intent(phone.this, email.class);
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
                        Intent intent = new Intent(phone.this, about.class);
                        startActivity(intent);
                        break;


                }
                return false;
            }
        });
        setUpToolbar();

    }

    private void setUpToolbar() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("PHONE BLACKLIST");
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
        int id =item.getItemId();
        if(id==R.id.phone_clearlog)
        {
            //code for phone clear log




        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    //pressing back button twice to exit
    public void onBackPressed() {
        if (!bb) {
            Toast.makeText(this, "press back again to exit", Toast.LENGTH_LONG).show();
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

}

