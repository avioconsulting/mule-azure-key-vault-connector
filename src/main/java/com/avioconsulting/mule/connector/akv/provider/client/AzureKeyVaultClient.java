package com.avioconsulting.mule.connector.akv.provider.client;

import com.avioconsulting.mule.connector.akv.provider.api.error.SecretNotFoundException;
import com.avioconsulting.mule.connector.akv.provider.api.error.UnknownKeyVaultException;
import com.avioconsulting.mule.connector.akv.provider.client.model.KeyVaultError;
import com.avioconsulting.mule.connector.akv.provider.client.model.Secret;
import com.google.gson.Gson;
import org.mule.runtime.http.api.HttpConstants;
import org.mule.runtime.http.api.client.HttpClient;
import org.mule.runtime.http.api.client.HttpRequestOptions;
import org.mule.runtime.http.api.domain.message.request.HttpRequest;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AzureKeyVaultClient extends AzureClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureKeyVaultClient.class);
    public static final String PARAM_API_VERSION = "api-version";
    public static final String API_VERSION = "7.0";

    public AzureKeyVaultClient(HttpClient httpClient, String baseUri, String tenantId, String clientId, String clientSecret, Integer timeout) {
        super(httpClient, baseUri, tenantId, clientId, clientSecret, timeout);
    }


    public Secret getSecret(String path) {
        HttpRequest request = getAuthenticatedHttpRequestBuilder().
                uri(path).
                addQueryParam(PARAM_API_VERSION, API_VERSION).
                method(HttpConstants.Method.GET).build();
        HttpRequestOptions requestOptions = getHttpRequestOptionsBuilder().build();
        CompletableFuture<HttpResponse> completable = getHttpClient().sendAsync(request, requestOptions);
        try {
            HttpResponse response = completable.get();
            Gson gson = new Gson();
            Integer statusCode = response.getStatusCode();
            if(statusCode == 200) {
                Secret secret = gson.fromJson(new InputStreamReader(response.getEntity().getContent()), Secret.class);
                LOGGER.info(secret.toString());
                return secret;
            } else {
                KeyVaultError error = gson.fromJson(new InputStreamReader(response.getEntity().getContent()), KeyVaultError.class);
                if(statusCode == 404) {
                    throw new SecretNotFoundException(error.getError().getMessage());
                } else {
                    throw new UnknownKeyVaultException(error.getError().getMessage());
                }
            }
        } catch (InterruptedException|ExecutionException e) {
            LOGGER.error("Error retrieving secret at " + path, e);
            throw new UnknownKeyVaultException(e.getMessage());
        }
    }


}
