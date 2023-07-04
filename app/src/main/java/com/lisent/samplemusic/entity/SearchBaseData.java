package com.lisent.samplemusic.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchBaseData {

    private ResultDTO result;

    private Integer code;

    public ResultDTO getResult() {
        return result;
    }

    public void setResult(ResultDTO result) {
        this.result = result;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public static class ResultDTO {

        private List<SongsDTO> songs;

        public List<SongsDTO> getSongs() {
            return songs;
        }

        public void setSongs(List<SongsDTO> songs) {
            this.songs = songs;
        }

        public static class SongsDTO {

            private Integer id;

            private String name;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<ArtistsDTO> getArtists() {
                return artists;
            }

            public void setArtists(List<ArtistsDTO> artists) {
                this.artists = artists;
            }

            public AlbumDTO getAlbum() {
                return album;
            }

            public void setAlbum(AlbumDTO album) {
                this.album = album;
            }


            private List<ArtistsDTO> artists;

            private AlbumDTO album;


            public static class AlbumDTO {

                private Integer id;
                private String name;
                private String picUrl;

                public String getPicUrl() {
                    return picUrl;
                }

                public void setPicUrl(String picUrl) {
                    this.picUrl = picUrl;
                }

                public Integer getId() {
                    return id;
                }

                public void setId(Integer id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }


            }
            public static class ArtistsDTO {

                private Integer id;

                public Integer getId() {
                    return id;
                }

                public void setId(Integer id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                private String name;
            }
        }
    }
}
