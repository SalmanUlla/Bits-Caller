package c.bit.bitscaller;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class IncomingCall extends BroadcastReceiver {
    DatabaseHelper db;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    String d = (String) df.format(Calendar.getInstance().getTime());
    String date = (String) d.substring(0, 10);
    String time = (String) d.substring(11, 16);

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            db = new DatabaseHelper(context);
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //TODO:IMPORTANT TO CHANGE
            float longitude = (float) 35.2525; //(float) location.getLongitude();
            float latitude = (float) 122.8584;// (float) location.getLatitude();
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                String name = getContactName(number, context);
                if ((name == "") && (db.checkphoneblock(number) == 999)) {
                    Intent i = new Intent(context, AlertDial.class);
                    i.putExtra("NAME", "Blocked Caller");
                    i.putExtra("NUMBER", number);
                    db.insertphone(number, latitude, longitude, date, time, 1);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                } else if ((name != "") && (db.checkphoneblock(number) == 999)) {
                    Intent i = new Intent(context, AlertDial.class);
                    i.putExtra("NAME", name);
                    i.putExtra("NUMBER", number);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    db.insertphone(number, latitude, longitude, date, time, 0);
                    context.startActivity(i);
                } else if ((name == "") && (db.checkphoneblock(number) == 0)) {
                    Intent i = new Intent(context, AlertDial.class);
                    i.putExtra("NAME", "Unknown Caller");
                    i.putExtra("NUMBER", number);
                    db.insertphone(number, latitude, longitude, date, time, 0);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                } else if ((name == "") && (db.checkphoneblock(number) == 1)) {
                    Intent i = new Intent(context, AlertDial.class);
                    i.putExtra("NAME", "Blocked Caller");
                    i.putExtra("NUMBER", number);
                    db.insertphone(number, latitude, longitude, date, time, 1);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                } else if ((name != "") && (db.checkphoneblock(number) == 1)) {
                    Intent i = new Intent(context, AlertDial.class);
                    i.putExtra("NAME", "Blocked Caller");
                    i.putExtra("NUMBER", number);
                    db.insertphone(number, latitude, longitude, date, time, 1);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                } else if ((name != "") && (db.checkphoneblock(number) == 0)) {
                    Intent i = new Intent(context, AlertDial.class);
                    i.putExtra("NAME", name);
                    i.putExtra("NUMBER", number);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    db.insertphone(number, latitude, longitude, date, time, 0);
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
