package com.codace.gitnux.ui.overview;

import android.os.Bundle;

import com.codace.gitnux.R;
import com.codace.gitnux.adapter.PagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class OverviewActivity extends AppCompatActivity {

    public static final String EXTRA_USER = "EXTRA_USER";

    private TabLayout overviewTabsView;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        setUpToolbar();
        setUpTabs();
    }

    private void setUpToolbar() {
        setSupportActionBar(findViewById(R.id.overview_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setUpTabs() {
        overviewTabsView = findViewById(R.id.overview_tabs);
        viewPager = findViewById(R.id.overview_viewpager);

        String user = getIntent().getStringExtra(EXTRA_USER);

        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_USER, user);

        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(bundle);

        RepositoryFragment repositoryFragment = new RepositoryFragment();
        repositoryFragment.setArguments(bundle);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(profileFragment);
        fragments.add(repositoryFragment);

        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), fragments));
        overviewTabsView.setupWithViewPager(viewPager);

        overviewTabsView.getTabAt(0).setText(R.string.profile);
        overviewTabsView.getTabAt(1).setText(R.string.repository);
    }

}
