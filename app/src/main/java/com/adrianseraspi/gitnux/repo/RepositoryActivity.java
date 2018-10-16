package com.adrianseraspi.gitnux.repo;

import android.os.Bundle;
import android.view.MenuItem;

import com.adrianseraspi.gitnux.R;
import com.adrianseraspi.gitnux.adapter.PagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class RepositoryActivity extends AppCompatActivity {

    public static final String EXTRA_REPO_OWNER = "EXTRA_REPO_OWNER";
    public static final String EXTRA_REPO_NAME = "EXTRA_REPO_NAME";
    public static final String EXTRA_HEADERS = "EXTRA_HEADERS";
    public static final String EXTRA_REPO_URL = "EXTRA_URL";

    private String url;
    private String owner;
    private String repoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);
        setUpActionBar();
        setUpTabs();
    }

    private void setUpActionBar() {
        url = getIntent().getStringExtra(EXTRA_REPO_URL);
        owner = getIntent().getStringExtra(EXTRA_REPO_OWNER);
        repoName = getIntent().getStringExtra(EXTRA_REPO_NAME);

        setSupportActionBar(findViewById(R.id.repository_toolbar));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(owner + "/" + repoName);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setUpTabs() {
        TabLayout tabLayout = findViewById(R.id.repository_tabs);
        ViewPager viewPager = findViewById(R.id.repository_viewpager);

        ArrayList<String> titleOfDirs = new ArrayList<>();
        ArrayList<String> urls = new ArrayList<>();

        titleOfDirs.add("Home");
        urls.add(url);

        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_REPO_OWNER, owner);
        bundle.putString(EXTRA_REPO_NAME, repoName);
        bundle.putStringArrayList(EXTRA_REPO_URL, urls);
        bundle.putStringArrayList(EXTRA_HEADERS, titleOfDirs);

        ReadMeFragment readMeFragment = new ReadMeFragment();
        readMeFragment.setArguments(bundle);

        ListContentFragment listContentFragment = new ListContentFragment();
        listContentFragment.setArguments(bundle);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(readMeFragment);
        fragments.add(listContentFragment);

        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), fragments));
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText(getString(R.string.read_me));
        tabLayout.getTabAt(1).setText(getString(R.string.files));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
