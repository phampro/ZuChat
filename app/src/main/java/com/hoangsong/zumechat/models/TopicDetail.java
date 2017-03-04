package com.hoangsong.zumechat.models;

import java.util.List;

/**
 * Created by Tang on 04/10/2016.
 */

public class TopicDetail {
    private int id;
    private int cat_id;
    private String cat_name;
    private String title_en;
    private String title_vi;
    private List<ItemTopicDetail> content_topic;
    private String image_url;
    private String topic_source;
    private String topic_url;
    private String topic_date;
    private int is_video;
    private boolean is_favorite;
    private boolean isFirstShow; //sub
    private boolean isPay;// sub

    private boolean isHasAds = false;// sub

    public TopicDetail(int id, int cat_id, String cat_name, String title_en, String title_vi, List<ItemTopicDetail> content_topic, String image_url, String topic_source, String topic_url, String topic_date, int is_video, boolean is_favorite) {
        this.id = id;
        this.cat_id = cat_id;
        this.cat_name = cat_name;
        this.title_en = title_en;
        this.title_vi = title_vi;
        this.content_topic = content_topic;
        this.image_url = image_url;
        this.topic_source = topic_source;
        this.topic_url = topic_url;
        this.topic_date = topic_date;
        this.is_video = is_video;
        this.is_favorite = is_favorite;
        this.isFirstShow = false;
        this.isPay = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public boolean is_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(boolean is_favorite) {
        this.is_favorite = is_favorite;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getTitle_vi() {
        return title_vi;
    }

    public void setTitle_vi(String title_vi) {
        this.title_vi = title_vi;
    }

    public List<ItemTopicDetail> getContent_topic() {
        return content_topic;
    }

    public void setContent_topic(List<ItemTopicDetail> content_topic) {
        this.content_topic = content_topic;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTopic_source() {
        return topic_source;
    }

    public void setTopic_source(String topic_source) {
        this.topic_source = topic_source;
    }

    public String getTopic_url() {
        return topic_url;
    }

    public void setTopic_url(String topic_url) {
        this.topic_url = topic_url;
    }

    public String getTopic_date() {
        return topic_date;
    }

    public void setTopic_date(String topic_date) {
        this.topic_date = topic_date;
    }

    public int getIs_video() {
        return is_video;
    }

    public void setIs_video(int is_video) {
        this.is_video = is_video;
    }

    public boolean isFirstShow() {
        return isFirstShow;
    }

    public void setFirstShow(boolean firstShow) {
        isFirstShow = firstShow;
    }

    public boolean isPay() {
        return isPay;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }

    public boolean isHasAds() {
        return isHasAds;
    }

    public void setHasAds(boolean hasAds) {
        isHasAds = hasAds;
    }
}
