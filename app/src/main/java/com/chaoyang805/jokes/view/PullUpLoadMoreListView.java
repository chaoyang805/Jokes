package com.chaoyang805.jokes.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.chaoyang805.jokes.R;

/**
 * Created by chaoyang805 on 2015/9/15.
 * 上拉加载更多的listview
 */
public class PullUpLoadMoreListView extends ListView implements AbsListView.OnScrollListener {
    /**
     * listview的footer，显示上拉加载更多的提示
     */
    private TextView mFooter;
    /**
     * listview总的item数量
     */
    private int mTotalItemCount;
    /**
     * 最后一个可见的item项的索引
     */
    private int mLastVisibleItem;
    /**
     * 是否正在加载更多的标志位
     */
    private boolean isLoading = false;
    /**
     * 加载更多的回调对象
     */
    private OnLoadMoreListener mListener;
    /**
     * 是否可以执行加载更多的操作
     */
    private boolean mCanLoadMore = true;


    /**
     * 加载更多时的监听接口
     */
    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    /**
     * 设置监听的方法
     * @param listener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mListener = listener;
    }


    public PullUpLoadMoreListView(Context context) {
        this(context, null);
    }

    public PullUpLoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mFooter = (TextView) LayoutInflater.from(context).inflate(R.layout.footer, null, false);
        this.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //如果最后一个可见的item是listview的最后一项，则显示下拉加载更多的footer
        if (mLastVisibleItem == mTotalItemCount && mCanLoadMore) {
            if (!isLoading) {
                this.addFooterView(mFooter);
                isLoading = true;
                if (mListener != null) {
                    mListener.onLoadMore();
                }
            }
        }
    }

    /**
     * 设置加载结束的方法
     */
    public void loadComplete(){
        this.removeFooterView(mFooter);
        isLoading = false;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        mCanLoadMore = canLoadMore;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //最后一个可见的item为第一个可见的item加上可见项目的数量
        mLastVisibleItem = firstVisibleItem + visibleItemCount;
        mTotalItemCount = totalItemCount;
    }

}
