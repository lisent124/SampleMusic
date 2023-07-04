package com.lisent.samplemusic.entity;

import java.util.List;

public class SongBaseData {

    private String name;
    private Integer id;
    private List<ArDTO> ar;
    private AlDTO al;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ArDTO> getAr() {
        return ar;
    }

    public void setAr(List<ArDTO> ar) {
        this.ar = ar;
    }

    public AlDTO getAl() {
        return al;
    }

    public void setAl(AlDTO al) {
        this.al = al;
    }

    public static class AlDTO {
        private Integer id;
        private String name;
        private String picUrl;

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

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }
    }

    public static class ArDTO {
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
    }
}
