package com.avioconsulting.mule.connector.akv.provider.api.client.client.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CertificateSubjectAlternativeNames {

  @SerializedName(value = "dns_names")
  private List<String> dnsNames;

  @SerializedName(value = "emails")
  private List<String> emails;

  @SerializedName(value = "upns")
  private List<String> upns;

  public List<String> getEmails() {
    return emails;
  }

  public void setEmails(List<String> emails) {
    this.emails = emails;
  }

  public List<String> getUpns() {
    return upns;
  }


  public void setUpns(List<String> upns) {
    this.upns = upns;
  }

  public List<String> getDnsNames() {
    return dnsNames;
  }

  public void setDnsNames(List<String> dnsNames) {
    this.dnsNames = dnsNames;
  }


  @Override
  @SuppressWarnings("checkstyle:OperatorWrap")
  public String toString() {
    return "CertificateSubjectAlternativeNames{" +
        "dnsNames=" + dnsNames +
        ", emails=" + emails +
        ", upns=" + upns +
        '}';
  }
}
