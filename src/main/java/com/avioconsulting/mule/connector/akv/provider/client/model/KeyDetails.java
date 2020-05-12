package com.avioconsulting.mule.connector.akv.provider.client.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class KeyDetails {

    @SerializedName(value = "kid")
    private String kid;

    @SerializedName(value = "kty")
    private String kty;

    //TODO Array of Strings

    //@SerializedName(value = "key_ops")
    //private Map<String,String> key_ops;

    @SerializedName(value = "n")
    private String n;

    @SerializedName(value = "e")
    private String e;

    @SerializedName(value = "enabled")
    private Boolean enabled;

    @SerializedName(value = "created")
    private Integer created;

    @SerializedName(value = "updated")
    private Integer updated;

    @SerializedName(value = "recoveryLevel")
    private String recoveryLevel;

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getKty() {
        return kty;
    }

    public void setKty(String kty) {
        this.kty = kty;
    }

   // public Map<String, String> getKey_ops() {
  //      return key_ops;
  //  }

   // public void setKey_ops(Map<String, String> key_ops) {
   //     this.key_ops = key_ops;
   // }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public Integer getUpdated() {
        return updated;
    }

    public void setUpdated(Integer updated) {
        this.updated = updated;
    }

    public String getRecoveryLevel() {
        return recoveryLevel;
    }

    public void setRecoveryLevel(String recoveryLevel) {
        this.recoveryLevel = recoveryLevel;
    }

    @Override
    public String toString(){
        return "{" +
                "key{" +
                "kid='" + kid + '\'' +
                ", kty='" + kty + '\'' +
                //", key_ops='" + key_ops + '\'' +
                ", n='" + n + '\'' +
                ", e='" + e + '\'' +
                "}" +
                "attributes{" +
                "enabled=" + enabled + '\'' +
                ", created=" + created + '\'' +
                ", updated='" + updated + '\'' +
                ", recoveryLevel=" + recoveryLevel +
                '}' +
                '}';
    }
}
