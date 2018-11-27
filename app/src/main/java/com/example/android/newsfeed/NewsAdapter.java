package com.example.android.newsfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Adapter class for populating the viewport with news efficiently.
 */
public class NewsAdapter extends ArrayAdapter {

    public NewsAdapter(Context context, ArrayList<News> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Recycle of inflate view
        View newsItemView = convertView;
        if (newsItemView == null) {
            newsItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_item_view, parent, false);
        }

        // Get current news object
        final News currentNews = (News) getItem(position);

        // Set title
        TextView titleTextView = newsItemView.findViewById(R.id.news_title_text_view);
        titleTextView.setText(currentNews.getTitle());

        // Set section
        TextView sectionTextView = newsItemView.findViewById(R.id.news_section_text_view);
        sectionTextView.setText(currentNews.getSection());

        return newsItemView;
    }
}
