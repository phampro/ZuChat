package com.hoangsong.zumechat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.adapters.ListChatAdapter;
import com.hoangsong.zumechat.adapters.ListMemberAdapter;
import com.hoangsong.zumechat.connection.DownloadAsyncTask;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.ChatInfo;
import com.hoangsong.zumechat.models.ChatMessageList;
import com.hoangsong.zumechat.models.MemberInfo;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.MyDateTime;
import com.hoangsong.zumechat.untils.Utils;
import com.hoangsong.zumechat.view.EndlessRecyclerViewScrollListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Tang on 10/22/2016.
 */

public class FragmentChatDetail extends BaseFragment{
    private Context context;
    private View thisView;
    private int page_size = 20;
    private String token = "";
    private RelativeLayout vPaddingActionBar;
    private TextView tvStatusConect, tvTitle, tvEmpty;
    private RecyclerView listView;
    private ListChatAdapter mAdapter;
    private ProgressBar progressBarLoadChat;
    private LinearLayoutManager llm;
    private EndlessRecyclerViewScrollListener endlessScroll;
    private EditText txtChatMessage;
    private ImageButton ibtnSendMessage, ibtnBack;
    private ArrayList<ChatInfo> listChat = new ArrayList<ChatInfo>();
    private ArrayList<ChatInfo> listChat2 = new ArrayList<ChatInfo>();


    boolean firstload = true;
    boolean isLoadMore = false;
    private int page = 1;
    private int pageError = -1;
    private String userid = "";
    private String sender_id_get = "";
    private String username_get = "";
    Calendar cal = Calendar.getInstance();
    int dayCurrent = cal.get(Calendar.DAY_OF_YEAR);
    int woy = -1;

