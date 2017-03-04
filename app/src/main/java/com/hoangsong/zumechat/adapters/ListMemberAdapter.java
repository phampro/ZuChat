package com.hoangsong.zumechat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.models.MemberInfo;
import com.hoangsong.zumechat.models.SideMenuItem;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.view.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by WeiGuang on 14/12/2015.
 */
public class ListMemberAdapter extends BaseAdapter {
    private final Context context;
    private View rowView;
    private final List<MemberInfo> arrData;

    public ListMemberAdapter(Context context, List<MemberInfo> arrData) {
        this.context = context;
        this.arrData = arrData;
    }

    @Override
    public int getCount() {
        return arrData.size();
    }

    @Override
    public MemberInfo getItem(int position) {
        return arrData.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = convertView;
        rowView = inflater.inflate(R.layout.item_list_base, null);
        ViewHolder holder = new ViewHolder(rowView);
        MemberInfo item = arrData.get(position);
        holder.tvName.setText(item.getUsername());
        holder.tvStatus.setText(item.getDescription());
        holder.tvDistance.setText(item.getDistance());
        holder.tvTimeAfterOffline.setText(item.getOffline_on());
        String img_url = item.getProfile_url();
        if(!img_url.equals(""))
            Picasso.with(context).load(img_url).placeholder(R.drawable.ic_profile_normal).error(R.drawable.ic_profile_normal).transform(new CircleTransform()).into(holder.ivAvatar);
        switch (item.getJob_status()){
            case Constants.TYPE_STATUS_ONLINE:
                holder.ivStatus.setImageResource(R.drawable.bg_dot_green);
                break;
            case Constants.TYPE_STATUS_OFFLINE:
                holder.ivStatus.setImageResource(R.drawable.bg_dot_gay);
                break;
            case Constants.TYPE_STATUS_BUSY:
                holder.ivStatus.setImageResource(R.drawable.bg_dot_red);
                break;
        }
        return rowView;
    }

    public class ViewHolder {
        TextView tvName, tvStatus, tvDistance, tvTimeAfterOffline;
        private ImageView ivAvatar, ivStatus;
        private LinearLayout llHi;

        public ViewHolder(View v) {
            this.ivAvatar = (ImageView) v.findViewById(R.id.ivAvatar);
            this.ivStatus = (ImageView) v.findViewById(R.id.ivStatus);
            this.tvName = (TextView) v.findViewById(R.id.tvName);
            this.tvStatus = (TextView) v.findViewById(R.id.tvStatus);
            this.tvDistance = (TextView) v.findViewById(R.id.tvDistance);
            this.tvTimeAfterOffline = (TextView) v.findViewById(R.id.tvTimeAfterOffline);
            this.llHi = (LinearLayout) v.findViewById(R.id.llHi);
        }
    }
}
