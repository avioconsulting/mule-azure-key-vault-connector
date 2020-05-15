package com.avioconsulting.mule.connector.akv.provider.api.error;

import org.mule.runtime.extension.api.exception.ModuleException;

public class UnknownKeyVaultException extends ModuleException {

  public UnknownKeyVaultException(String message) {
    super(message, AzureKeyVaultErrorType.UNKNOWN_ERROR);
  }
}
