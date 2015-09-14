package com.chaoyang805.jokes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.chaoyang805.jokes.bean.Joke;
import com.chaoyang805.jokes.fragment.FragmentJokeDetail;
import com.chaoyang805.jokes.fragment.FragmentJokes;

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
        mJokesFragment = new FragmentJokes();
        mJokeDetailFragment = new FragmentJokeDetail();
        getFragmentManager().beginTransaction().addToBackStack("jokes_fragment").add(R.id.container, mJokesFragment).commit();
    }

    @Override
    public void onItemClick(Joke joke) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(FragmentJokeDetail.ARGUMENTS_KEY, joke);
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

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
