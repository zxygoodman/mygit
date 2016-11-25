package com.feicui.fragmentnews.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxingye on 2016/11/1 0001.
 */

public class HttpClientUtils {

    private static HttpClient httpclient;
    private static String resultStr;

    public static synchronized HttpClient getHttpClient() {
        if (httpclient == null) {
            // 建立参数对象
            HttpParams params = new BasicHttpParams();
            // 设置超时时间
            ConnManagerParams.setTimeout(params, 5000);
            // 设置最大连接数
            ConnManagerParams.setMaxTotalConnections(params, 8);
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            httpclient = new DefaultHttpClient(params);
        }
        return httpclient;
    }

    public static String getHttp(final String url) {
        try {
            // 获取httpClient
            HttpClient httpClient = getHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = httpResponse.getEntity();
                resultStr = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }

    public static String postHttp(String url, String key, String value) {
        String result = null;
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair(key, value));
        try {
            HttpPost httpPost = new HttpPost();
            httpPost.setEntity(new UrlEncodedFormEntity(list,
                    org.apache.http.protocol.HTTP.UTF_8));
            HttpResponse httpResponse = httpclient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
