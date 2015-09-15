package com.chaoyang805.jokes.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by chaoyang805 on 2015/8/15.
 * 显示Toast的工具类
 */
public class ToastUtils {

    private static Toast mToast;
    /**
     * 是否使用Toast
     */
    private static boolean isUseToast = true;

    /**
     * 是否使用Toast
     * @param useToast
     */
    public static void setIsUseToast(boolean useToast) {
        isUseToast = useToast;
    }

    /**
     *显示Toast，下一个Toast会直接覆盖上一个，而不是上一个显示完才显示下一个
     * @param context
     * @param msg
     * @param duration 使用Toast.LENGTH_SHORT或者Toast.LENGTH_LONG
     */
    public static void showToast(Context context, CharSequence msg, int duration) {
        //如果不使用Toast则直接返回
        if (!isUseToast) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, duration);
        } else {
            mToast.setDuration(duration);
            mToast.setText(msg);
        }
        mToast.show();
    }

    /**
     * 显示Toast，下一个Toast会直接覆盖上一个，而不是上一个显示完才显示下一个
     * @param context
     * @param strinResId 字符串资源id
     * @param duration 使用Toast.LENGTH_SHORT或者Toast.LENGTH_LONG
     */
    public static void showToast(Context context, int strinResId, int duration) {
        String msg = context.getResources().getString(strinResId);
        showToast(context, msg, duration);
    }
}
