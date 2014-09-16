package com.chooblarin.gtwooblarin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.chooblarin.gtwooblarin.R
import com.nostra13.universalimageloader.core.ImageLoader
import groovy.transform.CompileStatic
import twitter4j.Status

@CompileStatic
class TweetAdapter extends BindableAdapter<Status> {

    Context context
    List<Status> statusList
    ImageLoader imageLoader

    TweetAdapter(Context context, List<Status> statusList) {
        super(context, statusList)
        this.context = context
        this.statusList = statusList
        imageLoader = ImageLoader.getInstance()
    }

    @Override
    Status getItem(int position) { statusList[position] }

    @Override
    View newView(LayoutInflater inflater, int position, ViewGroup parent) {
        inflater.inflate(R.layout.list_item_tweet, parent, false)
    }

    @Override
    void bindView(Status item, View view) {
        ImageView icon = (ImageView) view.findViewById(R.id.twitter_icon)
        icon.setImageDrawable(context.resources.getDrawable(R.drawable.non_image))

        imageLoader.displayImage(item.user.profileImageURL, icon)

        (view.findViewById(R.id.twitter_id) as TextView).setText("@${item.user.screenName}")
        (view.findViewById(R.id.twitter_name) as TextView).setText(item.user.name)
        (view.findViewById(R.id.tweet) as TextView).setText(item.text)
    }
}