package com.hfad.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoListDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "todolist";
    private static final int DB_VERSION = 1;

    TodoListDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    public Boolean insertRecord(String record, boolean done, String date_create) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues recordValues = new ContentValues();
        recordValues.put("RECORD", record);
        recordValues.put("DONE", done);
        recordValues.put("DATE_CREATE", date_create);
        long res = db.insert("LIST_RECORD", null, recordValues);
        if (res == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean updateData(int _id, String record, boolean done)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues recordValues = new ContentValues();
        recordValues.put("RECORD", record);
        recordValues.put("DONE", done);
        Cursor cursor = db.rawQuery("Select * from LIST_RECORD where _id = ?",
                new String[]{String.valueOf(_id)});
        if (cursor.getCount() > 0) {
            long result = db.update("LIST_RECORD", recordValues, "_id = ?",
                    new String[]{String.valueOf(_id)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Boolean deleteData(int _id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from LIST_RECORD where _id = ?",
                new String[]{String.valueOf(_id)});
        if (cursor.getCount() > 0) {
            long result = db.delete("LIST_RECORD", "_id = ?",
                    new String[]{String.valueOf(_id)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE LIST_RECORD(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "RECORD TEXT," +
                    "DONE NUMERIC," +
                    "DATE_CREATE TEXT);");
            //     insertRecord("text 12", false, "2020-10-10");
            // insertRecord("text 2", true, "2020-10-10");
        }
        if (oldVersion == 2) {
          //  insertRecord("text 122", true, "2020-10-10");
        }
    }


    public Cursor getData() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from LIST_RECORD", null);
        return  cursor;
    }
}
