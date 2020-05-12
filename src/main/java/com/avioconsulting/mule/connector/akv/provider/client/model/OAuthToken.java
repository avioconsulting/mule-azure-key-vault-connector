package com.avioconsulting.mule.connector.akv.provider.client.model;

import com.google.gson.annotations.SerializedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class OAuthToken {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthToken.class);

    @SerializedName(value = "token_type")
    private String tokenType;

    @SerializedName(value = "expires_in")
    private Long expiresIn;

    @SerializedName(value = "ext_expires_in")
    private Long extExpiresIn;

    @SerializedName(value = "access_token")
    private String accessToken;

    private Long expiresOn;

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Long getExtExpiresIn() {
        return extExpiresIn;
    }

    public void setExtExpiresIn(Long extExpiresIn) {
        this.extExpiresIn = extExpiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setExpiresOn() {
        if(expiresIn != null) {
            this.expiresOn = Instant.now().getEpochSecond() + (expiresIn - 10);
        }
    }

    @Override
    public String toString() {
        return "OAuthToken{" +
                "tokenType='" + tokenType + '\'' +
                ", expiresIn='" + expiresIn + '\'' +
                ", expiresOn='" + expiresOn + '\'' +
                ", extExpiresIn='" + extExpiresIn + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }

    public Boolean isValid() {
        Long now = Instant.now().getEpochSecond();
        if(expiresOn == null) {
            return false;
        }

        if(now < expiresOn) {
            return true;
        } else {
            return false;
        }
    }
}
