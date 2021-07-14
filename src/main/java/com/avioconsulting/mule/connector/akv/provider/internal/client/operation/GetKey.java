package com.avioconsulting.mule.connector.akv.provider.internal.client.operation;

import com.avioconsulting.mule.connector.akv.provider.internal.error.KeyNotFoundException;
import com.avioconsulting.mule.connector.akv.provider.api.model.Key;
import com.avioconsulting.mule.connector.akv.provider.internal.client.AzureKeyVaultClient;
import com.avioconsulting.mule.connector.akv.provider.internal.client.KeyVaultRequest;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.util.concurrent.CompletableFuture;

import static com.avioconsulting.mule.connector.akv.provider.internal.client.AzureKeyVaultClient.BASE_KEY_PATH;

public class GetKey extends KeyVaultRequest<Key> {
    private final String name;

    public GetKey(AzureKeyVaultClient client, String name) {
        super(client);
        this.name = name;
    }

    @Override
    public CompletableFuture<HttpResponse> prepare() {
        return prepareGetRequest(BASE_KEY_PATH + name);
    }

    @Override
    public Class<Key> getType() {
        return Key.class;
    }

    @Override
    public ModuleException notFoundException(String message) {
        return new KeyNotFoundException(message);
    }

    @Override
    public String errorText() {
        return "Error retrieving Key at " + name;
    }
}
