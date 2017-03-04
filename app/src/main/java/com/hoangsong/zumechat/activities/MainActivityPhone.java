package com.hoangsong.zumechat.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.adapters.TabMenuAdapter;
import com.hoangsong.zumechat.dialog.DialogConfirm;
import com.hoangsong.zumechat.fragment.AppInfoFragment;
import com.hoangsong.zumechat.fragment.FeedBackFragment;
import com.hoangsong.zumechat.fragment.FragmentChat;
import com.hoangsong.zumechat.fragment.FragmentChatDetail;
import com.hoangsong.zumechat.fragment.FragmentFollowed;
import com.hoangsong.zumechat.fragment.FragmentPeople;
import com.hoangsong.zumechat.fragment.FragmentTravelTo;
import com.hoangsong.zumechat.fragment.ListAppFragment;
import com.hoangsong.zumechat.fragment.SettingFragment;
import com.hoangsong.zumechat.helpers.ParserHelper;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.BroadcastSignalRInfo;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.socket.AndroidPlatformComponent;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.NotificationsUtils;
import com.hoangsong.zumechat.untils.PopupCallback;
import com.hoangsong.zumechat.untils.Utils;
import com.hoangsong.zumechat.view.CircleTransform;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;

public class MainActivityPhone extends AppCompatActivity implements PopupCallback, View.OnClickListener{
    private static final String TAG = MainActivityPhone.class.getSimpleName();
    private int exitNumber = 0;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentPeople fragmentPeople;
    private FragmentChat fragmentChat;
    private FragmentFollowed fragmentFollowed;
    private FragmentTravelTo fragmentTravelTo;
    private RelativeLayout vPaddingActionBar;
    private static Context context;
    private static FragmentManager fm;
    private static TextView tvTitle, tvActionMenu;
    private static ImageButton ibtnMenu;
    private ImageView ivAvatar;
    //private Button btnAddXu;
    private boolean isShowAds = false;
    private String current_name_menu = "";

    //Signal
    private HubConnection connection;
    private HubProxy hub;

    //pop back
    private static String sender_id_current = "";
    private static PopupCallback popupCallbackSub;
    private static  int numPopupSub = 0;

