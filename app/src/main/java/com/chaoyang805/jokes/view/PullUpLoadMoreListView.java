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
     * 是否滑动到了最后一项
     */
    private boolean isScrollToBottom = false;
    /**
     * footerview的高度
     */
    private int mFooterHeight = 0;

    /**
     * 加载更多时的监听接口
     */
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    /**
     * 设置监听的方法
     *
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
        initFooterView();
        this.setOnScrollListener(this);
    }

    /**
     * 初始化footerview
     */
    private void initFooterView() {
        mFooter = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.footer, null, false);
        mFooter.measure(0, 0);
        mFooterHeight = mFooter.getMeasuredHeight();
        //隐藏footerview
        mFooter.setPadding(0, -mFooterHeight, 0, 0);
        this.addFooterView(mFooter);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_FLING || scrollState == SCROLL_STATE_IDLE) {

            if (isScrollToBottom && !isLoading) {
                if (!isLoading) {
                    isLoading = true;
                    mFooter.setPadding(0, 0, 0, 0);
                    setSelection(this.getCount());

                    if (mListener != null) {
                        mListener.onLoadMore();
                    }
                }
            }
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (getLastVisiblePosition() == totalItemCount - 1) {
            isScrollToBottom = true;
        } else {
            isScrollToBottom = false;
        }
    }


    /**
     * 设置加载结束的方法
     */
    public void loadComplete() {
        mFooter.setPadding(0,-mFooterHeight,0,0);
        isLoading = false;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        mCanLoadMore = canLoadMore;
    }

    public boolean isCanLoadMore(){
        return mCanLoadMore;
    }

    /**
     * 设置footer上的文本
     *
     * @param text
     */
    public void setFooterText(String text) {
        mFooter.setText(text);
    }

}
