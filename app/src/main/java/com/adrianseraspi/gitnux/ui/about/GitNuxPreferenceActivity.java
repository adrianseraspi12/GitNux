package com.adrianseraspi.gitnux.ui.about;

import android.os.Bundle;

import com.adrianseraspi.gitnux.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GitNuxPreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new GitNuxPreferenceFragment())
                .commit();

        setTitle(R.string.about);
    }
}