    public FragmentChatDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        setFragmentActivity(getActivity());
        if (thisView == null) {
            thisView = inflater.inflate(R.layout.fragment_chat_detail, null);
            initView(thisView);

        } else {
            ViewGroup parent = (ViewGroup) thisView.getParent();
            if (parent != null) {
                parent.removeView(thisView);
            }
        }
        return thisView;
    }

    private void initView(View v){
        setSubPopUpCallback(this, Constants.ID_POPUP_CHAT_DETAIL);
        vPaddingActionBar = (RelativeLayout) v.findViewById(R.id.vPaddingActionBar);
        Utils.setViewPaddingStatusBar(vPaddingActionBar, context);
        sender_id_get = getArguments().getString("sender_id");
        username_get = getArguments().getString("user_name");
        setSenderIdCurrent(sender_id_get);

        token = Prefs.getUserInfo() != null ? Prefs.getUserInfo().getToken() : "";
        userid = Prefs.getUserInfo() != null ? Prefs.getUserInfo().getId() : "";

        progressBarLoadChat = (ProgressBar) v.findViewById(R.id.progressBarLoadChat);
        tvStatusConect = (TextView) v.findViewById(R.id.tvStatusConect);
        tvEmpty = (TextView) v.findViewById(R.id.tvEmpty);
        tvTitle = (TextView) v.findViewById(R.id.tvTitle);
        listView = (RecyclerView) v.findViewById(R.id.msgview);
        listView.setHasFixedSize(true);
        llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        txtChatMessage = (EditText) v.findViewById(R.id.txtChatMessage);
        ibtnSendMessage = (ImageButton) v.findViewById(R.id.ibtnSendMessage);
        ibtnBack = (ImageButton) v.findViewById(R.id.ibtnBack);

        tvTitle.setText(username_get);

        ibtnSendMessage.setOnClickListener(this);
        ibtnBack.setOnClickListener(this);

        endlessScroll = new EndlessRecyclerViewScrollListener(llm) {

            @Override
            public void onLoadMore(int current_page) {
                // TODO Auto-generated method stub
                customLoadMoreDataFromApi();

            }
        };

        endlessScroll.reset(0, true);
        mAdapter = new ListChatAdapter(getActivity(), listChat);
        listView.setAdapter(mAdapter);
        listView.setLayoutManager(llm);
        listView.setHasFixedSize(true);
        listView.setAdapter(mAdapter);

        listView.addOnScrollListener(endlessScroll);
        loadChatList(page);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.ibtnSendMessage:
                if(txtChatMessage.getText().length()>0) {
                    String msg = txtChatMessage.getText().toString().trim();
                    for (int i = 0; i < Constants._MSG_BLACKLIST.length; i++) {
                        msg = msg.replaceAll("(?i)" + Constants._MSG_BLACKLIST[i], "****");
                    }
                    msg = msg.replaceAll("\\w*\\*{4}", "****");
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("receiver_id", sender_id_get);
                        obj.put("chat_type", Constants.CHAT_TYPE_TEXT);
                        obj.put("chat_message", msg);
                        obj.put("photo", "");
                        obj.put("token", token);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(Constants.DEBUG_MODE)
                        Log.d("post data", "post data: "+obj.toString());
                    new DownloadAsyncTask(context, Constants.SEND_MESSAGE_CHAT, Constants.ID_METHOD_SEND_MESSAGE_CHAT,
                            FragmentChatDetail.this, false, DownloadAsyncTask.HTTP_VERB.POST.getVal(), obj.toString());
                    txtChatMessage.setText("");
                }
                break;
            case R.id.ibtnBack:
                onBackPressed();
                break;

        }
    }

    private void loadChatList(int page_index){
        new DownloadAsyncTask(context,
                Constants.GET_CHAT_MESSAGES+"?receiver_id=" + sender_id_get+"&page_index="+page_index+"&page_size="+page_size+"&token="+token,
                Constants.ID_METHOD_GET_CHAT_MESSAGES,
                this, false, DownloadAsyncTask.HTTP_VERB.GET.getVal(), "{}");
    }

    public void customLoadMoreDataFromApi() {
        if(isLoadMore){
            if (pageError > 0) {
                //Log.e("page error", pageError + "-----");
                progressBarLoadChat.setVisibility(View.VISIBLE);
                loadChatList(pageError);

            } else {
                //Log.e("page error", pageError + "-----");
                page = page + 1;
                progressBarLoadChat.setVisibility(View.VISIBLE);
                loadChatList(page);

            }
        }
    }

    private void sortList(){
        if(listChat2.size()>0){
            tvEmpty.setVisibility(View.GONE);
            listChat.clear();
            listChat.addAll(listChat2);
            for(int i = 0; i< listChat.size(); i++){
                ChatInfo chatObj = listChat.get(i);
                shortBackground(i);
                if(!chatObj.getCreated_on().equals("")){
                    if (woy != MyDateTime.getDateOfYear(MyDateTime.getFormatDateReg(chatObj.getCreated_on()))) {
                        woy = MyDateTime.getDateOfYear(MyDateTime.getFormatDateReg(chatObj.getCreated_on()));
                        if(woy == dayCurrent){
                            listChat.add(i, new ChatInfo("", "Today", "", "", "", "", ""));
                        }else{
                            listChat.add(i, new ChatInfo("", MyDateTime.getFormatDateRegStr(chatObj.getCreated_on()), "", "", "", "", ""));
                        }
                    }
                }
            }
            if(!listChat.get(0).getCreated_on().equals("")){
                if(woy == dayCurrent){
                    listChat.add(0, new ChatInfo("", "Today", "", "", "", "", ""));
                }else{
                    listChat.add(0, new ChatInfo("", MyDateTime.getFormatDateRegStr(listChat.get(0).getCreated_on()), "", "", "", "", ""));
                }
            }
        }else{
            tvEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void shortBackground(int index){
        if(index == 0){
            if(listChat.get(0).getSender_id().equals(userid)){
                listChat.get(0).setBackground(1);
            }else{
                listChat.get(0).setBackground(2);
            }
        }else{
            String old = listChat.get(index-1).getSender_id();
            String mnew = listChat.get(index).getSender_id();
            if(mnew.equals(old)){
                //listChat.get(index-1).setShowTime(false);
                if(mnew.equals(userid)){
                    listChat.get(index).setBackground(-1);
                }else{
                    listChat.get(index).setBackground(-2);
                }
            }else if(!mnew.equals(old)){
                if(mnew.equals(userid)){
                    listChat.get(index).setBackground(1);
                }else{
                    listChat.get(index).setBackground(2);
                }
            }
        }

    }

    @Override
    public void jsonCallback(Object data, int processID, int index) {
        // TODO Auto-generated method stub
        if (processID == Constants.ID_METHOD_GET_CHAT_MESSAGES) {
            if (data != null) {
                Response response = (Response) data;
                if (response.getError_code() == Constants.ERROR_CODE_SUCCESS) {
                    ChatMessageList dataInfo = (ChatMessageList) response.getData();
                    if (firstload) {
                        for(int i=dataInfo.getListChat().size()-1; i>=0 ; i--){
                            listChat2.add(dataInfo.getListChat().get(i));
                        }
                        sortList();
                        mAdapter.notifyDataSetChanged();
                        //listView.scrollToPosition(listChat.size());
                        listView.invalidate();
                        listView.scrollToPosition(llm.getChildCount() + dataInfo.getListChat().size());
                        firstload = false;
                        if(dataInfo.getMaxPage()>1){
                            isLoadMore = true;
                        }
                    } else {
                        for (int i = 0; i < dataInfo.getListChat().size(); i++) {
                            listChat2.add(0, dataInfo.getListChat().get(i));
                        }
                        sortList();
                        mAdapter.notifyDataSetChanged();
                        listView.invalidate();
                        listView.scrollToPosition(llm.getChildCount() + (dataInfo.getListChat().size()-1));
                        //Log.e("chitcount: ", "chitcount: "+llm.getChildCount());
                        progressBarLoadChat.setVisibility(View.GONE);
                        pageError = -1;
                    }
                    //}
                    //progressBarLoadChat.setVisibility(View.GONE);
                    //Log.v("page num ", page + "----");
                    //Log.v("size list ", listChat.size() + "----");
                    //Log.v("size adapter ", mAdapter.getItemCount() + "----");
                } else {
                    if (pageError < 0) {
                        try{
                            Utils.showSimpleDialogAlert(context, response.getMessage());
                        }catch(Exception e){}
                    }
                    endlessScroll.reset(0, true);
                    pageError = page;
                }
            } else {
                if (pageError < 0) {
                    try{
                        Utils.showSimpleDialogAlert(context, getString(R.string.alert_no_connection));
                    }catch(Exception e){}
                }
                endlessScroll.reset(0, true);
                pageError = page;
            }
        }else if(processID == Constants.ID_METHOD_SEND_MESSAGE_CHAT){
            if(data != null){
                Response response = (Response) data;
                if(response.getError_code() == Constants.ERROR_CODE_SUCCESS){
                    if(response.getData() != null){
                        ChatInfo chatInfo = (ChatInfo) response.getData();
                        listChat.add(chatInfo);
                        listChat2.add(chatInfo);
                        sortList();
                        tvEmpty.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged();
                        listView.scrollToPosition(mAdapter.getItemCount() - 1);
                        listView.invalidate();
                    }
                }else {
                    Utils.showSimpleDialogAlert(context, response.getMessage());
                }
            }else
                Utils.showSimpleDialogAlert(context, getString(R.string.alert_unexpected_error));
        }
    }

    @Override
    public void jsonError(String msg, int processID) {
        // TODO Auto-generated method stub
        if (pageError < 0) {
            try{
                Utils.showSimpleDialogAlert(context, msg);
            }catch(Exception e){}
        }
        endlessScroll.reset(0, true);
        pageError = page;
    }

    @Override
    public void popUpCallback(Object data, int processID, Object obj, int num, int index) {
        super.popUpCallback(data, processID, obj, num, index);
        if(processID == Constants.ID_POPUP_CHAT_RECEIVED_MESSAGE){
            if(data != null){
                ChatInfo chatInfo = (ChatInfo) data;
                listChat.add(chatInfo);
                listChat2.add(chatInfo);
                sortList();
                tvEmpty.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
                listView.scrollToPosition(mAdapter.getItemCount() - 1);
                listView.invalidate();
            }
        }
    }
}
