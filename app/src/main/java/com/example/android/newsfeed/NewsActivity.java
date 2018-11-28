package com.example.android.newsfeed;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Main activity showing a news feed.
 *
 * The activity implements the loader callback interface to be able to fetch news asynchronously.
 */
public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    // Define member variables
    /** ID used to uniquely identify and seed the news loader. */
    private final int NEWS_LOADER_ID = 0;

    /** URL used to query The Guardian's API -- We query articles about Google */
    private final String NEWS_QUERY_URL = "https://content.guardianapis.com/search?q=google&show-tags=contributor&api-key=test";
    
    private NewsAdapter newsAdapter;

    // Define constructor
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set view layout
        setContentView(R.layout.news_activity);

        // Get news list view
        ListView newsListView = findViewById(R.id.news_list_view);

        // Instantiate adapter that takes as input an empty list of news objects
        newsAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Bind news adapter and list view to populate view with news
        newsListView.setAdapter(newsAdapter);

        // Bind item click listener to ListView, which sends intent to browser to navigate to article
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Bind click listener to ListView items.
             *
             * @param parentListView ListView that contains all the news items.
             * @param clickedItemView ItemView that represents the news item clicked.
             * @param position Position of the clicked ItemView in the adapter's list.
             * @param rowId Row of the clicked ItemView in the parent ListView.
             */
            @Override
            public void onItemClick(AdapterView<?> parentListView, View clickedItemView, int position, long rowId) {
                // Get clicked article
                News clickedNews = (News) newsAdapter.getItem(position);

                // Convert String URL into URI object
                Uri clickedNewsUri = Uri.parse(clickedNews.getUrl());

                // Create intent to open URI
                Intent clickedNewsIntent = new Intent(Intent.ACTION_VIEW, clickedNewsUri);

                // Send intent to open URI in browser
                startActivity(clickedNewsIntent);
            }
        });

        // Get loader manager to set up loader for scheduling async tasks on secondary thread
        LoaderManager loaderManager = getLoaderManager();

        // Initialize loader to fetch news asynchronously
        // We pass it this activity as loaderCallBacks parameter, which is valid since this activity
        // implements that interface
        loaderManager.initLoader(NEWS_LOADER_ID, null, this);
    }

    // Implement loader callback interface
    /**
     * Instantiate and return a new loader for the given ID and query.
     *
     * @param id Unique number to identify and seed loader.
     * @param queryString URL string for querying news API.
     * @return New loader for asynchronously querying news API.
     */
    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle queryString) {
        return new NewsLoader(this, NEWS_QUERY_URL);
    }

    /**
     * Method called when a previously created loader has finished fetching data.
     *
     * Clears the adapter's list and adds the news passed, if there are any.
     *
     * @param newsLoader Previously created news loader object.
     * @param news List of news objects.
     */
    @Override
    public void onLoadFinished(Loader<List<News>> newsLoader, List<News> news) {
        // Remove news objects from adapter -- that is, empty its list
        newsAdapter.clear();

        // If news list passed as argument contains any news, add them to the adapter's list.
        // This will trigger the list view to update.
        if (news != null && !news.isEmpty()) {
            newsAdapter.addAll(news);
        }
    }

    /**
     * Called when a previously created loader is being reset, thus making its data unavailable.
     *
     * @param newsLoader Previously created news loader object.
     */
    @Override
    public void onLoaderReset(Loader<List<News>> newsLoader) {
        // Remove news objects from adapter -- that is, empty its list
        newsAdapter.clear();
    }
}
