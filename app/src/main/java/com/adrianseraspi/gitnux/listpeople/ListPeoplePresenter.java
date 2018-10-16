package com.adrianseraspi.gitnux.listpeople;

import com.adrianseraspi.gitnux.api.model.Following;
import com.adrianseraspi.gitnux.base.BasePresenter;
import com.adrianseraspi.gitnux.base.BaseView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListPeoplePresenter extends BasePresenter {

    private BaseView<List<Following>> listPeopleView;

    ListPeoplePresenter(BaseView<List<Following>> baseView) {
        super(baseView);
        this.listPeopleView = baseView;
    }

    void loadCurrentUserFollowing(int currentPage) {
        String accessToken = getSharedPreferences().getString("access_token", "");
        Call<List<Following>> callFollowing = getGitHubClient().getCurrentUserFollowing(
                accessToken,
                currentPage,
                30);

        callFollowingList(callFollowing);
    }

    void loadCurrentUserFollower(int currentPage) {
        String accessToken = getSharedPreferences().getString("access_token", "");
        Call<List<Following>> callFollowing = getGitHubClient().getCurrentUserFollowers(
                accessToken,
                currentPage,
                30);

        callFollowingList(callFollowing);
    }

    private void callFollowingList(Call<List<Following>> callFollowing) {
        callFollowing.enqueue(new Callback<List<Following>>() {

            @Override
            public void onResponse(Call<List<Following>> call, Response<List<Following>> response) {

                if (response.body().size() > 0) {
                    listPeopleView.onSuccess(response.body());
                }
                else {
                    listPeopleView.onError("All users loaded");
                }

            }

            @Override
            public void onFailure(Call<List<Following>> call, Throwable t) {

            }

        });
    }

}
