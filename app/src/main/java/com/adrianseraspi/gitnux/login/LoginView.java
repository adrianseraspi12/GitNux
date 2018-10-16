package com.adrianseraspi.gitnux.login;

import com.adrianseraspi.gitnux.api.model.AccessToken;
import com.adrianseraspi.gitnux.base.BaseView;

import retrofit2.Call;

public interface LoginView extends BaseView<String> {

    void showProgressBar();

    void hideProgressBar();

    void startBrowser();

    Call<AccessToken> provideAccessTokenCall(String code);

}
