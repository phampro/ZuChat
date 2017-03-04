package com.hoangsong.zumechat.helpers;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.hoangsong.zumechat.ZuMeChat;
import com.hoangsong.zumechat.models.AccountInfo;
import com.hoangsong.zumechat.models.CategoryTopic;
import com.hoangsong.zumechat.models.MasterData;
import com.hoangsong.zumechat.models.TopicDetail;

public class Prefs {
    private static final String PREFERENCE_IMAGE_NAME = "pref.currentimagename";
    private static final String PREFERENCES_TEMPFOLDER = "pref.tempfolder";
    private static final String PREFERENCES_HELPER = "prefhelper";
    private static final String PREFERENCE_DEVICE_ID = "pref.deviceid";
    private static final String PREFERENCE_REGISTER_DEVICE_OK = "pref.device_registed";
    private static final String PREFERENCE_LOGIN_DATA = "pref.login_data";
    private static final String PREFERENCE_READ_SPEED = "pref.readspeed";
    private static final String PREFERENCE_EMAIL = "pref.email";
    private static final String PREFERENCE_NEW_CATEGORY = "pref.newcategory";

    private static SharedPreferences getPrefs() {
        return ZuMeChat.getInstance().getSharedPreferences(PREFERENCES_HELPER, Context.MODE_PRIVATE);
    }

    //GCM
    public static void setDeviceId(String deviceID){
        getPrefs().edit().putString(PREFERENCE_DEVICE_ID, deviceID).commit();
    }
    public static String getDeviceID(){
        return getPrefs().getString(PREFERENCE_DEVICE_ID, "");
    }
    public static void setDeviceRegister(boolean registed){
        getPrefs().edit().putBoolean(PREFERENCE_REGISTER_DEVICE_OK, registed).commit();
    }
    public static boolean getDeviceRegister(){
        return getPrefs().getBoolean(PREFERENCE_REGISTER_DEVICE_OK, false);
    }

    public static String getCurrentImageName() {
        return getPrefs().getString(PREFERENCE_IMAGE_NAME, "");
    }

    public static void setCurrentImageName(String value) {
        getPrefs().edit().putString(PREFERENCE_IMAGE_NAME, value).commit();
    }

    public static boolean getTempFolder() {
        return getPrefs().getBoolean(PREFERENCES_TEMPFOLDER, false);
    }

    public static void setTempFolder(boolean value) {
        getPrefs().edit().putBoolean(PREFERENCES_TEMPFOLDER, value).commit();
    }

    public static String getDataString(String PrefName) {
        return getPrefs().getString(PrefName, "");
    }

    public static void setDataString(String PrefName,String value) {
        getPrefs().edit().putString(PrefName, value).commit();
    }

    public static void setReadSpeed(float speed){
        getPrefs().edit().putFloat(PREFERENCE_READ_SPEED, speed).commit();
    }
    public static float getReadSpeed(){
        return getPrefs().getFloat(PREFERENCE_READ_SPEED, 1f);
    }


    public static void setUserInfo(AccountInfo loginData) {
        String data = "";
        if (loginData != null) {
            Gson gson = new Gson();
            data = gson.toJson(loginData);
        }
        getPrefs().edit().putString(PREFERENCE_LOGIN_DATA, data).commit();
    }

    public static AccountInfo getUserInfo() {
        Gson gson = new Gson();
        String data = getPrefs().getString(PREFERENCE_LOGIN_DATA, "");
        return !data.equalsIgnoreCase("") ? gson.fromJson(data, AccountInfo.class) : null;
    }

    public static String parseString(TopicDetail topicDetail) {
        String data = "";
        if (topicDetail != null) {
            Gson gson = new Gson();
            data = gson.toJson(topicDetail);
        }
        return  data;
    }

    public static TopicDetail parseObject(String topicDetailStr) {
        Gson gson = new Gson();
        String data = topicDetailStr;
        return !data.equalsIgnoreCase("") ? gson.fromJson(data, TopicDetail.class) : null;
    }

    public static String parseCategoryString(CategoryTopic topicDetail) {
        String data = "";
        if (topicDetail != null) {
            Gson gson = new Gson();
            data = gson.toJson(topicDetail);
        }
        return  data;
    }

    public static CategoryTopic parseStringCategory(String topicDetailStr) {
        Gson gson = new Gson();
        String data = topicDetailStr;
        return !data.equalsIgnoreCase("") ? gson.fromJson(data, CategoryTopic.class) : null;
    }

    public static void setEmail(String email){
        getPrefs().edit().putString(PREFERENCE_EMAIL, email).commit();
    }
    public static String getEmail(){
        return getPrefs().getString(PREFERENCE_EMAIL, "");
    }

    public static void setNewsCategory(MasterData response) {
        String data = "";
        if (response != null) {
            Gson gson = new Gson();
            data = gson.toJson(response);
        }
        getPrefs().edit().putString(PREFERENCE_NEW_CATEGORY, data).commit();
    }

    public static MasterData getNewsCategory() {
        Gson gson = new Gson();
        String data = getPrefs().getString(PREFERENCE_NEW_CATEGORY, "");
        return !data.equalsIgnoreCase("") ? gson.fromJson(data, MasterData.class) : null;
    }

}
