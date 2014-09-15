package com.chooblarin.gtwooblarin.ui.fragment

import android.app.Fragment
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.chooblarin.gtwooblarin.R
import com.chooblarin.gtwooblarin.adapter.TweetAdapter
import com.chooblarin.gtwooblarin.helper.TwitterHelper
import com.chooblarin.gtwooblarin.task.Fluent
import groovy.transform.CompileStatic
import twitter4j.Status
import twitter4j.Twitter
import twitter4j.TwitterException

@CompileStatic
class TweetListFragment extends Fragment {

    Twitter twitter;
    SwipeRefreshLayout refreshLayout
    ListView listView
    TweetAdapter tweetAdapter;
    Button tweetButton

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tweet_list, container, false);

        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.tweet_swipe_refresh);
        listView = (ListView) rootView.findViewById(R.id.tweet_list_view);
        tweetButton = (Button) rootView.findViewById(R.id.tweet_button);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshLayout.setOnRefreshListener {
            reloadTimeLine()
        }

        tweetButton.setOnClickListener {
            TweetDialogFragment.newInstance {String text -> this.tweet(text) }
                    .show(this.activity.fragmentManager, "tweet_dialog")
        }

        tweetAdapter = new TweetAdapter(activity, [])
        listView.setAdapter(tweetAdapter)

        reloadTimeLine();
    }

    void reloadTimeLine() {
        Fluent.async {
            if (this.twitter == null) {
                this.twitter = TwitterHelper.getTwitter(this.activity.applicationContext)
            }

            return this.twitter.getHomeTimeline()

        } then {
            this.refreshLayout.setRefreshing(false);

            this.tweetAdapter.clear()
            this.tweetAdapter.addAll(it as List<Status>)
        }
    }

    void tweet(String tweetText) {
        Fluent.async {
            try {
                this.twitter.updateStatus(tweetText)
                return true

            } catch (TwitterException e) {
                e.printStackTrace()
                return false
            }

        } then {
            boolean success = it as Boolean

            String message = success ? 'ツイートした' :  'ツイート失敗'
            Toast.makeText(this.activity.applicationContext, message, Toast.LENGTH_SHORT).show()

            if (success) {
                reloadTimeLine()
            }
        }
    }
}