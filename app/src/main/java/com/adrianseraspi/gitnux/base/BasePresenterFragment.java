package com.adrianseraspi.gitnux.base;

import android.widget.Toast;

import com.adrianseraspi.gitnux.GitNuxApplication;
import com.adrianseraspi.gitnux.api.network.NetworkConnectionListener;

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
