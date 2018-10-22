package com.codace.gitnux.ui.repo;

import com.codace.gitnux.api.model.Contents;
import com.codace.gitnux.api.model.File;
import com.codace.gitnux.ui.base.BasePresenter;
import com.codace.gitnux.ui.base.BaseView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class RepositoryPresenter extends BasePresenter {

    private final BaseView repositoryView;

    RepositoryPresenter(BaseView baseView) {
        super(baseView);
        this.repositoryView = baseView;
    }


    void loadReadMe(String owner, String repoName) {
        String accessToken = getSharedPreferences().getString("access_token", "");

        Call<File> readMeCall = getGitHubClient().getRepoReadMe(owner, repoName, accessToken);
        readMeCall.enqueue(new Callback<File>() {

            @Override
            public void onResponse(Call<File> call, Response<File> response) {
                if (response.body() != null) {
                    repositoryView.onSuccess(response.body());
                }
                else {
                    repositoryView.onError("No readme found");
                }
            }

            @Override
            public void onFailure(Call<File> call, Throwable t) {

            }

        });
    }

    void loadContentsWithUrl(String url) {
        String accessToken = getSharedPreferences().getString("access_token", "");

        Call<List<Contents>> listContentsCall = getGitHubClient()
                .getListContentsWithUrl(url, accessToken);

        listContentsCallback(listContentsCall);
    }

    private void listContentsCallback(Call<List<Contents>> listContentsCall) {
        listContentsCall.enqueue(new Callback<List<Contents>>() {

            @Override
            public void onResponse(Call<List<Contents>> call, Response<List<Contents>> response) {
                if (response.body() != null) {
                    repositoryView.onSuccess(response.body());
                }
                else {
                    repositoryView.onError("No contents found");
                }

            }

            @Override
            public void onFailure(Call<List<Contents>> call, Throwable t) {

            }

        });
    }

}
