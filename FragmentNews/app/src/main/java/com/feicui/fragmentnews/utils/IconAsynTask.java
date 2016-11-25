package com.feicui.fragmentnews.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.feicui.fragmentnews.adapter.BitmapListener;
import com.feicui.fragmentnews.adapter.NewsAdapter;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2016/11/9 0009.
 */

public class IconAsynTask extends AsyncTask<String, Void, Bitmap> {

    private BitmapListener bitmapListener;

    public IconAsynTask(NewsAdapter newsAdapter) {
        this.bitmapListener = newsAdapter;
    }

    private String stringURL;

    // 该方法将在执行实际的后台操作前被UI thread调用。可以在该方法中做一些准备工作，如在界面上显示一个进度条
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    // 将在onPreExecute 方法执行后马上执行，该方法运行在后台线程中。这里将主要负责执行那些很耗时的后台计算工作
    // 可以调用 publishProgress方法来更新实时的任务进度。该方法是抽象方法，子类必须实现
    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bitmap = null;
        stringURL = params[0];
        try {
            URL url = new URL(params[0]);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // 在publishProgress方法被调用后，UI thread将调用这个方法从而在界面上展示任务的进展情况，例如通过一个进度条进行展示
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
    // 在doInBackground 执行完成后，onPostExecute 方法将被UI thread调用，后台的计算结果将通过该方法传递到UI thread
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap != null) {
            ImageInstance.getInstance().getCache().put(stringURL, bitmap);
            bitmapListener.getBitmapData(bitmap);
        }

    }
}
