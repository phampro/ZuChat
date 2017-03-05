package com.hoangsong.zumechat.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.plus.PlusShare;
import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.connection.DownloadAsyncTask;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.AccountInfo;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.JsonCallback;
import com.hoangsong.zumechat.untils.UtilCountry;
import com.hoangsong.zumechat.untils.Utils;
import com.hoangsong.zumechat.view.CircleTransform;
import com.mukesh.countrypicker.models.Country;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

public class MyProfileActivity extends AppCompatActivity implements JsonCallback, View.OnClickListener {
    private ImageView ivAvatar, ivBackground, ivStatus, ivCountry;
    private TextView tvEdit, tvNickName, tvStatus, tvGender, tvCountry, tvTotalFavorite, tvStatusOnline, tvShareGoogle, tvShareFacebook, tvAddContact, tvHi, tvTitle;
    private Context context;
    private ImageButton ibtnBack;
    private final int REFRESH_CODE = 1;
    private LinearLayout llGuest, llNormal;
    private AccountInfo accountInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        initUI();
    }

    private void initUI() {
        context = MyProfileActivity.this;
        ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        ivStatus = (ImageView) findViewById(R.id.ivStatus);
        ivCountry = (ImageView) findViewById(R.id.ivCountry);
        ivBackground = (ImageView) findViewById(R.id.ivBackground);
        tvEdit = (TextView) findViewById(R.id.tvEdit);
        tvNickName = (TextView) findViewById(R.id.tvNickName);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvGender = (TextView) findViewById(R.id.tvGender);
        tvCountry = (TextView) findViewById(R.id.tvCountry);
        tvTotalFavorite = (TextView) findViewById(R.id.tvTotalFavorite);
        tvStatusOnline = (TextView) findViewById(R.id.tvStatusOnline);
        tvShareGoogle = (TextView) findViewById(R.id.tvShareGoogle);
        tvShareFacebook = (TextView) findViewById(R.id.tvShareFacebook);
        tvAddContact = (TextView) findViewById(R.id.tvAddContact);
        tvHi = (TextView) findViewById(R.id.tvHi);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ibtnBack = (ImageButton) findViewById(R.id.ibtnBack);

        llGuest = (LinearLayout) findViewById(R.id.llGuest);
        llNormal = (LinearLayout) findViewById(R.id.llNormal);

        tvShareGoogle.setOnClickListener(this);
        tvShareFacebook.setOnClickListener(this);
        ibtnBack.setOnClickListener(this);
        tvEdit.setOnClickListener(this);
        tvStatusOnline.setOnClickListener(this);
        tvAddContact.setOnClickListener(this);
        tvHi.setOnClickListener(this);

        getProfile();

    }

    private void getProfile() {
        new DownloadAsyncTask(context, Constants.GET_PROFILE + "?token=" + Prefs.getUserInfo().getToken(), Constants.ID_METHOD_GET_PROFILE, MyProfileActivity.this, true,
                DownloadAsyncTask.HTTP_VERB.GET.getVal(), "{}");
    }

    private void setDataForm() {
        if (accountInfo != null) {
            String urlAvatar = accountInfo.getProfile_url();
            String urlBackground = accountInfo.getBackground_url();
            if (!urlAvatar.equals("")) {
                ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
                Picasso.with(context).load(urlAvatar).memoryPolicy(MemoryPolicy.NO_CACHE).placeholder(R.drawable.ic_profile_normal).fit().centerCrop().transform(new CircleTransform()).into(ivAvatar);
            }
            if (!urlBackground.equals("")) {
                ivBackground = (ImageView) findViewById(R.id.ivBackground);
                Picasso.with(context).load(urlBackground).memoryPolicy(MemoryPolicy.NO_CACHE).fit().centerCrop().into(ivBackground);
            }
            tvNickName.setText(accountInfo.getUsername());
            tvStatus.setText(accountInfo.getDescription());
            tvGender.setText(accountInfo.getGender());
            tvCountry.setText(accountInfo.getCountry());
            Country country = UtilCountry.getCountry(accountInfo.getCountry_code());
            if (country != null) {
                ivCountry.setBackgroundResource(UtilCountry.getFlagResId(this, accountInfo.getCountry_code()));
            }
            tvTotalFavorite.setText(accountInfo.getTotal_favorites() + "");
            tvStatusOnline.setText(accountInfo.getJob_status().substring(0, 1).toUpperCase() + accountInfo.getJob_status().substring(1, accountInfo.getJob_status().length()));
            tvTitle.setText(getString(R.string.app_name));

            if (!accountInfo.getId().equalsIgnoreCase(Prefs.getUserInfo().getId())) {
                llNormal.setVisibility(View.GONE);
                llGuest.setVisibility(View.VISIBLE);
            } else {
                llNormal.setVisibility(View.VISIBLE);
                llGuest.setVisibility(View.GONE);
            }

            updateStatus();
        }
    }

    private void updateStatus() {
        String status = accountInfo.getOnline_status();
        if (status.equalsIgnoreCase("online")) {
            ivStatus.setBackgroundResource(R.drawable.ic_circle_green);
        } else if (status.equalsIgnoreCase("offline")) {
            ivStatus.setBackgroundResource(R.drawable.ic_circle_green);
        } else if (status.equalsIgnoreCase("busy")) {
            ivStatus.setBackgroundResource(R.drawable.ic_circle_green);
        }
    }

    private void shereWithFacebook() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Share url");
        intent.setType("text/plain");

        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().contains("facebook")) {
                intent.setPackage(info.activityInfo.packageName);
            }
        }

        startActivity(intent);
    }

    private void shereWithGooglePlus() {
        Intent shareIntent = new PlusShare.Builder(this)
                .setType("text/plain")
                .setText("Welcome to the Google+ platform.")
                .setContentUrl(Uri.parse("https://developers.google.com/+/"))
                .getIntent();
        startActivity(shareIntent);
    }

    private void updateJobStatus(String status) {
        try {
            JSONObject postData = new JSONObject();
            postData.put("job_status", status);
            postData.put("token", Prefs.getUserInfo().getToken());
            new DownloadAsyncTask(context, Constants.UPDATE_JOB_STATUS, Constants.ID_UPDATE_JOB_STATUS, MyProfileActivity.this, true,
                    DownloadAsyncTask.HTTP_VERB.POST.getVal(), postData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addFavourite() {
        try {
            JSONObject postData = new JSONObject();
            postData.put("member_id", accountInfo.getId());
            postData.put("token", Prefs.getUserInfo().getToken());
            new DownloadAsyncTask(context, Constants.ADD_FAOURITE, Constants.ID_ADD_FAOURITE, MyProfileActivity.this, true,
                    DownloadAsyncTask.HTTP_VERB.POST.getVal(), postData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(MyProfileActivity.this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_status, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_online:
                        tvStatusOnline.setText(MyProfileActivity.this.getResources().getString(R.string.menu_online));
                        updateJobStatus(tvStatusOnline.getText().toString().toLowerCase() + "");
                        break;
                    case R.id.action_offline:
                        tvStatusOnline.setText(MyProfileActivity.this.getResources().getString(R.string.menu_offline));
                        updateJobStatus(tvStatusOnline.getText().toString().toLowerCase() + "");
                        break;
                    case R.id.action_busy:
                        tvStatusOnline.setText(MyProfileActivity.this.getResources().getString(R.string.menu_busy));
                        updateJobStatus(tvStatusOnline.getText().toString().toLowerCase() + "");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public void jsonCallback(Object data, int processID, int index) {
        if (data != null) {
            if (processID == Constants.ID_METHOD_GET_PROFILE) {
                Response response = (Response) data;
                if (response.getError_code() == Constants.ERROR_CODE_SUCCESS) {
                    accountInfo = (AccountInfo) response.getData();
                    setDataForm();
                } else {
                    Utils.showSimpleDialogAlert(this, response.getMessage());
                }

            } else if (processID == Constants.ID_UPDATE_JOB_STATUS) {
                Response response = (Response) data;
                if (response.getError_code() == Constants.ERROR_CODE_SUCCESS) {
                    accountInfo = (AccountInfo) response.getData();
                    updateStatus();
                } else {
                    Utils.showSimpleDialogAlert(this, response.getMessage());
                }
            } else if (processID == Constants.ID_ADD_FAOURITE) {
                Response response = (Response) data;
                if (response.getError_code() == Constants.ERROR_CODE_SUCCESS) {
                    getProfile();
                    Utils.showSimpleDialogAlert(this, response.getMessage());
                } else {
                    Utils.showSimpleDialogAlert(this, response.getMessage());
                }
            }
        } else {
            Utils.showSimpleDialogAlert(this, this.getString(R.string.alert_unexpected_error));
        }
    }

    @Override
    public void jsonError(String msg, int processID) {
        Utils.showSimpleDialogAlert(this, msg);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tvShareGoogle:
                shereWithGooglePlus();
                break;
            case R.id.tvShareFacebook:
                shereWithFacebook();
                break;
            case R.id.ibtnBack:
                finish();
                break;
            case R.id.tvStatusOnline:
                showPopupMenu(tvStatusOnline);
                break;
            case R.id.tvEdit:
                startActivityForResult(new Intent(MyProfileActivity.this, MyProfileEditActivity.class), REFRESH_CODE);
                break;
            case R.id.tvAddContact:
                addFavourite();
                break;
            case R.id.tvHi:
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REFRESH_CODE) {
            getProfile();
        }
    }
}
