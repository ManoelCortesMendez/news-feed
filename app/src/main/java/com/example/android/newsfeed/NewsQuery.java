package com.example.android.newsfeed;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for fetching news data from The Guardian's API.
 *
 * Since we won't be subclassing it, we can declare it as final.
 */
public final class NewsQuery {

    /**
     * Define tag for logging origin of errors / exceptions.
     */
    private static final String LOG_TAG = NewsQuery.class.getName();

    /**
     * Private, empty constructor, since we won't instantiate NewsQuery objects, because
     * this is a utility class: methods can be called directly using the class name, since they're
     * declared as static.
     */
    private NewsQuery() {
    }

    /**
     * Fetch news data from The Guardian's API.
     *
     * @param queryUrlString URL used to query the API.
     * @return List of news objects.
     */
    public static List<News> fetchNews(String queryUrlString) {

        // Create query URL object from query URL string.
        URL queryUrlObject = makeUrlObject(queryUrlString);

        // Fetch news data in JSON
        String newsJson = null;

        try {
            newsJson = fetchJson(queryUrlObject);
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Problem fetching JSON: ", exception);
        }

        // Extract relevant fields from JSON response and create list of news objects
        List<News> news = extractFeaturesFromJson(newsJson);

        return news;
    }

    /**
     * Make URL object from URL string.
     *
     * @param urlString URL string.
     * @return URL object.
     */
    private static URL makeUrlObject(String urlString) {
        URL urlObject = null;

        try {
            urlObject = new URL(urlString);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Problem making URL object: ", exception);
        }

        return urlObject;
    }

    /**
     * Fetch JSON data.
     *
     * @param queryUrl URL object used to query the API.
     * @return JSON response received from the served.
     * @throws IOException if a problem occurs while closing the input stream.
     */
    private static String fetchJson(URL queryUrl) throws IOException {
        String jsonResponse = "";

        // If query URL object is null, return early.
        if (queryUrl == null) {
            return jsonResponse;
        }

        // Fetch data
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            // Configure and establish HTTP connection
            httpURLConnection = (HttpURLConnection) queryUrl.openConnection();
            httpURLConnection.setReadTimeout(10000 /* milliseconds */);
            httpURLConnection.setConnectTimeout(15000 /* milliseconds */);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            // If connection successful (response code 200)...
            if (httpURLConnection.getResponseCode() == 200) {
                // ... get and read input stream
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Problem establishing HTTP connection. Response code received: " + httpURLConnection.getResponseCode());
            }
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Problem reading input stream: ", exception);
        } finally {
            // Close connection and stream, if these exist
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                // May throw IOException, which is why the enclosing method signature specifies
                // that IOException may be thrown. Therefore, the fetchJson method should always be
                // called within a try/catch.
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    /**
     * Read bytestream efficiently.
     *
     * @param inputStream Input bytestream to read.
     * @return String translation of the input bytestream.
     * @throws IOException if a problem occurs while reading the input bytestream.
     */
    private static String readStream(InputStream inputStream) throws IOException {

        // Instantiate StringBuilder to store response read from input stream
        StringBuilder stringBuilder = new StringBuilder();

        // If we're receiving a stream of bytes...
        if (inputStream != null) {
            // ... instantiate stream reader and pass it the bytestream
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

            // Instantiate buffer reader for efficiency --- to read line by line instead of character by character
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            // Read stream line by line until end of bytestream
            String line = bufferedReader.readLine();

            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }

        // Return lines stored as a single String
        return stringBuilder.toString();
    }

    /**
     * Extract news date from JSON string, such as title, publication date, and author.
     *
     * @param newsJson JSON strings that represents a series of news.
     * @return A list of news objects.
     */
    private static List<News> extractFeaturesFromJson(String newsJson) {
        // If newsJson is empty or null, return early
        if (TextUtils.isEmpty(newsJson)) {
            return null;
        }

        // Create empty list to store news objects
        List<News> news = new ArrayList<>();

        // Try to parse newsJson
        try {
            // Convert JSON String to JSON Object
            JSONObject responseObject = new JSONObject(newsJson);

            // Extract JSON object indexed by 'response'
            JSONObject newsObject = responseObject.getJSONObject("response");

            // Extract JSON array indexed by 'results'. It represents a list of articles.
            JSONArray newsArray = newsObject.getJSONArray("results");

            // For each news in newsArray, create a news object
            for (int i = 0; i < newsArray.length(); i++) {
                // Get news at current index
                JSONObject newsProperties = newsArray.getJSONObject(i);

                // Extract required properties from current news object
                String url = newsProperties.getString("webUrl");
                String title = newsProperties.getString("webTitle");
                String section = newsProperties.getString("sectionName");

                // Extract optional date property from current news object
                String date = newsProperties.optString("webPublicationDate");

                // Extract optional authors property from current news object
                JSONArray authorsArray = newsProperties.getJSONArray("tags");
                ArrayList<String> authors = new ArrayList<>();

                // If the authors array is empty, this for loop will be skipped
                for (int j = 0; j < authorsArray.length(); j++) {
                    JSONObject author = authorsArray.getJSONObject(j);
                    authors.add(author.getString("webTitle"));
                }

                // Create news object with properties extracted
                News currentNews = new News(url, title, section, date, authors);

                // Append news to news list
                news.add(currentNews);
            }

        } catch (JSONException exception) {
            Log.e(LOG_TAG, "Problem extracting news data from JSON: ", exception);
        }

        return news;
    }
}
