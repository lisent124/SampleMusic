package com.lisent.samplemusic.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lisent.samplemusic.entity.Song;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SongRecord {
    private MySQLiteHleper songPlayRecord ;
    private final String table = "songPlayRecord";
    private SQLiteDatabase db;

    public SongRecord(Context context) {
        songPlayRecord = new MySQLiteHleper(context,"mySong.db",null,1);
        db = songPlayRecord.getWritableDatabase();
    }

    private ContentValues songToContent(Song song){
        ContentValues content = new ContentValues();
        content.put("id", song.getId());
        content.put("name",song.getName());
        content.put("url",song.getUrl());
        content.put("singer",song.getArtistName());
        content.put("album",song.getAlbumName());
        content.put("cover",song.getAlbumPicUrl());
        Date date = new Date();
        long time = date.getTime();
        content.put("playTime",time);
        return content;
    }

    @SuppressLint("Range")
    private Song cursorToSong(Cursor c){
        int id = c.getInt(c.getColumnIndex("id"));
        String name = c.getString(c.getColumnIndex("name"));
        String singer = c.getString(c.getColumnIndex("singer"));
        String album = c.getString(c.getColumnIndex("album"));
        String url = c.getString(c.getColumnIndex("url"));
        String cover = c.getString(c.getColumnIndex("cover"));
        long playTime = c.getLong(c.getColumnIndex("playTime"));
        Song song = new Song();
        song.setId(id);
        song.setName(name);
        song.setArtistName(singer);
        song.setAlbumName(album);
        song.setAlbumPicUrl(cover);
        song.setUrl(url);
        song.setPlayTime(playTime);

        return song;
    }

    /**
     * 插入歌曲
     * @param song
     * @return
     */
    public long insertSong(Song song){
        long insert = db.insert(table, null, songToContent(song));
        return insert;
    }

    /**
     * 获取最近播放的歌曲信息
     * @return
     */
    public Song getLatestSong(){
        String sql =
                String.format("select * from %s order by = playTime desc", table);
        Cursor cursor = db.rawQuery(sql, new String[]{});
        if (cursor.moveToFirst()){
            return cursorToSong(cursor);
        }
        return null;
    }

    /**
     * 返回最近播放过的歌曲
     * @param page
     * @param limit
     * @return
     */
    @SuppressLint("Range")
    public List<Song> getSongs(int page, int limit){
        String sql =
                String.format("select * from %s order by playTime desc limit ?,?",table);
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(page), String.valueOf(limit)});
        List<Song> songs = new ArrayList<>();
        if(cursor.getCount() == 0) return null;
        while (cursor.moveToNext()){
            songs.add(cursorToSong(cursor));
        }
        return songs;
    }

    /**
     * 通过ID获得歌曲
     * @param songId
     * @return
     */
    public Song getSong(int songId){
        String sql =
                String.format("select * from %s where id = ?",table);
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(songId)});
        if (cursor.moveToFirst()){
            return cursorToSong(cursor);
        }
        return null;
    }

    /**
     * 删除歌曲
     * @param songId
     * @return
     */
    public int deleteSong(int songId){
        return db.delete(table, "id=?", new String[]{String.valueOf(songId)});
    }

    /**
     * 唯一的插入，保证数据库中没有相同的数据
     * @param song
     * @return
     */
    public Long uniqueInsertSong(Song song){
        int id = song.getId();
        if (getSong(id) != null){
            deleteSong(id);
        }
        return insertSong(song);
    }

    /**
     * 获取下一首歌曲
     * @param songId
     * @return
     */
    public Song getNextSong(int songId){
        Song song = getSong(songId);
        if (song == null) return null;
        String sql =
                String.format("select * from %s where playTime < ? order by playTime desc limit 1",table);
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(song.getPlayTime())});
        if (cursor.moveToFirst()){
            return cursorToSong(cursor);
        }
        return null;
    }

    /**
     * 获取上一首歌曲
     * @param songId
     * @return
     */
    public Song getPrevSong(int songId){
        Song song = getSong(songId);
        if (song == null) return null;
        String sql =
                String.format("select * from %s where playTime > ? order by playTime limit 1",table);
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(song.getPlayTime())});
        if (cursor.moveToFirst()){
            return cursorToSong(cursor);
        }
        return null;
    }

}
