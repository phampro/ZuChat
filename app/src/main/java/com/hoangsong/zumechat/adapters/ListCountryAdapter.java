package com.hoangsong.zumechat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.models.CountryInfo;
import com.mukesh.countrypicker.models.Country;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pvthiendeveloper on 3/23/2017.
 */

public class ListCountryAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CountryInfo> listCoutryInfo;

    public ListCountryAdapter(Context context, ArrayList<CountryInfo> list) {
        this.context = context;
        this.listCoutryInfo = list;
    }

    @Override
    public int getCount() {
        return listCoutryInfo.size();
    }

    @Override
    public Object getItem(int i) {
        return listCoutryInfo.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        OneItem oneItem;
        View v = view;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.item_list_country, viewGroup, false);
            oneItem = new OneItem(v);
            v.setTag(oneItem);
        } else {
            oneItem = (OneItem) v.getTag();
        }
        CountryInfo item = listCoutryInfo.get(i);
        if (item.isSelected()) {
            oneItem.ivSelected.setVisibility(View.VISIBLE);
        } else {
            oneItem.ivSelected.setVisibility(View.INVISIBLE);
        }
        oneItem.tvName.setText(item.getCountry().getName());
        return v;
    }

    private class OneItem {
        TextView tvName;
        ImageView ivSelected;

        public OneItem(View v) {
            tvName = (TextView) v.findViewById(R.id.tvName);
            ivSelected = (ImageView) v.findViewById(R.id.ivSelected);
        }
    }
}
