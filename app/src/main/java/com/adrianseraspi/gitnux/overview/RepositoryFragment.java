package com.adrianseraspi.gitnux.overview;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.adrianseraspi.gitnux.R;
import com.adrianseraspi.gitnux.adapter.RepositoryAdapter;
import com.adrianseraspi.gitnux.api.model.Repository;
import com.adrianseraspi.gitnux.base.BasePresenterFragment;
import com.adrianseraspi.gitnux.listrepo.ListRepositoryPresenter;
import com.adrianseraspi.gitnux.util.EndlessRecyclerViewOnScrollListener;
import com.adrianseraspi.gitnux.util.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class RepositoryFragment extends BasePresenterFragment<List<Repository>> {

    private View view;

    private RepositoryAdapter mAdapter;

    private ListRepositoryPresenter presenter;

    private List<Repository> repositoryList = new ArrayList<>();

    private EndlessRecyclerViewOnScrollListener scrollListener;

    private String user;

    @BindView(R.id.main_list) RecyclerView listRepoView;
    @BindView(R.id.main_list_progressbar) ProgressBar listProgressBar;
    @BindView(R.id.repository_progressbar) ProgressBar repoProgressbar;

    public RepositoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_repository, container, false);
        ButterKnife.bind(this, view);
        setUpRecyclerView();
        loadPresenter();
        return view;
    }

    private void loadPresenter() {
        user = getArguments().getString(OverviewActivity.EXTRA_USER);
        presenter = new ListRepositoryPresenter(this);
        loadRepo(1);

        Timber.i("User= %s", user);
    }

    private void setUpRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        listRepoView.setLayoutManager(llm);

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
                loadRepo(current_page);
            }
        };

        listRepoView.addOnScrollListener(scrollListener);
    }

    @Override
    public void onSuccess(List<Repository> repositories) {
        repositoryList.addAll(repositories);

        if (mAdapter == null) {
            hideProgressBar(repoProgressbar);
            view.findViewById(R.id.repository_progressbar).setVisibility(View.GONE);
            mAdapter = new RepositoryAdapter(repositoryList);
            listRepoView.setAdapter(mAdapter);
        }
        else {
            new Handler().postDelayed(() -> {
                hideProgressBar(listProgressBar);
                mAdapter.addItems(repositories);
            }, 2000);


        }
    }

    private void loadRepo(int currentPage) {
        if (user != null) {
            presenter.loadUserRepo(user, currentPage);
        }
        else {
            presenter.loadCurrentUserRepo(currentPage);
        }
    }

    @Override
    public void onError(String message) {
        super.onError(message);
        listRepoView.setPadding(0, 0, 0, 0);
        hideProgressBar(listProgressBar);
        hideProgressBar(repoProgressbar);
    }

    @Override
    public void onInternetUnavailable() {
        hideProgressBar(repoProgressbar);
        View rootView = view.findViewById(R.id.repository_rootview);
        SnackbarUtil.showNoInternetConnection(rootView, () -> {
            showProgressBar(repoProgressbar);
            mAdapter = null;
            scrollListener.reset();
            loadPresenter();
        });
    }

    private void showProgressBar(ProgressBar pb) {
        pb.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(ProgressBar pb) {
        pb.setVisibility(View.GONE);
    }

}
