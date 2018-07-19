package com.narmware.smartseva.pojo;

/**
 * Created by rohitsavant on 25/05/18.
 */

public class MyBookingsResponse {
    String response;
    MyBookings[] data;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public MyBookings[] getData() {
        return data;
    }

    public void setData(MyBookings[] data) {
        this.data = data;
    }
}
