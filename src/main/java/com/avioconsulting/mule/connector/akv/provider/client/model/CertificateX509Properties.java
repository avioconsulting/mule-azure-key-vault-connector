package com.avioconsulting.mule.connector.akv.provider.client.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class CertificateX509Properties {

    @SerializedName(value = "subject")
    private String subject;

    @SerializedName(value = "sans")
    private CertificateSubjectAlternativeNames sans;

    @SerializedName(value = "ekus")
    private List<String> ekus;

    @SerializedName(value = "key_usage")
    private List<String> keyUsage;

    @SerializedName(value = "validity_months")
    private Integer validityMonths;

    @SerializedName(value = "basic_constraints")
    private Map<String,String> basicConstraints;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public CertificateSubjectAlternativeNames getSans() {
        return sans;
    }

    public void setSans(CertificateSubjectAlternativeNames sans) {
        this.sans = sans;
    }

    public List<String> getEkus() {
        return ekus;
    }

    public void setEkus(List<String> ekus) {
        this.ekus = ekus;
    }

    public List<String> getKeyUsage() {
        return keyUsage;
    }

    public void setKeyUsage(List<String> keyUsage) {
        this.keyUsage = keyUsage;
    }

    public Integer getValidityMonths() {
        return validityMonths;
    }

    public void setValidityMonths(Integer validityMonths) {
        this.validityMonths = validityMonths;
    }

    public Map<String, String> getBasicConstraints() {
        return basicConstraints;
    }

    public void setBasicConstraints(Map<String, String> basicConstraints) {
        this.basicConstraints = basicConstraints;
    }

    @Override
    public String toString() {
        return "CertificateX509Properties{" +
                "subject='" + subject + '\'' +
                ", sans=" + sans +
                ", ekus=" + ekus +
                ", keyUsage=" + keyUsage +
                ", validityMonths=" + validityMonths +
                ", basicConstraints=" + basicConstraints +
                '}';
    }
}
