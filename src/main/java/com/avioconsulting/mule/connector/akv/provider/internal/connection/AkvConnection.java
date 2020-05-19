package com.avioconsulting.mule.connector.akv.provider.internal.connection;

import com.avioconsulting.mule.connector.akv.provider.client.AzureKeyVaultClient;
import com.avioconsulting.mule.connector.akv.provider.client.model.Certificate;
import com.avioconsulting.mule.connector.akv.provider.client.model.Key;
import com.avioconsulting.mule.connector.akv.provider.client.model.Secret;
import org.mule.runtime.http.api.client.HttpClient;

/**
 * This class represents an extension connection just as example (there is no real connection with
 * anything here c:).
 */
public final class AkvConnection {

  private HttpClient httpClient;
  private String vaultName;
  private String baseUri;
  private String tenantId;
  private String clientId;
  private String clientSecret;
  private final Integer timeout;
  private String id;
  private AzureKeyVaultClient client;


  public AkvConnection(HttpClient httpClient, String vaultName, String baseUri, String tenantId, String clientId,
      String clientSecret, Integer timeout) {
    this.httpClient = httpClient;
    this.vaultName = vaultName;
    this.baseUri = baseUri;
    this.tenantId = tenantId;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.timeout = timeout;
    this.id = null;
    initAkvClient();
  }

  public void disconnect() {
    this.client = null;
  }

  public Boolean isValid() {
    return client != null && client.isValid();
  }

  public void initAkvClient() {
    client = new AzureKeyVaultClient(httpClient, vaultName, baseUri, tenantId, clientId, clientSecret,
        timeout);
  }

  public Secret getSecret(String path) {
    return client.getSecret(path);
  }

  public Key getKey(String path) {
    return client.getKey(path);
  }

  public Certificate getCertificate(String path) {
    return client.getCertificate(path);
  }

  public HttpClient getHttpClient() {
    return httpClient;
  }

  public void setHttpClient(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  public String getBaseUri() {
    return baseUri;
  }

  public void setBaseUri(String baseUri) {
    this.baseUri = baseUri;
  }

  public String getVaultName() {
    return vaultName;
  }

  public void setVaultName(String vaultName) {
    this.vaultName = vaultName;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
