package com.avioconsulting.mule.connector.akv.provider.internal.client.operation;

import com.avioconsulting.mule.connector.akv.provider.internal.error.KeyNotFoundException;
import com.avioconsulting.mule.connector.akv.provider.api.model.Encrypt;
import com.avioconsulting.mule.connector.akv.provider.internal.client.AzureKeyVaultClient;
import com.avioconsulting.mule.connector.akv.provider.internal.client.KeyVaultRequest;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.util.concurrent.CompletableFuture;

import static com.avioconsulting.mule.connector.akv.provider.internal.client.AzureKeyVaultClient.*;

public class EncryptWithKey extends KeyVaultRequest<Encrypt> {
    private final String keyName;
    private final String algorithm;
    private final String value;

    public EncryptWithKey(AzureKeyVaultClient client, String keyName, String algorithm, String value) {
        super(client);
        this.keyName = keyName;
        this.algorithm = algorithm;
        this.value = value;
    }

    @Override
    public CompletableFuture<HttpResponse> prepare() {
        String jsonRequest = "{\"alg\": " + '"' + algorithm + "\" , \"value\": " + '"' + value + "\"}";
        return preparePostRequest(BASE_KEY_PATH + keyName + BASE_ENCRYPT_PATH, jsonRequest, APPLICATION_JSON);
    }

    @Override
    public Class<Encrypt> getType() {
        return Encrypt.class;
    }

    @Override
    public ModuleException notFoundException(String message) {
        return new KeyNotFoundException(message);
    }

    @Override
    public String errorText() {
        return "Error Encrypting with Key " + keyName;
    }
}
