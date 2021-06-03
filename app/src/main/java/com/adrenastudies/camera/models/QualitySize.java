package com.adrenastudies.camera.models;

public class QualitySize {

    private Integer width;
    private Integer height;
    private int qualityProfile;

    public QualitySize(Integer width, Integer height, int qualityProfile) {
        this.width = width;
        this.height = height;
        this.qualityProfile = qualityProfile;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWith(Integer with) {
        this.width = with;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public int getQualityProfile() {
        return qualityProfile;
    }

    public void setQualityProfile(int qualityProfile) {
        this.qualityProfile = qualityProfile;
    }
}
