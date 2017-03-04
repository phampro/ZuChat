package com.hoangsong.zumechat.models;

import java.util.List;

/**
 * Created by Tang on 14/10/2016.
 */

public class CategoryTopic {
    private String name;
    private List<TopicDetail> listTopic;

    public CategoryTopic(String name, List<TopicDetail> listTopic) {
        this.name = name;
        this.listTopic = listTopic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TopicDetail> getListTopic() {
        return listTopic;
    }

    public void setListTopic(List<TopicDetail> listTopic) {
        this.listTopic = listTopic;
    }
}
