package com.adrianseraspi.gitnux.repo;


import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.adrianseraspi.gitnux.R;
import com.adrianseraspi.gitnux.adapter.BaseAdapter;
import com.adrianseraspi.gitnux.api.model.Contents;
import com.adrianseraspi.gitnux.base.BasePresenterFragment;
import com.adrianseraspi.gitnux.codeviewer.CodeViewerActivity;
import com.adrianseraspi.gitnux.util.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListContentFragment extends BasePresenterFragment<List<Contents>> {

    private View view;

    private RepositoryPresenter presenter;

    private List<String> listOfUrl;
    private List<String> listOfDirTitle = new ArrayList<>();

    @BindView(R.id.list_file_directory) TextView titleDirectoryView;
    @BindView(R.id.list_file_back) ImageButton backDirView;
    @BindView(R.id.file_list) RecyclerView listContentsView;
    @BindView(R.id.list_file_horizontal_scrollview) HorizontalScrollView hScrollView;

    public ListContentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list_file, container, false);
        ButterKnife.bind(this, view);
        showFiles();
        setUpRecyclerView();
        showHeader();
        setBackDirectoryListener();
        return view;
    }

    private void showFiles() {
        listOfUrl = getArguments().getStringArrayList(RepositoryActivity.EXTRA_REPO_URL);
        listOfDirTitle = getArguments().getStringArrayList(RepositoryActivity.EXTRA_HEADERS);
        String loadUrl = listOfUrl.get(listOfUrl.size() - 1);

        presenter = new RepositoryPresenter(this);
        presenter.loadContentsWithUrl(loadUrl);
    }

    private void showHeader() {
        titleDirectoryView.setMovementMethod(new ScrollingMovementMethod());

        for (int i = 0; i < listOfDirTitle.size(); i++) {
            String title = listOfDirTitle.get(i);
            titleDirectoryView.append(title + " | ");
            scrollToTheEnd();
        }
    }

    private void setUpRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());

        listContentsView.setLayoutManager(llm);
        listContentsView.addItemDecoration(new DividerItemDecoration(getContext(),
                llm.getOrientation()));
    }

    private void setBackDirectoryListener() {
        backDirView.setOnClickListener(v -> {

            if (listOfUrl.size() > 0 && listOfDirTitle.size() > 0) {
                listOfUrl.remove(listOfUrl.size() - 1);
                listOfDirTitle.remove(listOfDirTitle.size() - 1);
            }

            String url = listOfUrl.get(listOfUrl.size() - 1);
            titleDirectoryView.setText("");
            showHeader();
            presenter.loadContentsWithUrl(url);
            Timber.i("Loaded url = %s", url);

        });
    }

    @Override
    public void onSuccess(List<Contents> data) {
        FilesAdapter filesAdapter = new FilesAdapter(data);
        listContentsView.setAdapter(filesAdapter);
        scrollToTheEnd();

        if (listOfUrl.size() > 1) {
            backDirView.setEnabled(true);
        } else {
            backDirView.setEnabled(false);
        }
    }

    private void scrollToTheEnd() {
        hScrollView.post(() -> hScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT));
    }

    @Override
    public void onInternetUnavailable() {
        listContentsView.setAdapter(null);
        View rootView = view.findViewById(R.id.list_file_rootview);
        SnackbarUtil.showNoInternetConnection(rootView, () -> {

            String url = listOfUrl.get(listOfUrl.size() -1);
            presenter.loadContentsWithUrl(url);
        });
    }

    class FilesAdapter extends BaseAdapter<Contents, FilesAdapter.ViewHolder> {

        FilesAdapter(List<Contents> list) {
            super(list);
        }

        @Override
        public ViewHolder onCreateVH(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(
                    R.layout.row_item_files,
                    parent,
                    false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindVH(ViewHolder holder, int position, Contents model) {
            holder.titleView.setText(model.getName());
            holder.setOnClickListener(model);

            if (model.getType().equals("dir")) {
                holder.iconView.setImageResource(R.drawable.ic_folder);
            }
            else if (model.getType().equals("file")) {
                holder.iconView.setImageResource(R.drawable.ic_file);
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView iconView;
            private TextView titleView;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                iconView = itemView.findViewById(R.id.item_files_icon);
                titleView = itemView.findViewById(R.id.item_files_title);
            }

            void setOnClickListener(Contents model) {
                itemView.setOnClickListener(v -> {

                    if (model.getType().equals("dir")) {
                        titleDirectoryView.append(model.getName() + " | ");

                        listOfDirTitle.add(model.getName());
                        listOfUrl.add(model.getUrl());

                        presenter.loadContentsWithUrl(model.getUrl());
                    }
                    else if (model.getType().equals("file")) {

                        Intent intent = new Intent(getContext(), CodeViewerActivity.class);
                        intent.putExtra(CodeViewerActivity.EXTRA_URL, model.getUrl());
                        startActivity(intent);

                    }

                });

            }

        }

    }

}
