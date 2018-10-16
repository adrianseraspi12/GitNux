package com.adrianseraspi.gitnux.codeviewer;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adrianseraspi.gitnux.GitNuxApplication;
import com.adrianseraspi.gitnux.R;
import com.adrianseraspi.gitnux.api.model.File;
import com.adrianseraspi.gitnux.base.BasePresenter;
import com.adrianseraspi.gitnux.base.BasePresenterActivity;
import com.adrianseraspi.gitnux.base.BaseView;
import com.adrianseraspi.gitnux.util.SnackbarUtil;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.kbiakov.codeview.CodeView;

public class CodeViewerActivity extends BasePresenterActivity<File> {

    public static final String EXTRA_URL = "EXTRA_URL";

    @BindView(R.id.main_horizontal_progressbar) ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_viewer);
        ButterKnife.bind(this);
        setUpActionBar();
        showPresenterData();
    }

    private void setUpActionBar() {
        setSupportActionBar(findViewById(R.id.main_toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    private void showPresenterData() {
        String url = getIntent().getStringExtra(EXTRA_URL);

        CodeViewerPresenter presenter = new CodeViewerPresenter(this);
        presenter.loadData(url);
        showProgressBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(File file) {
        setTitle(file.getName());

        new Handler().postDelayed(() -> {

            hideProgressBar();
            CodeView codeView = findViewById(R.id.code_viewer_container);
            codeView.setCode(file.getContent());

        }, 2000);

    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onInternetUnavailable() {
        hideProgressBar();
        View view = findViewById(R.id.code_viewer_root);
        SnackbarUtil.showNoInternetConnection(view, () -> {
            showProgressBar();
            showPresenterData();
        });
    }
}
