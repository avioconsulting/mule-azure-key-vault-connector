package com.avioconsulting.mule.connector.akv.provider.api.client;

import com.avioconsulting.mule.connector.akv.provider.api.client.model.*;
import com.avioconsulting.mule.connector.akv.provider.api.error.CertificateNotFoundException;
import com.avioconsulting.mule.connector.akv.provider.api.error.KeyNotFoundException;
import com.avioconsulting.mule.connector.akv.provider.api.error.SecretNotFoundException;
import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.mule.runtime.api.exception.DefaultMuleException;
import org.mule.runtime.http.api.HttpConstants;
import org.mule.runtime.http.api.client.HttpClient;
import org.mule.runtime.http.api.client.HttpRequestOptions;
import org.mule.runtime.http.api.domain.entity.ByteArrayHttpEntity;
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
  public static final String BASE_ENCRYPT_PATH = "/encrypt";
  public static final String BASE_DECRYPT_PATH = "/decrypt";
  public static final String HTTP_CONTENT_TYPE = "Content-Type";
  public static final String APPLICATION_JSON = "application/json";
  private static final Logger LOGGER = LoggerFactory.getLogger(AzureKeyVaultClient.class);

  public AzureKeyVaultClient(HttpClient httpClient, String vaultName, String baseUri,
                             String tenantId, String clientId, String clientSecret,
                             Integer timeout) throws DefaultMuleException {
    super(httpClient, vaultName, baseUri, tenantId, clientId, clientSecret, timeout);
  }

  /**
   * Retrieves a secret out of Azure Key Vault.
   *
   * @param secretName    Name of the secret
   * @return Secret       Object containing the secret and associated metadata
   */
  public Secret getSecret(String secretName) throws DefaultMuleException {
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
            .fromJson(new InputStreamReader(response.getEntity().getContent(), "UTF-8"),
                    Secret.class);
        LOGGER.info(secret.toString());
        return secret;
      } else {
        KeyVaultError error = gson
            .fromJson(new InputStreamReader(response.getEntity().getContent(), "UTF-8"),
                KeyVaultError.class);
        if (statusCode == 404) {
          throw new SecretNotFoundException(error.getError().getMessage());
        } else {
          throw new DefaultMuleException(error.getError().getMessage());
        }
      }
    } catch (InterruptedException | ExecutionException | UnsupportedEncodingException e) {
      LOGGER.error("Error retrieving secret at " + secretName, e);
      throw new DefaultMuleException("Error retrieving secret at " + secretName, e);
    }
  }

  /**
   * Retrieves a key out of Azure Key Vault.
   *
   * @param keyName    Name of the key
   * @return Key       Object containing the key and associated metadata
   */
  public Key getKey(String keyName) throws DefaultMuleException {
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
            .fromJson(new InputStreamReader(response.getEntity().getContent(), "UTF-8"), Key.class);
        LOGGER.info(key.toString());
        return key;
      } else {
        KeyVaultError error = gson
            .fromJson(new InputStreamReader(response.getEntity().getContent(), "UTF-8"),
                KeyVaultError.class);
        if (statusCode == 404) {
          throw new KeyNotFoundException(error.getError().getMessage());
        } else {
          throw new DefaultMuleException(error.getError().getMessage());
        }
      }
    } catch (InterruptedException | ExecutionException | UnsupportedEncodingException e) {
      LOGGER.error("Error retrieving Key at " + keyName, e);
      throw new DefaultMuleException("Error retrieving Key at " + keyName, e);
    }
  }

  /**
   * Retrieves a certificate out of Azure Key Vault.
   *
   * @param certificateName    Name of the certificate
   * @return Certificate       Object containing the certificate and associated metadata
   */
  public Certificate getCertificate(String certificateName) throws DefaultMuleException {

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
            .fromJson(new InputStreamReader(response.getEntity().getContent(), "UTF-8"),
                    Certificate.class);
        LOGGER.info(certificate.toString());
        return certificate;
      } else {
        LOGGER.info(response.toString());
        KeyVaultError error = gson
            .fromJson(new InputStreamReader(response.getEntity().getContent(), "UTF-8"),
                KeyVaultError.class);
        if (statusCode == 404) {
          throw new CertificateNotFoundException(error.getError().getMessage());
        } else {
          throw new DefaultMuleException(error.getError().getMessage());
        }
      }
    } catch (InterruptedException | ExecutionException | UnsupportedEncodingException e) {
      LOGGER.error("Error retrieving Certificate at " + certificateName, e);
      throw new DefaultMuleException("Error retrieving certificate at " + certificateName, e);
    }
  }

  public Encrypt encryptKey(String keyName, String alg, String value) throws DefaultMuleException {
    String jsonRequest = "{\"alg\": " + '"' + alg + "\" , \"value\": " + '"' + value + "\"}";
    ByteArrayHttpEntity entity = new ByteArrayHttpEntity(jsonRequest.getBytes());
    HttpRequest request = getAuthenticatedHttpRequestBuilder()
            .uri(getHttpBaseUri() + BASE_KEY_PATH + keyName + BASE_ENCRYPT_PATH)
            .addQueryParam(PARAM_API_VERSION, API_VERSION)
            .method(HttpConstants.Method.POST)
            .addHeader(HTTP_CONTENT_TYPE, APPLICATION_JSON)
            .entity(entity)
            .build();
    LOGGER.info("encryptKey Request: " + request.toString() + jsonRequest );
    HttpRequestOptions requestOptions = getHttpRequestOptionsBuilder().build();
    CompletableFuture<HttpResponse> completable = getHttpClient()
            .sendAsync(request, requestOptions);
    try {
      HttpResponse response = completable.get();
      Gson gson = new Gson();
      Integer statusCode = response.getStatusCode();
      if (statusCode == 200) {
        Encrypt encrypt = gson
                .fromJson(new InputStreamReader(response.getEntity().getContent(), "UTF-8"), Encrypt.class);
        LOGGER.info(encrypt.toString());
        return encrypt;
      } else {
        KeyVaultError error = gson
                .fromJson(new InputStreamReader(response.getEntity().getContent(), "UTF-8"),
                        KeyVaultError.class);
        LOGGER.info("response key vault error string" + error.toString());
        if (statusCode == 404) {
          throw new KeyNotFoundException(error.getError().getMessage());
        } else {
          throw new DefaultMuleException(error.getError().getMessage());
        }
      }
    } catch (InterruptedException | ExecutionException | UnsupportedEncodingException e) {
      LOGGER.error("Error Encrypting Key at " + keyName, e);
      throw new DefaultMuleException("Error Encrypting Key at " + keyName, e);
    }
  }

  public Decrypt decryptKey(String keyName, String alg, String value) throws DefaultMuleException {
    String jsonRequest = "{\"alg\": " + '"' + alg + "\" , \"value\": " + '"' + value + "\"}";
    ByteArrayHttpEntity entity = new ByteArrayHttpEntity(jsonRequest.getBytes());
    HttpRequest request = getAuthenticatedHttpRequestBuilder()
            .uri(getHttpBaseUri() + BASE_KEY_PATH + keyName + BASE_DECRYPT_PATH)
            .addQueryParam(PARAM_API_VERSION, API_VERSION)
            .method(HttpConstants.Method.POST)
            .addHeader(HTTP_CONTENT_TYPE, APPLICATION_JSON)
            .entity(entity)
            .build();
    LOGGER.info("decryptKey Request: " + request.toString() + jsonRequest );
    HttpRequestOptions requestOptions = getHttpRequestOptionsBuilder().build();
    CompletableFuture<HttpResponse> completable = getHttpClient()
            .sendAsync(request, requestOptions);
    try {
      HttpResponse response = completable.get();
      Gson gson = new Gson();
      Integer statusCode = response.getStatusCode();
      if (statusCode == 200) {
        Decrypt decrypt = gson
                .fromJson(new InputStreamReader(response.getEntity().getContent(), "UTF-8"), Decrypt.class);
        LOGGER.info(decrypt.toString());
        return decrypt;
      } else {
        KeyVaultError error = gson
                .fromJson(new InputStreamReader(response.getEntity().getContent(), "UTF-8"),
                        KeyVaultError.class);
        LOGGER.info("response key vault error string" + error.toString());
        if (statusCode == 404) {
          throw new KeyNotFoundException(error.getError().getMessage());
        } else {
          throw new DefaultMuleException(error.getError().getMessage());
        }
      }
    } catch (InterruptedException | ExecutionException | UnsupportedEncodingException e) {
      LOGGER.error("Error Decrypting Key at " + keyName, e);
      throw new DefaultMuleException("Error Decrypting Key at " + keyName, e);
    }
  }
}
