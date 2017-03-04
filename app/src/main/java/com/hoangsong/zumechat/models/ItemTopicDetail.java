package com.hoangsong.zumechat.models;

/**
 * Created by Tang on 04/10/2016.
 */

public class ItemTopicDetail {
    private String item_en;
    private String item_vi;
    private String url;
    private String media_description;
    private boolean isShow_vi;
    private boolean isPay;

    public ItemTopicDetail(String item_en, String item_vi, String url, String media_description, boolean isShow_vi) {
        this.item_en = item_en;
        this.item_vi = item_vi;
        this.url = url;
        this.media_description = media_description;
        this.isShow_vi = isShow_vi;
        this.isPay = false;
    }

    public String getItem_en() {
        return item_en;
    }

    public void setItem_en(String item_en) {
        this.item_en = item_en;
    }

    public String getItem_vi() {
        return item_vi;
    }

    public void setItem_vi(String item_vi) {
        this.item_vi = item_vi;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMedia_description() {
        return media_description;
    }

    public void setMedia_description(String media_description) {
        this.media_description = media_description;
    }

    public boolean isShow_vi() {
        return isShow_vi;
    }

    public void setShow_vi(boolean show_vi) {
        isShow_vi = show_vi;
    }

    public boolean isPay() {
        return isPay;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }
}
