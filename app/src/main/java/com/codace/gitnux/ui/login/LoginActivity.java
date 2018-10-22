package com.codace.gitnux.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.codace.gitnux.GitNuxApplication;
import com.codace.gitnux.R;
import com.codace.gitnux.api.model.AccessToken;
import com.codace.gitnux.api.network.NetworkConnectionListener;
import com.codace.gitnux.api.service.GitHubClient;
import com.codace.gitnux.api.url.GithubUrl;
import com.codace.gitnux.ui.base.BasePresenter;
import com.codace.gitnux.ui.home.HomeActivity;
import com.codace.gitnux.util.LoadingDialog;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements LoginView, NetworkConnectionListener {

    @BindString(R.string.clientSecret) String clientSecret;
    @BindString(R.string.clientId) String clientId;
    @BindString(R.string.redirectUri) String redirectUri;

    private LoginPresenter presenter;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        presenter = new LoginPresenter(this);
        loadingDialog = new LoadingDialog(this);
        loadingDialog.setCanceledOnTouchOutside(false);
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

    @OnClick(R.id.login_button)
    public void loginUser() {
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
