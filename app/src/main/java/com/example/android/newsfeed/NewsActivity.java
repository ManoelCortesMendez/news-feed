package com.example.android.newsfeed;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Main activity showing a news feed.
 * <p>
 * The activity implements the loader callback interface to be able to fetch news asynchronously.
 */
public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    // Define member variables
    /**
     * ID used to uniquely identify and seed the news loader.
     */
    private final int NEWS_LOADER_ID = 0;

    /**
     * URL used to query The Guardian's API -- We query articles about Google
     */
    private final String NEWS_QUERY_URL = "https://content.guardianapis.com/search?q=google&show-tags=contributor&show-fields=thumbnail&api-key=54a72938-b6be-4f0b-b258-15a961c183cf";

    /**
     * TextView to display when no news is found.
     */
    private TextView noNewsTextView;

    /**
     * Adapter to bind news list to list of news objects and display them efficiently (only when on screen)
     */
    private NewsAdapter newsAdapter;

    /**
     * Inflate activity contents when activity is created.
     *
     * @param savedInstanceState Previous activity state, if any has been saved -- to pick up where you left off.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set view layout
        setContentView(R.layout.news_activity);

        // Get news list view
        ListView newsListView = findViewById(R.id.news_list_view);

        // Get empty view -- that is, the no news text view
        noNewsTextView = findViewById(R.id.no_news_text_view);

        // Use dedicated method to set no news text view as the view to default to when there's no data
        // The empty view starts without text, so it won't blink while news are being fetched
        // When news data is successfully fetched, the no text view is automatically hidden
        newsListView.setEmptyView(noNewsTextView);

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

        // Get ConnectivityManager to check network state -- that is, if we have internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get current network info
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // If we're connected to a network...
        if (networkInfo != null && networkInfo.isConnected()) {
            // ... fetch data
            // Get loader manager to set up loader for scheduling async tasks on secondary thread
            final LoaderManager loaderManager = getLoaderManager();

            // Initialize loader to fetch news asynchronously
            // We pass it this activity as loaderCallBacks parameter, which is valid since this activity
            // implements that interface
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // ... display error
            // Hide loading indicator
            ProgressBar progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);

            // Display connection error message
            noNewsTextView.setText("No internet connection.");
        }
    }

    // Implement options menu (settings)

    /**
     * Inflate contents of the activity' standard options menu -- that is, the settings menu 
     * accessible via the action bar.
     *
     * @param menu Options menu in which to place your items.
     * @return true for the menu to be displayed, false otherwise.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate options menu using menu XML resource
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Hook called whenever a menu item is selected. Defines which actions to take depending on
     * which item was selected. Here, there's only one item to handle: Settings -- that is, opening
     * the settings activity.
     *
     * @param menuItem Menu item that was selected.
     * @return true to handle interaction with menu here, or false to let the parent class do it.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        // Get id of menu item clicked
        int itemId = menuItem.getItemId();

        // If menu item clicked is 'Settings'
        if (itemId == R.id.open_settings_menu_item) {
            // Open Settings activity via explicit intent
            Intent openSettings = new Intent(this, SettingsActivity.class);
            startActivity(openSettings);
            return true; // To handle menu interaction here via our custom implementation
        }

        return super.onOptionsItemSelected(menuItem); // To handle menu interaction via default channel in parent
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
     * <p>
     * Clears the adapter's list and adds the news passed, if there are any.
     *
     * @param newsLoader Previously created news loader object.
     * @param news       List of news objects.
     */
    @Override
    public void onLoadFinished(Loader<List<News>> newsLoader, List<News> news) {
        // Hide progress, since data fetching has now resolved (either succeeded or failed)
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        // Remove news objects from adapter -- that is, empty its list
        newsAdapter.clear();

        // If news list passed as argument contains any news, add them to the adapter's list.
        // This will trigger the list view to update.
        if (news != null && !news.isEmpty()) {
            newsAdapter.addAll(news);
        }

        // Now that fetching has resolved, add text to no news text view.
        // If fetching failed (no news retrieved), the text will be visible.
        // If fetching fulfilled (news retrieved), the text view will be automatically hidden
        // since it was set using a special method in onCreate(). So text won't be visible.
        noNewsTextView.setText("No news found.");
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
