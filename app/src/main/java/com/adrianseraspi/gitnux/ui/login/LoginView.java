package com.adrianseraspi.gitnux.ui.login;

import com.adrianseraspi.gitnux.api.model.AccessToken;
import com.adrianseraspi.gitnux.ui.base.BaseView;

import retrofit2.Call;

interface LoginView extends BaseView<String> {

    void showProgressBar();

    void hideProgressBar();

    void startBrowser();

    Call<AccessToken> provideAccessTokenCall(String code);

}
