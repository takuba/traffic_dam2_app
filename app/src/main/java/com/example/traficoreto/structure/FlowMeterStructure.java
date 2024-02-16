package com.example.traficoreto.structure;

import java.io.Serializable;

public class FlowMeterStructure implements Serializable {
    public String meterId, provinceId, municipalityId, description, latitude, longitude, geometry,sourceId;
    public String icon;
    public boolean favourite = false;
    public int page;

    public FlowMeterStructure(String meterId,String sourceId, String provinceId, String municipalityId, String description, String latitude, String longitude, String geometry,int page) {
        this.meterId = meterId;
        this.sourceId = sourceId;
        this.provinceId = provinceId;
        this.municipalityId = municipalityId;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.geometry = geometry;
        this.page = page;
    }
    public FlowMeterStructure(String meterId, String description) {
        this.meterId = meterId;
        this.description = description;
    }
    public FlowMeterStructure(String icon) {
        this.icon = icon;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getMeterId() {
        return meterId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getMunicipalityId() {
        return municipalityId;
    }

    public void setMunicipalityId(String municipalityId) {
        this.municipalityId = municipalityId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }
}
