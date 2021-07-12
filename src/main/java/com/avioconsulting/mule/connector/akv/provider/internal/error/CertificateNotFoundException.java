package com.avioconsulting.mule.connector.akv.provider.internal.error;

import org.mule.runtime.extension.api.exception.ModuleException;

public class CertificateNotFoundException extends ModuleException {

  public CertificateNotFoundException(String message) {
    super(message, AzureKeyVaultErrorType.CERTIFICATE_NOT_FOUND);
  }
}
