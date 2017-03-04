package com.hoangsong.zumechat.models;

import java.util.ArrayList;

/**
 * Created by Tang on 17/10/2016.
 */

public class MasterData {
    private ArrayList<Category> listNewsCategory;

    public MasterData(ArrayList<Category> listNewsCategory) {
        this.listNewsCategory = listNewsCategory;
    }

    public ArrayList<Category> getListNewsCategory() {
        return listNewsCategory;
    }

    public void setListNewsCategory(ArrayList<Category> listNewsCategory) {
        this.listNewsCategory = listNewsCategory;
    }
}
