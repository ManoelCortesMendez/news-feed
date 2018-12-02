package com.example.android.newsfeed;

import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Activity that manages the app settings.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    /**
     * Special fragment dedicated to managing user preferences -- e.g. store and edit settings.
     */
    public static class NewsPreferenceFragment extends PreferenceFragment {

        /**
         * Inflate contents of settings activity with preference widgets.
         *
         * @param savedInstanceState
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Add preferences defined in XML to settings activity
            addPreferencesFromResource(R.xml.settings_main);
        }
    }
}
