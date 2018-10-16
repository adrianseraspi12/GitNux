package com.adrianseraspi.gitnux.ui.repo;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adrianseraspi.gitnux.R;
import com.adrianseraspi.gitnux.api.model.File;
import com.adrianseraspi.gitnux.ui.base.BasePresenterFragment;
import com.adrianseraspi.gitnux.util.SnackbarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.noties.markwon.Markwon;
import ru.noties.markwon.SpannableConfiguration;
import ru.noties.markwon.il.AsyncDrawableLoader;
import timber.log.Timber;

public class ReadMeFragment extends BasePresenterFragment<File> {

    private View view;

    @BindView(R.id.read_me_progressbar) ProgressBar progressBar;

    public ReadMeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_read_me, container, false);
        ButterKnife.bind(this, view);
        showReadMe();
        return view;
    }

    private void showReadMe() {
        String owner = getArguments().getString(RepositoryActivity.EXTRA_REPO_OWNER);
        String repoName = getArguments().getString(RepositoryActivity.EXTRA_REPO_NAME);

        RepositoryPresenter presenter = new RepositoryPresenter(this);
        presenter.loadReadMe(owner, repoName);

        Timber.i("Owner = %s", owner);
        Timber.i("Repository = %s", repoName);
    }

    @Override
    public void onSuccess(File file) {
        TextView readmeView = view.findViewById(R.id.read_me_container);
        SpannableConfiguration configuration = SpannableConfiguration.builder(getContext())
                .asyncDrawableLoader(AsyncDrawableLoader.create())
                .build();

        new Handler().postDelayed(() -> {
            Markwon.setMarkdown(readmeView, configuration, file.getContent());
            hideProgressBar();
        }, 2000);

    }

    @Override
    public void onError(String message) {
        super.onError(message);
        hideProgressBar();
    }

    @Override
    public void onInternetUnavailable() {
        hideProgressBar();
        View rootView = view.findViewById(R.id.read_me_rootview);
        SnackbarUtil.showNoInternetConnection(rootView, () -> {

            showProgressBar();
            showReadMe();

        });
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

}
