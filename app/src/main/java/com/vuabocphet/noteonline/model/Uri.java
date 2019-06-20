package com.vuabocphet.noteonline.model;

public class Uri {

    private String id;
    private String url;

    public Uri(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public Uri() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
