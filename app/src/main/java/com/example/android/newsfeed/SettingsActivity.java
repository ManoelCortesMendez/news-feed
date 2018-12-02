package com.example.android.newsfeed;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
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
    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

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

            // Get search keywords preference object using its reference key
            Preference searchKeywordsPreference = findPreference(getString(R.string.settings_search_keywords_key));

            // Bind search keywords preference object
            bindPreferenceDisplayedToState(searchKeywordsPreference);
        }


        /**
         * Method called when a preference has been changed by the user.
         *
         * @param preferenceChanged The preference changed by the user.
         * @param newPreferenceValue The new value of the preference.
         * @return true to update the state of the preference with the new value.
         */
        @Override
        public boolean onPreferenceChange(Preference preferenceChanged, Object newPreferenceValue) {
            // Get new value of the preference as a string
            String newPreferenceValueString = newPreferenceValue.toString();

            // Update value of preference in summary (the UI)
            preferenceChanged.setSummary(newPreferenceValueString);

            return true; // to update value of the preference in sharedPreferences (the state)
        }

        /**
         * Set listener on preference to update its value on change.
         *
         * @param currentPreferenceObject Preference on which to set the change listener.
         */
        private void bindPreferenceDisplayedToState(Preference currentPreferenceObject) {
            // Set listener on current preference object
            currentPreferenceObject.setOnPreferenceChangeListener(this);

            // Get global preference object -- that holds the entire app state (ie its current settings)
            SharedPreferences globalPreferenceObject = PreferenceManager.getDefaultSharedPreferences(currentPreferenceObject.getContext());

            // Get state (value) of the current preference object from the global preference object
            String currentPreferenceValue = globalPreferenceObject.getString(currentPreferenceObject.getKey(), "");

            // Update and display current preference object with its current value in the global preference object
            onPreferenceChange(currentPreferenceObject, currentPreferenceValue);
        }
    }
}
