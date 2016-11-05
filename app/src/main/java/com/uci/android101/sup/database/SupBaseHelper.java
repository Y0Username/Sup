package com.uci.android101.sup.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by somarkoe on 11/5/16.
 */

public class SupBaseHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "supBase.db";

    public SupBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + SupDbSchema.SupTable.NAME + "(" +
            " _id integer primary key autoincrement, " +
            SupDbSchema.SupTable.Cols.UUID + ", " +
            SupDbSchema.SupTable.Cols.NAME + ", " +
            SupDbSchema.SupTable.Cols.DATE + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        return;
    }
}
