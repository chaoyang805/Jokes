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
import android.widget.TextView;
import android.widget.Toast;

import com.chaoyang805.jokes.R;
import com.chaoyang805.jokes.adapter.JokeListAdapter;
import com.chaoyang805.jokes.model.Joke;
import com.chaoyang805.jokes.net.BaseConnection;
import com.chaoyang805.jokes.net.GetJokes;
import com.chaoyang805.jokes.utils.Constant;
import com.chaoyang805.jokes.utils.FileTools;
import com.chaoyang805.jokes.utils.ToastUtils;
import com.chaoyang805.jokes.view.PullUpLoadMoreListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chaoyang805 on 2015/9/13.
 */
public class FragmentJokes extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        AdapterView.OnItemClickListener, PullUpLoadMoreListView.OnLoadMoreListener {
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
    private PullUpLoadMoreListView mListView;
    /**
     * 提示用户下拉刷新的textview
     */
    private TextView mTvHint;
    /**
     * 获取新笑话数据的对象
     */
    private GetJokes mNewJokes;
    /**
     * 当前显示的页数
     */
    private int mCurrentPage = 1;
    /**
     * 每页加载的数据数量
     */
    private int mPerPageItemCount = 10;

    /**
     * list的item被点击到时的回调
     */
    public interface OnItemClickListener {
        void onItemClick(Joke joke);
    }

    /**
     * 保存数据库中查询出来的笑话数据
     */
    private List<Joke> mJokes;
    /**
     * 获取笑话数据的回调
     */
    private GetJokes.CallBack mJokesCallback = new GetJokes.CallBack() {
        @Override
        public void onFinished(int resultCode, String result) {

            if (mCurrentPage > 1) {
                mListView.loadComplete();
            }
            if (resultCode == Constant.RESULT_CODE_SUCCESS) {
                if (TextUtils.isEmpty(result)) {
                    ToastUtils.showToast(getActivity(), R.string.no_more_jokes, Toast.LENGTH_SHORT);
                } else {
                    //将json格式的数据解析成joke对象，加载到listView中
                    List<Joke> jokes = FileTools.parseJokeResult(result);
                    //如果获得的数据数量小于10就不再开启加载更多的功能
                    mListView.setCanLoadMore(jokes.size() >= mPerPageItemCount);
                    if (mCurrentPage == 1) {
                        //只缓存第一页的数据到本地
                        FileTools.cacheJokes(getActivity(), result);
                        mAdapter.refreshList(jokes);
                        ToastUtils.showToast(getActivity(), R.string.loading_complete, Toast.LENGTH_SHORT);
                    } else {
                        mAdapter.addAll(jokes);
                        ToastUtils.showToast(getActivity(), String.format("加载第%d页", mCurrentPage), Toast.LENGTH_SHORT);
                    }
                }
            } else if (resultCode == Constant.RESULT_CODE_FAIL) {
                ToastUtils.showToast(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT);
            }
            //无论哪种情况，都要取消下拉刷新的显示
            mRefreshLayout.setRefreshing(false);
        }
    };


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
        if (cachedJokes.equals(String.valueOf(Constant.RESULT_CODE_FAIL))) {
            ToastUtils.showToast(getActivity(), R.string.failed_to_get_offline_data, Toast.LENGTH_SHORT);
            if (mJokes.size() <= 0) {
                shouldShowHint();
            }
        } else {
            List<Joke> jokes = FileTools.parseJokeResult(cachedJokes);
            //获得本地数据成功后刷新listView的数据
            mAdapter.refreshList(jokes);
            //如果获得的数据数量小于10就不再开启加载更多的功能
            mListView.setCanLoadMore(jokes.size() >= mPerPageItemCount);
            shouldShowHint();
            ToastUtils.showToast(getActivity(), R.string.no_internet_loading_offline_data, Toast.LENGTH_SHORT);
        }
    }

    /**
     * 根据mJokes是否为0来判断是否显示下拉刷新的提示
     */
    private void shouldShowHint() {
        if (mJokes.size() > 0) {
            mListView.setVisibility(View.VISIBLE);
            mTvHint.setVisibility(View.GONE);
        } else {
            mListView.setVisibility(View.GONE);
            mTvHint.setVisibility(View.VISIBLE);
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
        mListView = (PullUpLoadMoreListView) rootView.findViewById(R.id.joke_list);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnLoadMoreListener(this);
        mTvHint = (TextView) rootView.findViewById(R.id.tv_hint);
        mTvHint.setVisibility(View.GONE);
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
        //下拉刷新默认回到第一页
        mCurrentPage = 1;
        if (BaseConnection.isNetWorkAvailable(getActivity())) {
            //如果网络可用，开启获取笑话的网络请求,下拉刷新加载第一页时，为startId赋一个很大的值，确保获得的为最新的数据
            mNewJokes = new GetJokes(mJokesCallback, mPerPageItemCount, 1000000);
            mNewJokes.startRequest();
        } else {
            //无网络的话，停止刷新，并进行提示
            getCachedJokes();
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onLoadMore() {
        //获得最后一个item的id.
        int lastId = mAdapter.getItem(mJokes.size() - 1).getID();
        if (BaseConnection.isNetWorkAvailable(getActivity())) {
            //如果网络可用，开启获取笑话的网络请求,下拉刷新加载第一页时，为startId赋一个很大的值，确保获得的为最新的数据
            mNewJokes = new GetJokes(mJokesCallback, mPerPageItemCount, lastId);
            mNewJokes.startRequest();
            mCurrentPage++;
        } else {
            ToastUtils.showToast(getActivity(), R.string.internet_unavailable_cant_load_more, Toast.LENGTH_SHORT);
            mListView.loadComplete();
        }
    }

    /**
     * listView的item被点击时，传递到Activity中
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //关联的activity必须实现fragment的接口，否则就抛出异常
        if (!(getActivity() instanceof OnItemClickListener)) {
            throw new IllegalStateException("MainActivty must implents " +
                    "com.chaoyang805.jokes.fragment.FragmentJokes.OnItemClickListener");
        } else {
            if (view.getId() != R.id.footer) {
                Joke joke = mJokes.get(position);
                ((OnItemClickListener) getActivity()).onItemClick(joke);
            }
        }
    }

}
