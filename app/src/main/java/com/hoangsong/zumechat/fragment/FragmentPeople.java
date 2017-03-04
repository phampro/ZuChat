package com.hoangsong.zumechat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.adapters.ListMemberAdapter;
import com.hoangsong.zumechat.connection.DownloadAsyncTask;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.MemberInfo;
import com.hoangsong.zumechat.models.MemberList;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tang on 10/22/2016.
 */

public class FragmentPeople extends BaseFragment{
    private Context context;
    private View thisView;
    private int page_size = 20;
    private String token = "";
    private ListView lvMember;
    private ArrayList<MemberInfo> list_member = new ArrayList<>();
    private ListMemberAdapter adp;

    public FragmentPeople() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        setFragmentActivity(getActivity());
        if (thisView == null) {
            thisView = inflater.inflate(R.layout.fragment_base, null);
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
        token = Prefs.getUserInfo() != null ? Prefs.getUserInfo().getToken() : "";
        lvMember = (ListView) v.findViewById(R.id.lvMember);
        adp = new ListMemberAdapter(context, list_member);
        lvMember.setAdapter(adp);

        lvMember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("sender_id", list_member.get(position).getId());
                bundle.putString("user_name", list_member.get(position).getUsername());
                switchFragment(Constants.side_nav_fr_chat_detail, bundle, true);
            }
        });

        searchFriend(1);

    }

    private void searchFriend(int page_index){
        JSONObject obj = new JSONObject();
        try {
            obj.put("country_id", 1);
            obj.put("type", "country");
            obj.put("latitude", 0.0);
            obj.put("longtitude", 0.0);
            obj.put("page_index", page_index);
            obj.put("page_size", page_size);
            obj.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(Constants.DEBUG_MODE)
            Log.d("post data", "post data: "+obj.toString());
        new DownloadAsyncTask(context, Constants.SEARCH_FRIEND, Constants.ID_METHOD_SEARCH_FRIEND,
                FragmentPeople.this, false, DownloadAsyncTask.HTTP_VERB.POST.getVal(), obj.toString());
    }


    @Override
    public void jsonCallback(Object data, int processID, int index) {
        if (processID == Constants.ID_METHOD_SEARCH_FRIEND) {
            if (data != null) {
                Response response = (Response) data;
                if (response.getError_code() == Constants.ERROR_CODE_SUCCESS) {
                    MemberList dataInfo = (MemberList) response.getData();
                    list_member.clear();
                    list_member.addAll(dataInfo.getFriends());
                    adp.notifyDataSetChanged();
                } else {
                    Utils.showSimpleDialogAlert(context, response.getMessage());
                }
            } else {
                Utils.showSimpleDialogAlert(context, getString(R.string.alert_unexpected_error));
            }
        }
    }

    @Override
    public void jsonError(String msg, int processID) {
        Utils.showSimpleDialogAlert(context, msg);
    }
}
