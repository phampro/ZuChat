package com.hoangsong.zumechat.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.adapters.ListAdsAdapter;
import com.hoangsong.zumechat.connection.DownloadAsyncTask;
import com.hoangsong.zumechat.dialog.DialogChooseCountry;
import com.hoangsong.zumechat.dialog.DialogChooseImage;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.Advertisement;
import com.hoangsong.zumechat.models.CountryInfo;
import com.hoangsong.zumechat.models.Image;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.JsonCallback;
import com.hoangsong.zumechat.untils.MyDateTime;
import com.hoangsong.zumechat.untils.MyDateTimeISO;
import com.hoangsong.zumechat.untils.PopupCallback;
import com.hoangsong.zumechat.untils.UtilCountry;
import com.hoangsong.zumechat.untils.Utils;
import com.mukesh.countrypicker.models.Country;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddAdsActivity extends AppCompatActivity implements View.OnClickListener, PopupCallback, JsonCallback {
    private TextView tvTitle, tvFromDate, tvToDate;
    private ImageButton ibtnBack, ibtnPickBanner, ibnPickThumb;
    private Context context;
    private ImageView ivBanner, ivThumb;
    private EditText txtName, txtContent, txtUrl, txtCountries;
    private LinearLayout llFromDate, llToDate;
    private SwitchCompat swPublish, swDisplay;
    private Button btnSave;
    private File captureImage;
    private final int REUQEST_CODE_CAPTURE = 1;
    private final int REUQEST_CODE_CHOOSE = 2;
    private Bitmap bmBanner, bmThumb;
    private String fromDate, toDate;
    private Advertisement adsUpdate;
    private boolean isPickPanner, isPickThumb;
    private JSONArray countries;
    private ArrayList<CountryInfo> listCountryInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ads);
        initUI();
    }

    private void initUI() {
        context = getApplicationContext();
        listCountryInfo = new ArrayList<>();

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvFromDate = (TextView) findViewById(R.id.tvFromDate);
        tvToDate = (TextView) findViewById(R.id.tvToDate);
        ibtnBack = (ImageButton) findViewById(R.id.ibtnBack);
        ibtnPickBanner = (ImageButton) findViewById(R.id.ibtnPickBanner);
        ibnPickThumb = (ImageButton) findViewById(R.id.ibnPickThumb);
        ivBanner = (ImageView) findViewById(R.id.ivBanner);
        ivThumb = (ImageView) findViewById(R.id.ivThumb);
        txtName = (EditText) findViewById(R.id.txtName);
        txtContent = (EditText) findViewById(R.id.txtContent);
        txtUrl = (EditText) findViewById(R.id.txtUrl);
        txtCountries = (EditText) findViewById(R.id.txtCountries);
        llFromDate = (LinearLayout) findViewById(R.id.llFromDate);
        llToDate = (LinearLayout) findViewById(R.id.llToDate);
        swPublish = (SwitchCompat) findViewById(R.id.swPublish);
        swDisplay = (SwitchCompat) findViewById(R.id.swDisplay);
        btnSave = (Button) findViewById(R.id.btnSave);

        tvTitle.setText(getString(R.string.title_add_advertisement));

        ibtnBack.setOnClickListener(this);
        llFromDate.setOnClickListener(this);
        llToDate.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        ibtnPickBanner.setOnClickListener(this);
        txtCountries.setOnClickListener(this);
        ibnPickThumb.setOnClickListener(this);

        getMyIntent();
        convertCountryToCountryInfo();

    }

    private void getMyIntent() {
        Intent i = getIntent();
        if (i != null) {
            String idAds = i.getStringExtra("idAds");
            if (idAds != null) {
                if (!idAds.equals("")) {
                    getAdvertisementDetail(idAds);
                }
            }
        }
    }

    private void convertCountryToCountryInfo() {
        for (Country country : UtilCountry.getAllCountries()) {
            listCountryInfo.add(new CountryInfo(country, true));
        }
    }

    private void getListCountry(ArrayList<String> listname) {
        int size = listCountryInfo.size();
        for (int i = 0; i < size; i++) {
            boolean isExist = false;
            for (String name : listname) {
                if (listCountryInfo.get(i).getCountry().getName().equalsIgnoreCase(name)) {
                    isExist = true;
                }
            }
            if(isExist){
                listCountryInfo.get(i).setSelected(true);
            }else{
                listCountryInfo.get(i).setSelected(false);
            }
        }
    }

    private JSONArray countryInfoToJson() {
        try {
            countries = new JSONArray();
            for (CountryInfo countryInfo : listCountryInfo) {
                if (countryInfo.isSelected()) {
                    JSONObject ob = new JSONObject();
                    ob.put("code", countryInfo.getCountry().getCode());
                    countries.put(ob);
                }
            }
            return countries;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap checkBannerSize(Bitmap bitmap) {
        if (bitmap != null) {
            if (bitmap.getWidth() < 320 || bitmap.getHeight() < 720) {
                Utils.showSimpleDialogAlert(AddAdsActivity.this, getString(R.string.msg_error_choose_a_larger_image_size));
                return null;
            } else {
                return bitmap;
            }
        }
        return null;
    }

    private void capturePicture(int requestId) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        captureImage = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(captureImage);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, requestId);
    }

    private void choosePicture(int idRequest) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.title_select_picture)), idRequest);
    }

    private void showDialogDateTime(final TextView textView, final boolean isFromDate, final boolean isToDate) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(AddAdsActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String myMonth = (month + 1) < 10 ? "0" + (month + 1) : (month + 1) + "";
                String myDay = day < 10 ? "0" + day : day + "";
                String date = myDay + "/" + myMonth + "/" + year;
                textView.setText(date);
                if (isFromDate) {
                    fromDate = year + "/" + myMonth + "/" + myDay;
                } else if (isToDate) {
                    toDate = year + "/" + myMonth + "/" + myDay;
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private String getThumb(ArrayList<Image> images) {
        String url = "";
        int size = images.size();
        for (int i = 0; i < size; i++) {
            Image image = images.get(i);
            if (image.getType().equalsIgnoreCase("thumb")) {
                url = image.getUrl();
                return url;
            }
        }
        return url;
    }

    private String getBanner(ArrayList<Image> images) {
        String url = "";
        int size = images.size();
        for (int i = 0; i < size; i++) {
            Image image = images.get(i);
            if (image.getType().equalsIgnoreCase("banner")) {
                url = image.getUrl();
                return url;
            }
        }
        return url;
    }

    private void getAdvertisementDetail(String id) {
        new DownloadAsyncTask(AddAdsActivity.this, Constants.GET_ADS_DETAIL + "?id=" + id + "&token=" + Prefs.getUserInfo().getToken(), Constants.ID_GET_ADS_DETAIL, AddAdsActivity.this, true, DownloadAsyncTask.HTTP_VERB.GET.getVal(), "{}");
    }

    private void onSave() {
        try {
            String name = txtName.getText().toString().trim();
            String content = txtContent.getText().toString().trim();
            String url = txtUrl.getText().toString().trim();
            if (bmBanner == null) {
                Utils.showSimpleDialogAlert(AddAdsActivity.this, R.string.msg_please_choose_banner);
            } else if (bmThumb == null) {
                Utils.showSimpleDialogAlert(AddAdsActivity.this, R.string.msg_please_choose_thumb);
            } else if (name.equals("")) {
                Utils.showSimpleDialogAlert(AddAdsActivity.this, R.string.msg_please_enter_name);
            } else if (name.length() < 6 || name.length() > 500) {
                Utils.showSimpleDialogAlert(AddAdsActivity.this, R.string.msg_the_name_must_be_at_least);
            } else if (content.equals("")) {
                Utils.showSimpleDialogAlert(AddAdsActivity.this, R.string.msg_please_enter_content);
            } else if (url.equals("")) {
                Utils.showSimpleDialogAlert(AddAdsActivity.this, R.string.msg_please_enter_url);
            } else if (fromDate.equals("") || fromDate.equalsIgnoreCase("N/A")) {
                Utils.showSimpleDialogAlert(AddAdsActivity.this, R.string.msg_please_enter_from_date);
            } else if (toDate.equals("") || toDate.equalsIgnoreCase("N/A")) {
                Utils.showSimpleDialogAlert(AddAdsActivity.this, R.string.msg_please_enter_to_date);
            } else {

                JSONObject postData = new JSONObject();
                postData.put("name", name);
                postData.put("start_date", fromDate);
                postData.put("end_date", toDate);
                postData.put("content", content);
                postData.put("url", url);
                postData.put("thumb_image", Utils.encodeFileToBase64Binary(bmThumb));
                postData.put("banner_image", Utils.encodeFileToBase64Binary(bmBanner));
                postData.put("is_publish", swPublish.isChecked());
                postData.put("is_show", swDisplay.isChecked());
                if(!swPublish.isChecked()){
                    postData.put("countries", countryInfoToJson());
                }
                postData.put("token", Prefs.getUserInfo().getToken());
                if(adsUpdate == null) {
                    new DownloadAsyncTask(AddAdsActivity.this, Constants.CREATE_ADS, Constants.ID_CREATE_ADS, AddAdsActivity.this, true, DownloadAsyncTask.HTTP_VERB.POST.getVal(), postData.toString());
                }else{
                    postData.put("id", adsUpdate.getId());
                    new DownloadAsyncTask(AddAdsActivity.this, Constants.UPDATE_ADS, Constants.ID_UPDATE_ADS, AddAdsActivity.this, true, DownloadAsyncTask.HTTP_VERB.POST.getVal(), postData.toString());
                }
            }
        } catch (Exception e) {
            if (Constants.DEBUG_MODE) e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.ibtnBack:
                finish();
                break;
            case R.id.ibtnPickBanner:
                isPickThumb = false;
                isPickPanner = true;
                new DialogChooseImage(AddAdsActivity.this, AddAdsActivity.this).show();
                break;
            case R.id.ibnPickThumb:
                isPickPanner = false;
                isPickThumb = true;
                new DialogChooseImage(AddAdsActivity.this, AddAdsActivity.this).show();
                break;
            case R.id.llFromDate:
                showDialogDateTime(tvFromDate, true, false);
                break;
            case R.id.llToDate:
                showDialogDateTime(tvToDate, false, true);
                break;
            case R.id.btnSave:
                onSave();
                break;
            case R.id.txtCountries:
                new DialogChooseCountry(AddAdsActivity.this, AddAdsActivity.this, listCountryInfo).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void popUpCallback(Object data, int processID, Object obj, int num, int index) {
        if (num == Constants.ID_POPUP_CHOSE_PHOTO_LIBRARY) {
            choosePicture(REUQEST_CODE_CHOOSE);
        } else if (num == Constants.ID_POPUP_TAKE_PHOTO) {
            capturePicture(REUQEST_CODE_CAPTURE);
        } else if (processID == Constants.ID_POP_COUNTRY) {
            listCountryInfo = (ArrayList<CountryInfo>) data;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REUQEST_CODE_CHOOSE) {
                    if (isPickPanner && !isPickThumb) {
                        Uri uri = data.getData();
                        Bitmap bitmap = checkBannerSize(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
                        if (bitmap != null) {
                            CropImage.activity(uri).setMinCropWindowSize(320, 720).start(this);
                        }
                    } else if (!isPickPanner && isPickThumb) {
                        Uri uri = data.getData();
                        Bitmap bitmap = checkBannerSize(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
                        if (bitmap != null) {
                            CropImage.activity(uri).setMinCropWindowSize(400, 300).start(this);
                        }
                    }

                } else if (requestCode == REUQEST_CODE_CAPTURE) {
                    if (isPickPanner && !isPickThumb) {
                        Uri uri = Uri.fromFile(captureImage);
                        Bitmap bitmap = checkBannerSize(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
                        if (bitmap != null) {
                            CropImage.activity(uri).setMinCropWindowSize(320, 720).start(this);
                        }
                    } else if (!isPickPanner && isPickThumb) {
                        Uri uri = Uri.fromFile(captureImage);
                        Bitmap bitmap = checkBannerSize(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
                        if (bitmap != null) {
                            CropImage.activity(uri).setMinCropWindowSize(320, 720).start(this);
                        }
                    }

                } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri uri = result.getUri();
                    if (captureImage != null) {
                        captureImage.delete();
                    }
                    if (isPickPanner && !isPickThumb) {
                        bmBanner = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        if (bmBanner != null) {
                            Picasso.with(context).load(uri).fit().centerCrop().into(ivBanner);
                        }
                    } else if (!isPickPanner && isPickThumb) {
                        bmThumb = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        if (bmThumb != null) {
                            Picasso.with(context).load(uri).fit().centerCrop().into(ivThumb);
                        }
                    }
                }
            }
        } catch (Exception e) {
            if (Constants.DEBUG_MODE) e.printStackTrace();
        }

    }

    @Override
    public void jsonCallback(Object data, int processID, int index) {
        if (processID == Constants.ID_CREATE_ADS) {
            if (data != null) {
                Response response = (Response) data;
                String msg = response.getMessage();
                int error = response.getError_code();
                if (error == Constants.ERROR_CODE_SUCCESS) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Utils.showSimpleDialogAlert(AddAdsActivity.this, msg);
                }
            } else {
                Utils.showSimpleDialogAlert(AddAdsActivity.this, getString(R.string.alert_unexpected_error));
            }
        } else if (processID == Constants.ID_GET_ADS_DETAIL) {
            if (data != null) {
                Response response = (Response) data;
                String msg = response.getMessage();
                int error = response.getError_code();
                if (error == Constants.ERROR_CODE_SUCCESS) {
                    adsUpdate = (Advertisement) response.getData();
                    if (adsUpdate != null) {
                        txtName.setText(adsUpdate.getName());
                        txtUrl.setText(adsUpdate.getUrl());
                        txtContent.setText(adsUpdate.getContent());
                        String urlThumb = getThumb(adsUpdate.getImages());
                        if (!urlThumb.equals("")) {
                            Picasso.with(context).load(urlThumb).fit().centerCrop().into(ivThumb, new Callback() {
                                @Override
                                public void onSuccess() {
                                    BitmapDrawable drawable = (BitmapDrawable) ivThumb.getDrawable();
                                    bmThumb = drawable.getBitmap();
                                }

                                @Override
                                public void onError() {

                                }
                            });
                        }
                        String urlBanner = getBanner(adsUpdate.getImages());
                        if (!urlBanner.equals("")) {
                            Picasso.with(context).load(urlBanner).fit().centerCrop().into(ivBanner, new Callback() {
                                @Override
                                public void onSuccess() {
                                    BitmapDrawable drawable = (BitmapDrawable) ivBanner.getDrawable();
                                    bmBanner = drawable.getBitmap();
                                }

                                @Override
                                public void onError() {

                                }
                            });
                        }
                        getListCountry(adsUpdate.getCountries());
                        swDisplay.setChecked(adsUpdate.isShow());
                        swPublish.setChecked(adsUpdate.isPublish());
                        try {
                            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            Date fromDate = sf.parse(adsUpdate.getStart_date());
                            Date toDate = sf.parse(adsUpdate.getEnd_date());
                            tvFromDate.setText(MyDateTimeISO.getDateFormat(fromDate));
                            tvToDate.setText(MyDateTimeISO.getDateFormat(toDate));

                            SimpleDateFormat sfServer = new SimpleDateFormat("yyyy/MM/dd");
                            this.fromDate = sfServer.format(fromDate);
                            this.toDate = sfServer.format(toDate);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    Utils.showSimpleDialogAlert(AddAdsActivity.this, msg);
                }
            } else {
                Utils.showSimpleDialogAlert(AddAdsActivity.this, getString(R.string.alert_unexpected_error));
            }
        }else if(processID == Constants.ID_UPDATE_ADS){
            if(data != null){
                Response response = (Response) data;
                String msg = response.getMessage();
                int error = response.getError_code();
                if (error == Constants.ERROR_CODE_SUCCESS) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Utils.showSimpleDialogAlert(AddAdsActivity.this, msg);
                }
            }else{
                Utils.showSimpleDialogAlert(AddAdsActivity.this, getString(R.string.alert_unexpected_error));
            }
        }
    }

    @Override
    public void jsonError(String msg, int processID) {
        Utils.showSimpleDialogAlert(AddAdsActivity.this, msg);
    }
}
