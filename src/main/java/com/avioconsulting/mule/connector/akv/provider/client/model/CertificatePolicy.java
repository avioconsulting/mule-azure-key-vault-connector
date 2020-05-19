package com.avioconsulting.mule.connector.akv.provider.client.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CertificatePolicy {

  @SerializedName(value = "id")
  private String id;

  @SerializedName(value = "secret_props")
  private CertificateSecretProperties secretProperties;

  @SerializedName(value = "attributes")
  private EntityAttributes attributes;

  @SerializedName(value = "key_props")
  private CertificateKeyProperties keyProperties;

  @SerializedName(value = "x509_props")
  private CertificateX509Properties x509Properties;

  @SerializedName(value = "lifetime_actions")
  private List<CertificateLifetimeAction> lifetimeAction;

  @SerializedName(value = "issuer")
  private CertificateIssuer issuer;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public CertificateSecretProperties getSecretProperties() {
    return secretProperties;
  }

  public void setSecretProperties(CertificateSecretProperties secretProperties) {
    this.secretProperties = secretProperties;
  }

  public EntityAttributes getAttributes() {
    return attributes;
  }

  public void setAttributes(EntityAttributes attributes) {
    this.attributes = attributes;
  }

  public CertificateKeyProperties getKeyProperties() {
    return keyProperties;
  }

  public void setKeyProperties(CertificateKeyProperties keyProperties) {
    this.keyProperties = keyProperties;
  }

  public CertificateX509Properties getX509Properties() {
    return x509Properties;
  }

  public void setX509Properties(CertificateX509Properties x509Properties) {
    this.x509Properties = x509Properties;
  }

  public List<CertificateLifetimeAction> getLifetimeAction() {
    return lifetimeAction;
  }

  public void setLifetimeAction(List<CertificateLifetimeAction> lifetimeAction) {
    this.lifetimeAction = lifetimeAction;
  }

  public CertificateIssuer getIssuer() {
    return issuer;
  }

  public void setIssuer(CertificateIssuer issuer) {
    this.issuer = issuer;
  }

  @Override
  @SuppressWarnings("checkstyle:OperatorWrap")
  public String toString() {
    return "CertificatePolicy{" +
        "id='" + id + '\'' +
        ", secretProperties=" + secretProperties +
        ", attributes=" + attributes +
        ", keyProperties=" + keyProperties +
        ", x509Properties=" + x509Properties +
        ", lifetimeAction=" + lifetimeAction +
        ", issuer=" + issuer +
        '}';
  }
}
