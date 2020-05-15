package com.avioconsulting.mule.connector.akv.provider.client.model;

import com.google.gson.annotations.SerializedName;

public class KeyVaultError {

  @SerializedName(value = "error")
  private Error error;

  public Error getError() {
    return error;
  }

  public void setError(Error error) {
    this.error = error;
  }

  @Override
  public String toString() {
    return "KeyVaultError{" + "error=" + error + '}';
  }
}
