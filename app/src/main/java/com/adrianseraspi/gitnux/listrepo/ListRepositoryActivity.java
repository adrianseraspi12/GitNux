package com.adrianseraspi.gitnux.listrepo;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.adrianseraspi.gitnux.R;
import com.adrianseraspi.gitnux.adapter.RepositoryAdapter;
import com.adrianseraspi.gitnux.api.model.Repository;
import com.adrianseraspi.gitnux.base.BasePresenterActivity;
import com.adrianseraspi.gitnux.util.EndlessRecyclerViewOnScrollListener;
import com.adrianseraspi.gitnux.util.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ListRepositoryActivity extends BasePresenterActivity<List<Repository>> {

    private ListRepositoryPresenter presenter;
    private RepositoryAdapter mAdapter;
    private List<Repository> repositories = new ArrayList<>();
    private EndlessRecyclerViewOnScrollListener scrollListener;

    @BindView(R.id.main_list) RecyclerView listRepoView;
    @BindView(R.id.main_list_progressbar) ProgressBar listProgressbar;
    @BindView(R.id.main_horizontal_progressbar) ProgressBar toolbarProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_repository);
        ButterKnife.bind(this);
        setUpToolbar();
        setUpRecyclerView();
        showListOfRepo();
    }

    private void setUpToolbar() {
        setSupportActionBar(findViewById(R.id.main_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setUpRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listRepoView.setLayoutManager(layoutManager);

        listRepoView.addItemDecoration(new DividerItemDecoration(
                ListRepositoryActivity.this,
                layoutManager.getOrientation()));

        scrollListener = new EndlessRecyclerViewOnScrollListener(layoutManager) {

            @Override
            public void onStartLoading() {
                showProgressBar(listProgressbar);
            }

            @Override
            public void onStopLoading() {
                hideProgressBar(listProgressbar);
            }

            @Override
            public void onLoadMore(int current_page) {
                presenter.loadCurrentUserRepo(current_page);
            }

        };

        listRepoView.addOnScrollListener(scrollListener);
    }

    private void showListOfRepo() {
        presenter = new ListRepositoryPresenter(this);
        presenter.loadCurrentUserRepo(1);
    }

    @Override
    public void onSuccess(List<Repository> newRepositories) {
        repositories.addAll(newRepositories);

        if (mAdapter == null) {
            hideProgressBar(toolbarProgressBar);
            mAdapter = new RepositoryAdapter(repositories);
            listRepoView.setAdapter(mAdapter);

        } else {
            new Handler().postDelayed(() -> {
                mAdapter.addItems(newRepositories);
                hideProgressBar(listProgressbar);
            }, 2000);

        }

    }

    @Override
    public void onError(String message) {
        super.onError(message);
        listRepoView.setPadding(0, 0, 0, 0);
        hideProgressBar(listProgressbar);
        hideProgressBar(toolbarProgressBar);
    }

    @Override
    public void onInternetUnavailable() {
        hideProgressBar(toolbarProgressBar);
        View view = findViewById(R.id.list_repository_rootview);
        SnackbarUtil.showNoInternetConnection(view, () -> {

            showProgressBar(toolbarProgressBar);
            mAdapter = null;
            scrollListener.reset();
            showListOfRepo();

        });
    }

    private void showProgressBar(ProgressBar pb) {
        pb.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(ProgressBar pb) {
        pb.setVisibility(View.GONE);
    }

}