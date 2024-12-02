package com.porodkin.timeloggerbot;

public class CurrentSessionEntityResponse {
    Boolean success;
    String workSessionId;
    String message;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getWorkSessionId() {
        return workSessionId;
    }

    public void setWorkSessionId(String workSessionId) {
        this.workSessionId = workSessionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