    //TANG

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setTranslucentStatusBar(getWindow());
        setContentView(R.layout.activity_main);
        context = MainActivityPhone.this;
        EventBus.getDefault().register(this);
        pageInit();
        connectSignalr(Constants.HOST_SIGNAL_R);
    }

    public void pageInit(){

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        vPaddingActionBar = (RelativeLayout) findViewById(R.id.vPaddingActionBar);
        Utils.setViewPaddingStatusBar(vPaddingActionBar, this);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvActionMenu = (TextView) findViewById(R.id.tvActionMenu);
        tvTitle.setTypeface(Utils.getFontTahoma(context));
        ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        ibtnMenu = (ImageButton)findViewById(R.id.ibtnMenu);

        ibtnMenu.setOnClickListener(this);

        if(Prefs.getUserInfo()!= null){
            String img_url = Prefs.getUserInfo().getProfile_url();
            if(!img_url.equals(""))
                Picasso.with(this).load(img_url).placeholder(R.drawable.ic_profile_normal).error(R.drawable.ic_profile_normal).transform(new CircleTransform()).into(ivAvatar);
        }

        fm = getSupportFragmentManager();

        tvTitle.setText(getString(R.string.app_name));
        loadTab();

        tvActionMenu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                switch (s.toString()){
                    case Constants.MENU_SETTING:
                        break;
                    case Constants.MENU_CONTACT_US:
                        break;
                    case Constants.MENU_SHARE_APP:
                        break;
                    case Constants.MENU_LOG_OUT:
                        new DialogConfirm(context, context.getString(R.string.app_name), context.getString(R.string.msg_log_out_app), 0, MainActivityPhone.this).show();
                        break;
                    case Constants.MENU_DELETE:
                        break;
                    case Constants.MENU_BLOCK:
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibtnMenu:
                switch (current_name_menu){
                    case Constants.TAB_PEOPLE:
                        showActionMenu(Constants.arrMenuPeople);
                        break;
                    case Constants.TAB_CHAT:
                        showActionMenu(Constants.arrMenuChat);
                        break;
                    case Constants.TAB_FOLLOWED:
                        showActionMenu(Constants.arrMenuFollowed);
                        break;
                    case Constants.TAB_TRAVEL_TO:
                        showActionMenu(Constants.arrMenuTravelTo);
                        break;
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        try{
            if(connection != null)
                connection.stop();
        }catch(Exception e){e.printStackTrace();}
        super.onDestroy();
    }

    @Subscribe
    public void onEvent(String type) {
        if(type.equalsIgnoreCase(Constants.PUSH_RELOAD_MENU)){
        }else if(type.equals(Constants.PUSH_RELOAD_MENU_AND_CHANGE_PASS)){
        }else if(type.equals(Constants.PUSH_RELOAD_MENU_XU)){
        }
    }

    public static void switchFragment(String fragmentName, Bundle bundle, boolean isAddToBackStack) {

        if (fm != null) {
            // Perform the FragmentTransaction to load in the list tab content.
            // Using FragmentTransaction#replace will destroy any Fragments
            // currently inside R.id.fragment_content and add the new Fragment
            // in its place.

            FragmentTransaction ft = fm.beginTransaction();

            Fragment fragment = null;

            if(fm.findFragmentByTag(fragmentName) == null && fragmentName.equalsIgnoreCase(context.getString(R.string.menu_setting))){
                fragment = new SettingFragment();
            }
            else if(fm.findFragmentByTag(fragmentName) == null && fragmentName.equalsIgnoreCase(context.getString(R.string.menu_app_info))){
                fragment = new AppInfoFragment();
            }

            else if(fm.findFragmentByTag(fragmentName) == null && fragmentName.equalsIgnoreCase(context.getString(R.string.menu_list_app))){
                fragment = new ListAppFragment();
            }

            else if(fm.findFragmentByTag(fragmentName) == null && fragmentName.equalsIgnoreCase(context.getString(R.string.menu_feedback))){
                fragment = new FeedBackFragment();
            }

            else if(fm.findFragmentByTag(fragmentName) == null && fragmentName.equalsIgnoreCase(Constants.side_nav_fr_chat_detail)){
                fragment = new FragmentChatDetail();
            }

            if(fragment !=null && ft !=null){


                fragment.setRetainInstance(false);
                fragment.setArguments(bundle);
                ft.replace(R.id.content_frame, fragment, fragmentName);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                if(isAddToBackStack){
                    ft.addToBackStack(null);
                }
                //ft.commit();

                ft.commitAllowingStateLoss();

                //tvTitle.setText(fragmentName);
					/*
					if(fragmentName.equalsIgnoreCase(context.getString(R.string.side_nav_home))) {
						updateActionBarTitle(true, fragmentName);
					}else{
						updateActionBarTitle(false, fragmentName);
					}
					*/

            }
            //tvTitle.setText(fragmentName);
        }
    }

    private void loadTab() {
        current_name_menu = Constants.TAB_PEOPLE;
        fragmentPeople = new FragmentPeople();
        fragmentChat = new FragmentChat();
        fragmentFollowed = new FragmentFollowed();
        fragmentTravelTo = new FragmentTravelTo();
        final TabMenuAdapter tabMenuAdapter = new TabMenuAdapter(getSupportFragmentManager());
        tabMenuAdapter.addFrag(fragmentPeople, Constants.TAB_PEOPLE);
        tabMenuAdapter.addFrag(fragmentChat, Constants.TAB_CHAT);
        tabMenuAdapter.addFrag(fragmentFollowed, Constants.TAB_FOLLOWED);
        tabMenuAdapter.addFrag(fragmentTravelTo, Constants.TAB_TRAVEL_TO);
        viewPager.setAdapter(tabMenuAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                current_name_menu = tabMenuAdapter.getPageTitle(tab.getPosition()).toString();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public static  void showActionMenu(String[] menu){
        Utils.showListPopupWindow(context, menu, tvActionMenu, ibtnMenu);
    }

    //pop back
    public static void popBackToFragment(Object data, int process, int num, int index){
        if(popupCallbackSub != null) {
            popupCallbackSub.popUpCallback(data, process, null, num, index);
        }
    }

    public static  void setSubPopUpCallback(PopupCallback popupCallback, int numPopup){
        popupCallbackSub = popupCallback;
        numPopupSub = numPopup;
    }

    public static  void setSenderIdCurrent(String sender_id){
        sender_id_current = sender_id;
    }
    //

    @Override
    public void onBackPressed() {
        if(fm!=null && fm.getBackStackEntryCount() > 0){
            fm.popBackStack();
        }else{
            if(Prefs.getUserInfo() != null){
                if(exitNumber > 0){
                    moveTaskToBack(true);
                    exitNumber = 0;
                }else{
                    Toast.makeText(MainActivityPhone.this, getString(R.string.msg_exit_app), Toast.LENGTH_SHORT).show();
                    exitNumber++;
                }
            }else{
                super.onBackPressed();
                finish();
            }
        }
    }

    @Override
    public void popUpCallback(Object data, int processID, Object obj, int num, int index) {
        if(processID == Constants.ID_POPUP_CONFIRM_YES){
            if(num == 1){
            }else if(num == 0){
                Prefs.setUserInfo(null);
                Intent in = new Intent(this, LogInActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
            }
        }else if(processID == Constants.ID_POPUP_LOG_IN){
            //loadMenu();
        }else if(processID == Constants.ID_POPUP_CONFIRM_OK){
            finish();
        }
    }

    //SignalR

    private void checkConnectSignalR(){
        if(connection != null){
            if(!connection.getState().toString().equalsIgnoreCase("Connected")){
                try {
                    connection.disconnect();
                }catch (Exception e){e.printStackTrace();}
                connectSignalr(Constants.HOST_SIGNAL_R);
                //connectSignalr("http://192.168.10.50/MyApi/signalr/Hubs");
            }
        }
    }

    private void connectSignalr(String host){
        String deviceId = Prefs.getDeviceID();
        if(!deviceId.equalsIgnoreCase("")){
            Platform.loadPlatformComponent( new AndroidPlatformComponent() );
            //connection = new HubConnection(host);
            String text = "connType=ZuChat_API&customer_token="+(Prefs.getUserInfo() != null ? Prefs.getUserInfo().getToken() : "");
            connection = new HubConnection(host, text, false, new Logger() {
                @Override
                public void log(String s, LogLevel logLevel) {
                    //Log.d(getClass().getSimpleName(), "text: "+s);
                }
            });
            hub = connection.createHubProxy("MobileHub");
            //hub = connection.createHubProxy("chatHub");

            SignalRFuture<Void> awaitConnection = connection.start();
            try {
                awaitConnection.get();
                if(Constants.DEBUG_MODE){
                    Log.e(TAG+": SignalR connected", "SignalR getConnectionId: "+connection.getConnectionId());
                }
                //updateSignalrConnectionID(connection.getConnectionId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            hub.subscribe( this );
            hub.on("receiverMessage", new SubscriptionHandler1<String>()
                    {
                        @Override
                        public void run(String returnObject) {
                            final String jsonContent = returnObject;
                            if(Constants.DEBUG_MODE){
                                Log.d(TAG+": jsonContent", jsonContent );
                                Log.e(TAG+": SignalR braodcast", "braodcast message ");
                            }

                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Response response = ParserHelper.getBroadcastSignalR(jsonContent);
                                    if(response != null){
                                        if(response.getError_code() == Constants.ERROR_CODE_SUCCESS && response.getData() != null){
                                            BroadcastSignalRInfo broadcastSignalRInfo = (BroadcastSignalRInfo) response.getData();
                                            //Toast.makeText(MainActivityPhone.this, jsonContent, Toast.LENGTH_LONG).show();
                                            processReceiverMessage(broadcastSignalRInfo);
                                        }else {

                                        }
                                    }
                                }
                            });
                        }
                    },
                    String.class);
        }else {
            String message = "Make sure push notification has been activated. Do you want go to menu to check settings?";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try{
                    if(!NotificationsUtils.isNotificationEnabled(this)){
                        message = "Push notification is not enabled. Do you want to go to settings menu?";
                    }
                }catch (Exception e){e.printStackTrace();}
            }
            NotificationsUtils.showSettingsAlert(this, "com.hoangsong.zumechat", message);
        }
    }

    private void processReceiverMessage(BroadcastSignalRInfo broadcastSignalRInfo){
        if(broadcastSignalRInfo != null){
            if(broadcastSignalRInfo.getChat() != null && broadcastSignalRInfo.getChat().getSender_id().equals(sender_id_current)){
                popBackToFragment(broadcastSignalRInfo.getChat(), Constants.ID_POPUP_CHAT_RECEIVED_MESSAGE, 0, 0);
            }
        }

    }
}