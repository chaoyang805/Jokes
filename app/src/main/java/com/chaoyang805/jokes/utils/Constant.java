package com.chaoyang805.jokes.utils;

/**
 * Created by chaoyang805 on 2015/9/13.
 * 代码中所用到的常量类
 */
public class Constant {
    /**
     * fragment的argument的key
     */
    public static final String FRAGMENT_ARG_KEY = "arguments";
    /**
     * 网络连接超时的时间
     */
    public static final int TIME_OUT_MILLIS = 10000;
    /**
     * 请求成功的返回代码
     */
    public static final int RESULT_CODE_SUCCESS = 1;
    /**
     * 请求失败的返回代码
     */
    public static final int RESULT_CODE_FAIL = 0;
//    public static final String URL = "http://192.168.0.107/wordpress/wp-db.php";
    /**
     * 网络请求地址
     */
    public static final String URL = "http://jokesdemo.sinaapp.com/wp-db.php";
    /**
     * 网络请求类型的参数名
     */
    public static final String REQUEST_TYPE = "request_type";
    /**
     * 获取数据的数量参数
     */
    public static final String LIMIT = "limit";
    /**
     * 获取数据的起始id参数，根据id来进行分页
     */
    public static final String LAST_ID = "last_id";
    /**
     * 获取最新笑话的网络请求参数值
     */
    public static final String GET_JOKES = "get_jokes";
    /**
     * 获取评论的网络请求参数值
     */
    public static final String GET_COMMENTS = "get_comments";
    /**
     * 离线缓存文件的文件名
     */
    public static final String OFFLINE_FILENAME = "jokes.json";

}
