package com.avioconsulting.mule.connector.akv.provider.internal.connection;

import com.avioconsulting.mule.connector.akv.provider.api.client.AzureKeyVaultClient;
import com.avioconsulting.mule.connector.akv.provider.api.client.model.Certificate;
import com.avioconsulting.mule.connector.akv.provider.api.client.model.Key;
import com.avioconsulting.mule.connector.akv.provider.api.client.model.Secret;
import org.mule.runtime.api.exception.DefaultMuleException;
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


  /**
   * Provides an Azure Key Vault connection object for retrieving secrets, keys, and certificates.
   *
   * @param httpClient      Mule Client for sending the HTTP Request and receiving the response
   * @param vaultName       Azure vault name
   * @param baseUri         Authorization Base URL for Azure
   * @param tenantId        Azure Tenant ID
   * @param clientId        Azure Client ID
   * @param clientSecret    Azure Client Secret
   * @param timeout         Request timeout in ms, default 30000
   */
  public AkvConnection(HttpClient httpClient, String vaultName, String baseUri, String tenantId,
                       String clientId, String clientSecret, Integer timeout)
          throws DefaultMuleException {
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

  public void initAkvClient() throws DefaultMuleException {
    client = new AzureKeyVaultClient(httpClient, vaultName, baseUri, tenantId,
            clientId, clientSecret, timeout);
  }

  public Secret getSecret(String path) throws DefaultMuleException {
    return client.getSecret(path);
  }

  public Key getKey(String path) throws DefaultMuleException {
    return client.getKey(path);
  }

  public Certificate getCertificate(String path) throws DefaultMuleException {
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
