package com.codace.gitnux.ui.login;

import com.codace.gitnux.api.model.AccessToken;
import com.codace.gitnux.ui.base.BaseView;

import retrofit2.Call;

interface LoginView extends BaseView<String> {

    void showProgressBar();

    void hideProgressBar();

    void startBrowser();

    Call<AccessToken> provideAccessTokenCall(String code);

}
