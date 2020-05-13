package com.avioconsulting.mule.connector.akv.provider.client.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

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
    public String toString() {
        return "CertificateLifetimeAction{" +
                "trigger=" + trigger +
                ", action=" + action +
                '}';
    }
}
