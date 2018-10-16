package com.adrianseraspi.gitnux.listpeople;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.adrianseraspi.gitnux.GitNuxApplication;
import com.adrianseraspi.gitnux.R;
import com.adrianseraspi.gitnux.adapter.FollowingAdapter;
import com.adrianseraspi.gitnux.api.model.Following;
import com.adrianseraspi.gitnux.base.BasePresenterFragment;
import com.adrianseraspi.gitnux.util.EndlessRecyclerViewOnScrollListener;
import com.adrianseraspi.gitnux.util.SnackbarUtil;

import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowingFragment extends BasePresenterFragment<List<Following>> implements
        FollowingAdapter.CallbackAdapter {

    private View view;

    private FollowingAdapter adapter;

    private ListPeoplePresenter presenter;

    private EndlessRecyclerViewOnScrollListener scrollListener;

    @BindView(R.id.main_list) RecyclerView listFollowingView;
    @BindView(R.id.main_list_progressbar) ProgressBar listProgress;
    @BindView(R.id.following_progressbar) ProgressBar followingProgressBar;

    public FollowingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_following, container, false);
        ButterKnife.bind(this, view);
        setUpRecyclerView();
        loadPresenter();
        return view;
    }

    private void loadPresenter() {
        presenter = new ListPeoplePresenter(this);
        presenter.loadCurrentUserFollowing(1);
    }

    private void setUpRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        listFollowingView.setLayoutManager(llm);

        listFollowingView.addItemDecoration(new DividerItemDecoration(getContext(),
                llm.getOrientation()));

        scrollListener = new EndlessRecyclerViewOnScrollListener(llm) {

            @Override
            public void onStartLoading() {
                showProgressBar(listProgress);
            }

            @Override
            public void onStopLoading() {
                hideProgressBar(listProgress);
            }

            @Override
            public void onLoadMore(int current_page) {
                presenter.loadCurrentUserFollowing(current_page);
            }

        };

        listFollowingView.addOnScrollListener(scrollListener);
    }

    @Override
    public void initClient(FollowingAdapter followingAdapter) {
        ((GitNuxApplication) getActivity().getApplication())
                .getAppComponent(getActivity(), this)
                .inject(followingAdapter);
    }

    @Override
    public void onSuccess(List<Following> userList) {
        if (adapter == null) {
            hideProgressBar(followingProgressBar);
            adapter = new FollowingAdapter(userList, this);
            listFollowingView.setAdapter(adapter);
        }
        else {
            new Handler().postDelayed(() -> {
                hideProgressBar(listProgress);
                adapter.addItems(userList);
            }, 2000);
        }
    }

    @Override
    public void onError(String message) {
        super.onError(message);
        hideProgressBar(listProgress);
        hideProgressBar(followingProgressBar);
        listFollowingView.setPadding(0, 0, 0, 0);
    }

    @Override
    public void onInternetUnavailable() {
        hideProgressBar(followingProgressBar);
        View rootView = view.findViewById(R.id.following_rootview);
        SnackbarUtil.showNoInternetConnection(rootView, () -> {

            adapter = null;
            showProgressBar(followingProgressBar);
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
