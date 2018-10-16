package com.adrianseraspi.gitnux.util;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class SnackbarUtil {

    public static void showNoInternetConnection(View view, Callback callback) {
        Snackbar.make(view, "No internet connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("Refresh", v -> callback.onRefreshClick())
                .show();
    }

    public interface Callback {

        void onRefreshClick();

    }

}
