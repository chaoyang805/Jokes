package com.chaoyang805.jokes.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.chaoyang805.jokes.utils.Constant;

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
     * 网络请求的地址
     */
    private String mUrl;
    /**
     * 网络请求已经完成的标志位
     */
    private volatile boolean mIsComplete = false;
    /**
     * 异步任务对象
     */
    private AsyncTask<String, Void, String> mTask;

    /**
     * 构造方法里开启一个线程来执行网络操作
     *
     * @param urlStr       网络地址
     * @param paramsName   网址中的参数名称
     * @param paramsValues 网址中的参数值下标和paramsName一一对应
     */
    public BaseConnection(String urlStr, String[] paramsName, String[] paramsValues, final CallBack callBack) {
        //将原始的网址和请求参数拼接起来
        StringBuffer urlBuffer = new StringBuffer(urlStr + "?");
        for (int i = 0; i < paramsName.length; i++) {
            urlBuffer.append(paramsName[i] + "=" + paramsValues[i]);
            if (i != paramsName.length - 1) {
                urlBuffer.append("&");
            }
        }
        mUrl = urlBuffer.toString();
        mTask = new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... urlParams) {
                URL url = null;
                InputStream in = null;
                InputStreamReader isr = null;
                BufferedReader bfr = null;
                try {
                    url = new URL(urlParams[0].toString());
                    final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //设置超时时长为10s
                    connection.setConnectTimeout(Constant.TIME_OUT_MILLIS);
                    //开启一个线程来计算超时的时间
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                Thread.sleep(Constant.TIME_OUT_MILLIS);
                                //连接超时后通过publishProgress更新进度
                                if (!mIsComplete) {
                                    publishProgress();
                                    connection.disconnect();
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }.start();
                    //从连接中获得输入流
                    in = connection.getInputStream();
                    //将输入流包装
                    isr = new InputStreamReader(in, "UTF-8");
                    bfr = new BufferedReader(isr);
                    String line;
                    StringBuffer sb = new StringBuffer();
                    //一行一行地读取
                    while ((line = bfr.readLine()) != null) {
                        sb.append(line);
                    }
                    //关闭输入流
                    bfr.close();
                    isr.close();
                    in.close();
                    return sb.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //返回为null,说明出现异常
                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
                //执行到这里时，说明已经连接超时
                if (callBack != null) {
                    callBack.onFinished(Constant.RESULT_CODE_FAIL, null);
                }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                mIsComplete = true;
                //网络请求完成后执行回调方法
                if (callBack != null) {
                    if (!TextUtils.isEmpty(result)) {
                        callBack.onFinished(Constant.RESULT_CODE_SUCCESS, result);
                    } else {
                        callBack.onFinished(Constant.RESULT_CODE_FAIL, result);
                    }
                }
            }
        };
    }

    /**
     * 开始进行网络连接
     */
    public void connect() {
        mTask.execute(mUrl);
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
