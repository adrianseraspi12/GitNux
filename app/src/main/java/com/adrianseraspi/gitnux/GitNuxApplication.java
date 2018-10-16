package com.adrianseraspi.gitnux;

import android.app.Activity;
import android.app.Application;

import com.adrianseraspi.gitnux.api.network.NetworkConnectionListener;
import com.adrianseraspi.gitnux.di.AppComponent;
import com.adrianseraspi.gitnux.di.AppModule;
import com.adrianseraspi.gitnux.di.DaggerAppComponent;
import com.adrianseraspi.gitnux.di.GithubModule;

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
