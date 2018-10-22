package com.codace.gitnux.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codace.gitnux.R;
import com.codace.gitnux.ui.about.GitNuxPreferenceActivity;
import com.codace.gitnux.adapter.BaseAdapter;
import com.codace.gitnux.api.model.Events;
import com.codace.gitnux.api.model.User;
import com.codace.gitnux.ui.base.BasePresenterActivity;
import com.codace.gitnux.ui.listpeople.ListPeopleActivity;
import com.codace.gitnux.ui.listrepo.ListRepositoryActivity;
import com.codace.gitnux.ui.liststarred.ListStarredActivity;
import com.codace.gitnux.ui.login.LoginActivity;
import com.codace.gitnux.ui.overview.OverviewActivity;
import com.codace.gitnux.ui.repo.RepositoryActivity;
import com.codace.gitnux.util.EndlessRecyclerViewOnScrollListener;
import com.codace.gitnux.util.SnackbarUtil;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import net.danlew.android.joda.DateUtils;

import org.joda.time.DateTime;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static net.danlew.android.joda.DateUtils.FORMAT_ABBREV_RELATIVE;

public class HomeActivity extends BasePresenterActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.home_toolbar) Toolbar toolbar;
    @BindView(R.id.main_list) RecyclerView listEventsView;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.main_list_progressbar) ProgressBar listProgressBar;
    @BindView(R.id.main_horizontal_progressbar) ProgressBar toolbarProgressbar;

    private EventsAdapter mAdapter;
    private HomePresenter homePresenter;
    private EndlessRecyclerViewOnScrollListener rvOnScroll;

    private CircleImageView userPicView;
    private TextView userNameView;
    private TextView userLoginView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        setUpNavDrawer();
        setUpNavView();
        setUpRecyclerView();
        showData();
    }

    private void setUpNavDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setUpNavView() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        userPicView = view.findViewById(R.id.nav_profile_image);
        userNameView = view.findViewById(R.id.nav_profile_name);
        userLoginView = view.findViewById(R.id.nav_profile_login);
    }

    private void setUpRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listEventsView.setLayoutManager(layoutManager);

        listEventsView.addItemDecoration(new DividerItemDecoration(this,
                layoutManager.getOrientation()));

        rvOnScroll = new EndlessRecyclerViewOnScrollListener(layoutManager) {

            @Override
            public void onStartLoading() {
                showProgressBar(listProgressBar);
            }

            @Override
            public void onStopLoading() {
                hideProgressBar(listProgressBar);
            }

            @Override
            public void onLoadMore(int current_page) {
                homePresenter.load(current_page);
            }

        };

        listEventsView.addOnScrollListener(rvOnScroll);
    }

    private void showData() {
        homePresenter = new HomePresenter(this);
        homePresenter.load(1);
        homePresenter.loadCurrentUser();
    }
    
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {

            case R.id.menu_overview:
                startActivity(OverviewActivity.class);
                break;

            case R.id.menu_people:
                startActivity(ListPeopleActivity.class);
                break;

            case R.id.menu_repositories:
                startActivity(ListRepositoryActivity.class);
                break;

            case R.id.menu_stars:
                startActivity(ListStarredActivity.class);
                break;

            case R.id.menu_about:
                startActivity(GitNuxPreferenceActivity.class);
                break;

            case R.id.menu_home:
                recreate();
                break;

            case R.id.menu_logout:
                showConfirmationDialog();
                break;

            default:
                throw new IllegalArgumentException("Invalid Id= " + id);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startActivity(Class<?> activityClass) {
        startActivity(new Intent(this, activityClass));
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.logout_user));
        builder.setMessage(getString(R.string.logout_user_message));
        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton(getString(R.string.logout), (dialog, which) -> {
            homePresenter.removeAccessToken();

            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void showProgressBar(ProgressBar pb) {
        pb.setVisibility(View.VISIBLE);
    }

    void hideProgressBar(ProgressBar pb) {
        pb.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onSuccess(Object data) {
        if (data instanceof User) {
            User user = (User) data;
            userNameView.setText(user.getName());
            userLoginView.setText(user.getLogin());

            Picasso.get().load(user.getAvatarUrl())
                    .placeholder(R.drawable.ic_user)
                    .centerCrop()
                    .fit()
                    .into(userPicView);
        }
        else if (data instanceof List){
            List<Events> events = (List<Events>) data;

            if (mAdapter == null) {
                hideProgressBar(toolbarProgressbar);
                mAdapter = new EventsAdapter(events);
                listEventsView.setAdapter(mAdapter);
            }
            else {
                new Handler().postDelayed(() -> {
                    mAdapter.addItems(events);
                    hideProgressBar(listProgressBar);
                }, 2000);

            }

        }
    }

    @Override
    public void onError(String message) {
        super.onError(message);
        hideProgressBar(listProgressBar);
        hideProgressBar(toolbarProgressbar);
        listEventsView.setPadding(0, 0, 0, 0);
    }

    @Override
    public void onInternetUnavailable() {
        homePresenter.loadOfflineUserInfo();
        hideProgressBar(toolbarProgressbar);
        View rootView = findViewById(R.id.home_root_view);
        SnackbarUtil.showNoInternetConnection(rootView, () -> {

            mAdapter = null;
            rvOnScroll.reset();
            showProgressBar(toolbarProgressbar);
            showData();

        });
    }

    class EventsAdapter extends BaseAdapter<Events, EventsAdapter.ViewHolder> {

        EventsAdapter(List<Events> list) {
            super(list);
        }

        @Override
        public ViewHolder onCreateVH(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_item_events,
                    parent,
                    false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindVH(ViewHolder holder, int position, Events model) {
            DateTime createdDate = DateTime.parse(model.getCreatedAt());

            holder.typeView.setText(model.getType());
            holder.titleView.setText(model.getRepo().getName());
            holder.setClickListener(model);

            holder.dateView.setText(
                    DateUtils.getRelativeTimeSpanString(holder.itemView.getContext(),
                            createdDate, FORMAT_ABBREV_RELATIVE)
            );

            setScaleAnimation(holder.itemView, position);
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.item_events_type) TextView typeView;
            @BindView(R.id.item_events_title) TextView titleView;
            @BindView(R.id.item_events_created_at) TextView dateView;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            private void setClickListener(Events model) {
                itemView.setOnClickListener(v -> {

                    String[] repoName = model.getRepo().getName().split("/");
                    String owner = repoName[0];
                    String repo = repoName[1];

                    Intent intent = new Intent(HomeActivity.this,
                            RepositoryActivity.class);
                    intent.putExtra(RepositoryActivity.EXTRA_REPO_OWNER, owner);
                    intent.putExtra(RepositoryActivity.EXTRA_REPO_NAME, repo);
                    intent.putExtra(RepositoryActivity.EXTRA_REPO_URL,
                            model.getRepo().getContentUrl());
                    startActivity(intent);
                });
            }

        }

    }

}
