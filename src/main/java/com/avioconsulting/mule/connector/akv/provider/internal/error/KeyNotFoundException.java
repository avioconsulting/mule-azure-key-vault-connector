package com.avioconsulting.mule.connector.akv.provider.internal.error;

import org.mule.runtime.extension.api.exception.ModuleException;

public class KeyNotFoundException extends ModuleException {

  public KeyNotFoundException(String message) {
    super(message, AzureKeyVaultErrorType.KEY_NOT_FOUND);
  }

}
