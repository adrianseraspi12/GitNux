package com.codace.gitnux.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codace.gitnux.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class LoginDialog extends DialogFragment {

    @BindView(R.id.dialog_webview_container) WebView webView;
    @BindView(R.id.dialog_webview_link) TextView linkView;
    @BindView(R.id.dialog_webview_progressbar) ProgressBar progressBar;
    @BindString(R.string.clientId) String clientId;
    @BindString(R.string.redirectUri) String redirectUri;

    private LoginDialogCallback callback;

    static LoginDialog newInstance() {
        return new LoginDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_webview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        webView.setWebViewClient(new WebViewClientSupport());
        linkView.setText(getUrl());
        webView.loadUrl(getUrl());
    }

    private String getUrl() {
        return "https://github.com/login/oauth/authorize"
                + "?client_id=" + clientId
                + "&scope=repo"
                + "&redirect_uri=" + redirectUri;
    }

    void setWebViewListener(LoginDialogCallback callback) {
        this.callback = callback;
    }

    @OnClick(R.id.dialog_webview_close)
    void onCloseClick() {
        dismiss();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        callback = null;
    }

    class WebViewClientSupport extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Timber.i("Url = %s", url);
            return handleUri(url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String uri = request.getUrl().toString();
            Timber.i("Url = %s", uri);
            return handleUri(uri);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }

        private boolean handleUri(String url) {
            if (url != null && url.startsWith(redirectUri)) {

                if (callback != null) {
                    callback.onGetData(url);
                }

                dismiss();

                return true;
            }
            return false;
        }

    }

    public interface LoginDialogCallback {

        void onGetData(String url);

    }
}
