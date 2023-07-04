package com.lisent.samplemusic.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * 为统计歌曲播放记录所创建的sqlite
 */
public class MySQLiteHleper extends SQLiteOpenHelper {
    public MySQLiteHleper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createSongPlayRecord = "create table songPlayRecord(" +
                "id integer primary key," +
                "name varchar(64)," +
                "singer varchar(32)," +
                "album varchar(32)," +
                "url text," +
                "cover text," +
                "playTime datetime)";
        sqLiteDatabase.execSQL(createSongPlayRecord);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
