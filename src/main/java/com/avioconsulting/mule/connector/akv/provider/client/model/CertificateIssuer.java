package com.avioconsulting.mule.connector.akv.provider.client.model;

import com.google.gson.annotations.SerializedName;

public class CertificateIssuer {

  @SerializedName(value = "cty")
  private String certificateType;

  @SerializedName(value = "cert_transparency")
  private Boolean certTransparency;

  @SerializedName(value = "name")
  private String name;

  public String getCertificateType() {
    return certificateType;
  }

  public void setCertificateType(String certificateType) {
    this.certificateType = certificateType;
  }

  public Boolean getCertTransparency() {
    return certTransparency;
  }

  public void setCertTransparency(Boolean certTransparency) {
    this.certTransparency = certTransparency;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "CertificateIssuer{" +
        "certificateType='" + certificateType + '\'' +
        ", certTransparency=" + certTransparency +
        ", name='" + name + '\'' +
        '}';
  }
}
