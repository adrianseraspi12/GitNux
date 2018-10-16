package com.adrianseraspi.gitnux.listrepo;

import com.adrianseraspi.gitnux.api.model.Repository;
import com.adrianseraspi.gitnux.base.BasePresenter;
import com.adrianseraspi.gitnux.base.BaseView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListRepositoryPresenter extends BasePresenter {

    private final BaseView baseView;

    public ListRepositoryPresenter(BaseView baseView) {
        super(baseView);
        this.baseView = baseView;
    }


    public void loadCurrentUserRepo(int currentPage) {
        String accessToken = getSharedPreferences().getString("access_token", "");
        Call<List<Repository>> reposCall = getGitHubClient().reposForCurrentUser(
                accessToken,
                currentPage,
                30);

        callRepoList(reposCall);
    }

    public void loadUserRepo(String user, int currentPage) {
        String accessToken = getSharedPreferences().getString("access_token", "");

        Call<List<Repository>> reposCall = getGitHubClient().reposForUser(
                user,
                currentPage,
                30,
                accessToken);

        callRepoList(reposCall);
    }

    private void callRepoList(Call<List<Repository>> reposCall) {
        reposCall.enqueue(new Callback<List<Repository>>() {

            @Override
            public void onResponse(Call<List<Repository>> call,
                                   Response<List<Repository>> response) {

                if (response.body().size() > 0) {
                    baseView.onSuccess(response.body());
                } else {
                    baseView.onError("All repositories are loaded");
                }

            }

            @Override
            public void onFailure(Call<List<Repository>> call, Throwable t) {

            }

        });
    }

}
