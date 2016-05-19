package com.timetracker.Service.AncillaryServices;

import java.util.HashMap;
import java.util.Map;

public class ResponseMessage {

    private boolean status;
    private String message;


    Map<String,Object> responseObjects;

    public ResponseMessage(){}


    public void addResponseObject(String objectName,Object object){
        if(responseObjects==null)
            responseObjects=new HashMap<>();
        responseObjects.put(objectName,object);
    }



    public ResponseMessage(String message){
        this.message=message;
    }

    public ResponseMessage(boolean status){
        this.status=status;
    }

    public ResponseMessage(boolean status,String message){
        this.status=status;
        this.message=message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean result) {
        this.status = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getResponseObjects() {
        return responseObjects;
    }

    public void setResponseObjects(Map<String, Object> responseObjects) {
        this.responseObjects = responseObjects;
    }
}
