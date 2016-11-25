package com.feicui.fragmentnews.bean;

import android.graphics.Bitmap;

/**
 * Created by zhangxingye on 2016/11/2 0002.
 */

public class JsonData {
    private String summary;
    private Bitmap bitmap;
    private String stamp;
    private String title;
    private String link;
    private String icon;
    private int nid;
    private int type;

    public JsonData(String summary, Bitmap bitmap, String stamp, String title, String link, String icon, int nid, int type) {
        this.summary = summary;
        this.bitmap = bitmap;
        this.stamp = stamp;
        this.title = title;
        this.link = link;
        this.icon = icon;
        this.nid = nid;
        this.type = type;
    }

    public JsonData(String summary, String icon, String stamp, String title, String link, int nid, int type) {
        this.summary = summary;
        this.icon = icon;
        this.stamp = stamp;
        this.title = title;
        this.link = link;
        this.nid = nid;
        this.type = type;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "JsonData{" +
                "summary='" + summary + '\'' +
                ", bitmap=" + bitmap +
                ", stamp='" + stamp + '\'' +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", icon='" + icon + '\'' +
                ", nid=" + nid +
                ", type=" + type +
                '}';
    }
}
