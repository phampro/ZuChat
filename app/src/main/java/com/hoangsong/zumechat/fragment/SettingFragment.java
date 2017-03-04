package com.hoangsong.zumechat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.untils.Utils;

/**
 * Created by Tang on 10/10/2016.
 */

public class SettingFragment extends Fragment {
    private Context context;
    private RadioGroup rdgSettingSpeed;
    private RadioButton rdSlow, rdNormal, rdFast;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        context = getActivity();
        pageInit(view);


        return view;
    }

    private void pageInit(View v){

        rdgSettingSpeed = (RadioGroup) v.findViewById(R.id.rdgSettingSpeed);
        rdSlow = (RadioButton) v.findViewById(R.id.rdSlow);
        rdNormal = (RadioButton) v.findViewById(R.id.rdNormal);
        rdFast = (RadioButton) v.findViewById(R.id.rdFast);
        TextView tvTitleGroup = (TextView) v.findViewById(R.id.tvTitleGroup);
        tvTitleGroup.setTypeface(Utils.getFontTahoma(context));
        rdSlow.setTypeface(Utils.getFontTahoma(context));
        rdNormal.setTypeface(Utils.getFontTahoma(context));
        rdFast.setTypeface(Utils.getFontTahoma(context));

        rdgSettingSpeed.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.rdFast) {
                    Prefs.setReadSpeed(1.5f);
                } else if(checkedId == R.id.rdSlow) {
                    Prefs.setReadSpeed(0.5f);
                } else {
                    Prefs.setReadSpeed(1f);
                }
            }

        });

        if(Prefs.getReadSpeed()== 1.5){
            rdFast.setChecked(true);
        }else if(Prefs.getReadSpeed() == 0.5){
            rdSlow.setChecked(true);
        }else {
            rdNormal.setChecked(true);
        }
    }
}