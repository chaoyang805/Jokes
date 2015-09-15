package com.chaoyang805.jokes.utils;

import android.content.Context;

import com.chaoyang805.jokes.model.Joke;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chaoyang805 on 2015/9/14.
 */
public class FileTools {


    /**
     * 将笑话离线缓存到本地
     * @param content
     */
    public static void cacheJokes(Context context,String content){

        try {
            FileOutputStream fos = context.openFileOutput(Constant.OFFLINE_FILENAME, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            osw.write(content);
            osw.flush();
            fos.flush();
            osw.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从手机内存读取离线的数据
     * @param context
     * @return
     */
    public static String readCachedJokes(Context context) {
        String result;
        try {
            FileInputStream fis = context.openFileInput(Constant.OFFLINE_FILENAME);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            char[] buffer = new char[fis.available()];
            isr.read(buffer);
            isr.close();
            fis.close();
            result = new String(buffer);
            return result;
            //出现异常说明缓存数据获取失败，将result赋值为失败的代码进行返回
        } catch (FileNotFoundException e) {
            result = String.valueOf(Constant.RESULT_CODE_FAIL);
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            result = String.valueOf(Constant.RESULT_CODE_FAIL);
            e.printStackTrace();
        } catch (IOException e) {
            result = String.valueOf(Constant.RESULT_CODE_FAIL);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将json格式的字符串解析成joke对象
     * @param result
     * @return 返回为joke类型的list
     */
    public static List<Joke> parseJokeResult(String result) {
        try {
            //去掉结果中的html标签
            result = result.replaceAll("\\<.*?>", "");
            //解析json字符串
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArr = jsonObject.getJSONArray("result");
            JSONObject jsonItem;
            Joke joke;
            List<Joke> jokes = new ArrayList<>();
            for (int i = 0; i < jsonArr.length(); i++) {
                //将json对象封装成joke对象，添加到List中
                jsonItem = jsonArr.getJSONObject(i);
                joke = new Joke(jsonItem.getInt("ID"), jsonItem.getString("title"),
                        jsonItem.getString("content"), jsonItem.getInt("comment_count"),
                        jsonItem.getString("date"));
                jokes.add(joke);
            }
            return jokes;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
