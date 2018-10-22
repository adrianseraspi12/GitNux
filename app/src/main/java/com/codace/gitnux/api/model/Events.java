package com.codace.gitnux.api.model;

import com.google.gson.annotations.SerializedName;

public class Events {

    private String type;
    private Repository repo;

    @SerializedName("created_at") private String createdAt;

    public String getType() {
        return type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Repository getRepo() {
        return repo;
    }
}
