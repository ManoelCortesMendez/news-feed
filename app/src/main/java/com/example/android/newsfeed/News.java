package com.example.android.newsfeed;

/**
 * Class representing news objects.
 */
public class News {

    // Declare fields
    private String title;
    private String section;
    private String date;

    // Define constructor
    public News(String title, String section, String date) {
        this.title = title;
        this.section = section;
        this.date = date;
    }

    // Define methods
    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getDate() {
        return date;
    }

    /**
     * Helper method that summarizes the object for debugging purposes.
     *
     * @return String description of the object and its fields.
     */
    @Override
    public String toString() {
        return "News {" +  "\n" +
                "Title: " + title + "\n" +
                "Section: " + section + "\n" +
                "Date: " + date + "\n" +
                "}";
    }
}
