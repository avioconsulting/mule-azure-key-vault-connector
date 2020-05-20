package com.avioconsulting.mule.connector.akv.provider.api.client.model;

import com.google.gson.annotations.SerializedName;

public class CertificateSecretProperties {

  @SerializedName(value = "contentType")
  private String contentType;

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  @Override
  @SuppressWarnings("checkstyle:OperatorWrap")
  public String toString() {
    return "CertificateSecretProperties{" +
        "contentType='" + contentType + '\'' +
        '}';
  }
}
