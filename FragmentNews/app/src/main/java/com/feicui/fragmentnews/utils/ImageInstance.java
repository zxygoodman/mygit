package com.feicui.fragmentnews.utils;

import android.graphics.Bitmap;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * =hn
 */

public class ImageInstance {

    private static ImageInstance imageInstance;
    private  ImageLruCache imageLru;
    private static HashMap<String, SoftReference<Bitmap>> map = new HashMap<String, SoftReference<Bitmap>>();

    private ImageInstance() {

    }

    public static ImageInstance getInstance() {
        if (imageInstance == null) {
            imageInstance = new ImageInstance();
        }
        return imageInstance;
    }

    public  ImageLruCache getCache() {

        int size = (int) Runtime.getRuntime().maxMemory();
        int maxSize = size / 8;
        if(imageLru==null){
            imageLru = new ImageLruCache(maxSize, map);

        }

        return imageLru;
    }

    public Bitmap getImage(String key) {
        Bitmap bitmap = getCache().get(key);
        if (bitmap != null) {
            return bitmap;
        } else {
            SoftReference<Bitmap> softReference = map.get(key);
            if (softReference != null) {
                bitmap = softReference.get();
                return bitmap;
            } else {
                map.remove(key);
            }
        }
        return null;
    }
}
