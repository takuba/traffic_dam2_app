package com.example.traficoreto.structure;

public class UserFavouriteStructure {
    private int id;
    private int user_id;
    private String type;
    private String fav_id;
    private String sourceId;

    public UserFavouriteStructure(int id, int user_id, String type, String fav_id, String sourceId) {
        this.id = id;
        this.user_id = user_id;
        this.type = type;
        this.fav_id = fav_id;
        this.sourceId = sourceId;
    }
    public UserFavouriteStructure(int user_id, String type, String fav_id, String sourceId) {
        this.user_id = user_id;
        this.type = type;
        this.fav_id = fav_id;
        this.sourceId = sourceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFav_id() {
        return fav_id;
    }

    public void setFav_id(String fav_id) {
        this.fav_id = fav_id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
}
