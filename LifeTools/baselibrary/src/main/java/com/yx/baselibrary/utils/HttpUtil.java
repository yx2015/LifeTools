package com.yx.baselibrary.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by yx on 2017-03-14.
 */

public class HttpUtil {


    public static void sendHttpRequest(String baseUrl, HttpCallBackListener listener) {
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(baseUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(8000);
            httpURLConnection.setReadTimeout(8000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            InputStream in = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }

            if (listener != null) {
                listener.onFinsh(response.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onError(e);
            }
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }


    public interface HttpCallBackListener {
        void onFinsh(String response);

        void onError(Exception e);
    }


    public static void sendOkHttpRequest(String baseUrl, Callback callback){
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder().url(baseUrl).build();
        okHttpClient.newCall(request).enqueue(callback);

    }

}
