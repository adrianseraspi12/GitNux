package com.codace.gitnux.api.service;

import com.codace.gitnux.api.model.AccessToken;
import com.codace.gitnux.api.model.Contents;
import com.codace.gitnux.api.model.Events;
import com.codace.gitnux.api.model.File;
import com.codace.gitnux.api.model.Following;
import com.codace.gitnux.api.model.Repository;
import com.codace.gitnux.api.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface GitHubClient {

    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    @FormUrlEncoded
    Call<AccessToken> getAccessToken(
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("code") String clientCode
    );

    @GET("/user")
    Call<User> getCurrentUser(@Query("access_token") String accessToken);

    @GET("/users/{user}")
    Call<User> getUser(
            @Path("user") String user,
            @Query("access_token") String accessToken);

    @GET
    Call<User> getUserWithThisUrl(
            @Url String url,
            @Query("access_token") String access_token);

    @GET("/user/following")
    Call<List<Following>> getCurrentUserFollowing(
            @Query("access_token") String accessToken,
            @Query("page") int page,
            @Query("per_page") int perPage);

    @GET("/user/followers")
    Call<List<Following>> getCurrentUserFollowers(
            @Query("access_token") String accessToken,
            @Query("page") int page,
            @Query("per_page") int perPage);

    @GET("/users/{user}/events")
    Call<List<Events>> eventsForUser(
            @Path("user") String user,
            @Query("access_token") String accessToken,
            @Query("page") int page,
            @Query("per_page") int perPage);

    @GET("/user/repos")
    Call<List<Repository>> reposForCurrentUser(
            @Query("access_token") String accessToken,
            @Query("page") int page,
            @Query("per_page") int perPage);

    @GET("/users/{user}/repos")
    Call<List<Repository>> reposForUser(
            @Path("user") String user,
            @Query("page") int page,
            @Query("per_page") int perPage,
            @Query("access_token") String accessToken);

    @GET("/user/starred")
    Call<List<Repository>> starredForUser(
            @Query("access_token") String accessToken,
            @Query("page") int page,
            @Query("per_page") int perPage);

    @GET("/repos/{owner}/{repoName}/readme")
    Call<File> getRepoReadMe(
            @Path("owner") String owner,
            @Path("repoName") String repoName,
            @Query("access_token") String access_token);

    @GET
    Call<File> getFileWithUrl(
            @Url String url,
            @Query("access_token") String access_token);

    @GET
    Call<List<Contents>> getListContentsWithUrl(
            @Url String url,
            @Query("access_token") String access_token);

}
