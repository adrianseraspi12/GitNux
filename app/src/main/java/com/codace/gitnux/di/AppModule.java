package com.codace.gitnux.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.codace.gitnux.GitNuxApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final GitNuxApplication application;

    public AppModule(GitNuxApplication application) {
        this.application = application;
    }

    @Provides
    GitNuxApplication provideGitNuxApplication() {
        return application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Singleton
    @Provides
    SharedPreferences provideSharedPreferences(Application app) {
        return app.getSharedPreferences("gitnux", Context.MODE_PRIVATE);
    }

}
