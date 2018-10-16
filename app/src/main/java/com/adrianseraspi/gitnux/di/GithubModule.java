package com.adrianseraspi.gitnux.di;

import android.app.Activity;

import com.adrianseraspi.gitnux.api.network.NetworkConnectionInterceptor;
import com.adrianseraspi.gitnux.api.network.NetworkConnectionListener;
import com.adrianseraspi.gitnux.api.network.NetworkStatus;
import com.adrianseraspi.gitnux.api.service.GitHubClient;
import com.adrianseraspi.gitnux.api.url.GithubUrl;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class GithubModule {

    private Activity activity;
    private NetworkConnectionListener mListener;

    public GithubModule(Activity activity, NetworkConnectionListener listener) {
        this.activity = activity;
        this.mListener = listener;
    }

    @Provides
    Activity provideActivity() {
        return activity;
    }

    @Provides
    NetworkConnectionListener provideNetworkConnectionListener() {
        return mListener;
    }

    @Provides
    Interceptor provideNetworkConnectionInterceptor(Activity activity,
                                                    NetworkConnectionListener listener) {

        return new NetworkConnectionInterceptor() {

            @Override
            public boolean isInternetAvailable() {
                return NetworkStatus.isInternetAvailable(activity.getApplicationContext());
            }

            @Override
            public void onInternetUnavailable() {
                if (listener != null) {
                    activity.runOnUiThread(listener::onInternetUnavailable);
                }
            }

        };
    }

    @Provides
    OkHttpClient provideHttpClient(Interceptor interceptor) {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
    }

    @Provides
    Retrofit provideRetrofit(OkHttpClient httpClient) {
        return new Retrofit.Builder()
                .baseUrl(GithubUrl.API_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    GitHubClient provideGithubClient(Retrofit retrofit) {
        return retrofit.create(GitHubClient.class);
    }

}
