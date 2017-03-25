package com.hoangsong.zumechat.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.adapters.ListCountryAdapter;
import com.hoangsong.zumechat.models.CountryInfo;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.PopupCallback;
import com.hoangsong.zumechat.untils.UtilCountry;
import com.hoangsong.zumechat.untils.Utils;
import com.mukesh.countrypicker.models.Country;

import org.json.JSONObject;

import java.io.EOFException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pvthiendeveloper on 3/23/2017.
 */

public class DialogChooseCountry extends Dialog {
    private ListView lvCountry;
    private Button btnCancel, btnOk;
    private ListCountryAdapter adp;
    private ArrayList<CountryInfo> list;
    private PopupCallback popupCallback;

    public DialogChooseCountry(Context context, PopupCallback popupCallback, ArrayList<CountryInfo> listCountryInfo) {
        super(context);
        this.popupCallback = popupCallback;
        this.list = listCountryInfo;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Utils.setTranslucentStatusBar(getWindow());
        setContentView(R.layout.dialog_choose_country);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        initUI();
    }

    private void initUI() {

        lvCountry = (ListView) findViewById(R.id.lvCountry);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnOk = (Button) findViewById(R.id.btnOk);

        setCountryAdapter();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupCallback.popUpCallback(list, Constants.ID_POP_COUNTRY, null, 0, 0);
                dismiss();
            }
        });
    }

    private void setCountryAdapter() {
        adp = new ListCountryAdapter(getContext(), list);
        lvCountry.setAdapter(adp);
        lvCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CountryInfo countryInfo = list.get(i);
                if (countryInfo.isSelected()) {
                    list.get(i).setSelected(false);
                    adp.notifyDataSetChanged();
                } else {
                    list.get(i).setSelected(true);
                    adp.notifyDataSetChanged();
                }
            }
        });
    }

}
