package com.chaoyang805.jokes.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.chaoyang805.jokes.Config;
import com.chaoyang805.jokes.R;
import com.chaoyang805.jokes.adapter.JokeListAdapter;
import com.chaoyang805.jokes.bean.Joke;
import com.chaoyang805.jokes.net.BaseConnection;
import com.chaoyang805.jokes.net.GetJokes;
import com.chaoyang805.jokes.utils.FileTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chaoyang805 on 2015/9/13.
 */
public class FragmentJokes extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
    /**
     * 下拉刷新控件
     */
    private SwipeRefreshLayout mRefreshLayout;
    /**
     * listview的数据适配器
     */
    private JokeListAdapter mAdapter;
    /**
     * listview 控件
     */
    private ListView mListView;
    /**
     * 保存数据库中查询出来的笑话数据
     */
    private List<Joke> mJokes;

    public interface OnItemClickListener {
        void onItemClick(Joke joke);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView;
        rootView = inflater.inflate(R.layout.fragment_jokes, container, false);
        initViews(rootView);
        if (!BaseConnection.isNetWorkAvailable(getActivity())) {
            //如果没有网络连接就加载离线缓存数据
            getCachedJokes();
        } else {
            //有网络连接就自动刷新加载最新的网络数据
            mRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(true);
                }
            });
            this.onRefresh();
        }
        return rootView;
    }

    /**
     * 网络连接失败时获取本地的离线缓存
     */
    private void getCachedJokes() {
        String cachedJokes = FileTools.readCachedJokes(getActivity());
        //获取缓存数据失败后，不进行任何操作
        if (cachedJokes.equals(String.valueOf(Config.RESULT_CODE_FAIL))) {
            Toast.makeText(getActivity(), "获取缓存数据失败", Toast.LENGTH_SHORT).show();
        } else {
            List<Joke> jokes = FileTools.parseJokeResult(cachedJokes);
            //获得本地数据成功后刷新listView的数据
            mAdapter.refreshList(jokes);
            Toast.makeText(getActivity(), "无网络连接，加载离线数据", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 初始化视图控件
     *
     * @param rootView
     */
    private void initViews(View rootView) {

        //初始化listView
        mJokes = new ArrayList<>();
        mAdapter = new JokeListAdapter(getActivity(), mJokes);
        mListView = (ListView) rootView.findViewById(R.id.joke_list);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        //初始化下拉刷新控件
        mRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    /**
     * 下拉刷新时从网络中加载数据
     */
    @Override
    public void onRefresh() {
        if (BaseConnection.isNetWorkAvailable(getActivity())) {

            new GetJokes(new GetJokes.CallBack() {
                @Override
                public void onFinished(int resultCode, String result) {
                    if (resultCode == Config.RESULT_CODE_SUCCESS) {
                        if (TextUtils.isEmpty(result)) {
                            mRefreshLayout.setRefreshing(false);
                            Toast.makeText(getActivity(), String.format("加载失败"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        FileTools.cacheJokes(getActivity(), result);
                        List<Joke> jokes = FileTools.parseJokeResult(result);
                        mAdapter.refreshList(jokes);
                        Toast.makeText(getActivity(), String.format("加载完成"), Toast.LENGTH_SHORT).show();
                        mRefreshLayout.setRefreshing(false);
                    } else if (resultCode == Config.RESULT_CODE_FAIL) {
                        Toast.makeText(getActivity(), String.format("加载失败"), Toast.LENGTH_SHORT).show();
                        mRefreshLayout.setRefreshing(false);
                    }
                }
                //每次请求获取最新的10条数据
            }, 10);
        } else {
            mRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), "无网络连接", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!(getActivity() instanceof OnItemClickListener)) {
            throw new IllegalStateException("MainActivty must implents " +
                    "com.chaoyang805.jokes.fragment.FragmentJokes.OnItemClickListener");
        } else {
            Joke joke = mJokes.get(position);
            ((OnItemClickListener) getActivity()).onItemClick(joke);
        }
    }

}
