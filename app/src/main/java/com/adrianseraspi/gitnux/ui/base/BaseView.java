package com.adrianseraspi.gitnux.ui.base;

public interface BaseView<T> {

    void init(BasePresenter presenter);

    void onSuccess(T data);

    void onError(String message);

}
