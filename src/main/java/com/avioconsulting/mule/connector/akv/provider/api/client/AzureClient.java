package com.avioconsulting.mule.connector.akv.provider.api.client;

import com.avioconsulting.mule.connector.akv.provider.api.client.model.OAuthToken;
import com.avioconsulting.mule.connector.akv.provider.api.error.AccessDeniedException;
import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.mule.runtime.api.exception.DefaultMuleException;
import org.mule.runtime.http.api.HttpConstants;
import org.mule.runtime.http.api.client.HttpClient;
import org.mule.runtime.http.api.client.HttpRequestOptions;
import org.mule.runtime.http.api.client.HttpRequestOptionsBuilder;
import org.mule.runtime.http.api.domain.entity.ByteArrayHttpEntity;
import org.mule.runtime.http.api.domain.message.request.HttpRequest;
import org.mule.runtime.http.api.domain.message.request.HttpRequestBuilder;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AzureClient {

  public static final String PARAM_GRANT_TYPE = "grant_type";
  public static final String PARAM_CLIENT_ID = "client_id";
  public static final String PARAM_CLIENT_SECRET = "client_secret";
  public static final String PARAM_SCOPE = "scope";
  public static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
  public static final String SCOPE = "https://vault.azure.net/.default";
  public static final String APPLICATION_X_WWW_FORM_URLENCODED =
          "application/x-www-form-urlencoded";
  public static final String HTTP_CONTENT_TYPE = "Content-Type";
  public static final String AUTH_ENDPOINT = "/oauth2/v2.0/token";
  public static final String AUTH_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer ";
  public static final Integer DEFAULT_TIMEOUT = 30000;
  private static final Logger LOGGER = LoggerFactory.getLogger(AzureClient.class);
  public static final String AZURE_HOST = ".vault.azure.net";

  private final HttpClient httpClient;
  private final String vaultName;
  private final String baseUri;
  private final String tenantId;
  private final String clientId;
  private final String clientSecret;
  private final Integer timeout;
  private OAuthToken token;

  /**
   * Provides a client object, used for retrieving entities from Azure Key Vault.
   *
   * @param httpClient      Mule Client for sending the HTTP Request and receiving the response
   * @param vaultName       Azure vault name
   * @param baseUri         Authorization Base URL for Azure
   * @param tenantId        Azure Tenant ID
   * @param clientId        Azure Client ID
   * @param clientSecret    Azure Client Secret
   * @param timeout         Request timeout in ms, default 30000
   */
  public AzureClient(HttpClient httpClient, String vaultName, String baseUri, String tenantId,
                     String clientId, String clientSecret, Integer timeout)
          throws AccessDeniedException, DefaultMuleException {
    this.httpClient = httpClient;
    this.vaultName = vaultName;
    this.baseUri = baseUri;
    this.tenantId = tenantId;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    if (timeout == null) {
      this.timeout = DEFAULT_TIMEOUT;
    } else {
      this.timeout = timeout;
    }
    authenticate();
  }


  private void authenticate() throws DefaultMuleException {
    Map<String, Object> params = new HashMap<>();
    params.put(PARAM_GRANT_TYPE, GRANT_TYPE_CLIENT_CREDENTIALS);
    params.put(PARAM_CLIENT_ID, clientId);
    params.put(PARAM_CLIENT_SECRET, clientSecret);
    params.put(PARAM_SCOPE, SCOPE);

    String body = mapToUrlParams(params);
    ByteArrayHttpEntity entity = new ByteArrayHttpEntity(body.getBytes(StandardCharsets.UTF_8));
    HttpRequest request = HttpRequest.builder()
        .uri(getAuthBaseUri() + tenantId + AUTH_ENDPOINT)
        .method(HttpConstants.Method.POST)
        .addHeader(HTTP_CONTENT_TYPE, APPLICATION_X_WWW_FORM_URLENCODED).entity(entity).build();

    HttpRequestOptions requestOptions = HttpRequestOptions.builder().responseTimeout(timeout)
        .followsRedirect(false).build();
    CompletableFuture<HttpResponse> completable = httpClient.sendAsync(request, requestOptions);
    try {
      HttpResponse response = completable.get();
      if (response.getStatusCode() == 200) {
        Gson gson = new Gson();
        token = gson
                .fromJson(new InputStreamReader(response.getEntity().getContent(), "UTF-8"),
                        OAuthToken.class);
        token.setExpiresOn();
        LOGGER.info(token.toString());
      } else {
        throw new AccessDeniedException("Failed to authenticate.  "
                + "Authentication service returned status code: "
                + response.getStatusCode());
      }
    } catch (InterruptedException | ExecutionException | UnsupportedEncodingException e) {
      e.printStackTrace();
      throw new DefaultMuleException(e.getMessage());
    }
  }

  protected HttpClient getHttpClient() {
    return httpClient;
  }

  protected HttpRequestBuilder getAuthenticatedHttpRequestBuilder() {
    return HttpRequest.builder().addHeader(AUTH_HEADER, BEARER_PREFIX + token.getAccessToken());
  }

  protected HttpRequestOptionsBuilder getHttpRequestOptionsBuilder() {
    return HttpRequestOptions.builder().responseTimeout(timeout).followsRedirect(false);
  }

  protected String mapToUrlParams(Map<String, Object> map) {
    String params = "";
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      if (params.length() > 0) {
        params = params + "&" + entry.getKey() + "=" + entry.getValue().toString();
      } else {
        params = entry.getKey() + "=" + entry.getValue().toString();
      }
    }
    return params;
  }

  /**
   * Validation method that verifies authentication.
   *
   * @return Boolean for valid client configuration
   */
  public boolean isValid() {
    if (!token.isValid()) {
      try {
        LOGGER.info("Access Token expired, re-authenticating.");
        authenticate();
        return token.isValid();
      } catch (DefaultMuleException e) {
        return false;
      }
    } else {
      return true;
    }
  }

  /**
   * Builds a base URL for Key Vault HTTP operations.  This value can be overridden using
   * system property AKV_TEST_URL.
   *
   * @return String URL for Key Vault HTTP operations
   */
  public String getHttpBaseUri() {
    String url = System.getProperty("AKV_TEST_URL");
    if (url != null && url.length() > 0) {
      LOGGER.warn("Using AKV Test URL: " + url);
      return url;
    }
    return "https://" + vaultName + AZURE_HOST;
  }

  /**
   * Builds a Authentication base URL.  This value can be overridden using system
   * property AKV_TEST_AUTH_URL.
   *
   * @return String URL for Key Vault HTTP operations
   */
  public String getAuthBaseUri() {
    String url = System.getProperty("AKV_TEST_AUTH_URL");
    if (url != null && url.length() > 0) {
      LOGGER.warn("Using AKV Test Auth URL: " + url);
      return url;
    }
    return baseUri;
  }
}
