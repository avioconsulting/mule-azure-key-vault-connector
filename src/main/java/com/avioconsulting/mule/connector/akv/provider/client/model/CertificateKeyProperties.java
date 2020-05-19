package com.avioconsulting.mule.connector.akv.provider.client.model;

import com.google.gson.annotations.SerializedName;

public class CertificateKeyProperties {

  @SerializedName(value = "exportable")
  private Boolean exportable;
  @SerializedName(value = "kty")
  private String keyType;
  @SerializedName(value = "crv")
  private String keyCurve;
  @SerializedName(value = "key_size")
  private Integer keySize;
  @SerializedName(value = "reuse_key")
  private Boolean reuseKey;

  public Boolean getExportable() {
    return exportable;
  }

  public void setExportable(Boolean exportable) {
    this.exportable = exportable;
  }

  public String getKeyType() {
    return keyType;
  }

  public void setKeyType(String keyType) {
    this.keyType = keyType;
  }

  public String getKeyCurve() {
    return keyCurve;
  }

  public void setKeyCurve(String keyCurve) {
    this.keyCurve = keyCurve;
  }

  public Integer getKeySize() {
    return keySize;
  }

  public void setKeySize(Integer keySize) {
    this.keySize = keySize;
  }

  public Boolean getReuseKey() {
    return reuseKey;
  }

  public void setReuseKey(Boolean reuseKey) {
    this.reuseKey = reuseKey;
  }

  @Override
  @SuppressWarnings("checkstyle:OperatorWrap")
  public String toString() {
    return "{ " +
        "exportable : " + exportable + ", " +
        "key_size : " + keySize + ", " +
        "crv : " + keyCurve + ", " +
        "kty : " + keyType + ", " +
        "reuse_key : " + reuseKey +
        " }";
  }
}
