package com.adrianseraspi.gitnux.ui.user;

import com.adrianseraspi.gitnux.api.model.User;
import com.adrianseraspi.gitnux.ui.base.BasePresenter;
import com.adrianseraspi.gitnux.ui.base.BaseView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class UserPresenter extends BasePresenter implements Callback<User> {

    private final BaseView<User> baseView;

    public UserPresenter(BaseView baseView) {
        super(baseView);
        this.baseView = baseView;
    }

    public void loadUser(String user) {
        String accessToken = getSharedPreferences().getString("access_token", "");
        Call<User> userCall = getGitHubClient().getUser(user, accessToken);
        userCall.enqueue(this);
    }

    public void loadCurrentUser() {
        String accessToken = getSharedPreferences().getString("access_token", "");
        Call<User> userCall = getGitHubClient().getCurrentUser(accessToken);
        userCall.enqueue(this);
    }

    @Override
    public void onResponse(Call<User> call, Response<User> response) {
        if (response.body() != null) {
            baseView.onSuccess(response.body());
        } else {
            Timber.e("User is null");
        }
    }

    @Override
    public void onFailure(Call<User> call, Throwable t) {

    }

}
