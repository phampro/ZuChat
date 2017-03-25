package com.hoangsong.zumechat.models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pvthiendeveloper on 3/17/2017.
 */

public class Advertisement {
    private String id;
    private String name;
    private String start_date;
    private String end_date;
    private String created_on;
    private String content;
    private String url;
    private boolean isPublish;
    private boolean isShow;
    private ArrayList<Image> images;
    private ArrayList<String> countries;

    public Advertisement() {
    }

    public Advertisement(String id, String name, String start_date, String end_date, String created_on, String content, String url, boolean isPublish, boolean isShow, ArrayList<Image> images, ArrayList<String> countries) {
        this.id = id;
        this.name = name;
        this.start_date = start_date;
        this.created_on = created_on;
        this.content = content;
        this.url = url;
        this.isPublish = isPublish;
        this.isShow = isShow;
        this.images = images;
        this.countries = countries;
        this.end_date = end_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isPublish() {
        return isPublish;
    }

    public void setPublish(boolean publish) {
        isPublish = publish;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public ArrayList<String> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<String> countries) {
        this.countries = countries;
    }
}
