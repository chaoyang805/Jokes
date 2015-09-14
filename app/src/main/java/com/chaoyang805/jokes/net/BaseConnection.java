package com.chaoyang805.jokes.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.chaoyang805.jokes.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by chaoyang805 on 2015/9/13.
 * 网络通信的基础类
 */
public class BaseConnection {

    private static final String TAG = "BaseConnection";

    /**
     * 构造方法里开启一个线程来执行网络操作
     *
     * @param urlStr       网络地址
     * @param paramsName   网址中的参数名称
     * @param paramsValues 网址中的参数值下标和paramsName一一对应
     */
    public BaseConnection(final String urlStr, String[] paramsName, String[] paramsValues, final CallBack callBack) {
        final StringBuffer urlBuffer = new StringBuffer(urlStr + "?");
        for (int i = 0; i < paramsName.length; i++) {
            urlBuffer.append(paramsName[i] + "=" + paramsValues[i]);
            if (i != paramsName.length - 1) {
                urlBuffer.append("&");
            }
        }
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                try {
                    URL url = new URL(urlBuffer.toString());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(10000);
                    InputStream inputStream = connection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader bfr = new BufferedReader(isr);
                    String line;
                    StringBuffer sb = new StringBuffer();
                    while ((line = bfr.readLine()) != null) {
                        sb.append(line);
                    }
                    bfr.close();
                    isr.close();
                    inputStream.close();
                    return  sb.toString();
                } catch (MalformedURLException e) {
                    if (callBack != null) {
                        callBack.onFinished(Config.RESULT_CODE_FAIL, null);
                    }
                    e.printStackTrace();
                } catch (IOException e) {
                    if (callBack != null) {
                        callBack.onFinished(Config.RESULT_CODE_FAIL, null);
                    }
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (callBack != null) {
                    callBack.onFinished(Config.RESULT_CODE_SUCCESS, result);
                }
            }
        }.execute();
    }

    /**
     * 判断网络是否可用的方法
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 网络请求完成后的回调
     */
    public interface CallBack {
        /**
         * @param resultCode 返回代码，1表示成功，0表示失败。
         * @param result     json格式的结果字符串
         */
        void onFinished(int resultCode, String result);
    }

}
