package com.avioconsulting.mule.connector.akv.provider.client.model;

import com.google.gson.annotations.SerializedName;

public class Error {

    @SerializedName(value = "code")
    private String code;

    @SerializedName(value = "message")
    private String message;

    @SerializedName(value = "innerError")
    private Error innerError;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Error getInnerError() {
        return innerError;
    }

    public void setInnerError(Error innerError) {
        this.innerError = innerError;
    }

    @Override
    public String toString() {
        return "Error{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", innerError=" + innerError +
                '}';
    }
}
