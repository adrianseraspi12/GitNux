package com.codace.gitnux.ui.listpeople;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.codace.gitnux.GitNuxApplication;
import com.codace.gitnux.R;
import com.codace.gitnux.adapter.FollowingAdapter;
import com.codace.gitnux.api.model.Following;
import com.codace.gitnux.ui.base.BasePresenterFragment;
import com.codace.gitnux.util.EndlessRecyclerViewOnScrollListener;
import com.codace.gitnux.util.SnackbarUtil;

import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowerFragment extends BasePresenterFragment<List<Following>> implements
        FollowingAdapter.CallbackAdapter {

    private View view;

    private FollowingAdapter adapter;

    private ListPeoplePresenter presenter;
    private EndlessRecyclerViewOnScrollListener scrollListener;

    @BindView(R.id.main_list) RecyclerView listFollowerView;
    @BindView(R.id.main_list_progressbar) ProgressBar listProgressBar;
    @BindView(R.id.follower_progressbar) ProgressBar followerProgressBar;

    public FollowerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_follower, container, false);
        ButterKnife.bind(this, view);
        setUpRecyclerView();
        loadPresenter();

        return view;
    }

    private void loadPresenter() {
        presenter = new ListPeoplePresenter(this);
        presenter.loadCurrentUserFollower(1);
    }

    private void setUpRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        listFollowerView.setLayoutManager(llm);

        listFollowerView.addItemDecoration(new DividerItemDecoration(getContext(),
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
                presenter.loadCurrentUserFollower(current_page);
            }
        };

        listFollowerView.addOnScrollListener(scrollListener);
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
            hideProgressBar(followerProgressBar);
            adapter = new FollowingAdapter(userList, this);
            listFollowerView.setAdapter(adapter);
        }
        else {

            new Handler().postDelayed(() -> {
                hideProgressBar(listProgressBar);
                adapter.addItems(userList);
            }, 2000);

        }
    }

    @Override
    public void onError(String message) {
        super.onError(message);
        hideProgressBar(followerProgressBar);
        hideProgressBar(listProgressBar);
        listFollowerView.setPadding(0, 0, 0, 0);
    }

    @Override
    public void onInternetUnavailable() {
        hideProgressBar(followerProgressBar);
        View rootView = view.findViewById(R.id.follower_rootview);
        SnackbarUtil.showNoInternetConnection(rootView, () -> {

            adapter = null;
            showProgressBar(followerProgressBar);
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
