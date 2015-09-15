package com.chaoyang805.jokes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chaoyang805.jokes.R;
import com.chaoyang805.jokes.model.Joke;

import java.util.List;

/**
 * Created by chaoyang805 on 2015/9/13.
 */
public class JokeListAdapter extends BaseAdapter {
    /**
     * context对象
     */
    private Context mContext;
    /**
     * 笑话数据
     */
    private List<Joke> mJokes;

    public JokeListAdapter(Context context,List<Joke> jokes) {
        mContext = context;
        mJokes = jokes;
    }

    /**
     * 更新数据的方法
     * @param newJokes
     */
    public void refreshList(List<Joke> newJokes){
        mJokes.clear();
        mJokes.addAll(newJokes);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return mJokes.size();
    }

    @Override
    public Joke getItem(int position) {
        return mJokes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_joke, parent, false);
            viewHolder.tvPostDate = (TextView) convertView.findViewById(R.id.tv_post_date);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Joke joke = getItem(position);
        viewHolder.tvTitle.setText(joke.getTitle());
        viewHolder.tvPostDate.setText(mContext.getString(R.string.post_date,
                joke.getPostDate().substring(0, joke.getPostDate().lastIndexOf(":"))));
        return convertView;
    }

    static class ViewHolder{
        /**
         * item布局中的标题textview
         */
        TextView tvTitle;
        /**
         * item布局中的日期textview
         */
        TextView tvPostDate;

    }
}
