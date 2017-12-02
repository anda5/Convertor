package com.example.gogo.orar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GoGo on 11/26/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "dbOrar";

    // Contacts table name
    private static final String TABLE_ORAR= "orar";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_ZIUA = "ziua";
    private static final String KEY_MATERIE = "materie";
    private static final String KEY_PROFESOR = "profesor";
    private static final String KEY_SALA = "sala";
    private static final String KEY_TIP = "tip";
    private static final String KEY_ORA_INCEPUT = "orainceput";
    private static final String KEY_ORA_SFARSIT = "orasfarsit";
    private static final String KEY_PARA = "para";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_ORAR + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ZIUA + " TEXT,"
                + KEY_MATERIE + " TEXT,"
                + KEY_PROFESOR+ " TEXT,"
                + KEY_SALA + " TEXT,"
                + KEY_TIP + " TEXT,"
                + KEY_ORA_INCEPUT + " TEXT,"
                + KEY_ORA_SFARSIT + " TEXT,"
                + KEY_PARA + " INTEGER DEFAULT 0"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORAR);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
       public int addContact(Orar orar) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ZIUA, orar.getZiua());
        values.put(KEY_MATERIE, orar.getMaterie());
        values.put(KEY_PROFESOR, orar.getProfesor());
        values.put(KEY_SALA, orar.getSala());
        values.put(KEY_TIP,  orar.getTip());
        values.put(KEY_ORA_INCEPUT, orar.getOraInceput());
        values.put(KEY_ORA_SFARSIT, orar.getOraSfarsit());
        values.put(KEY_PARA, orar.getPara());


        // Inserting Row
           int id = (int) db.insert(TABLE_ORAR, null, values);
        return id;
    }

    // Getting single contact
    Orar getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ORAR, new String[] { KEY_ID,
                        KEY_ZIUA, KEY_MATERIE, KEY_PROFESOR, KEY_SALA, KEY_TIP, KEY_ORA_INCEPUT,KEY_ORA_SFARSIT,KEY_PARA }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        boolean bol = cursor.getString(8).equals("1")? true : false;

        Orar orar = new Orar(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),cursor.getString(3), cursor.getString(4),
                cursor.getString(5), cursor.getString(6),cursor.getString(7), bol);
        // return contact
        return orar;
    }

    // Getting All Contacts
    public List<Orar> getAllContacts() {
        List<Orar> contactList = new ArrayList<Orar>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ORAR;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Orar orar = new Orar();
                orar.setId(Integer.parseInt(cursor.getString(0)));
                orar.setZiua(cursor.getString(1));
                orar.setMaterie(cursor.getString(2));
                orar.setProfesor(cursor.getString(3));
                orar.setSala(cursor.getString(4));
                orar.setTip(cursor.getString(5));
                orar.setOraInceput(cursor.getString(6));
                orar.setOraSfarsit(cursor.getString(7));
                orar.setPara(cursor.getString(8).equals("1")? true: false);
                // Adding contact to list
                contactList.add(orar);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Updating single contact
    public int updateContact(Orar orar,String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ZIUA, orar.getZiua());
        values.put(KEY_MATERIE, orar.getMaterie());
        values.put(KEY_PROFESOR, orar.getProfesor());
        values.put(KEY_SALA, orar.getSala());
        values.put(KEY_TIP,  orar.getTip());
        values.put(KEY_ORA_INCEPUT, orar.getOraInceput());
        values.put(KEY_ORA_SFARSIT, orar.getOraSfarsit());
        values.put(KEY_PARA, orar.getPara());

        // updating row
        return db.update(TABLE_ORAR, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    // Deleting single contact
    public void deleteContact(Orar contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ORAR, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
        db.close();
    }


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ORAR;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}