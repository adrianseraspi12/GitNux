package com.adrianseraspi.gitnux.codeviewer;

import com.adrianseraspi.gitnux.api.model.File;
import com.adrianseraspi.gitnux.base.BasePresenter;
import com.adrianseraspi.gitnux.base.BaseView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodeViewerPresenter extends BasePresenter {

    private BaseView<File> codeViewerView;

    CodeViewerPresenter(BaseView<File> baseView) {
        super(baseView);
        this.codeViewerView = baseView;
    }

    void loadData(String url) {
        String accessToken = getSharedPreferences().getString("access_token", "");

        Call<File> fileCall = getGitHubClient().getFileWithUrl(url, accessToken);
        fileCall.enqueue(new Callback<File>() {

            @Override
            public void onResponse(Call<File> call, Response<File> response) {
                codeViewerView.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<File> call, Throwable t) {

            }

        });
    }

}
