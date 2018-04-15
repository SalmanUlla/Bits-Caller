package c.bit.bitscaller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
public class SmsList extends BroadcastReceiver {

    private static final String TAG = "Message recieved";
    DatabaseHelper db;

    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    String d = (String) df.format(Calendar.getInstance().getTime());
    String date = (String) d.substring(0, 10);
    String time = (String) d.substring(11, 16);

    @Override
    public void onReceive(Context context, Intent intent) {
        db = new DatabaseHelper(context);
        Bundle pudsBundle = intent.getExtras();
        Object[] pdus = (Object[]) pudsBundle.get("pdus");
        SmsMessage messages = SmsMessage.createFromPdu((byte[]) pdus[0]);
        String name = getContactName(messages.getOriginatingAddress(), context);
        Intent smsIntent = new Intent(context, AlertSMS.class);
        smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (name == "") {
            smsIntent.putExtra("name", "Blocked Number");
            db.insertsms(messages.getOriginatingAddress(), messages.getMessageBody(), 1, date, time);
        } else {
            smsIntent.putExtra("name", name);
            db.insertsms(messages.getOriginatingAddress(), messages.getMessageBody(), 0, date, time);
        }
        smsIntent.putExtra("MessageNumber", messages.getOriginatingAddress());
        smsIntent.putExtra("Message", messages.getMessageBody());
        context.startActivity(smsIntent);
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