package com.example.android.newsfeed;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Loader class for loading news data asynchronously on a background thread.
 */
public class NewsLoader extends AsyncTaskLoader<List<News>> {

    /**
     * URL used to query the news API
     **/
    private String queryUrl;

    /**
     * Instantiate loader for fetching news using passed URL to query API.
     * <p>
     * Triggers onStartLoading() once finished.
     *
     * @param context  Activity associated with loader.
     * @param queryUrl URL used for querying API.
     */
    public NewsLoader(Context context, String queryUrl) {
        super(context);
        this.queryUrl = queryUrl;
    }

    /**
     * Called once constructor call has completed.
     */
    @Override
    protected void onStartLoading() {
        // Trigger loadInBackground() immediately -- that is, initiate fetch
        forceLoad();
    }

    /**
     * Fetch news.
     * <p>
     * This method is scheduled on the background thread to be handled asynchronously
     */
    @Override
    public List<News> loadInBackground() {

        // If query is empty, return early
        if (queryUrl == "") {
            return null;
        }

        // Fetch news data
        List<News> news = NewsQuery.fetchNews(queryUrl);

        return news;
    }
}
