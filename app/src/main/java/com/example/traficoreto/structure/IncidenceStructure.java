package com.example.traficoreto.structure;

import java.io.Serializable;

public class IncidenceStructure implements Serializable {
    public String incidenceId, sourceId, incidenceType, autonomousRegion, province, cause, startDate,	latitude,	longitude, incidenceLevel, direction;
    public String icon;
    public boolean favourite = false;
    public int page;
    public IncidenceStructure(String incidenceId, String sourceId, String incidenceType, String autonomousRegion, String province, String cause, String startDate, String latitude, String longitude, String incidenceLevel, String direction, int page) {
        this.incidenceId = incidenceId;
        this.sourceId = sourceId;
        this.incidenceType = incidenceType;
        this.autonomousRegion = autonomousRegion;
        this.province = province;
        this.cause = cause;
        this.startDate = startDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.incidenceLevel = incidenceLevel;
        this.direction = direction;
        this.page = page;

    }
    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
    public IncidenceStructure(String incidenceId, String incidenceType){
        this.incidenceId = incidenceId;
        this.incidenceType = incidenceType;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public IncidenceStructure(String icon){
        this.icon = icon;

    }
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getIncidenceId() {
        return incidenceId;
    }

    public void setIncidenceId(String incidenceId) {
        this.incidenceId = incidenceId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getIncidenceType() {
        return incidenceType;
    }

    public void setIncidenceType(String incidenceType) {
        this.incidenceType = incidenceType;
    }

    public String getAutonomousRegion() {
        return autonomousRegion;
    }

    public void setAutonomousRegion(String autonomousRegion) {
        this.autonomousRegion = autonomousRegion;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
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

    public String getIncidenceLevel() {
        return incidenceLevel;
    }

    public void setIncidenceLevel(String incidenceLevel) {
        this.incidenceLevel = incidenceLevel;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
