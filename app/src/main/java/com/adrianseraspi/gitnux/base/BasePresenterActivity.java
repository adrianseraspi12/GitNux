package com.adrianseraspi.gitnux.base;

import android.widget.Toast;

import com.adrianseraspi.gitnux.GitNuxApplication;
import com.adrianseraspi.gitnux.api.network.NetworkConnectionListener;
import com.adrianseraspi.gitnux.api.url.GithubUrl;

import androidx.appcompat.app.AppCompatActivity;

public class BasePresenterActivity<T> extends AppCompatActivity implements
        BaseView<T>,NetworkConnectionListener {

    @Override
    public void init(BasePresenter presenter) {
        ((GitNuxApplication) getApplication())
                .getAppComponent(this, this)
                .inject(presenter);
    }

    @Override
    public void onSuccess(T data) {

    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInternetUnavailable() {
    }
}
