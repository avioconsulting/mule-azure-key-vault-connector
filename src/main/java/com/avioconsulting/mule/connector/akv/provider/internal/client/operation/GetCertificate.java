package com.avioconsulting.mule.connector.akv.provider.internal.client.operation;

import com.avioconsulting.mule.connector.akv.provider.internal.error.CertificateNotFoundException;
import com.avioconsulting.mule.connector.akv.provider.api.model.Certificate;
import com.avioconsulting.mule.connector.akv.provider.internal.client.AzureKeyVaultClient;
import com.avioconsulting.mule.connector.akv.provider.internal.client.KeyVaultRequest;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.util.concurrent.CompletableFuture;

import static com.avioconsulting.mule.connector.akv.provider.internal.client.AzureKeyVaultClient.BASE_CERTIFICATE_PATH;
import static com.avioconsulting.mule.connector.akv.provider.internal.client.AzureKeyVaultClient.CERTIFICATE_STATUS_PATH;

public class GetCertificate extends KeyVaultRequest<Certificate> {
    private final String name;
    public GetCertificate(AzureKeyVaultClient client, String name) {
        super(client);
        this.name = name;
    }

    @Override
    public CompletableFuture<HttpResponse> prepare() {
        return prepareGetRequest(BASE_CERTIFICATE_PATH + name + CERTIFICATE_STATUS_PATH);
    }

    @Override
    public Class<Certificate> getType() {
        return Certificate.class;
    }

    @Override
    public ModuleException notFoundException(String message) {
        return new CertificateNotFoundException(message);
    }

    @Override
    public String errorText() {
        return "Error retrieving Certificate at " + name;
    }
}
