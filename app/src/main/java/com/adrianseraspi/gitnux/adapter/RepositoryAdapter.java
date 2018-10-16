package com.adrianseraspi.gitnux.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adrianseraspi.gitnux.R;
import com.adrianseraspi.gitnux.api.model.Repository;
import com.adrianseraspi.gitnux.ui.repo.RepositoryActivity;

import net.danlew.android.joda.DateUtils;

import org.joda.time.DateTime;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static net.danlew.android.joda.DateUtils.FORMAT_ABBREV_RELATIVE;

public class RepositoryAdapter extends BaseAdapter<Repository, RepositoryAdapter.ViewHolder> {

    public RepositoryAdapter(List<Repository> list) {
        super(list);
    }

    @Override
    public ViewHolder onCreateVH(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_item_repository,
                parent,
                false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindVH(ViewHolder holder, int position, Repository model) {
        DateTime updateDate = DateTime.parse(model.getUpdate());

        holder.titleView.setText(model.getName());
        holder.descView.setText(model.getDescription());
        holder.setClickListener(model);

        holder.starsView.setText(
                String.valueOf(model.getStarsCount())
        );
        holder.forkCountView.setText(
                String.valueOf(model.getForksCount())
        );
        holder.dateView.setText(
                DateUtils.getRelativeTimeSpanString(holder.itemView.getContext(),
                        updateDate, FORMAT_ABBREV_RELATIVE)
        );

        if (model.isFork()) {
            holder.forkView.setText(R.string.forked);
        }

        setScaleAnimation(holder.itemView, position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_repo_title) TextView titleView;
        @BindView(R.id.item_repo_fork) TextView forkView;
        @BindView(R.id.item_repo_description) TextView descView;
        @BindView(R.id.item_repo_update_date) TextView dateView;
        @BindView(R.id.item_repo_starred) TextView starsView;
        @BindView(R.id.item_repo_forks) TextView forkCountView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setClickListener(Repository repository) {
            Context context = itemView.getContext();
            itemView.setOnClickListener(v -> {

                Intent intent = new Intent(context, RepositoryActivity.class);

                intent.putExtra(RepositoryActivity.EXTRA_REPO_NAME, repository.getName());
                intent.putExtra(RepositoryActivity.EXTRA_REPO_URL, repository.getContentUrl());
                intent.putExtra(RepositoryActivity.EXTRA_REPO_OWNER,
                        repository.getOwner().getLogin());

                context.startActivity(intent);
            });
        }

    }

}
