package com.example.memo;

import org.litepal.crud.LitePalSupport;

public class Memo extends LitePalSupport {
    private String date;
    private String content;
    private String title;
    private Long id;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
