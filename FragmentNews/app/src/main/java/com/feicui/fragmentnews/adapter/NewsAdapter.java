package com.feicui.fragmentnews.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.feicui.fragmentnews.R;
import com.feicui.fragmentnews.bean.JsonData;
import com.feicui.fragmentnews.utils.IconAsynTask;
import com.feicui.fragmentnews.utils.ImageInstance;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/11/2 0002.
 */

public class NewsAdapter extends BaseAdapter implements BitmapListener{

    private LinkedList<JsonData> arrayList = new LinkedList<JsonData>();
    private Context context;
    private ViewHolder holder;
    private Bitmap bitmap;

    public NewsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate
                    (R.layout.layout_news_item, null);
            holder.tvSummary = (TextView) convertView.findViewById(R.id.tv_news_summary);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_news_title);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_news_time);
            holder.ivImg = (ImageView) convertView.findViewById(R.id.iv_news_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvSummary.setText(arrayList.get(position).getSummary());
        holder.tvTitle.setText(arrayList.get(position).getTitle());
        holder.tvTime.setText(arrayList.get(position).getStamp());
        // 显示图片
        String imgUrl = arrayList.get(position).getIcon();
        loadImg(imgUrl);
        return convertView;
    }

    public void setDataList(LinkedList<JsonData> arrayList) {
        this.arrayList = arrayList;
    }

    class ViewHolder {
        TextView tvSummary;
        TextView tvTitle;
        TextView tvTime;
        ImageView ivImg;
    }

    @Override
    public void getBitmapData(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void loadImg(String imgUrl){
        Bitmap bimaps = ImageInstance.getInstance().getImage(imgUrl);
        if(bimaps!=null){
            holder.ivImg.setImageBitmap(bimaps);
        }else{
            new IconAsynTask(this).execute(imgUrl);
            holder.ivImg.setImageBitmap(bitmap);

        }
    }

}
