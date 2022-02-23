package com.avioconsulting.mule.connector.akv.provider.internal.client;

import com.avioconsulting.mule.connector.akv.provider.internal.client.AzureKeyVaultClient;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.runtime.http.api.HttpConstants;
import org.mule.runtime.http.api.client.HttpRequestOptions;
import org.mule.runtime.http.api.domain.entity.ByteArrayHttpEntity;
import org.mule.runtime.http.api.domain.message.request.HttpRequest;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.util.concurrent.CompletableFuture;

import static com.avioconsulting.mule.connector.akv.provider.internal.client.AzureKeyVaultClient.*;

public abstract class KeyVaultRequest<T> {
    private final AzureKeyVaultClient client;
    protected KeyVaultRequest(AzureKeyVaultClient client) {
        this.client = client;
    }

    protected CompletableFuture<HttpResponse> prepareGetRequest(String path) {
        HttpRequest request = client.getAuthenticatedHttpRequestBuilder()
                .uri(client.getHttpBaseUri() + path)
                .addQueryParam(PARAM_API_VERSION, API_VERSION)
                .method(HttpConstants.Method.GET).build();
        HttpRequestOptions requestOptions = client.getHttpRequestOptionsBuilder().build();
        return client.getHttpClient().sendAsync(request, requestOptions);
    }

    protected CompletableFuture<HttpResponse> preparePostRequest(String path, String requestBody, String contentType) {
        ByteArrayHttpEntity entity = new ByteArrayHttpEntity(requestBody.getBytes());
        HttpRequest request = client.getAuthenticatedHttpRequestBuilder()
                .uri(client.getHttpBaseUri() + path)
                .addQueryParam(PARAM_API_VERSION, API_VERSION)
                .method(HttpConstants.Method.POST)
                .addHeader(HTTP_CONTENT_TYPE, contentType)
                .entity(entity)
                .build();
        HttpRequestOptions requestOptions = client.getHttpRequestOptionsBuilder().build();
        return client.getHttpClient().sendAsync(request, requestOptions);
    }

    public abstract Class<T> getType();

    public abstract CompletableFuture<HttpResponse> prepare();

    public abstract ModuleException notFoundException(String message);

    public abstract String errorText();
}
