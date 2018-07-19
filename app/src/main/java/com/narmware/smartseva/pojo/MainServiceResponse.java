package com.narmware.smartseva.pojo;

/**
 * Created by rohitsavant on 10/05/18.
 */

public class MainServiceResponse {
    ServiceMain[] result;

    public ServiceMain[] getData() {
        return result;
    }

    public void setData(ServiceMain[] data) {
        this.result = data;
    }
}
