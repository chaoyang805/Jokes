package com.chaoyang805.jokes.net;

import com.chaoyang805.jokes.utils.Constant;

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
     *
     * @param callBack 网络请求完成的回调
     * @param limit    请求笑话的数量
     */
    public GetJokes(final CallBack callBack, int limit,int lastId) {

        String[] paramsName = {Constant.REQUEST_TYPE, Constant.LIMIT,Constant.LAST_ID};
        String[] paramsValues = {Constant.GET_JOKES, String.valueOf(limit), String.valueOf(lastId)};
        //实例化一个网络基础类的对象
        mConnection = new BaseConnection(Constant.URL, paramsName, paramsValues, new BaseConnection.CallBack() {
            @Override
            public void onFinished(int resultCode, String result) {
                if (resultCode == Constant.RESULT_CODE_SUCCESS) {
                    if (callBack != null) {
                        callBack.onFinished(Constant.RESULT_CODE_SUCCESS, result);
                    }
                } else {
                    if (callBack != null) {
                        callBack.onFinished(Constant.RESULT_CODE_FAIL, result);
                    }
                }
            }
        });
    }

    /**
     * 开启网络请求的方法
     */
    public void startRequest() {
        mConnection.connect();
    }


    /**
     * 网络请求完成的回调
     */
    public interface CallBack {
        void onFinished(int resultCode, String result);
    }
}
