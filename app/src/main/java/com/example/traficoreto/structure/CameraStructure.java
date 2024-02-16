package com.example.traficoreto.structure;

import java.io.Serializable;

public class CameraStructure implements Serializable {
    public String cameraId, cameraName, urlImage, latitude, longitude, sourceId;
    public String icon;
    public boolean favourite = false;
    public int page;

    public CameraStructure(String cameraId, String cameraName, String urlImage, String latitude, String longitude, String sourceId, int page) {
        this.cameraId = cameraId;
        this.cameraName = cameraName;
        this.urlImage = urlImage;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sourceId = sourceId;
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public CameraStructure(String icon) {

        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public CameraStructure(String cameraId, String cameraName) {
        this.cameraId = cameraId;
        this.cameraName = cameraName;
    }
    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
}
