package com.narmware.smartseva.pojo;

/**
 * Created by rohitsavant on 08/05/18.
 */

public class ServiceMain {
    String service_name;
    String service_id;
    String service_image;
    String service_desc;

    public ServiceMain(String service_title, String service_id,String service_image) {
        this.service_name = service_title;
        this.service_id = service_id;
        this.service_image=service_image;
    }

    public String getService_title() {
        return service_name;
    }

    public void setService_title(String service_title) {
        this.service_name = service_title;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_image() {
        return service_image;
    }

    public void setService_image(String service_image) {
        this.service_image = service_image;
    }

    public String getService_desc() {
        return service_desc;
    }

    public void setService_desc(String service_desc) {
        this.service_desc = service_desc;
    }
}
