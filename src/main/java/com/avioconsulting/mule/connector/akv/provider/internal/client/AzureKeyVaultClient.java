package com.avioconsulting.mule.connector.akv.provider.internal.client;

import com.avioconsulting.mule.connector.akv.provider.api.model.*;
import com.avioconsulting.mule.connector.akv.provider.internal.client.operation.*;
import com.google.gson.Gson;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.exception.DefaultMuleException;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.http.api.client.HttpClient;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

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

  private <T> T doRequest(KeyVaultRequest<T> keyVaultRequest) throws MuleException {
    try {
      HttpResponse response = keyVaultRequest.prepare().get();
      Gson gson = new Gson();
      Integer statusCode = response.getStatusCode();
      if (statusCode == 200) {
        T result = gson.fromJson(new InputStreamReader(response.getEntity().getContent(), "UTF-8"), keyVaultRequest.getType());
        LOGGER.debug("Parsed AKV response: " + result.toString());
        return result;
      } else {
        KeyVaultError error = gson.fromJson(new InputStreamReader(response.getEntity().getContent(), "UTF-8"), KeyVaultError.class);
        if (statusCode == 404) {
          throw keyVaultRequest.notFoundException(error.getError().getMessage());
        } else if (statusCode == 401) {
          LOGGER.debug("Unauthorized: {}", error.toString());
          throw new ConnectionException(error.getError().getMessage());
        } else {
          LOGGER.debug("Unexpected AKV error, status code {}: {}", statusCode, error.toString());
          throw new DefaultMuleException(error.getError().getMessage());
        }
      }
    } catch (InterruptedException | UnsupportedEncodingException e) {
      LOGGER.error(keyVaultRequest.errorText(), e);
      throw new DefaultMuleException(e);
    } catch (ExecutionException e) {
      try {
        throw e.getCause();
      } catch (IOException inner) {
        LOGGER.error(keyVaultRequest.errorText(), inner);
        throw new ConnectionException(keyVaultRequest.errorText(), inner);
      } catch (TimeoutException inner) {
        LOGGER.error(keyVaultRequest.errorText(), inner);
        throw new com.avioconsulting.mule.connector.akv.provider.internal.error.TimeoutException(keyVaultRequest.errorText(), inner);
      } catch (Throwable inner) {
        LOGGER.error(keyVaultRequest.errorText(), inner);
        throw new DefaultMuleException(inner);
      }
    }
  }

  /**
   * Retrieves a secret out of Azure Key Vault.
   * @param secretName    Name of the secret
   * @return Secret       Object containing the secret and associated metadata
   */
  public Secret getSecret(String secretName) throws MuleException {
    return doRequest(new GetSecret(this, secretName));
  }

  /**
   * Retrieves a key out of Azure Key Vault.
   *
   * @param keyName    Name of the key
   * @return Key       Object containing the key and associated metadata
   */
  public Key getKey(String keyName) throws MuleException {
    return doRequest(new GetKey(this, keyName));
  }

  /**
   * Retrieves a certificate out of Azure Key Vault.
   *
   * @param certificateName    Name of the certificate
   * @return Certificate       Object containing the certificate and associated metadata
   */
  public Certificate getCertificate(String certificateName) throws MuleException {
    return doRequest(new GetCertificate(this, certificateName));
  }

  /**
   * Encrypts a message using Azure Key Vault.
   *
   * @param keyName    Key used for encryption
   * @param algorithm  Algorithm to use for encryption
   * @param value      Message to encrypt
   * @return Encryot       Object containing the encrypted data and ID of the key used
   */
  public Encrypt encryptKey(String keyName, String algorithm, String value) throws MuleException {
    return doRequest(new EncryptWithKey(this, keyName, algorithm, value));
  }

  /**
   * Decrypts a message using Azure Key Vault.
   *
   * @param keyName    Key used for decryption
   * @param algorithm  Algorithm to use for decryption
   * @param cipherText      Ciphertext to decrypt
   * @return Encryot       Object containing the decrypted data and ID of the key used
   */
  public Decrypt decryptKey(String keyName, String algorithm, String cipherText) throws MuleException {
    return doRequest(new DecryptWithKey(this, keyName, algorithm, cipherText));
  }
}
