package com.avioconsulting.mule.connector.akv.provider.api.client.client.model;

import com.google.gson.annotations.SerializedName;

public class Key {

  @SerializedName(value = "key")
  private KeyDetails key;

  @SerializedName(value = "attributes")
  private KeyAttributes attributes;

  public KeyDetails getKey() {
    return key;
  }

  public void setKey(KeyDetails key) {
    this.key = key;
  }

  public KeyAttributes getAttributes() {
    return attributes;
  }

  public void setAttributes(KeyAttributes attributes) {
    this.attributes = attributes;
  }

  @Override
  @SuppressWarnings("checkstyle:OperatorWrap")
  public String toString() {
    return "{"
        + "key{" + key.toString()
        + '}'
        + "attributes{" + attributes.toString()
        + '}'
        + '}';
  }

}
