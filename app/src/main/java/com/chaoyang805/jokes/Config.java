package com.chaoyang805.jokes;

/**
 * Created by chaoyang805 on 2015/9/13.
 */
public class Config {
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
