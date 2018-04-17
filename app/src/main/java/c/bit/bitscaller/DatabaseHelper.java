package c.bit.bitscaller;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "bitscaller.db";

    public static final String phonetablename = "PhoneTable";
    public static final String messagetable = "MessageTable";
    public static final String emailtablename = "EmailTable";
    public static final String rid = "Rid";
    public static final String number = "Number";
    public static final String bphone = "BPhone";
    public static final String lati = "Latitude";
    public static final String longi = "Longitude";
    public static final String date = "Date";
    public static final String time = "Time";
    public static final String email = "Email";
    public static final String status = "Status";
    public static final String body = "Body";

    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + phonetablename + "(" + rid + " INTEGER PRIMARY KEY AUTOINCREMENT, " + number + " TEXT, " + bphone + " INTEGER, " + lati + " REAL, " + longi + " REAL, " + date + " TEXT, '" + time + "' TEXT);");
        db.execSQL("create table " + messagetable + " (" + rid + " INTEGER PRIMARY KEY AUTOINCREMENT, " + number + " TEXT, " + body + " TEXT, " + status + " INTEGER, " + date + " TEXT, " + time + " TEXT);");
        db.execSQL("create table " + emailtablename + "(" + rid + " INTEGER PRIMARY KEY AUTOINCREMENT, " + email + " VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS PhoneTable");
        db.execSQL("DROP TABLE IF EXISTS MessageTable");
        db.execSQL("DROP TABLE IF EXISTS EmailTable");

        onCreate(db);
    }

    public DatabaseHelper open() throws SQLException {
        db = this.getWritableDatabase();
        return this;
    }

    public void close() {
        this.close();
    }

    public List<Pojo> getAllPhoneList(Context context) {
        List<Pojo> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM PhoneTable where BPhone=1 ORDER BY rid DESC", null);
        if (cursor.moveToFirst()) {
            do {
                Pojo listRecord = new Pojo();
                String name = getContactName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.number)), context);
                if (name == "")
                    listRecord.setName("Blocked Number");
                else
                    listRecord.setName(name);
                listRecord.setNumber(cursor.getString(cursor.getColumnIndex(DatabaseHelper.number)));
                listRecord.setLati(cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.lati)));
                listRecord.setLongi(cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.longi)));
                listRecord.setDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.date)));
                listRecord.setTime(cursor.getString(cursor.getColumnIndex(DatabaseHelper.time)));
                list.add(listRecord);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }


    public List<Pojo> getAllSMSList(Context context) {
        List<Pojo> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM MessageTable where Status=1 ORDER BY rid DESC", null);
        if (cursor.moveToFirst()) {
            do {
                Pojo listRecord = new Pojo();
                String name = getContactName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.number)), context);
                if (name == "")
                    listRecord.setName("Blocked Number");
                else
                    listRecord.setName(name);
                listRecord.setNumber(cursor.getString(cursor.getColumnIndex(DatabaseHelper.number)));
                listRecord.setBody(cursor.getString(cursor.getColumnIndex(DatabaseHelper.body)));
                listRecord.setDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.date)));
                listRecord.setTime(cursor.getString(cursor.getColumnIndex(DatabaseHelper.time)));
                list.add(listRecord);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Pojo> getAllManagePhoneList(Context context) {
        List<Pojo> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM PhoneTable GROUP BY Number ORDER BY rid DESC", null);
        if (cursor.moveToFirst()) {
            do {
                Pojo listRecord = new Pojo();
                String name = getContactName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.number)), context);
                if (name == "")
                    listRecord.setName("Blocked Number");
                else
                    listRecord.setName(name);
                listRecord.setNumber(cursor.getString(cursor.getColumnIndex(DatabaseHelper.number)));
                listRecord.setBphone(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.bphone)));
                list.add(listRecord);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Pojo> getAllManageSMSList(Context context) {
        List<Pojo> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM MessageTable GROUP BY Number ORDER BY rid DESC", null);
        if (cursor.moveToFirst()) {
            do {
                Pojo listRecord = new Pojo();
                String name = getContactName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.number)), context);
                if (name == "")
                    listRecord.setName("Blocked Number");
                else
                    listRecord.setName(name);
                listRecord.setNumber(cursor.getString(cursor.getColumnIndex(DatabaseHelper.number)));
                listRecord.setBsms(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.status)));
                list.add(listRecord);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public void clearlogphoneblacklist() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Delete from " + phonetablename + "", null);
        cursor.moveToFirst();
        cursor.close();
    }

    public void clearlogsmsblacklist() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Delete from " + messagetable + "", null);
        cursor.moveToFirst();
    }

    public void blockphone(String numb) {
        db = this.getWritableDatabase();
        String qry = "UPDATE PhoneTable SET Bphone=1 WHERE Number=" + numb + ";";
        db.execSQL(qry);
    }

    public void unblockphone(String numb) {
        db = this.getWritableDatabase();
        String qry = "UPDATE PhoneTable SET Bphone=0 WHERE Number=" + numb + ";";
        db.execSQL(qry);
    }

    public void blocksms(String numb) {
        db = this.getWritableDatabase();
        String qry = "UPDATE MessageTable SET Status=1 WHERE Number=" + numb + ";";
        db.execSQL(qry);
    }

    public void unblocksms(String numb) {
        db = this.getWritableDatabase();
        String qry = "UPDATE MessageTable SET Status=0 WHERE Number=" + numb + ";";
        db.execSQL(qry);
    }

    public int checkphoneblock(String numb) {
        int status = 999;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM PhoneTable where Number=" + numb + " ORDER BY rid DESC LiMIT 1", null);
        if (cursor.moveToFirst()) {
            status = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.bphone));
            cursor.close();
            db.close();
            if (status == 0)
                return 0;
            else if (status == 1)
                return 1;
        }
        return 999;
    }

    public int checksmsblock(String numb) {
        int status = 999;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM MessageTable where Number=" + numb + " ORDER BY rid DESC LiMIT 1", null);
        if (cursor.moveToFirst()) {
            status = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.bphone));
            cursor.close();
            db.close();
            if (status == 0)
                return 0;
            else if (status == 1)
                return 1;
        }
        return 999;
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

    public void insertphone(String numb, float lat, float lon, String dat, String tim, int bpho) {
        db = this.getWritableDatabase();
        String qry = "Insert into PhoneTable(" + number + "," + bphone + "," + lati + ", " + longi + ", " + date + ", " + time + ") Values (" + numb + ", " + bpho + ", " + lat + ", " + lon + ", '" + dat + "', '" + tim + "');";
        db.execSQL(qry);
    }


    public void insertsms(String numb, String mess, int stat, String da, String ti) {
        db = this.getWritableDatabase();
        String qry = "Insert into MessageTable(" + number + ", " + body + ", " + status + ", " + date + ", " + time + ") Values (" + numb + ", '" + mess + "', " + stat + ", '" + da + "', '" + ti + "');";
        db.execSQL(qry);
    }
}