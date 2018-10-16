package com.adrianseraspi.gitnux.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.adrianseraspi.gitnux.GitNuxApplication;
import com.adrianseraspi.gitnux.R;
import com.adrianseraspi.gitnux.api.model.AccessToken;
import com.adrianseraspi.gitnux.api.network.NetworkConnectionListener;
import com.adrianseraspi.gitnux.api.service.GitHubClient;
import com.adrianseraspi.gitnux.api.url.GithubUrl;
import com.adrianseraspi.gitnux.base.BasePresenter;
import com.adrianseraspi.gitnux.home.HomeActivity;
import com.adrianseraspi.gitnux.util.LoadingDialog;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements LoginView, NetworkConnectionListener {

    private String clientSecret = "67fe79e63f1d758d1122a8295d1c1b33d7e1eea8";
    private String clientId = "e1368abff00f39d58fef";
    private String redirectUri = "gitnux://callback";

    private LoginPresenter presenter;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        presenter = new LoginPresenter(this);
        loadingDialog = new LoadingDialog(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (presenter.hasAccessToken()) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Uri uri = getIntent().getData();

        if (uri != null && uri.toString().startsWith(redirectUri)) {
            presenter.saveAccessToken(uri);
        }

    }

    public void loginUser(View view) {
        presenter.authenticateUser();
    }

    @Override
    public void init(BasePresenter presenter) {
        ((GitNuxApplication) getApplication())
                .getAppComponent(LoginActivity.this, this)
                .inject(presenter);
    }

    @Override
    public void onSuccess(String data) {
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {
        loadingDialog.show();
    }

    @Override
    public void hideProgressBar() {
        loadingDialog.dismiss();
    }

    @Override
    public void startBrowser() {
        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://github.com/login/oauth/authorize"
                        + "?client_id=" + clientId
                        + "&scope=repo"
                        + "&redirect_uri=" + redirectUri));

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public Call<AccessToken> provideAccessTokenCall(String code) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(GithubUrl.OAUTH_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        GitHubClient client = retrofit.create(GitHubClient.class);
        return client.getAccessToken(
                clientId,
                clientSecret,
                code);
    }

    @Override
    public void onInternetUnavailable() {
        Toast.makeText(LoginActivity.this,
                "No internet Connection",
                Toast.LENGTH_SHORT).show();
    }
}
