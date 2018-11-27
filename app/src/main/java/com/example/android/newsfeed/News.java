package com.example.android.newsfeed;

/**
 * Class representing news objects.
 */
public class News {

    // Declare fields
    private String title;
    private String section;

    // Define constructor
    public News(String title, String section) {
        this.title = title;
        this.section = section;
    }

    // Define methods
    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
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
                "}";
    }
}
