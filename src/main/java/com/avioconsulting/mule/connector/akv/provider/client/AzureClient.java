package com.avioconsulting.mule.connector.akv.provider.client;

import com.avioconsulting.mule.connector.akv.provider.client.model.OAuthToken;
import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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

  private static final Logger LOGGER = LoggerFactory.getLogger(AzureClient.class);

  public static final String PARAM_GRANT_TYPE = "grant_type";
  public static final String PARAM_CLIENT_ID = "client_id";
  public static final String PARAM_CLIENT_SECRET = "client_secret";
  public static final String PARAM_SCOPE = "scope";
  public static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
  public static final String SCOPE = "https://vault.azure.net/.default";
  public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
  public static final String HTTP_CONTENT_TYPE = "Content-Type";
  public static final String AUTH_ENDPOINT = "/oauth2/v2.0/token";
  public static final String AUTH_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer ";
  public static final Integer DEFAULT_TIMEOUT = 30000;


  private final HttpClient httpClient;
  private final String baseUri;
  private final String tenantId;
  private final String clientId;
  private final String clientSecret;
  private OAuthToken token;
  private final Integer timeout;

  public AzureClient(HttpClient httpClient, String baseUri, String tenantId, String clientId,
      String clientSecret, Integer timeout) {
    this.httpClient = httpClient;
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


  private void authenticate() {
    Map<String, Object> params = new HashMap<>();
    params.put(PARAM_GRANT_TYPE, GRANT_TYPE_CLIENT_CREDENTIALS);
    params.put(PARAM_CLIENT_ID, clientId);
    params.put(PARAM_CLIENT_SECRET, clientSecret);
    params.put(PARAM_SCOPE, SCOPE);

    String body = mapToUrlParams(params);
    ByteArrayHttpEntity entity = new ByteArrayHttpEntity(body.getBytes(StandardCharsets.UTF_8));
    HttpRequest request = HttpRequest.builder().
        uri(baseUri + tenantId + AUTH_ENDPOINT).
        method(HttpConstants.Method.POST).
        addHeader(HTTP_CONTENT_TYPE, APPLICATION_X_WWW_FORM_URLENCODED).entity(entity).build();
//        System.out.println("Auth Request: " + request.toString());

    HttpRequestOptions requestOptions = HttpRequestOptions.builder().responseTimeout(timeout)
        .followsRedirect(false).build();
    CompletableFuture<HttpResponse> completable = httpClient.sendAsync(request, requestOptions);
    try {
      HttpResponse response = completable.get();
      Gson gson = new Gson();
      token = gson
          .fromJson(new InputStreamReader(response.getEntity().getContent()), OAuthToken.class);
      token.setExpiresOn();
      LOGGER.info(token.toString());
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
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
    for (String k : map.keySet()) {
      if (params.length() > 0) {
        params = params + "&" + k + "=" + map.get(k).toString();
      } else {
        params = k + "=" + map.get(k).toString();
      }
    }
    return params;
  }

  public boolean isValid() {
    if (!token.isValid()) {
      LOGGER.info("Access Token expired, re-authenticating.");
      authenticate();
      return token.isValid();
    } else {
      return true;
    }
  }
}
