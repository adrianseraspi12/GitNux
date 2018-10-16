package com.adrianseraspi.gitnux.ui.liststarred;

import com.adrianseraspi.gitnux.api.model.Repository;
import com.adrianseraspi.gitnux.ui.base.BasePresenter;
import com.adrianseraspi.gitnux.ui.base.BaseView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ListStarredPresenter extends BasePresenter {

    private final BaseView<List<Repository>> listStarredView;

    ListStarredPresenter(BaseView<List<Repository>> listStarredView) {
        super(listStarredView);
        this.listStarredView = listStarredView;
    }

    void load(int currentPage) {
        String accessToken = getSharedPreferences().getString("access_token", "");
        Call<List<Repository>> callStarred = getGitHubClient().starredForUser(
                accessToken,
                currentPage,
                30);

        callStarred.enqueue(new Callback<List<Repository>>() {

            @Override
            public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {

                if (response.body().size() > 0) {
                    listStarredView.onSuccess(response.body());
                } else {
                    listStarredView.onError("All starred repositories loaded");
                }

            }

            @Override
            public void onFailure(Call<List<Repository>> call, Throwable t) {

            }

        });

    }

}
