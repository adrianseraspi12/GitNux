package com.codace.gitnux.ui.liststarred;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.codace.gitnux.R;
import com.codace.gitnux.adapter.RepositoryAdapter;
import com.codace.gitnux.api.model.Repository;
import com.codace.gitnux.ui.base.BasePresenterActivity;
import com.codace.gitnux.util.EndlessRecyclerViewOnScrollListener;
import com.codace.gitnux.util.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ListStarredActivity extends BasePresenterActivity<List<Repository>> {

    private ListStarredPresenter presenter;

    private RepositoryAdapter mAdapter;

    private final List<Repository> starredRepoList = new ArrayList<>();

    private EndlessRecyclerViewOnScrollListener scrollListener;

    @BindView(R.id.main_list) RecyclerView listStarredRepoView;
    @BindView(R.id.main_list_progressbar) ProgressBar listProgressBar;
    @BindView(R.id.main_horizontal_progressbar) ProgressBar toolbarProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_starred);
        ButterKnife.bind(this);
        setUpToolbar();
        setUpRecyclerView();
        showList();
    }

    private void setUpToolbar() {
        setSupportActionBar(findViewById(R.id.main_toolbar));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        listStarredRepoView.setLayoutManager(llm);

        listStarredRepoView.addItemDecoration(new DividerItemDecoration(
                ListStarredActivity.this,
                llm.getOrientation()));

        scrollListener = new EndlessRecyclerViewOnScrollListener(llm) {

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
                presenter.load(current_page);
            }

        };

        listStarredRepoView.addOnScrollListener(scrollListener);
    }

    private void showList() {
        presenter = new ListStarredPresenter(this);
        presenter.load(1);
    }

    @Override
    public void onSuccess(List<Repository> listStarred) {
        starredRepoList.addAll(listStarred);

        if (mAdapter == null) {
            hideProgressBar(toolbarProgressBar);
            mAdapter = new RepositoryAdapter(starredRepoList);
            listStarredRepoView.setAdapter(mAdapter);
        }
        else {
            new Handler().postDelayed(() -> {
                hideProgressBar(listProgressBar);
                mAdapter.addItems(listStarred);
            }, 2000);

        }
    }

    @Override
    public void onError(String message) {
        super.onError(message);
        hideProgressBar(toolbarProgressBar);
        hideProgressBar(listProgressBar);
        listStarredRepoView.setPadding(0, 0, 0, 0);
    }

    @Override
    public void onInternetUnavailable() {
        hideProgressBar(toolbarProgressBar);
        View view = findViewById(R.id.list_starred_rootview);
        SnackbarUtil.showNoInternetConnection(view, () -> {

            showProgressBar(toolbarProgressBar);
            mAdapter = null;
            scrollListener.reset();
            showList();

        });
    }

    private void showProgressBar(ProgressBar pb) {
        pb.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(ProgressBar pb) {
        pb.setVisibility(View.GONE);
    }

}
