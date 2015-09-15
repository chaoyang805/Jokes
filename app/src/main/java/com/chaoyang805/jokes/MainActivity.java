package com.chaoyang805.jokes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.chaoyang805.jokes.model.Joke;
import com.chaoyang805.jokes.fragment.FragmentJokeDetail;
import com.chaoyang805.jokes.fragment.FragmentJokes;
import com.chaoyang805.jokes.utils.Constant;

public class MainActivity extends AppCompatActivity implements FragmentJokes.OnItemClickListener {
    /**
     * 笑话列表的fragment
     */
    private FragmentJokes mJokesFragment;
    /**
     * 笑话详情界面的fragment
     */
    private FragmentJokeDetail mJokeDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化界面，加载默认的fragment
        initFragment();
    }

    /**
     * 初始化fragment，并添加到Activity上
     */
    private void initFragment() {
        mJokesFragment = new FragmentJokes();
        mJokeDetailFragment = new FragmentJokeDetail();
        getFragmentManager().beginTransaction()
                .addToBackStack("jokes_fragment")
                .add(R.id.container, mJokesFragment).commit();
    }

    /**
     * JokesFragment中listitem被点击时的监听
     * @param joke
     */
    @Override
    public void onItemClick(Joke joke) {

        Bundle arguments = new Bundle();
        //将点击到的item对应的joke对象传递给下一个DetailFragment
        arguments.putSerializable(Constant.FRAGMENT_ARG_KEY, joke);
        mJokeDetailFragment.setArguments(arguments);
        getFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.animator.fragment_enter,
                        R.animator.fragment_exit,
                        R.animator.fragment_pop_enter,
                        R.animator.fragment_pop_exit)
                .addToBackStack("fragment_detail")
                .hide(mJokesFragment)
                .add(R.id.container, mJokeDetailFragment).commit();

    }

    /**
     * 按下返回键时，退回到第一个fragment再退出程序
     */
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
