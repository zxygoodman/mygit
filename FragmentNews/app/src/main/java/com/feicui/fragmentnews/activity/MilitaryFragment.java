package com.feicui.fragmentnews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.feicui.fragmentnews.R;
import com.feicui.fragmentnews.adapter.NewsAdapter;
import com.feicui.fragmentnews.bean.JsonData;
import com.feicui.fragmentnews.db.DBManager;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

import static com.feicui.fragmentnews.utils.HttpClientUtils.getHttp;

/**
 * Created by Administrator on 2016/11/16 0016.
 */

public class MilitaryFragment extends Fragment {

    private LinkedList<JsonData> arrayList;
    private PullToRefreshListView lvNews;
    private JsonData jsonData;
    private NewsAdapter newsAdapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MyHandlerMessage(msg);

        }
    };

    private void MyHandlerMessage(Message msg) {
        // 往适配器中添加数据源
        switch (msg.what) {
            case 1:
                arrayList = (LinkedList<JsonData>) msg.obj;
                newsAdapter.setDataList(arrayList);
                newsAdapter.notifyDataSetChanged();
                break;
            case 2:
                arrayList = (LinkedList<JsonData>) msg.obj;
                newsAdapter.setDataList(arrayList);
                // 通知程序数据集已经改变，如果不做通知，那么将不会刷新ListView
                newsAdapter.notifyDataSetChanged();
                lvNews.onRefreshComplete();
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_military_fragment, null);

        lvNews = (PullToRefreshListView) view.findViewById(R.id.lv_news);

        lvNews.setMode(PullToRefreshBase.Mode.BOTH);
        newsAdapter = new NewsAdapter(getActivity());
        lvNews.setAdapter(newsAdapter);

        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewsPageActivity.class);
                String link = arrayList.get(position).getLink();
                intent.putExtra("link", link);
                startActivity(intent);
            }
        });
        // 判断数据库中是否存在数据
        DBManager dbManager = DBManager.getInstance(getActivity());
        if (dbManager.haveDataOrNot()) {
            arrayList = dbManager.selectData();
            newsAdapter.setDataList(arrayList);
            newsAdapter.notifyDataSetChanged();
        } else {
            // 开启线程从网络加载数据，用message发送到主线程中更新数据源
            new Thread(new Runnable() {
                @Override
                public void run() {
                    arrayList = getJsonData();
                    Message msg = new Message();

                    msg.what = 1;
                    msg.obj = arrayList;
                    handler.sendMessage(msg);

                }
            }).start();
        }
        // 下拉刷新，上拉加载数据
        initRefresh();

        return view;
    }

    private void initRefresh() {
        // 监听刷新
        lvNews.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        arrayList = getJsonData();
                        Message msg = new Message();

                        msg.what = 2;
                        msg.obj = arrayList;
                        handler.sendMessage(msg);

                    }
                }).start();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        arrayList = getJsonData();
                        Message msg = new Message();

                        msg.what = 2;
                        msg.obj = arrayList;
                        handler.sendMessage(msg);

                    }
                }).start();
            }
        });
    }

    public LinkedList<JsonData> getJsonData() {
        final String url = "http://118.244.212.82:9092/newsClient/news_list?ver=1&subid=1&dir=1&nid=1&stamp=20140321&cnt=20";
        arrayList = new LinkedList<JsonData>();
        try {
            String json = getHttp(url);
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject innerObject = jsonArray.getJSONObject(i);
                String summary = innerObject.getString("summary");
                String icon = innerObject.getString("icon");
                String stamp = innerObject.getString("stamp");
                String title = innerObject.getString("title");
                String link = innerObject.getString("link");
                int nid = innerObject.getInt("nid");
                int type = innerObject.getInt("type");
                // 将数据传入Bean文件当中
                jsonData = new JsonData(summary, icon, stamp, title, link, nid,
                        type);
                DBManager.getInstance(getActivity()).insertData(jsonData);
                // 将Bean对象添加到arrayList集合中
                arrayList.add(jsonData);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
