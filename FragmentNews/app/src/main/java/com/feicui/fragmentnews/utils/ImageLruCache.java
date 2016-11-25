package com.feicui.fragmentnews.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/11/10 0010.
 */

public class ImageLruCache extends LruCache<String,Bitmap> {

    private HashMap<String,SoftReference<Bitmap>> map = new HashMap<String,SoftReference<Bitmap>>();
    public ImageLruCache(int maxSize, HashMap<String, SoftReference<Bitmap>> map) {
        super(maxSize);
        this.map = map;
    }
    // 强引用添加图片大小
    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getByteCount();
    }
    // 移除图片放入软引用
    @Override
    protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {

        if (evicted) {
            SoftReference<Bitmap> softReference = new SoftReference<Bitmap>(oldValue);
            map.put(key,softReference);
        }
    }
}
