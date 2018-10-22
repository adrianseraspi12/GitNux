package com.codace.gitnux.ui.base;

import android.widget.Toast;

import com.codace.gitnux.GitNuxApplication;
import com.codace.gitnux.api.network.NetworkConnectionListener;

import androidx.fragment.app.Fragment;

public class BasePresenterFragment<T> extends Fragment implements
        BaseView<T>, NetworkConnectionListener {
    @Override
    public void init(BasePresenter presenter) {
        ((GitNuxApplication) getActivity().getApplication())
                .getAppComponent(getActivity(), this)
                .inject(presenter);
    }

    @Override
    public void onSuccess(T data) {

    }

    @Override
    public void onError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInternetUnavailable() {

    }
}
