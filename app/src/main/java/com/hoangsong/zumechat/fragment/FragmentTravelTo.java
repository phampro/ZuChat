package com.hoangsong.zumechat.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.activities.ManageAdsActivity;
import com.hoangsong.zumechat.activities.MyProfileActivity;
import com.hoangsong.zumechat.adapters.ListAdsAdapter;
import com.hoangsong.zumechat.adapters.ListMemberAdapter;
import com.hoangsong.zumechat.connection.DownloadAsyncTask;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.Advertisement;
import com.hoangsong.zumechat.models.ListAdvertisement;
import com.hoangsong.zumechat.models.MemberInfo;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.JsonCallback;
import com.hoangsong.zumechat.untils.Utils;
import com.hoangsong.zumechat.view.EndlessListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tang on 10/22/2016.
 */

public class FragmentTravelTo extends BaseFragment implements EndlessListView.EndlessListener {
    private Context context;
    private View thisView;

    //Load more
    private EndlessListView endlessListView;
    private ListAdsAdapter adp;
    private ArrayList<Advertisement> listAds;
    private int page = 1;
    private int pageError = -1;
    private boolean firstLoad = true;
    private ListAdvertisement listAdvertisement;

    public FragmentTravelTo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        setFragmentActivity(getActivity());
        if (thisView == null) {
            thisView = inflater.inflate(R.layout.fragment_travel_to, null);
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
        listAds = new ArrayList<>();

        endlessListView = (EndlessListView) v.findViewById(R.id.endlessListView);
        endlessListView.setLoadingView(R.layout.dialog_progress);
        adp = new ListAdsAdapter(context, listAds, false);
        endlessListView.setAdapter(adp);
        endlessListView.reset(0, true);
        endlessListView.setListener(this);

        getAllAdvertisementList(page, true);
    }

    private void resetEndless() {
        page = 1;
        pageError = -1;
        firstLoad = true;
        listAds.clear();
        listAdvertisement = new ListAdvertisement();
    }

    private void getAllAdvertisementList(int page, boolean showDialog) {
        try {
            new DownloadAsyncTask(context, Constants.GET_ALL_ADVERTISEMENT_LIST + "?page_index=" + page + "&page_size=" + 10 + "&token=" + Prefs.getUserInfo().getToken(), Constants.ID_GET_ALL_ADVERTISEMENT_LIST, FragmentTravelTo.this, showDialog, DownloadAsyncTask.HTTP_VERB.GET.getVal(), "{}");
        } catch (Exception e) {
            if (Constants.DEBUG_MODE) e.printStackTrace();
        }
    }
    @Override
    public void jsonCallback(Object data, int processID, int index) {
        if (processID == Constants.ID_GET_ALL_ADVERTISEMENT_LIST) {
            if (data != null) {
                Response response = (Response) data;
                int error = response.getError_code();
                String msg = response.getMessage();
                if (error == Constants.ERROR_CODE_SUCCESS && response.getData() != null) {
                    listAdvertisement = (ListAdvertisement) response.getData();
                    pageError = -1;
                    if (firstLoad) {
                        listAds.clear();
                        listAds.addAll(listAdvertisement.getListAds());
                        adp.notifyDataSetChanged();
                        firstLoad = false;
                    } else {
                        listAds.addAll(listAdvertisement.getListAds());
                        adp.notifyDataSetChanged();
                        endlessListView.hildeFooter();
                    }
                } else {
                    if (pageError < 0) {
                        Utils.showSimpleDialogAlert(getActivity(), msg);
                    }
                    endlessListView.reset(0, true);
                    pageError = page;
                    endlessListView.hildeFooter();
                }
            } else {
                if (pageError < 0) {
                    Utils.showSimpleDialogAlert(getActivity(), R.string.alert_unexpected_error);
                }
                endlessListView.reset(0, true);
                pageError = page;
                endlessListView.hildeFooter();
            }
        }
    }

    @Override
    public void jsonError(String msg, int processID) {
        if (processID == Constants.ID_GET_MY_ADS_LIST) {
            if (pageError < 0) {
                Utils.showSimpleDialogAlert(getActivity(), msg);
            }
            endlessListView.reset(0, true);
            pageError = page;
            endlessListView.hildeFooter();
        }
    }

    @Override
    public void loadData() {
//        if (page < listAdvertisement.getTotal_page()) {
        if (pageError > 0) {
            getAllAdvertisementList(pageError, false);
        } else {
            page++;
            getAllAdvertisementList(page, false);
        }
//        }
    }
}
