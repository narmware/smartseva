package com.narmware.smartseva.pojo;

/**
 * Created by rohitsavant on 10/05/18.
 */

public class BannerResponse {
    String response;
    BannerImages[] data;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public BannerImages[] getData() {
        return data;
    }

    public void setData(BannerImages[] data) {
        this.data = data;
    }
}
