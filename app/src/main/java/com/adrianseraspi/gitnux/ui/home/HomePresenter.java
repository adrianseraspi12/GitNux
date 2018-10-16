package com.adrianseraspi.gitnux.ui.home;

import com.adrianseraspi.gitnux.api.model.Events;
import com.adrianseraspi.gitnux.api.model.User;
import com.adrianseraspi.gitnux.ui.base.BaseView;
import com.adrianseraspi.gitnux.ui.user.UserPresenter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class HomePresenter extends UserPresenter {

    private final BaseView baseView;

    HomePresenter(BaseView baseView) {
        super(baseView);
        this.baseView = baseView;
    }

    void removeAccessToken() {
        getSharedPreferences().edit().remove("access_token").apply();
    }

    void loadOfflineUserInfo() {
        String login = getSharedPreferences().getString("login", "");
        String name = getSharedPreferences().getString("name", "");
        User user = new User(login, name);
        baseView.onSuccess(user);
    }

    void load(int currentPage) {
        String accessToken = getSharedPreferences().getString("access_token", "");
        Call<User> userCall = getGitHubClient().getCurrentUser(accessToken);
        userCall.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                String login = user.getLogin();

                getSharedPreferences().edit()
                        .putString("login", user.getLogin())
                        .putString("name", user.getName())
                        .apply();

                callEvents(accessToken, login, currentPage);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });


    }

    private void callEvents(String accessToken, String user, int currentPage) {
        Call<List<Events>> eventsCall = getGitHubClient().eventsForUser(
                user,
                accessToken,
                currentPage,
                30);

        eventsCall.enqueue(new Callback<List<Events>>() {

            @Override
            public void onResponse(Call<List<Events>> call, Response<List<Events>> response) {

                if (response.body().size() > 0) {
                    baseView.onSuccess(response.body());
                }
                else {
                    baseView.onError("All events are loaded");
                }

            }

            @Override
            public void onFailure(Call<List<Events>> call, Throwable t) {

            }

        });
    }

}
