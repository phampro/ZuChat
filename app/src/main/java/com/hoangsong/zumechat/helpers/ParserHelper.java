package com.hoangsong.zumechat.helpers;

import android.content.Context;
import android.util.Log;


import com.hoangsong.zumechat.models.Advertisement;
import com.hoangsong.zumechat.models.Image;
import com.hoangsong.zumechat.models.ListAdvertisement;
import com.hoangsong.zumechat.models.MemberInfo;
import com.hoangsong.zumechat.models.MemberList;
import com.hoangsong.zumechat.models.AccountInfo;
import com.hoangsong.zumechat.models.BroadcastSignalRInfo;
import com.hoangsong.zumechat.models.ChatInfo;
import com.hoangsong.zumechat.models.ChatMessageList;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParserHelper {
    private final String TAG = this.getClass().getSimpleName();
    private static final String ERROR_CODE = "errorCode";
    private static final String MESSAGE = "message";

    public Object parseData(Context context, String parseString, int processID, int statusCode) {
        if (Constants.DEBUG_MODE) {
            Log.d(TAG, "STRING_PARSER: " + parseString);
        }
        if (processID == Constants.ID_METHOD_LOGIN) {
            return getLoginResponse(parseString);
        } else if (processID == Constants.ID_METHOD_REGISTER) {
            return getLoginResponse(parseString);
        } else if (processID == Constants.ID_METHOD_GET_PROFILE) {
            return getLoginResponse(parseString);
        } else if (processID == Constants.ID_METHOD_CHANGE_PASSWORD) {
            return getResponse(parseString);
        } else if (processID == Constants.ID_METHOD_FORGOT_PASSWORD) {
            return getResponse(parseString);
        } else if (processID == Constants.ID_METHOD_FEEDBACK) {
            return getResponse(parseString);
        } else if (processID == Constants.ID_METHOD_SEND_MESSAGE_CHAT) {
            return getSendMessageChat(parseString);
        } else if (processID == Constants.ID_METHOD_GET_CHAT_MESSAGES) {
            return getMessageChat(parseString);
        } else if (processID == Constants.ID_METHOD_SEARCH_FRIEND) {
            return getSearchFriend(parseString);
        }

        //THIEN
        else if (processID == Constants.ID_METHOD_GET_PROFILE) {
            return getLoginResponse(parseString);
        } else if (processID == Constants.ID_UPDATE_PROFILE) {
            return getResponseNoData(parseString);
        } else if (processID == Constants.ID_UPDATE_JOB_STATUS) {
            return getLoginResponse(parseString);
        } else if (processID == Constants.ID_ADD_FAOURITE) {
            return getResponse(parseString);
        } else if (processID == Constants.ID_CREATE_ADS) {
            return getResponse(parseString);
        } else if (processID == Constants.ID_GET_MY_ADS_LIST) {
            return getAllAdvertisementList(parseString);
        } else if (processID == Constants.ID_DELETE_ADVERTISEMENT) {
            return getResponse(parseString);
        } else if (processID == Constants.ID_GET_ADS_DETAIL) {
            return getAdsDetail(parseString);
        }else if (processID == Constants.ID_UPDATE_ADS) {
            return getResponse(parseString);
        }

        return null;
    }

    private Response getResponse(String parseString) {
        if (!parseString.equalsIgnoreCase("")) {
            try {
                JSONObject object = new JSONObject(parseString);
                int errorCode = checkIntValue(object.getString(ERROR_CODE));
                String message = checkStringValue(object.getString(MESSAGE));
                if (!object.isNull("data") && errorCode == Constants.ERROR_CODE_SUCCESS) {
                    String data = checkStringValue(object.getString("data"));
                    return new Response(errorCode, message, data);
                } else {
                    return new Response(errorCode, message, "");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }


    private Object getResponseNoData(String parseString) {
        Response response = null;
        if (!parseString.equalsIgnoreCase("")) {
            try {
                JSONObject object = new JSONObject(parseString);
                int errorCode = checkIntValue(object.getString(ERROR_CODE));
                String message = checkStringValue(object.getString(MESSAGE));
                response = new Response(errorCode, message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    private Response getLoginResponse(String parseString) {
        Response response = null;
        if (!parseString.equalsIgnoreCase("")) {
            try {
                JSONObject mainData = new JSONObject(parseString);
                int errorCode = checkIntValue(mainData.getString(ERROR_CODE));
                String message = checkStringValue(mainData.getString(MESSAGE));
                AccountInfo accountInfo = null;
                JSONObject object = mainData.isNull("data") ? null : mainData.getJSONObject("data");
                if (errorCode == Constants.ERROR_CODE_SUCCESS && object != null) {
                    String id = object.has("id") ? checkStringValue(object.getString("id")) : "";
                    String token = object.has("token") ? checkStringValue(object.getString("token")) : "";
                    String code = object.has("code") ? checkStringValue(object.getString("code")) : "";
                    String username = object.has("username") ? checkStringValue(object.getString("username")) : "";
                    String email = object.has("email") ? checkStringValue(object.getString("email")) : "";
                    String gender = object.has("gender") ? checkStringValue(object.getString("gender")) : "";
                    String reg_date = object.has("reg_date") ? checkStringValue(object.getString("reg_date")) : "";
                    String job_status = object.has("job_status") ? checkStringValue(object.getString("job_status")) : "";
                    String online_status = object.has("online_status") ? checkStringValue(object.getString("online_status")) : "";
                    String profile_url = object.has("profile_url") ? checkStringValue(object.getString("profile_url")) : "";
                    String background_url = object.has("background_url") ? checkStringValue(object.getString("background_url")) : "";
                    String country = object.has("country_name") ? checkStringValue(object.getString("country_name")) : "";
                    String country_code = object.has("country_code") ? checkStringValue(object.getString("country_code")) : "";
                    String description = object.has("description") ? checkStringValue(object.getString("description")) : "";
                    int total_favorites = object.has("total_favorites") ? checkIntValue(object.getString("total_favorites")) : 0;
                    int total_credit = object.has("total_credit") ? checkIntValue(object.getString("total_credit")) : 0;
                    String credit_expiry_date = object.has("credit_expiry_date") ? checkStringValue(object.getString("credit_expiry_date")) : "";
                    accountInfo = new AccountInfo(id, token, code, username, email, gender, reg_date, job_status, online_status, profile_url, background_url, country, country_code, description, total_favorites, total_credit, credit_expiry_date);
                }
                response = new Response(errorCode, message, accountInfo);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return response;
    }

    public static Response getBroadcastSignalR(String parseString) {
        Response response = null;
        if (!parseString.equalsIgnoreCase("")) {
            try {
                JSONObject mainData = new JSONObject(parseString);
                int errorCode = checkIntValue(mainData.getString(ERROR_CODE));
                String message = checkStringValue(mainData.getString(MESSAGE));
                int type = mainData.has("type") ? checkIntValue(mainData.getString("type")) : -1;
                BroadcastSignalRInfo broadcastSignalRInfo = null;
                JSONObject object = mainData.isNull("data") ? null : mainData.getJSONObject("data");
                if (errorCode == Constants.ERROR_CODE_SUCCESS && object != null) {
                    int action = object.has("action") ? checkIntValue(object.getString("action")) : -1;
                    JSONObject chat = object.isNull("chat") ? null : object.getJSONObject("chat");
                    ChatInfo chatInfo = null;
                    if (chat != null) {
                        String id = chat.has("id") ? checkStringValue(chat.getString("id")) : "";
                        String chat_message = chat.has("chat_message") ? checkStringValue(chat.getString("chat_message")) : "";
                        String username = chat.has("username") ? checkStringValue(chat.getString("username")) : "";
                        String photo_url = chat.has("photo_url") ? checkStringValue(chat.getString("photo_url")) : "";
                        String chat_type = chat.has("chat_type") ? checkStringValue(chat.getString("chat_type")) : "";
                        String created_on = chat.has("created_on") ? checkStringValue(chat.getString("created_on")) : "";
                        String sender_id = chat.has("sender_id") ? checkStringValue(chat.getString("sender_id")) : "";
                        chatInfo = new ChatInfo(id, chat_message, username, photo_url, chat_type, created_on, sender_id);
                    }
                    broadcastSignalRInfo = new BroadcastSignalRInfo(type, action, chatInfo);
                }
                response = new Response(errorCode, message, broadcastSignalRInfo);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return response;
    }

    private Response getMessageChat(String parseString) {
        if (!parseString.equals("")) {
            try {
                JSONObject mainData = new JSONObject(parseString);
                int errorCode = mainData.has(ERROR_CODE) ? checkIntValue(mainData.getString(ERROR_CODE)) : -1;
                String message = mainData.has(MESSAGE) ? checkStringValue(mainData.getString(MESSAGE)) : "";
                JSONObject data = mainData.isNull("data") ? null : mainData.getJSONObject("data");
                if (errorCode == Constants.ERROR_CODE_SUCCESS && data != null) {
                    int total_page = mainData.has("total_page") ? checkIntValue(mainData.getString("total_page")) : 0;
                    //chats_list
                    ArrayList<ChatInfo> listMod = new ArrayList<>();
                    JSONArray arrayMod = data.isNull("chats") ? null : data.getJSONArray("chats");
                    if (arrayMod != null) {
                        for (int i = 0; i < arrayMod.length(); i++) {
                            JSONObject obj = arrayMod.getJSONObject(i);
                            int ordering = obj.has("ordering") ? checkIntValue(obj.getString("ordering")) : 0;
                            String chat_message = obj.has("chat_message") ? checkStringValue(obj.getString("chat_message")) : "";
                            String photo_url = obj.has("photo_url") ? checkStringValue(obj.getString("photo_url")) : "";
                            String chat_type = obj.has("chat_type") ? checkStringValue(obj.getString("chat_type")) : "";
                            String created_on = obj.has("created_on") ? checkStringValue(obj.getString("created_on")) : "";
                            String sender_id = obj.has("sender_id") ? checkStringValue(obj.getString("sender_id")) : "";
                            listMod.add(new ChatInfo("", chat_message, "", photo_url, chat_type, created_on, sender_id));
                        }
                    }
                    return new Response(errorCode, message, new ChatMessageList(total_page, listMod));
                } else {
                    return new Response(errorCode, message, null);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }


    private Response getSendMessageChat(String parseString) {
        Response response = null;
        if (!parseString.equalsIgnoreCase("")) {
            try {
                JSONObject mainData = new JSONObject(parseString);
                int errorCode = checkIntValue(mainData.getString(ERROR_CODE));
                String message = checkStringValue(mainData.getString(MESSAGE));
                ChatInfo chatInfo = null;
                JSONObject object = mainData.isNull("data") ? null : mainData.getJSONObject("data");
                if (errorCode == Constants.ERROR_CODE_SUCCESS && object != null) {
                    String id = object.has("id") ? checkStringValue(object.getString("id")) : "";
                    String chat_message = object.has("chat_message") ? checkStringValue(object.getString("chat_message")) : "";
                    String username = object.has("username") ? checkStringValue(object.getString("username")) : "";
                    String photo_url = object.has("photo_url") ? checkStringValue(object.getString("photo_url")) : "";
                    String chat_type = object.has("chat_type") ? checkStringValue(object.getString("chat_type")) : "";
                    String created_on = object.has("created_on") ? checkStringValue(object.getString("created_on")) : "";
                    String sender_id = object.has("sender_id") ? checkStringValue(object.getString("sender_id")) : "";
                    chatInfo = new ChatInfo(id, chat_message, username, photo_url, chat_type, created_on, sender_id);
                }
                response = new Response(errorCode, message, chatInfo);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return response;
    }

    private Response getSearchFriend(String parseString) {
        if (!parseString.equals("")) {
            try {
                JSONObject mainData = new JSONObject(parseString);
                int errorCode = mainData.has(ERROR_CODE) ? checkIntValue(mainData.getString(ERROR_CODE)) : -1;
                String message = mainData.has(MESSAGE) ? checkStringValue(mainData.getString(MESSAGE)) : "";
                JSONObject data = mainData.isNull("data") ? null : mainData.getJSONObject("data");
                if (errorCode == Constants.ERROR_CODE_SUCCESS && data != null) {
                    int total_page = mainData.has("total_page") ? checkIntValue(mainData.getString("total_page")) : 0;
                    //chats_list
                    ArrayList<MemberInfo> listMod = new ArrayList<>();
                    JSONArray arrayMod = data.isNull("friends") ? null : data.getJSONArray("friends");
                    if (arrayMod != null) {
                        for (int i = 0; i < arrayMod.length(); i++) {
                            JSONObject obj = arrayMod.getJSONObject(i);
                            int ordering = obj.has("ordering") ? checkIntValue(obj.getString("ordering")) : 0;
                            String id = obj.has("id") ? checkStringValue(obj.getString("id")) : "";
                            String username = obj.has("username") ? checkStringValue(obj.getString("username")) : "";
                            String profile_url = obj.has("profile_url") ? checkStringValue(obj.getString("profile_url")) : "";
                            boolean online_status = obj.has("online_status") ? checkBooleanValue(obj.getString("online_status")) : false;
                            String job_status = obj.has("job_status") ? checkStringValue(obj.getString("job_status")) : "";
                            String offline_on = obj.has("offline_on") ? checkStringValue(obj.getString("offline_on")) : "";
                            String description = obj.has("description") ? checkStringValue(obj.getString("description")) : "";
                            String country_code = obj.has("country_code") ? checkStringValue(obj.getString("country_code")) : "";
                            boolean is_block = obj.has("ordering") ? checkBooleanValue(obj.getString("is_block")) : false;
                            listMod.add(new MemberInfo(ordering, id, username, profile_url, online_status, job_status, offline_on, description, country_code, is_block, ""));
                        }
                    }
                    return new Response(errorCode, message, new MemberList(total_page, listMod));
                } else {
                    return new Response(errorCode, message, null);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    //THIEN
    private Response getAllAdvertisementList(String parseString) {
        if (!parseString.equals("")) {
            try {
                JSONObject mainData = new JSONObject(parseString);
                int errorCode = mainData.has(ERROR_CODE) ? checkIntValue(mainData.getString(ERROR_CODE)) : -1;
                String message = mainData.has(MESSAGE) ? checkStringValue(mainData.getString(MESSAGE)) : "";
                JSONObject data = mainData.isNull("data") ? null : mainData.getJSONObject("data");
                if (errorCode == Constants.ERROR_CODE_SUCCESS && data != null) {
                    int total_page = mainData.has("total_page") ? checkIntValue(mainData.getString("total_page")) : 0;
                    //advertisements
                    ArrayList<Advertisement> listAds = new ArrayList<>();
                    JSONArray advertisements = data.isNull("advertisements") ? null : data.getJSONArray("advertisements");
                    if (advertisements != null) {
                        int length = advertisements.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = advertisements.getJSONObject(i);
                            String id = obj.has("id") ? checkStringValue(obj.getString("id")) : "";
                            String name = obj.has("name") ? checkStringValue(obj.getString("name")) : "";
                            String content = obj.has("content") ? checkStringValue(obj.getString("content")) : "";
                            String url = obj.has("url") ? checkStringValue(obj.getString("url")) : "";
                            JSONArray images = obj.isNull("images") ? null : obj.getJSONArray("images");
                            ArrayList<Image> listImg = new ArrayList<>();
                            if (images != null) {
                                int lengthImg = images.length();
                                for (int j = 0; j < lengthImg; j++) {
                                    JSONObject obj2 = images.getJSONObject(j);
                                    String urlImg = obj2.has("url") ? checkStringValue(obj2.getString("url")) : "";
                                    String type = obj2.has("type") ? checkStringValue(obj2.getString("type")) : "";
                                    listImg.add(new Image(urlImg, type));
                                }
                            }
                            listAds.add(new Advertisement(id, name, "", "", "", content, url, false, false, listImg, null));
                        }
                    }
                    return new Response(errorCode, message, new ListAdvertisement(total_page, listAds));
                } else {
                    return new Response(errorCode, message, null);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    private Response getAdsDetail(String parseString) {
        if (!parseString.equals("")) {
            try {
                JSONObject mainData = new JSONObject(parseString);
                int errorCode = mainData.has(ERROR_CODE) ? checkIntValue(mainData.getString(ERROR_CODE)) : -1;
                String message = mainData.has(MESSAGE) ? checkStringValue(mainData.getString(MESSAGE)) : "";
                JSONObject data = mainData.isNull("data") ? null : mainData.getJSONObject("data");
                if (errorCode == Constants.ERROR_CODE_SUCCESS && data != null) {
                    //advertisement
                    String id = data.has("id") ? checkStringValue(data.getString("id")) : "";
                    String name = data.has("name") ? checkStringValue(data.getString("name")) : "";
                    String start_date = data.has("start_date") ? checkStringValue(data.getString("start_date")) : "";
                    String end_date = data.has("end_date") ? checkStringValue(data.getString("end_date")) : "";
                    String created_on = data.has("created_on") ? checkStringValue(data.getString("created_on")) : "";
                    String content = data.has("content") ? checkStringValue(data.getString("content")) : "";
                    String url = data.has("url") ? checkStringValue(data.getString("url")) : "";
                    boolean is_publish = data.has("is_publish") ? checkBooleanValue(data.getString("is_publish")) : false;
                    boolean is_show = data.has("is_show") ? checkBooleanValue(data.getString("is_show")) : false;

                    //List iamge
                    JSONArray images = data.isNull("images") ? null : data.getJSONArray("images");
                    ArrayList<Image> listImg = new ArrayList<>();
                    if (images != null) {
                        int lengthImg = images.length();
                        for (int j = 0; j < lengthImg; j++) {
                            JSONObject obj2 = images.getJSONObject(j);
                            String urlImg = obj2.has("url") ? checkStringValue(obj2.getString("url")) : "";
                            String type = obj2.has("type") ? checkStringValue(obj2.getString("type")) : "";
                            listImg.add(new Image(urlImg, type));
                        }
                    }

                    JSONArray arrayCountries = data.isNull("countries") ? null : data.getJSONArray("countries");
                    ArrayList<String> listCountry = new ArrayList<>();
                    if (arrayCountries != null) {
                        int length = arrayCountries.length();
                        for (int j = 0; j < length; j++) {
                            JSONObject obj2 = arrayCountries.getJSONObject(j);
                            String nameCountry = obj2.has("name") ? checkStringValue(obj2.getString("name")) : "";
                            listCountry.add(nameCountry);
                        }
                    }
                    return new Response(errorCode, message, new Advertisement(id, name, start_date, end_date, created_on, content, url, is_publish, is_show, listImg, listCountry));
                } else {
                    return new Response(errorCode, message, null);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    //END THIEN

    private Double getDoubleValue(String parseString, String objectKey) {
        if (!parseString.equalsIgnoreCase("")) {
            try {
                JSONObject object = new JSONObject(parseString);
                return checkDoubleValue(object.getString(objectKey));
            } catch (JSONException e) {
                e.printStackTrace();
                return 0.0;
            }
        } else {
            return 0.0;
        }
    }

    private String getStringValue(String parseString, String objectKey) {
        if (!parseString.equalsIgnoreCase("")) {
            try {
                JSONObject object = new JSONObject(parseString);
                return checkStringValue(object.getString(objectKey));
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    private static String checkStringValue(String sSource) {
        String sRetVal = sSource;
        if (sSource.equalsIgnoreCase("null")) {
            sRetVal = "";
        }
        return sRetVal;
    }

    private JSONObject getJsonObject(String jsonString, JSONObject jsonObject) {
        JSONObject jsonObjectResult = null;
        try {
            jsonObjectResult = jsonObject.getJSONObject(jsonString);

        } catch (Exception ex) {

        }
        return jsonObjectResult;
    }

    private JSONArray getJsonArray(String jsonString, JSONObject jsonObject) {
        JSONArray jsonArray = null;
        try {
            jsonArray = jsonObject.getJSONArray(jsonString);

        } catch (Exception ex) {

        }
        return jsonArray;
    }

    private String getJsonString(String jsonString, JSONObject jsonObject) {
        String result = "";
        try {
            result = jsonObject.isNull(jsonString) ? "" : jsonObject.getString(jsonString);
        } catch (Exception ex) {
        }
        return result;
    }

    private double getJsonDouble(String jsonString, JSONObject jsonObject) {
        double result = 0.0;
        try {
            result = jsonObject.getDouble(jsonString);

        } catch (Exception ex) {
        }
        return result;
    }

    private int getJsonInt(String jsonString, JSONObject jsonObject) {
        int result = 0;
        try {
            result = jsonObject.getInt(jsonString);
        } catch (Exception ex) {
        }
        return result;
    }

    private boolean getJsonBoolean(String jsonString, JSONObject jsonObject) {
        boolean result = false;
        try {
            result = jsonObject.getBoolean(jsonString);
        } catch (Exception ex) {
        }
        return result;
    }

    // xml

    private Boolean checkBooleanValue(String sSource) {
        Boolean sRetVal = true;
        if (sSource.equalsIgnoreCase("null") || sSource.equalsIgnoreCase("")
                || sSource.equalsIgnoreCase("false")) {
            sRetVal = false;
        }
        return sRetVal;
    }

    private static int checkIntValue(String sSource) {
        int sRetVal = 0;
        sSource = sSource.trim();
        if (sSource.equalsIgnoreCase("null") || sSource.equalsIgnoreCase("")
                || sSource.equalsIgnoreCase("0.0")) {
            sRetVal = 0;
        } else {
            sRetVal = Integer.parseInt(sSource);
        }
        return sRetVal;
    }

    private long checkLongValue(String sSource) {
        long sRetVal = 0;
        sSource = sSource.trim();
        if (sSource.equalsIgnoreCase("null") || sSource.equalsIgnoreCase("")
                || sSource.equalsIgnoreCase("0.0")) {
            sRetVal = 0;
        } else {
            sRetVal = Long.parseLong(sSource);
        }
        return sRetVal;
    }

    private double checkDoubleValue(String sSource) {
        double sRetVal = 0;
        sSource = sSource.trim();
        if (sSource.equalsIgnoreCase("null") || sSource.equalsIgnoreCase("")) {
            sRetVal = 0;
        } else {
            sRetVal = Double.parseDouble(sSource);
        }
        return sRetVal;
    }

    private float checkFloatValue(String sSource) {
        float sRetVal = 0;
        sSource = sSource.trim();
        if (sSource.equalsIgnoreCase("null") || sSource.equalsIgnoreCase("")) {
            sRetVal = 0;
        } else {
            sRetVal = Float.parseFloat(sSource);
        }
        return sRetVal;
    }

}
