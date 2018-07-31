package com.yjkmust.fourleafclover.model;

/**
 * Created by 11432 on 2018/7/31.
 */

public class AdvertisingModel {
    private int id;
    private String imgUrl;
    private String adverUrl;//跳转到广告的网页链接

    public AdvertisingModel(int id, String imgUrl) {
        this.id = id;
        this.imgUrl = imgUrl;
    }

    public AdvertisingModel(int id, String imgUrl, String adverUrl) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.adverUrl = adverUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAdverUrl() {
        return adverUrl;
    }

    public void setAdverUrl(String adverUrl) {
        this.adverUrl = adverUrl;
    }
}
