package com.adrianseraspi.gitnux.di;

import com.adrianseraspi.gitnux.adapter.FollowingAdapter;
import com.adrianseraspi.gitnux.base.BasePresenter;
import com.adrianseraspi.gitnux.login.LoginPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, GithubModule.class} )
public interface AppComponent {

    void inject(FollowingAdapter followingAdapter);

    void inject(BasePresenter presenter);

}
