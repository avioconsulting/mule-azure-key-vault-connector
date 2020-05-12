package com.avioconsulting.mule.connector.akv.provider.client.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class OAuthError {

    @SerializedName(value = "error")
    private String error;

    @SerializedName(value = "error_description")
    private String errorDescription;

    @SerializedName(value = "error_Codes")
    private List<Integer> errorCodes;

    @SerializedName(value = "timestamp")
    private Date timestamp;

    @SerializedName(value = "trace_id")
    private String traceId;

    @SerializedName(value = "correlation_id")
    private String correlationId;

    @SerializedName(value = "error_uri")
    private String errorUri;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public List<Integer> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(List<Integer> errorCodes) {
        this.errorCodes = errorCodes;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getErrorUri() {
        return errorUri;
    }

    public void setErrorUri(String errorUri) {
        this.errorUri = errorUri;
    }

    @Override
    public String toString() {
        return "OAuthError{" +
                "error='" + error + '\'' +
                ", errorDescription='" + errorDescription + '\'' +
                ", errorCodes=" + errorCodes +
                ", timestamp=" + timestamp +
                ", traceId='" + traceId + '\'' +
                ", correlationId='" + correlationId + '\'' +
                ", errorUri='" + errorUri + '\'' +
                '}';
    }
}
