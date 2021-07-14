package com.avioconsulting.mule.connector.akv.provider.internal.error;

import org.mule.runtime.extension.api.exception.ModuleException;

public class AccessDeniedException  extends ModuleException {
  public AccessDeniedException(String message) {
    super(message, AzureKeyVaultErrorType.ACCESS_DENIED);
  }
}
