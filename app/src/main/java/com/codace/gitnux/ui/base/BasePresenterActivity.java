package com.codace.gitnux.ui.base;

import android.widget.Toast;

import com.codace.gitnux.GitNuxApplication;
import com.codace.gitnux.api.network.NetworkConnectionListener;

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
