package com.adrianseraspi.gitnux.api.model;

import com.google.gson.annotations.SerializedName;

public class Repository {

    private String name;

    private String description;

    private String language;

    private String url;

    private boolean fork;

    private Parent parent;

    private Owner owner;

    @SerializedName("full_name") private String fullName;

    @SerializedName("contents_url") private String contentUrl;

    @SerializedName("updated_at") private String update;

    @SerializedName("stargazers_count") private int starsCount;

    @SerializedName("forks_count") private int forksCount;

    public String getFullName() {
        return fullName;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public Parent getParent() {
        return parent;
    }

    public String getUpdate() {
        return update;
    }

    public String getName() {
        return name;
    }

    public boolean isFork() {
        return fork;
    }

    public int getStarsCount() {
        return starsCount;
    }

    public int getForksCount() {
        return forksCount;
    }

    public String getContentUrl() {
        return url + "/contents";
    }

    public Owner getOwner() {
        return owner;
    }

    public class Parent {

        @SerializedName("full_name")
        private String fullName;

        public String getFullName() {
            return fullName;
        }
    }

    public class Owner {

        private String login;

        public String getLogin() {
            return login;
        }

    }

}
