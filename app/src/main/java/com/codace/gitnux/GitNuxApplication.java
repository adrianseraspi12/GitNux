package com.codace.gitnux;

import android.app.Activity;
import android.app.Application;

import com.codace.gitnux.api.network.NetworkConnectionListener;
import com.codace.gitnux.di.AppComponent;
import com.codace.gitnux.di.AppModule;
import com.codace.gitnux.di.DaggerAppComponent;
import com.codace.gitnux.di.GithubModule;

import net.danlew.android.joda.JodaTimeAndroid;

import io.github.kbiakov.codeview.classifier.CodeProcessor;
import timber.log.Timber;

public class GitNuxApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        Timber.plant(new Timber.DebugTree());
        CodeProcessor.init(this);
    }

    public AppComponent getAppComponent(Activity activity, NetworkConnectionListener _listener) {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .githubModule(new GithubModule(activity, _listener))
                .build();
    }

}
