package com.chaoyang805.jokes.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chaoyang805.jokes.utils.Constant;
import com.chaoyang805.jokes.R;
import com.chaoyang805.jokes.model.Joke;

/**
 * Created by chaoyang805 on 2015/9/13.
 */
public class FragmentJokeDetail extends Fragment {


    private TextView mTvTitle;
    private TextView mTvContent;
    private TextView mTvPostDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_joke_detail, container, false);
        //初始化视图控件
        initViews(rootView);
        //获得传过来的bundle对象
        Bundle arguments = getArguments();
        Joke joke = (Joke) arguments.getSerializable(Constant.FRAGMENT_ARG_KEY);

        //将数据显示在textview上
        String title = joke.getTitle();
        String content = joke.getContent();
        String date = joke.getPostDate();

        mTvTitle.setText(title);
        mTvContent.setText(getString(R.string.joke_content,content));
        //时间只显示到分钟，去掉秒
        mTvPostDate.setText(getString(R.string.post_date, date.substring(0, date.lastIndexOf(":"))));
        return rootView;
    }

    /**
     * 初始化视图控件
     * @param rootView
     */
    private void initViews(View rootView) {
        mTvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        mTvContent = (TextView) rootView.findViewById(R.id.tv_content);
        mTvPostDate = (TextView) rootView.findViewById(R.id.tv_post_date);
    }
}
