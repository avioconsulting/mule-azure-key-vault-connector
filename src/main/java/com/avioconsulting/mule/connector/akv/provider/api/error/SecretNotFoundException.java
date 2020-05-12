package com.avioconsulting.mule.connector.akv.provider.api.error;

import org.mule.runtime.extension.api.exception.ModuleException;

public class SecretNotFoundException extends ModuleException {
    public SecretNotFoundException(String message) {
        super(message, AzureKeyVaultErrorType.SECRET_NOT_FOUND);
    }
}
