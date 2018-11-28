package com.example.android.newsfeed;

import java.util.ArrayList;

/**
 * Class representing news objects.
 */
public class News {

    // Declare fields
    private String url;
    private String title;
    private String section;
    private String date;
    private ArrayList<String> authors;

    // Define constructor
    public News(String url, String title, String section, String date, ArrayList<String> authors) {
        this.url = url;
        this.title = title;
        this.section = section;
        this.date = date;
        this.authors = authors;
    }

    // Define methods
    public String getUrl() { return url; }

    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    /**
     * Helper method that summarizes the object for debugging purposes.
     *
     * @return String description of the object and its fields.
     */
    @Override
    public String toString() {
        return "News {" +  "\n" +
                "URL: " + url + "\n" +
                "Title: " + title + "\n" +
                "Section: " + section + "\n" +
                "Date: " + date + "\n" +
                "Authors: " + authors.toString() + "\n" +
                "}";
    }
}
