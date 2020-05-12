package com.avioconsulting.mule.connector.akv.provider.api.error;

import org.mule.runtime.extension.api.annotation.error.ErrorTypeProvider;
import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

import java.util.HashSet;
import java.util.Set;

public class AzureKeyVaultErrorTypeProvider implements ErrorTypeProvider {
    @Override
    public Set<ErrorTypeDefinition> getErrorTypes() {
        Set<ErrorTypeDefinition> errors = new HashSet<>();

        errors.add(AzureKeyVaultErrorType.ACCESS_DENIED);
        errors.add(AzureKeyVaultErrorType.SECRET_NOT_FOUND);
        errors.add(AzureKeyVaultErrorType.UNKNOWN_ERROR);

        return errors;
    }
}
