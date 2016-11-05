package com.uci.android101.sup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.uci.android101.sup.database.SupBaseHelper;
import com.uci.android101.sup.database.SupDbSchema;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by somarkoe on 11/5/16.
 */

public class SupDao {

    private static SupDao sSupDao;
    private SQLiteDatabase mDatabase;

    public static SupDao get(Context context) {
        if (sSupDao == null) {
            sSupDao = new SupDao(context);
        }
        return sSupDao;
    }

    private SupDao(Context context) {
        mDatabase = new SupBaseHelper(context.getApplicationContext()).getWritableDatabase();
    }

    public void addSup(Sup sup) {
        ContentValues values = getContentValues(sup);
        mDatabase.insert(SupDbSchema.SupTable.NAME, null, values);
    }

    public String getLatestSup(String latestSupFormat, String dateFormat) {
        SupCursorWrapper cursor = querySups();
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getLatestSup(latestSupFormat, dateFormat);
        } finally {
            cursor.close();
        }
    }

    private SupCursorWrapper querySups() {
        Cursor cursor = mDatabase.query(
                SupDbSchema.SupTable.NAME,
                null,
                null,
                null,
                null,
                null,
                SupDbSchema.SupTable.Cols.DATE + " DESC",
                "1"
        );
        return new SupCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Sup sup) {
        ContentValues values = new ContentValues();
        values.put(SupDbSchema.SupTable.Cols.UUID, sup.getId().toString());
        values.put(SupDbSchema.SupTable.Cols.NAME, sup.getFriend().getFriendName());
        values.put(SupDbSchema.SupTable.Cols.DATE, sup.getDate().getTime());
        return values;
    }

    private class SupCursorWrapper extends CursorWrapper {
        public SupCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public String getLatestSup(String latestSupFormat, String dateFormat) {
            DateTime dateTime = new DateTime(getLong(getColumnIndex(SupDbSchema.SupTable.Cols.DATE)));
            DateTimeFormatter fmt = DateTimeFormat.forPattern(dateFormat);
            String dateString = fmt.print(dateTime);
            String friendName = getString(getColumnIndex(SupDbSchema.SupTable.Cols.NAME));
            return String.format(latestSupFormat, friendName, dateString);
        }
    }

}
