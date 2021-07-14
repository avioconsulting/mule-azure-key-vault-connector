package com.avioconsulting.mule.connector.akv.provider.internal.client.operation;

import com.avioconsulting.mule.connector.akv.provider.internal.error.SecretNotFoundException;
import com.avioconsulting.mule.connector.akv.provider.api.model.Secret;
import com.avioconsulting.mule.connector.akv.provider.internal.client.AzureKeyVaultClient;
import com.avioconsulting.mule.connector.akv.provider.internal.client.KeyVaultRequest;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.util.concurrent.CompletableFuture;

import static com.avioconsulting.mule.connector.akv.provider.internal.client.AzureKeyVaultClient.BASE_SECRET_PATH;

public class GetSecret extends KeyVaultRequest<Secret> {
    private final String name;

    public GetSecret(AzureKeyVaultClient client, String name) {
        super(client);
        this.name = name;
    }

    @Override
    public CompletableFuture<HttpResponse> prepare() {
        return prepareGetRequest(BASE_SECRET_PATH + name);
    }

    @Override
    public Class<Secret> getType() {
        return Secret.class;
    }

    @Override
    public ModuleException notFoundException(String message) {
        return new SecretNotFoundException(message);
    }

    @Override
    public String errorText() {
        return "Error retrieving secret at " + name;
    }
}
