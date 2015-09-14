package com.chaoyang805.jokes.net;

import com.chaoyang805.jokes.Config;

/**
 * Created by chaoyang805 on 2015/9/13.
 */
public class GetJokes {
    /**
     * baseConnection对象
     */
    private BaseConnection mConnection;

    /**
     * 构造方法里实例化一个BaseConnection对象来从网络中获取笑话数据
     * @param callBack 网络请求完成的回调
     * @param count 请求笑话的数量
     */
    public GetJokes(final CallBack callBack,int count) {
        String[] paramsName = {Config.REQUEST_TYPE,Config.LIMIT};
        String[] paramsValues = {Config.GET_JOKES, String.valueOf(count)};
        mConnection = new BaseConnection(Config.URL, paramsName, paramsValues,
                new BaseConnection.CallBack() {
                    @Override
                    public void onFinished(int resultCode, String result) {
                        if (resultCode == Config.RESULT_CODE_SUCCESS) {
                            if (callBack != null) {
                                callBack.onFinished(Config.RESULT_CODE_SUCCESS, result);
                            }
                        } else {
                            if (callBack != null) {
                                callBack.onFinished(Config.RESULT_CODE_FAIL, result);
                            }
                        }
                    }
                });
    }

    /**
     * 网络请求完成的回调
     */
    public interface CallBack {
        void onFinished(int resultCode, String result);
    }
}
