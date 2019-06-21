package com.vuabocphet.noteonline.model;

public class NoteModel {
    public String id;
    public String subject;
    public String date;
    public String node;
    public String img;
    public String url;

    public NoteModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public NoteModel(String id, String subject, String date, String node, String img, String url) {
        this.id = id;
        this.subject = subject;
        this.date = date;
        this.node = node;
        this.img = img;
        this.url = url;
    }
}
