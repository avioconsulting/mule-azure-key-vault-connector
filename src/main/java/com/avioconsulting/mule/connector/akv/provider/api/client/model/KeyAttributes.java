package com.avioconsulting.mule.connector.akv.provider.api.client.model;

import com.google.gson.annotations.SerializedName;

public class KeyAttributes {

  @SerializedName(value = "created")
  private Long created;

  @SerializedName(value = "enabled")
  private Boolean enabled;

  @SerializedName(value = "exp")
  private Long expiry;

  @SerializedName(value = "nbf")
  private Long notBefore;

  @SerializedName(value = "updated")
  private Long lastUpdated;

  @SerializedName(value = "recoveryLevel")
  private String recoveryLevel;

  public Long getCreated() {
    return created;
  }

  public void setCreated(Long created) {
    this.created = created;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public Long getExpiry() {
    return expiry;
  }

  public void setExpiry(Long expiry) {
    this.expiry = expiry;
  }

  public Long getNotBefore() {
    return notBefore;
  }

  public void setNotBefore(Long notBefore) {
    this.notBefore = notBefore;
  }

  public Long getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Long lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public String getRecoveryLevel() {
    return recoveryLevel;
  }

  public void setRecoveryLevel(String recoveryLevel) {
    this.recoveryLevel = recoveryLevel;
  }

  @Override
  @SuppressWarnings("checkstyle:OperatorWrap")
  public String toString() {
    return "KeyAttributes{" +
        "created=" + created +
        ", enabled=" + enabled +
        ", expiry=" + expiry +
        ", notBefore=" + notBefore +
        ", lastUpdated=" + lastUpdated +
        ", recoveryLevel='" + recoveryLevel + '\'' +
        '}';
  }
}
