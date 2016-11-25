package com.feicui.fragmentnews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.feicui.fragmentnews.R;
import com.feicui.fragmentnews.activity.HomeActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/21 0021.
 */

public class LeftAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> list;
    public LeftAdapter(HomeActivity homeActivity, ArrayList<String> list){
        this.context = homeActivity;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_left_list,null);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_title);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv_img);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(list.get(position));
        return convertView;
    }

    class ViewHolder {
        ImageView iv;
        TextView tv;
    }
}
