package com.timetracker.Entities;


public enum Status {
    ACTIVE("Active"),
    NEW("New"),
    REFUSED("Refused"),
    FROZEN("Frozen"),
    COMPLETE("Complete");   
    

    private String status;

    Status(final String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

}
