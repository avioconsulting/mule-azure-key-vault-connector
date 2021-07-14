package com.avioconsulting.mule.connector.akv.provider.internal.client.operation;

import com.avioconsulting.mule.connector.akv.provider.internal.error.KeyNotFoundException;
import com.avioconsulting.mule.connector.akv.provider.api.model.Decrypt;
import com.avioconsulting.mule.connector.akv.provider.internal.client.AzureKeyVaultClient;
import com.avioconsulting.mule.connector.akv.provider.internal.client.KeyVaultRequest;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.util.concurrent.CompletableFuture;

import static com.avioconsulting.mule.connector.akv.provider.internal.client.AzureKeyVaultClient.*;

public class DecryptWithKey extends KeyVaultRequest<Decrypt> {
    private final String keyName;
    private final String algorithm;
    private final String cipherText;
    public DecryptWithKey(AzureKeyVaultClient client, String keyName, String algorithm, String cipherText) {
        super(client);
        this.keyName = keyName;
        this.algorithm = algorithm;
        this.cipherText = cipherText;
    }

    @Override
    public CompletableFuture<HttpResponse> prepare() {
        String jsonRequest = "{\"alg\": " + '"' + algorithm + "\" , \"value\": " + '"' + cipherText + "\"}";
        return preparePostRequest(BASE_KEY_PATH + keyName + BASE_DECRYPT_PATH, jsonRequest, APPLICATION_JSON);
    }

    @Override
    public Class<Decrypt> getType() {
        return Decrypt.class;
    }

    @Override
    public ModuleException notFoundException(String message) {
        return new KeyNotFoundException(message);
    }

    @Override
    public String errorText() {
        return "Error decrypting with key " + keyName;
    }
}
