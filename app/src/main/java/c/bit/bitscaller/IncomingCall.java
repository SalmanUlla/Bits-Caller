package c.bit.bitscaller;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static java.lang.System.exit;

public class IncomingCall extends BroadcastReceiver {
    DatabaseHelper db;
    Context ctx;

    @Override
    public void onReceive(Context context, Intent intent) {
        ctx = context;
        db = new DatabaseHelper(context);
        try {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if(number=="")
                exit(0);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                String name = getContactName(number, context);
                if ((name == "") && (db.checkphoneblock(number) == 999)) {
                    Intent i = new Intent(context, AlertDial.class);
                    i.putExtra("NAME", "Blocked Caller");
                    i.putExtra("NUMBER", number);
                    i.putExtra("Type", "NU");
                    //db.insertphone(number, latitude, longitude, date, time, 1);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                } else if ((name != "") && (db.checkphoneblock(number) == 999)) {
                    Intent i = new Intent(context, AlertDial.class);
                    i.putExtra("NAME", name);
                    i.putExtra("NUMBER", number);
                    i.putExtra("Type", "NK");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // db.insertphone(number, latitude, longitude, date, time, 0);
                    context.startActivity(i);
                } else if ((name == "") && (db.checkphoneblock(number) == 0)) {
                    Intent i = new Intent(context, AlertDial.class);
                    i.putExtra("NAME", "Unknown Caller");
                    i.putExtra("NUMBER", number);
                    i.putExtra("Type", "NUB");
                    //  db.insertphone(number, latitude, longitude, date, time, 0);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                } else if ((name == "") && (db.checkphoneblock(number) == 1)) {
                    Intent i = new Intent(context, AlertDial.class);
                    i.putExtra("NAME", "Blocked Caller");
                    i.putExtra("Type", "NB");
                    i.putExtra("NUMBER", number);
                    // db.insertphone(number, latitude, longitude, date, time, 1);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                } else if ((name != "") && (db.checkphoneblock(number) == 1)) {
                    Intent i = new Intent(context, AlertDial.class);
                    i.putExtra("NAME", "Blocked Caller");
                    i.putExtra("NUMBER", number);
                    i.putExtra("Type", "EB");
                    //   db.insertphone(number, latitude, longitude, date, time, 1);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                } else if ((name != "") && (db.checkphoneblock(number) == 0)) {
                    Intent i = new Intent(context, AlertDial.class);
                    i.putExtra("NAME", name);
                    i.putExtra("NUMBER", number);
                    i.putExtra("Type", "EUB");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //  db.insertphone(number, latitude, longitude, date, time, 0);
                    context.startActivity(i);
                }

            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public String getContactName(final String phoneNumber, Context context) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName = "";
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(0);
            }
            cursor.close();
        }

        return contactName;
    }
}
