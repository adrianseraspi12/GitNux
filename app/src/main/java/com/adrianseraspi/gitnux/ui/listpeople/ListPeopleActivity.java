package com.adrianseraspi.gitnux.ui.listpeople;

import android.os.Bundle;

import com.adrianseraspi.gitnux.R;
import com.adrianseraspi.gitnux.adapter.PagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class ListPeopleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_people);
        setUpToolbar();
        setUpTabs();
    }

    private void setUpToolbar() {
        setSupportActionBar(findViewById(R.id.list_people_toolbar));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpTabs() {
        TabLayout peopleTabsLayout = findViewById(R.id.people_tabs);
        ViewPager viewPager = findViewById(R.id.people_viewpager);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new FollowingFragment());
        fragments.add(new FollowerFragment());

        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), fragments));
        peopleTabsLayout.setupWithViewPager(viewPager);

        peopleTabsLayout.getTabAt(0).setText(R.string.followers);
        peopleTabsLayout.getTabAt(1).setText(R.string.following);
    }

}
