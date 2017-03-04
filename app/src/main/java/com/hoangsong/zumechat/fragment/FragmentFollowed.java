package com.hoangsong.zumechat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.adapters.ListMemberAdapter;
import com.hoangsong.zumechat.connection.DownloadAsyncTask;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.MemberInfo;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.JsonCallback;
import com.hoangsong.zumechat.untils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tang on 10/22/2016.
 */

public class FragmentFollowed extends BaseFragment{
    private Context context;
    private View thisView;
    private ListView lvMember;
    private ArrayList<MemberInfo> list_member = new ArrayList<>();
    private ListMemberAdapter adp;

    public FragmentFollowed() {
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
        list_member.add(new MemberInfo("1", "Tang Le", Constants.TYPE_STATUS_ONLINE, "hello !!!", "2 day", "", "17km"));
        list_member.add(new MemberInfo("2", "Bach Nang", Constants.TYPE_STATUS_OFFLINE, "hi !!!", "2 sec", "", "18km"));
        list_member.add(new MemberInfo("3", "Thien Pham", Constants.TYPE_STATUS_BUSY, "hello hello!!!", "2 min", "", "8km"));
        list_member.add(new MemberInfo("4", "Hoang Song", Constants.TYPE_STATUS_ONLINE, "ho ho ho !!!", "3 day", "", "180km"));
        lvMember = (ListView) v.findViewById(R.id.lvMember);
        adp = new ListMemberAdapter(context, list_member);
        lvMember.setAdapter(adp);

    }

    @Override
    public void jsonCallback(Object data, int processID, int index) {
        if(processID == Constants.ID_METHOD_FEEDBACK){
            if(data != null){
                Response response = (Response) data;
                Utils.showSimpleDialogAlert(context, response.getMessage());
            }else {
                Utils.showSimpleDialogAlert(context, context.getString(R.string.alert_unexpected_error));
            }
        }
    }

    @Override
    public void jsonError(String msg, int processID) {
        Utils.showSimpleDialogAlert(context, msg);
    }
}
