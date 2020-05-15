package com.avioconsulting.mule.connector.akv.provider.client.model;

import com.google.gson.annotations.SerializedName;

public class CertificateAction {

  @SerializedName(value = "action_type")
  private String actionType;

  public String getActionType() {
    return actionType;
  }

  public void setActionType(String actionType) {
    this.actionType = actionType;
  }

  @Override
  public String toString() {
    return "CertificateAction{" +
        "actionType='" + actionType + '\'' +
        '}';
  }
}
