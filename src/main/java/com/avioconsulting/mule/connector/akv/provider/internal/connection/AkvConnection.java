package com.avioconsulting.mule.connector.akv.provider.internal.connection;

import com.avioconsulting.mule.connector.akv.provider.client.AzureKeyVaultClient;
import com.avioconsulting.mule.connector.akv.provider.client.model.Secret;
import org.mule.runtime.http.api.client.HttpClient;

/**
 * This class represents an extension connection just as example (there is no real connection with anything here c:).
 */
public final class AkvConnection {

  private HttpClient httpClient;
  private String baseUri;
  private String tenantId;
  private String clientId;
  private String clientSecret;
  private Integer timeout;
  private String id;
  private AzureKeyVaultClient client;


  public AkvConnection(HttpClient httpClient, String baseUri, String tenantId, String clientId, String clientSecret, Integer timeout) {
    this.httpClient = httpClient;
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
    if(client != null && client.isValid()) {
      return true;
    } else {
      return false;
    }
  }

  public void initAkvClient() {
    client = new AzureKeyVaultClient(httpClient, baseUri, tenantId, clientId, clientSecret, timeout);
  }

  public Secret getSecret(String path) {
    return client.getSecret(path);
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
