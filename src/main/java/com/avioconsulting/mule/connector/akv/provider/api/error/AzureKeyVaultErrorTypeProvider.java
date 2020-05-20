package com.avioconsulting.mule.connector.akv.provider.api.error;

import java.util.HashSet;
import java.util.Set;
import org.mule.runtime.extension.api.annotation.error.ErrorTypeProvider;
import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

public class AzureKeyVaultErrorTypeProvider implements ErrorTypeProvider {

  @Override
  public Set<ErrorTypeDefinition> getErrorTypes() {
    Set<ErrorTypeDefinition> errors = new HashSet<>();

    errors.add(AzureKeyVaultErrorType.ACCESS_DENIED);
    errors.add(AzureKeyVaultErrorType.SECRET_NOT_FOUND);
    errors.add(AzureKeyVaultErrorType.UNKNOWN_ERROR);
    errors.add(AzureKeyVaultErrorType.KEY_NOT_FOUND);
    errors.add(AzureKeyVaultErrorType.CERTIFICATE_NOT_FOUND);

    return errors;
  }
}
