package com.codace.gitnux.di;

import com.codace.gitnux.adapter.FollowingAdapter;
import com.codace.gitnux.ui.base.BasePresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, GithubModule.class} )
public interface AppComponent {

    void inject(FollowingAdapter followingAdapter);

    void inject(BasePresenter presenter);

}
