package com.avioconsulting.mule.connector.akv.provider.api.client.model;

import com.google.gson.annotations.SerializedName;

public class Certificate {

  @SerializedName(value = "id")
  private String id;

  @SerializedName(value = "kid")
  private String kid;

  @SerializedName(value = "sid")
  private String sid;

  @SerializedName(value = "x5t")
  private String x5t;

  @SerializedName(value = "cer")
  private String certificate;

  @SerializedName(value = "contentType")
  private String contentType;

  @SerializedName(value = "cancellation_requested")
  private Boolean cancellationRequested;

  @SerializedName(value = "status")
  private String status;

  @SerializedName(value = "target")
  private String target;

  @SerializedName(value = "request_id")
  private String requestId;

  @SerializedName(value = "attributes")
  private EntityAttributes attributes;

  @SerializedName(value = "policy")
  private CertificatePolicy policy;

  public String getX5t() {
    return x5t;
  }

  public void setX5t(String x5t) {
    this.x5t = x5t;
  }

  public String getCertificate() {
    return certificate;
  }

  public void setCertificate(String certificate) {
    this.certificate = certificate;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public Boolean getCancellationRequested() {
    return cancellationRequested;
  }

  public void setCancellationRequested(Boolean cancellationRequested) {
    this.cancellationRequested = cancellationRequested;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public EntityAttributes getAttributes() {
    return attributes;
  }

  public void setAttributes(EntityAttributes attributes) {
    this.attributes = attributes;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getKid() {
    return kid;
  }

  public void setKid(String kid) {
    this.kid = kid;
  }

  public String getSid() {
    return sid;
  }

  public void setSid(String sid) {
    this.sid = sid;
  }

  public CertificatePolicy getPolicy() {
    return policy;
  }

  public void setPolicy(CertificatePolicy policy) {
    this.policy = policy;
  }

  @Override
  @SuppressWarnings("checkstyle:OperatorWrap")
  public String toString() {
    return "Certificate{" +
        "id='" + id + '\'' +
        ", kid='" + kid + '\'' +
        ", sid='" + sid + '\'' +
        ", x5t='" + x5t + '\'' +
        ", certificate='" + certificate + '\'' +
        ", contentType='" + contentType + '\'' +
        ", cancellationRequested=" + cancellationRequested +
        ", status='" + status + '\'' +
        ", target='" + target + '\'' +
        ", requestId='" + requestId + '\'' +
        ", attributes=" + attributes +
        ", policy=" + policy +
        '}';
  }
}
