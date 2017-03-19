package com.hoangsong.zumechat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.adapters.ListAdsAdapter;
import com.hoangsong.zumechat.connection.DownloadAsyncTask;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.Advertisement;
import com.hoangsong.zumechat.models.ListAdvertisement;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.JsonCallback;
import com.hoangsong.zumechat.untils.Utils;
import com.hoangsong.zumechat.view.EndlessListView;

import java.util.ArrayList;

public class ManageAdsActivity extends AppCompatActivity implements View.OnClickListener, JsonCallback, EndlessListView.EndlessListener {
    private FloatingActionButton fab;
    private TextView tvTitle;
    private ImageButton ibtnBack;

    //Load more
    private EndlessListView endlessListView;
    private ListAdsAdapter adp;
    private ArrayList<Advertisement> listAds;
    private int page = 1;
    private int pageError = -1;
    private boolean firstLoad = true;
    private ListAdvertisement listAdvertisement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_ads);

        initUI();
    }

    private void initUI() {
        listAds = new ArrayList<>();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        endlessListView = (EndlessListView) findViewById(R.id.endlessListView);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ibtnBack = (ImageButton) findViewById(R.id.ibtnBack);

        fab.setOnClickListener(this);
        ibtnBack.setOnClickListener(this);

        tvTitle.setText(getString(R.string.title_manage_advertisement));

        setEndlestLvAdp();

        getAllAdvertisementList(page, true);
    }

    private void setEndlestLvAdp() {
        endlessListView.setLoadingView(R.layout.dialog_progress);
        endlessListView.reset(0, true);
        endlessListView.setListener(this);
        adp = new ListAdsAdapter(ManageAdsActivity.this, listAds);
        endlessListView.setAdapter(adp);
        endlessListView.setDivider(null);
    }

    private void getAllAdvertisementList(int page, boolean showDialog) {
        try {
            new DownloadAsyncTask(ManageAdsActivity.this, Constants.GET_ALL_ADVERTISEMENT_LIST + "?page_index=" + page + "&page_size=" + 10 + "&token=" + Prefs.getUserInfo().getToken(), Constants.ID_GET_ALL_ADVERTISEMENT_LIST, ManageAdsActivity.this, showDialog, DownloadAsyncTask.HTTP_VERB.GET.getVal(), "{}");
        } catch (Exception e) {
            if (Constants.DEBUG_MODE) e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fab:
                startActivity(new Intent(ManageAdsActivity.this, AddAdsActivity.class));
                break;
            case R.id.ibtnBack:
                finish();
                break;
            default:
                break;
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
                    this.listAds = listAdvertisement.getListAds();
                    adp.notifyDataSetChanged();
                    pageError = -1;
                    if (firstLoad) {
                        firstLoad = false;
                    } else {
                        endlessListView.hildeFooter();
                    }
                } else {
                    Utils.showSimpleDialogAlert(this, msg);
                    endlessListView.reset(0, true);
                    pageError = page;
                }
            } else {
                Utils.showSimpleDialogAlert(this, R.string.alert_unexpected_error);
                endlessListView.reset(0, true);
                pageError = page;
            }
        }
    }

    @Override
    public void jsonError(String msg, int processID) {
        if (processID == Constants.ID_GET_ALL_ADVERTISEMENT_LIST) {
            Utils.showSimpleDialogAlert(this, msg);
            endlessListView.reset(0, true);
            pageError = page;
        }
    }

    @Override
    public void loadData() {
        if(page <= listAdvertisement.getTotal_page()) {
            if (pageError > 0) {
                getAllAdvertisementList(pageError, false);
            } else {
                page++;
                getAllAdvertisementList(pageError, false);
            }
        }else{
            endlessListView.hildeFooter();
        }
    }
}
