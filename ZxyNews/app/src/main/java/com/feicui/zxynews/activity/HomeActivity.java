package com.feicui.zxynews.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.feicui.zxynews.R;
import com.feicui.zxynews.adapter.NewsAdapter;
import com.feicui.zxynews.bean.JsonData;
import com.feicui.zxynews.db.DBManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.feicui.zxynews.utils.HttpClientUtils.getHttp;

public class HomeActivity extends Activity {

    private ArrayList<JsonData> arrayList;
    private ListView lvNews;
    private InputStream is;
    private JsonData jsonData;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        lvNews = (ListView) findViewById(R.id.lv_news);
        newsAdapter = new NewsAdapter(HomeActivity.this);
        lvNews.setAdapter(newsAdapter);
        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomeActivity.this, NewsPageActivity.class);
                String link = arrayList.get(position).getLink();
                intent.putExtra("link", link);
                startActivity(intent);
            }
        });
       DBManager dbManager= DBManager.getInstance(this);
        if (dbManager.haveDataOrNot()) {
            arrayList = dbManager.selectData();
            newsAdapter.setDataList(arrayList);
            newsAdapter.notifyDataSetChanged();

        } else {
            // 往适配器中添加数据源
            new Thread(new Runnable() {
                @Override
                public void run() {
                    arrayList = getJsonData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            newsAdapter.setDataList(arrayList);
                            newsAdapter.notifyDataSetChanged();

                        }
                    });
                }
            }).start();
        }
    }

    public ArrayList<JsonData> getJsonData() {
        final String url = "http://118.244.212.82:9092/newsClient/news_list?ver=1&subid=1&dir=1&nid=1&stamp=20140321&cnt=20";
        arrayList = new ArrayList<JsonData>();
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
                // 获取网络图片资源
                URL imgURL = new URL(icon);
                // 获取连接
                HttpURLConnection conn = (HttpURLConnection) imgURL.openConnection();
                // 设置超时时间
                conn.setConnectTimeout(5000);
                // 连接设置获得数据流
                conn.setDoInput(true);
                // 不使用缓存
                conn.setUseCaches(false);
                // 得到数据流
                is = conn.getInputStream();
                // 解析得到的图片
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                // 将数据传入Bean文件当中
                jsonData = new JsonData(summary, bitmap, stamp, title, link, icon, nid,
                        type);
                DBManager.getInstance(HomeActivity.this).insertData(jsonData);
                // 将Bean对象添加到arrayList集合中
                arrayList.add(jsonData);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭数据流
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }
}
