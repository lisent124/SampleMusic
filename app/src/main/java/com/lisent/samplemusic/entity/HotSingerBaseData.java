package com.lisent.samplemusic.entity;

import java.util.List;

public class HotSingerBaseData {
    private int code;

    private Boolean more;

    private List<Singer> artists;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Boolean getMore() {
        return more;
    }

    public void setMore(Boolean more) {
        this.more = more;
    }

    public List<Singer> getArtists() {
        return artists;
    }

    public void setArtists(List<Singer> artists) {
        this.artists = artists;
    }
}
