package com.adrianseraspi.gitnux.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adrianseraspi.gitnux.R;
import com.adrianseraspi.gitnux.api.model.Following;
import com.adrianseraspi.gitnux.api.model.User;
import com.adrianseraspi.gitnux.api.service.GitHubClient;
import com.adrianseraspi.gitnux.ui.overview.OverviewActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingAdapter extends BaseAdapter<Following, FollowingAdapter.ViewHolder> {

    @Inject
    SharedPreferences prefs;

    @Inject
    GitHubClient gitHubClient;

    public FollowingAdapter(List<Following> list, CallbackAdapter callbackAdapter) {
        super(list);
        callbackAdapter.initClient(this);
    }

    @Override
    public ViewHolder onCreateVH(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_user,
                        parent,
                        false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindVH(ViewHolder holder, int position, Following model) {
        String accessToken = prefs.getString("access_token", "");

        gitHubClient.getUserWithThisUrl(model.getUrl(), accessToken).enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                holder.nameView.setText(response.body().getLogin());
                holder.descView.setText(response.body().getBio());

                if (response.body().getLocation() != null) {
                    holder.locationView.setText(response.body().getLocation());
                }
                else {
                    holder.locationView.setVisibility(View.GONE);
                }

                Picasso.get().load(response.body().getAvatarUrl())
                        .fit().centerCrop().into(holder.imageView);

                holder.setClickListener(response.body().getLogin());
                holder.locationView.setCompoundDrawablesWithIntrinsicBounds(
                        holder.locationDrawable,
                        null, null,
                        null);

                setScaleAnimation(holder.itemView, position);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }

        });

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_user_name) TextView nameView;
        @BindView(R.id.item_user_image) CircleImageView imageView;
        @BindView(R.id.item_user_bio) TextView descView;
        @BindView(R.id.item_user_location) TextView locationView;
        @BindDrawable(R.drawable.ic_location) Drawable locationDrawable;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setClickListener(String user) {
            itemView.setOnClickListener(v -> {

                Context context = itemView.getContext();
                Intent intent = new Intent(context, OverviewActivity.class);
                intent.putExtra(OverviewActivity.EXTRA_USER, user);
                context.startActivity(intent);

            });

        }
    }

    public interface CallbackAdapter {

        void initClient(FollowingAdapter followingAdapter);

    }

}
