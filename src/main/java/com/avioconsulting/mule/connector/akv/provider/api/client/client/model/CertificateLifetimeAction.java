package com.avioconsulting.mule.connector.akv.provider.api.client.client.model;

import com.google.gson.annotations.SerializedName;

public class CertificateLifetimeAction {

  //TODO create TriggerType - https://docs.microsoft.com/en-us/rest/api/keyvault/getcertificate/getcertificate#trigger
  @SerializedName(value = "trigger")
  private CertificateTrigger trigger;
  //TODO create ActionType - https://docs.microsoft.com/en-us/rest/api/keyvault/getcertificate/getcertificate#action
  @SerializedName(value = "action")
  private CertificateAction action;

  public CertificateTrigger getTrigger() {
    return trigger;
  }

  public void setTrigger(CertificateTrigger trigger) {
    this.trigger = trigger;
  }

  public CertificateAction getAction() {
    return action;
  }

  public void setAction(CertificateAction action) {
    this.action = action;
  }

  @Override
  @SuppressWarnings("checkstyle:OperatorWrap")
  public String toString() {
    return "CertificateLifetimeAction{" +
        "trigger=" + trigger +
        ", action=" + action +
        '}';
  }
}
