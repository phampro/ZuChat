package com.hoangsong.zumechat.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.connection.DownloadAsyncTask;
import com.hoangsong.zumechat.dialog.DialogChooseImage;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.JsonCallback;
import com.hoangsong.zumechat.untils.PopupCallback;
import com.hoangsong.zumechat.untils.Utils;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddAdsActivity extends AppCompatActivity implements View.OnClickListener, PopupCallback, JsonCallback {
    private TextView tvTitle, tvFromDate, tvToDate;
    private ImageButton ibtnBack, ibtnPickBanner;
    private Context context;
    private ImageView ivBanner;
    private EditText txtName, txtContent, txtUrl, txtCountries;
    private LinearLayout llFromDate, llToDate;
    private SwitchCompat swPublish, swDisplay;
    private Button btnSave;
    private File captureImage;
    private final int REUQEST_CODE_CAPTURE = 1;
    private final int REUQEST_CODE_CHOOSE = 2;
    private Bitmap bmBanner, bmThumb;
    private String fromDate, toDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ads);
        initUI();
    }

    private void initUI() {
        context = getApplicationContext();

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvFromDate = (TextView) findViewById(R.id.tvFromDate);
        tvToDate = (TextView) findViewById(R.id.tvToDate);
        ibtnBack = (ImageButton) findViewById(R.id.ibtnBack);
        ibtnPickBanner = (ImageButton) findViewById(R.id.ibtnPickBanner);
        ivBanner = (ImageView) findViewById(R.id.ivBanner);
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
                String date = myDay+ "/" + myMonth + "/" + year;
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

    private void onSave() {
        try {
            String name = txtName.getText().toString().trim();
            String content = txtContent.getText().toString().trim();
            String url = txtUrl.getText().toString().trim();
            if (bmBanner == null || bmThumb == null) {
                Utils.showSimpleDialogAlert(AddAdsActivity.this, R.string.msg_please_choose_banner);
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
                postData.put("countries", null);
                postData.put("token", Prefs.getUserInfo().getToken());
                new DownloadAsyncTask(AddAdsActivity.this, Constants.CREATE_ADS, Constants.ID_CREATE_ADS, AddAdsActivity.this, true, DownloadAsyncTask.HTTP_VERB.POST.getVal(), postData.toString());
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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REUQEST_CODE_CHOOSE) {
                    Uri uri = data.getData();
                    bmBanner = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    Bitmap bitmap = checkBannerSize(bmBanner);
                    if (bitmap != null) {
                        CropImage.activity(uri).setMinCropWindowSize(320, 720).start(this);
                    }
                } else if (requestCode == REUQEST_CODE_CAPTURE) {
                    Uri uri = Uri.fromFile(captureImage);
                    bmBanner = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    Bitmap bitmap = checkBannerSize(bmBanner);
                    if (bitmap != null) {
                        CropImage.activity(uri).setMinCropWindowSize(320, 720).start(this);
                    }
                } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri uri = result.getUri();
                    bmThumb = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    if (bmThumb != null) {
                        Picasso.with(context).load(uri).fit().centerCrop().into(ivBanner);
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
        }
    }

    @Override
    public void jsonError(String msg, int processID) {
        Utils.showSimpleDialogAlert(AddAdsActivity.this, msg);
    }
}
