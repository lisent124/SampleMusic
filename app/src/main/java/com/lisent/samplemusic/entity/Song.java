package com.lisent.samplemusic.entity;

import java.util.Date;
import java.util.List;

public class Song {
    private int id;
    private String name;
    private int position;
    private int duration;
    private String url;
    private int albumId;
    private String albumName;

    private String albumPicUrl;
    private int artistId;
    private String artistName;

    private Long playTime;

    public Song(SearchBaseData.ResultDTO.SongsDTO song){
        id = song.getId();
        name = song.getName();
        albumId = song.getAlbum().getId();
        albumName = song.getAlbum().getName();
        albumPicUrl = song.getAlbum().getPicUrl();
        String string = "";
        List<SearchBaseData.ResultDTO.SongsDTO.ArtistsDTO> artists = song.getArtists();
        for (SearchBaseData.ResultDTO.SongsDTO.ArtistsDTO artist:
             artists) {
            string +="/" + artist.getName();
        }
        string = string.substring(1);
        artistName = string;
        artistId = -1;
    }

    public Song(SongBaseData song){
        id = song.getId();
        name = song.getName();

        albumId = song.getAl().getId();
        albumName = song.getAl().getName();
        albumPicUrl = song.getAl().getPicUrl();
        String string = "";
        List<SongBaseData.ArDTO> artists = song.getAr();
        for (SongBaseData.ArDTO artist:
                artists) {
            string += "/"+artist.getName();
        }
        string = string.substring(1);
        artistName = string;
        artistId = -1;

    }

    public Song() {

    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", position=" + position +
                ", duration=" + duration +
                ", url='" + url + '\'' +
                ", albumId=" + albumId +
                ", albumName='" + albumName + '\'' +
                ", albumPicUrl='" + albumPicUrl + '\'' +
                ", artistId=" + artistId +
                ", artistName='" + artistName + '\'' +
                ", playTime=" + playTime +
                '}';
    }

    public Long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Long playTime) {
        this.playTime = playTime;
    }

    public String getAlbumPicUrl() {
        return albumPicUrl;
    }

    public void setAlbumPicUrl(String albumPicUrl) {
        this.albumPicUrl = albumPicUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
