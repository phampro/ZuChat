package com.hoangsong.zumechat.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.ChatInfo;
import com.hoangsong.zumechat.untils.MyDateTime;
import com.hoangsong.zumechat.view.CircleTransform;
import com.squareup.picasso.Picasso;

public class ListChatAdapter extends RecyclerView.Adapter<ListChatAdapter.DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<ChatInfo> mDataset = new ArrayList<ChatInfo>();
    private static Activity context;
    private String userid = "";


    public static class DataObjectHolder extends RecyclerView.ViewHolder
    /*implements View
            .OnClickListener*/ {
    	RelativeLayout rlChatLeft, rlTvChatRight, rlTvChatLeft;
    	LinearLayout llChatRIght, llInfo;
        TextView tvName, chatTextLeft, chatTextRight, tvInfo, tvRightTime, tvLeftTime;
        ImageView ivAvata;

        public DataObjectHolder(View itemView) {
            super(itemView);
            rlChatLeft = (RelativeLayout) itemView.findViewById(R.id.rlChatLeft);
            rlTvChatRight = (RelativeLayout) itemView.findViewById(R.id.rlTvChatRight);
            rlTvChatLeft = (RelativeLayout) itemView.findViewById(R.id.rlTvChatLeft);
            llChatRIght = (LinearLayout) itemView.findViewById(R.id.llChatRIght);
            llInfo = (LinearLayout) itemView.findViewById(R.id.llInfo);
            ivAvata = (ImageView) itemView.findViewById(R.id.ivAvata);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            chatTextLeft = (TextView) itemView.findViewById(R.id.msgrLeft);
            chatTextRight = (TextView) itemView.findViewById(R.id.msgrRight);
            tvInfo = (TextView) itemView.findViewById(R.id.msgrInfo);
            tvLeftTime = (TextView) itemView.findViewById(R.id.tvLeftTime);
            tvRightTime = (TextView) itemView.findViewById(R.id.tvRightTime);
            
            Log.i(LOG_TAG, "Adding Listener");
            //itemView.setOnClickListener(this);
        }

        /*@Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }*/
    }

   /* public void setOnItemClickListener(MyClickListener myClickListener) {
        //ListChatAdapter.myClickListener = myClickListener;
    }*/

    public ListChatAdapter(Activity context, ArrayList<ChatInfo> myDataset) {
        this.mDataset = myDataset;
        this.userid = Prefs.getUserInfo() != null ? Prefs.getUserInfo().getId() : "";
        this.context = context;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        ChatInfo chatObj = mDataset.get(position);
    	if(chatObj.getBackground()!=0){
    		if(chatObj.getBackground() == 1){
    			holder.rlTvChatRight.setBackgroundResource(R.drawable.balloon_outgoing_normal);
    		}else if(chatObj.getBackground() == -1){
    			holder.rlTvChatRight.setBackgroundResource(R.drawable.balloon_outgoing_normal_etx);
    		}else if(chatObj.getBackground() == 2){
    			holder.rlTvChatLeft.setBackgroundResource(R.drawable.balloon_incoming_normal);
    		}else if(chatObj.getBackground() == -2){
    			holder.rlTvChatLeft.setBackgroundResource(R.drawable.balloon_incoming_normal_ext);
    		}
    	}
    	if(userid.equals(chatObj.getSender_id())){
    		holder.rlChatLeft.setVisibility(View.GONE);
    		holder.llChatRIght.setVisibility(View.VISIBLE);
    		holder.llInfo.setVisibility(View.GONE);
    		holder.chatTextRight.setText(chatObj.getChat_message());
    		if(!chatObj.getCreated_on().equals("") && chatObj.isShowTime()){
    			holder.tvRightTime.setVisibility(View.VISIBLE);
        		holder.tvRightTime.setText(MyDateTime.getFormatHourRegStr(chatObj.getCreated_on()));
    		}else{
    			holder.tvRightTime.setVisibility(View.GONE);
    		}
    		
    	}else if(!chatObj.getSender_id().equals("")){
    		holder.rlChatLeft.setVisibility(View.VISIBLE);
    		holder.llChatRIght.setVisibility(View.GONE);
    		holder.llInfo.setVisibility(View.GONE);
    		holder.tvName.setText(chatObj.getUsername());
    		holder.chatTextLeft.setText(chatObj.getChat_message());
    		if(!chatObj.getCreated_on().equals("") && chatObj.isShowTime()){
    			holder.tvLeftTime.setVisibility(View.VISIBLE);
    			holder.tvLeftTime.setText(MyDateTime.getFormatHourRegStr(chatObj.getCreated_on()));
    		}else{
    			holder.tvLeftTime.setVisibility(View.GONE);
    		}
    		
    		holder.ivAvata.setBackgroundResource(0);
    		if(chatObj.getBackground()!= -1 && chatObj.getBackground()!= -2){
    			holder.tvName.setVisibility(View.VISIBLE);
    			String urlImage = chatObj.getPhoto_url();
        		if(!urlImage.equalsIgnoreCase("")){
                    Picasso.with(context).load(urlImage).transform(new CircleTransform()).error(R.drawable.ic_launcher).resize(200, 200).centerCrop().placeholder(R.drawable.ic_launcher).into(holder.ivAvata);
        		}else{
        			holder.ivAvata.setImageResource(R.drawable.ic_launcher);
        		}
    		}else{
    			holder.ivAvata.setImageResource(R.drawable.ic_message_soild);
    			holder.tvName.setVisibility(View.GONE);
    		}
    		
    	}else{
    		holder.rlChatLeft.setVisibility(View.GONE);
    		holder.llChatRIght.setVisibility(View.GONE);
    		holder.llInfo.setVisibility(View.VISIBLE);
    		holder.tvInfo.setText(chatObj.getChat_message());
    	}
    }

    public void addItem(ChatInfo dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    public ChatInfo getItem(int index) {
        return this.mDataset.get(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    public void add(ChatInfo object) {
        mDataset.add(object);
        notifyDataSetChanged();
    }
}