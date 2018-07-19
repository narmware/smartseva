package com.narmware.smartseva.pojo;

/**
 * Created by rohitsavant on 10/05/18.
 */

public class SubServiceResponse {
    SubServices[] result;

    public SubServices[] getData() {
        return result;
    }

    public void setData(SubServices[] data) {
        this.result = data;
    }
}
