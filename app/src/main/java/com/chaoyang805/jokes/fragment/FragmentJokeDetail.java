package com.chaoyang805.jokes.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chaoyang805.jokes.R;
import com.chaoyang805.jokes.bean.Joke;

/**
 * Created by chaoyang805 on 2015/9/13.
 */
public class FragmentJokeDetail extends Fragment {

    public static final String ARGUMENTS_KEY = "arguments";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_joke_detail, container, false);
        TextView tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        TextView tvContent = (TextView) rootView.findViewById(R.id.tv_content);
        TextView tvPostDate = (TextView) rootView.findViewById(R.id.tv_post_date);
        Bundle arguments = getArguments();
        Joke joke = (Joke) arguments.getSerializable(ARGUMENTS_KEY);

        String title = joke.getTitle();
        String content = joke.getContent();
        String date = joke.getPostDate();

        tvTitle.setText(title);
        tvContent.setText(getString(R.string.joke_content,content));
        tvPostDate.setText(getString(R.string.post_date, date.substring(0, date.lastIndexOf(":"))));
        return rootView;
    }


}
