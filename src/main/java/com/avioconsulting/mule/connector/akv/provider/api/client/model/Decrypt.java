package com.avioconsulting.mule.connector.akv.provider.api.client.model;

import com.google.gson.annotations.SerializedName;

public class Decrypt {

    @SerializedName(value = "kid")
    private String kid;

    @SerializedName(value = "value")
    private String value;

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    @SuppressWarnings("checkstyle:OperatorWrap")
    public String toString() {
        return "{\"kid\": " + '"' + kid + "\" , \"value\": " + '"' + value + "\"}";
    }
}
