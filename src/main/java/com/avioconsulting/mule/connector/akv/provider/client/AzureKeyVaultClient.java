package com.avioconsulting.mule.connector.akv.provider.client;

import com.avioconsulting.mule.connector.akv.provider.api.error.CertificateNotFoundException;
import com.avioconsulting.mule.connector.akv.provider.api.error.KeyNotFoundException;
import com.avioconsulting.mule.connector.akv.provider.api.error.SecretNotFoundException;
import com.avioconsulting.mule.connector.akv.provider.api.error.UnknownKeyVaultException;
import com.avioconsulting.mule.connector.akv.provider.client.model.Certificate;
import com.avioconsulting.mule.connector.akv.provider.client.model.Key;
import com.avioconsulting.mule.connector.akv.provider.client.model.KeyVaultError;
import com.avioconsulting.mule.connector.akv.provider.client.model.Secret;
import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.mule.runtime.http.api.HttpConstants;
import org.mule.runtime.http.api.client.HttpClient;
import org.mule.runtime.http.api.client.HttpRequestOptions;
import org.mule.runtime.http.api.domain.message.request.HttpRequest;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AzureKeyVaultClient extends AzureClient {

  public static final String PARAM_API_VERSION = "api-version";
  public static final String API_VERSION = "7.0";
  public static final String CERTIFICATE_STATUS_PATH = "/completed";
  public static final String BASE_KEY_PATH = "/keys/";
  public static final String BASE_SECRET_PATH = "/secrets/";
  public static final String BASE_CERTIFICATE_PATH = "/certificates/";
  private static final Logger LOGGER = LoggerFactory.getLogger(AzureKeyVaultClient.class);

  public AzureKeyVaultClient(HttpClient httpClient,String vaultName, String baseUri, String tenantId,
      String clientId, String clientSecret, Integer timeout) {
    super(httpClient, vaultName, baseUri, tenantId, clientId, clientSecret, timeout);
  }

  public Secret getSecret(String secretName) {
    HttpRequest request = getAuthenticatedHttpRequestBuilder()
        .uri(getHttpBaseUri() + BASE_SECRET_PATH + secretName)
        .addQueryParam(PARAM_API_VERSION, API_VERSION)
        .method(HttpConstants.Method.GET).build();
    HttpRequestOptions requestOptions = getHttpRequestOptionsBuilder().build();
    CompletableFuture<HttpResponse> completable = getHttpClient()
        .sendAsync(request, requestOptions);
    try {
      HttpResponse response = completable.get();
      Gson gson = new Gson();
      Integer statusCode = response.getStatusCode();
      if (statusCode == 200) {
        Secret secret = gson
            .fromJson(new InputStreamReader(response.getEntity().getContent()), Secret.class);
        LOGGER.info(secret.toString());
        return secret;
      } else {
        KeyVaultError error = gson
            .fromJson(new InputStreamReader(response.getEntity().getContent()),
                KeyVaultError.class);
        if (statusCode == 404) {
          throw new SecretNotFoundException(error.getError().getMessage());
        } else {
          throw new UnknownKeyVaultException(error.getError().getMessage());
        }
      }
    } catch (InterruptedException | ExecutionException e) {
      LOGGER.error("Error retrieving secret at " + secretName, e);
      throw new UnknownKeyVaultException(e.getMessage());
    }
  }

  public Key getKey(String keyName) {
    HttpRequest request = getAuthenticatedHttpRequestBuilder()
        .uri(getHttpBaseUri() + BASE_KEY_PATH + keyName)
        .addQueryParam(PARAM_API_VERSION, API_VERSION)
        .method(HttpConstants.Method.GET)
        .build();
    LOGGER.info("GetKey Request: " + request.toString());
    HttpRequestOptions requestOptions = getHttpRequestOptionsBuilder().build();
    CompletableFuture<HttpResponse> completable = getHttpClient()
        .sendAsync(request, requestOptions);
    try {
      HttpResponse response = completable.get();
      Gson gson = new Gson();
      Integer statusCode = response.getStatusCode();
      if (statusCode == 200) {
        Key key = gson
            .fromJson(new InputStreamReader(response.getEntity().getContent()), Key.class);
        LOGGER.info(key.toString());
        return key;
      } else {
        KeyVaultError error = gson
            .fromJson(new InputStreamReader(response.getEntity().getContent()),
                KeyVaultError.class);
        if (statusCode == 404) {
          throw new KeyNotFoundException(error.getError().getMessage());
        } else {
          throw new UnknownKeyVaultException(error.getError().getMessage());
        }
      }
    } catch (InterruptedException | ExecutionException e) {
      LOGGER.error("Error retrieving Key at " + keyName, e);
      throw new UnknownKeyVaultException(e.getMessage());
    }
  }

  public Certificate getCertificate(String certificateName) {

    HttpRequest request = getAuthenticatedHttpRequestBuilder()
        .uri(getHttpBaseUri() + BASE_CERTIFICATE_PATH + certificateName + CERTIFICATE_STATUS_PATH)
        .addQueryParam(PARAM_API_VERSION, API_VERSION)
        .method(HttpConstants.Method.GET).build();
    LOGGER.info("GetCertificate Request: " + request.toString());
    HttpRequestOptions requestOptions = getHttpRequestOptionsBuilder().build();
    CompletableFuture<HttpResponse> completable = getHttpClient()
        .sendAsync(request, requestOptions);
    try {
      HttpResponse response = completable.get();
      Gson gson = new Gson();
      Integer statusCode = response.getStatusCode();
      LOGGER.info("Found status code: " + statusCode);
      if (statusCode == 200) {
        Certificate certificate = gson
            .fromJson(new InputStreamReader(response.getEntity().getContent()), Certificate.class);
        LOGGER.info(certificate.toString());
        return certificate;
      } else {
        LOGGER.info(response.toString());
        KeyVaultError error = gson
            .fromJson(new InputStreamReader(response.getEntity().getContent()),
                KeyVaultError.class);
        if (statusCode == 404) {
          throw new CertificateNotFoundException(error.getError().getMessage());
        } else {
          throw new UnknownKeyVaultException(error.getError().getMessage());
        }
      }
    } catch (InterruptedException | ExecutionException e) {
      LOGGER.error("Error retrieving Certificate at " + certificateName, e);
      throw new UnknownKeyVaultException(e.getMessage());
    }
  }
}
