package com.codace.gitnux.ui.login;

import android.net.Uri;

import com.codace.gitnux.api.model.AccessToken;
import com.codace.gitnux.ui.base.BasePresenter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class LoginPresenter extends BasePresenter {

    private final LoginView loginView;

    LoginPresenter(LoginView loginView) {
        super(loginView);
        this.loginView = loginView;
        loginView.init(LoginPresenter.this);
    }

    boolean hasAccessToken() {
        String accessToken = getSharedPreferences().getString("access_token", null);
        return accessToken != null;
    }

    void saveAccessToken(Uri uri) {
        loginView.showProgressBar();

        String code = uri.getQueryParameter("code");
        Call<AccessToken> accessTokenCall = loginView.provideAccessTokenCall(code);
        accessTokenCall.enqueue(new Callback<AccessToken>() {

            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                if (response.isSuccessful()) {
                    String accessToken = response.body().getAccessToken();
                    getSharedPreferences().edit().putString("access_token", accessToken).apply();
                    loginView.onSuccess("Successfully registered");
                }
                else {
                    loginView.onError("Error occurred, Please try again");
                }

                loginView.hideProgressBar();

            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                //  Handle error
            }

        });

    }

}
