package com.example.demo.health;

import java.time.Instant;

public class Link {

    private String code;
    private String targetUrl;
    private String createAt;
    private int clicks;

    public Link(String code, String targetUrl){
        this.code = code;
        this.targetUrl = targetUrl;
        this.clicks = 0;
        this.createAt = Instant.now().toString();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }
}

