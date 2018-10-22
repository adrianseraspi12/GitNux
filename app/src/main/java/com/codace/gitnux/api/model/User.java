package com.codace.gitnux.api.model;

import com.google.gson.annotations.SerializedName;

public class User {

    private String login;

    private String name;

    private String location;

    private String bio;

    @SerializedName("avatar_url") private String avatarUrl;

    public String getLogin() {
        return login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public User() {
    }

    public User(String login, String name) {
        this.login = login;
        this.name = name;
    }

}
