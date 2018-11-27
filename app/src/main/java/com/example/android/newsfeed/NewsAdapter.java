package com.example.android.newsfeed;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Adapter class for populating the viewport with news efficiently.
 */
public class NewsAdapter extends ArrayAdapter {

    /**
     * Define tag for logging origin of errors / exceptions.
     */
    private static final String LOG_TAG = NewsQuery.class.getName();

    public NewsAdapter(Context context, ArrayList<News> news) {
        super(context, 0, news);
    }

    /**
     * Build view using object at index 'position' and return it.
     *
     * @param position Index of the current news object to display.
     * @param convertView Potential view from scrap pile -- for recycling views.
     * @param parent Parent view group of the view we're inflating or recycling.
     * @return Fully-built view to display.
     */
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

        // Set date
        String dateToDisplay = formatDate(currentNews.getDate());
        TextView dateTextView = newsItemView.findViewById(R.id.news_date_text_view);
        dateTextView.setText(dateToDisplay);

        // Set authors
        TextView authorsTextView = newsItemView.findViewById(R.id.news_authors_text_view);
        authorsTextView.setText(String.join(", ", currentNews.getAuthors()));

        return newsItemView;
    }

    /**
     * Return formatted date string.
     *
     * @param dateString Input string date in format "yyyy-MM-dd".
     * @return Output string date in format "MMM dd, yyyy".
     */
    private String formatDate(String dateString) {
        // If date is empty, return early
        if (dateString == "") {
            return "Date N/A";
        }

        // Instantiate parser for parsing input string date
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");

        // Try to parse input string date
        Date date = null;
        try {
            date = dateParser.parse(dateString);
        } catch (ParseException exception) {
            Log.e(LOG_TAG, "Problem parsing the date: ", exception);
        }

        // Instantiate formatter for formatting output string date
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy");

        // Return formatted date
        return dateFormatter.format(date);
    }
}
