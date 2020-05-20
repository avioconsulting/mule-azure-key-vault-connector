package com.avioconsulting.mule.connector.akv.provider.api.client.client.model;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class Secret {

  @SerializedName(value = "contentType")
  private String contentType;

  @SerializedName(value = "id")
  private String id;

  @SerializedName(value = "kid")
  private String kid;

  @SerializedName(value = "managed")
  private Boolean managed;

  @SerializedName(value = "tags")
  private Map<String, String> tags;

  @SerializedName(value = "value")
  private String value;

  @SerializedName(value = "attributes")
  private SecretAttributes attributes;

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
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

  public Boolean getManaged() {
    return managed;
  }

  public void setManaged(Boolean managed) {
    this.managed = managed;
  }

  public Map<String, String> getTags() {
    return tags;
  }

  public void setTags(Map<String, String> tags) {
    this.tags = tags;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public SecretAttributes getAttributes() {
    return attributes;
  }

  public void setAttributes(SecretAttributes attributes) {
    this.attributes = attributes;
  }

  @Override
  @SuppressWarnings("checkstyle:OperatorWrap")
  public String toString() {
    return "Secret{" +
        "contentType='" + contentType + '\'' +
        ", id='" + id + '\'' +
        ", kid='" + kid + '\'' +
        ", managed=" + managed +
        ", tags=" + tags +
        ", value='" + value + '\'' +
        ", attributes=" + attributes +
        '}';
  }
}
