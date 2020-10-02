package org.schabi.newpipe.settings;

import android.os.Bundle;

import androidx.preference.Preference;

import org.schabi.newpipe.MainActivity;
import org.schabi.newpipe.R;

public class MainSettingsFragment extends BasePreferenceFragment {
    public static final boolean DEBUG = MainActivity.DEBUG;

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.main_settings);

        if (!DEBUG) {
            final Preference debug = findPreference(getString(R.string.debug_pref_screen_key));
            getPreferenceScreen().removePreference(debug);
        }
    }
}
