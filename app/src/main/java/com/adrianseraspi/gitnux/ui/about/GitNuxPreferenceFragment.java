package com.adrianseraspi.gitnux.ui.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.adrianseraspi.gitnux.BuildConfig;
import com.adrianseraspi.gitnux.R;

import androidx.preference.Preference;

public class GitNuxPreferenceFragment extends BasePreferenceFragmentCompat implements
        Preference.OnPreferenceClickListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        setListener();
    }

    private void setListener() {
        String appVersion = BuildConfig.VERSION_NAME;

        Preference versionPref = findPreference("app_version");
        Preference moreFromDevPref = findPreference("more_from_dev");
        Preference contributePref = findPreference("contribute");
        Preference privatePolicyPref = findPreference("privacy_policy");

        versionPref.setSummary(appVersion);

        moreFromDevPref.setOnPreferenceClickListener(this);
        contributePref.setOnPreferenceClickListener(this);
        privatePolicyPref.setOnPreferenceClickListener(this);

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        switch (preference.getKey()) {

            case "more_from_dev":
                startActivity("https://play.google.com/store/apps/developer?id=Adrian+Seraspi");
                break;

            case "contribute":
                startActivity("https://github.com/adrianseraspi12/GitNux");
                break;

            case "privacy_policy":
                startActivity("https://sites.google.com/view/gitnux/home");
                break;

        }

        return true;
    }

    private void startActivity(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
}
