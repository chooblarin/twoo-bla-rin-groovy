package com.chooblarin.gtwooblarin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by chooblarin on 2014/09/10.
 */
public abstract class BindableAdapter<T> extends ArrayAdapter<T> {
    private final LayoutInflater mInflater;

    public BindableAdapter(Context context, List<T> items) {
        super(context, 0, items);

        mInflater = LayoutInflater.from(context);
    }

    @Override
    public abstract T getItem(int position);

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = newView(mInflater, position, parent);

            if (convertView == null) {
                throw new IllegalStateException("newView result must not be null");
            }
        }

        bindView(getItem(position), convertView);
        return convertView;
    }

    public abstract View newView(LayoutInflater inflater, int position, ViewGroup parent);

    public abstract void bindView(T item, View view);
}
