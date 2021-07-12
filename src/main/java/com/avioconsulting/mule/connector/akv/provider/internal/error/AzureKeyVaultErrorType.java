package com.avioconsulting.mule.connector.akv.provider.internal.error;

import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

public enum AzureKeyVaultErrorType implements ErrorTypeDefinition<AzureKeyVaultErrorType> {
  ACCESS_DENIED,
  SECRET_NOT_FOUND,
  KEY_NOT_FOUND,
  CERTIFICATE_NOT_FOUND,
  TIMEOUT
}
