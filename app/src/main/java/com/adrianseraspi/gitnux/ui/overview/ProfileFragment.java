package com.adrianseraspi.gitnux.ui.overview;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adrianseraspi.gitnux.R;
import com.adrianseraspi.gitnux.api.model.User;
import com.adrianseraspi.gitnux.ui.base.BasePresenterFragment;
import com.adrianseraspi.gitnux.ui.user.UserPresenter;
import com.adrianseraspi.gitnux.util.SnackbarUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends BasePresenterFragment<User> {

    private View view;

    @BindView(R.id.profile_progressbar) ProgressBar progressBar;
    @BindView(R.id.profile_container) View containerView;
    @BindView(R.id.profile_info_name) TextView nameView;
    @BindView(R.id.profile_info_location) TextView locationView;
    @BindView(R.id.profile_bio) TextView bioView;
    @BindView(R.id.profile_info_image) CircleImageView picView;

    @BindDrawable(R.drawable.ic_location) Drawable locationDrawable;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        loadPresenter();
        return view;
    }

    private void loadPresenter() {
        String user = getArguments().getString(OverviewActivity.EXTRA_USER);

        UserPresenter userPresenter = new UserPresenter(this);
        showProgressBar();

        if (user != null) {
            userPresenter.loadUser(user);
        } else {
            userPresenter.loadCurrentUser();
        }

    }

    @Override
    public void onSuccess(User data) {
        new Handler().postDelayed(() -> {

            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(1000);
            anim.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    showDetails(data);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

            });

            hideProgressBar();
            containerView.startAnimation(anim);

        }, 2000);

    }

    private void showDetails(User user) {
        nameView.setText(user.getName());
        locationView.setText(user.getLocation());
        bioView.setText(user.getBio());
        Picasso.get().load(user.getAvatarUrl()).fit().centerCrop().into(picView);
        locationView.setCompoundDrawablesWithIntrinsicBounds(
                locationDrawable,
                null, null,
                null);
    }

    @Override
    public void onError(String message) {
        super.onError(message);
        hideProgressBar();
    }

    @Override
    public void onInternetUnavailable() {
        hideProgressBar();
        View rootView = view.findViewById(R.id.profile_rootview);
        SnackbarUtil.showNoInternetConnection(rootView, () -> {
            showProgressBar();
            loadPresenter();
        });
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

}
