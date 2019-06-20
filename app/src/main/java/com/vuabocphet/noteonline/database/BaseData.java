package com.vuabocphet.noteonline.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseData {

    private final String DB_NAME = "NOTE";
    private final String TB_NAME = "Note";
    private final int DB_VERSION = 1;
    private SQLiteDatabase database;

    public BaseData(Context context) {
        OpenHelper helper = new OpenHelper(context);
        database = helper.getWritableDatabase();

    }

    public class OpenHelper extends SQLiteOpenHelper {
        public OpenHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS Note(_id INTEGER PRIMARY KEY AUTOINCREMENT,date NVARCHAR,note NVARCHAR,img NVARCHAR,id_note NVARCHAR,subject NVARCHAR)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS Note");
            onCreate(db);
        }
    }

    public long insert(String date, String note, String img,String id_note,String subject) {
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("note", note);
        values.put("img", img);
        values.put("id_note", id_note);
        values.put("subject", subject);
        return database.insert(TB_NAME, null, values);
    }


    public void update(String date, String note, String img, int id,String subject) {
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("note", note);
        values.put("img", img);
        values.put("subject", subject);
        database.update(TB_NAME, values, "_id=" + id, null);
    }

    public void delete(int id) {
        database.delete(TB_NAME, "_id=" + id, null);
    }

    public void deleteTABLE(){
        database.execSQL("DELETE FROM Note");
    }

    public Cursor getData() {
        return database.query(TB_NAME, null, null, null, null, null, null);
    }
}
