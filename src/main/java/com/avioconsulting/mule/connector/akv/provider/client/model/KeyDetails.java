package com.avioconsulting.mule.connector.akv.provider.client.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class KeyDetails {

  @SerializedName(value = "kid")
  private String kid;

  @SerializedName(value = "kty")
  private String keyType;

  @SerializedName(value = "key_ops")
  private List<String> keyOps;

  @SerializedName(value = "n")
  private String rsaModulus;

  @SerializedName(value = "e")
  private String rsaPublicExponent;

  @SerializedName(value = "enabled")
  private Boolean enabled;

  @SerializedName(value = "created")
  private Integer created;

  @SerializedName(value = "updated")
  private Integer updated;

  @SerializedName(value = "recoveryLevel")
  private String recoveryLevel;

  public String getKid() {
    return kid;
  }

  public void setKid(String kid) {
    this.kid = kid;
  }

  public String getKeyType() {
    return keyType;
  }

  public List<String> getKeyOps() {
    return keyOps;
  }

  public void setKeyOps(List<String> keyOps) {
    this.keyOps = keyOps;
  }

  public void setKeyType(String keyType) {
    this.keyType = keyType;
  }

  public String getRsaModulus() {
    return rsaModulus;
  }

  public void setRsaModulus(String rsaModulus) {
    this.rsaModulus = rsaModulus;
  }

  public String getRsaPublicExponent() {
    return rsaPublicExponent;
  }

  public void setRsaPublicExponent(String rsaPublicExponent) {
    this.rsaPublicExponent = rsaPublicExponent;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public Integer getCreated() {
    return created;
  }

  public void setCreated(Integer created) {
    this.created = created;
  }

  public Integer getUpdated() {
    return updated;
  }

  public void setUpdated(Integer updated) {
    this.updated = updated;
  }

  public String getRecoveryLevel() {
    return recoveryLevel;
  }

  public void setRecoveryLevel(String recoveryLevel) {
    this.recoveryLevel = recoveryLevel;
  }

  @Override
  public String toString() {
    return "{" +
        "key{" +
        "kid='" + kid + '\'' +
        ", kty='" + keyType + '\'' +
        ", key_ops='" + keyOps + '\'' +
        ", n='" + rsaModulus + '\'' +
        ", e='" + rsaPublicExponent + '\'' +
        "}" +
        "attributes{" +
        "enabled=" + enabled + '\'' +
        ", created=" + created + '\'' +
        ", updated='" + updated + '\'' +
        ", recoveryLevel=" + recoveryLevel +
        '}' +
        '}';
  }
}
