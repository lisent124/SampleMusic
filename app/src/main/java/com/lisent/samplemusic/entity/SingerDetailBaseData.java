package com.lisent.samplemusic.entity;

import java.util.List;

public class SingerDetailBaseData {

    private List<SongBaseData> hotSongs;

    private Singer artist;

    private int code;

    public List<SongBaseData> getHotSongs() {
        return hotSongs;
    }

    public void setHotSongs(List<SongBaseData> hotSongs) {
        this.hotSongs = hotSongs;
    }

    public Singer getArtist() {
        return artist;
    }

    public void setArtist(Singer artist) {
        this.artist = artist;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
