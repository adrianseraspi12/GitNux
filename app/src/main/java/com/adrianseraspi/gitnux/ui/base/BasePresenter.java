package com.adrianseraspi.gitnux.ui.base;

import android.content.SharedPreferences;

import com.adrianseraspi.gitnux.api.service.GitHubClient;

import javax.inject.Inject;

public class BasePresenter {

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    GitHubClient gitHubClient;

    protected BasePresenter(BaseView baseView) {
        baseView.init(this);
    }

    protected GitHubClient getGitHubClient() {
        return gitHubClient;
    }

    protected SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

}
