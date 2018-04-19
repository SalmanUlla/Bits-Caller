package c.bit.bitscaller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AlertDial extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    Button dismiss;
    TextView name;
    TextView number;

    DatabaseHelper db;

    private float longitude;
    private float latitude;

    private LocationManager locationManager;
    private LocationListener locationListener;

    DecimalFormat loc_precision = new DecimalFormat("###.######");

    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    String d = (String) df.format(Calendar.getInstance().getTime());
    String date = (String) d.substring(0, 10);
    String time = (String) d.substring(11, 16);

    String numb;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new DatabaseHelper(AlertDial.this);
        super.onCreate(savedInstanceState);
        numb = getIntent().getStringExtra("NUMBER");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = (float) Double.parseDouble(loc_precision.format(location.getLatitude()));
                longitude = (float) Double.parseDouble(loc_precision.format(location.getLongitude()));

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.INTERNET}, 10);
        } else
            refresh_loc();


        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.popup_phone, null);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setView(dialogView);
        dismiss = dialogView.findViewById(R.id.dismiss);
        name = dialogView.findViewById(R.id.callername);
        number = dialogView.findViewById(R.id.callernumber);
        name.setText(getIntent().getStringExtra("NAME"));
        number.setText(numb);
        final AlertDialog ad = dialogBuilder.show();
        getLoc();
        dismiss.setOnClickListener(v -> {
            ad.dismiss();
            finish();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    refresh_loc();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void refresh_loc() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, locationListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("StaticFieldLeak")
    private void getLoc() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                refresh_loc();
                do {
                } while (latitude == 0.0);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                String status = getIntent().getStringExtra("Type");
                switch (status) {

                    case "NU":
                        db.insertphone(numb, latitude, longitude, date, time, 1);
                        break;
                    case "NK":
                        db.insertphone(numb, latitude, longitude, date, time, 0);
                        break;
                    case "NUB":
                        db.insertphone(numb, latitude, longitude, date, time, 0);
                        break;
                    case "NB":
                        db.insertphone(numb, latitude, longitude, date, time, 1);
                        break;
                    case "EB":
                        db.insertphone(numb, latitude, longitude, date, time, 1);
                        break;
                    case "EUB":
                        db.insertphone(numb, latitude, longitude, date, time, 0);
                        break;
                }
            }
        }.execute();
    }

}
