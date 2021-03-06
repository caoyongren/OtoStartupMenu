package com.android.startupmenu.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/*
* Created by Cao Yongren on 15-5-2017
*/

public class StartupMenuUtil {
    /*
    * When run app, need change SqlDataBase and sharedPreference.
    */
    public static void updateDataStorage(Context mContext, String pkgName) {
        StartupMenuSqliteOpenHelper  msoh = new StartupMenuSqliteOpenHelper(mContext,
                                                 "StartupMenu_database.db", null, 1);
        SQLiteDatabase mdb = msoh.getWritableDatabase();
        Cursor cursor = mdb.rawQuery("select * from " + TableIndexDefine.TABLE_APP_PERPO +
                                       " where " + TableIndexDefine.COLUMN_PERPO_PKGNAME +
                                       " = ? ", new String[] { pkgName });
        cursor.moveToNext();
        if (cursor.moveToFirst()) {
            int numbers = cursor.getInt(cursor.getColumnIndex(
                                     TableIndexDefine.COLUMN_PERPO_CLICK_NUM));
            numbers ++;
            ContentValues values = new ContentValues();
            values.put(TableIndexDefine.COLUMN_PERPO_CLICK_NUM, numbers);
            mdb.update(TableIndexDefine.TABLE_APP_PERPO, values, TableIndexDefine.
                       COLUMN_PERPO_PKGNAME + " = ?", new String[] { pkgName });
            SharedPreferences sharedPreference = mContext.getSharedPreferences("click",
                                                                 Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreference.edit();
            String type = sharedPreference.getString("type", "sortName");
            int order = sharedPreference.getInt("order", 0);
            editor.clear();
            editor.putBoolean("isClickApp", true);
            editor.putString("type", type);
            editor.putInt("order", order);
            editor.commit();
        }
    }
}
