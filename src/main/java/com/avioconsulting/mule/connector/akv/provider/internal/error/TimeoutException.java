package com.avioconsulting.mule.connector.akv.provider.internal.error;

import org.mule.runtime.extension.api.exception.ModuleException;

public class TimeoutException extends ModuleException {
    public TimeoutException(String message, Throwable cause) {
        super(message, AzureKeyVaultErrorType.TIMEOUT, cause);
    }
}
