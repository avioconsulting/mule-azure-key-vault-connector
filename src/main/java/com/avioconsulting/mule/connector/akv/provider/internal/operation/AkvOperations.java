package com.avioconsulting.mule.connector.akv.provider.internal.operation;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import com.avioconsulting.mule.connector.akv.provider.api.AkvConfiguration;
import com.avioconsulting.mule.connector.akv.provider.api.model.*;
import com.avioconsulting.mule.connector.akv.provider.internal.error.AzureKeyVaultErrorTypeProvider;
import com.avioconsulting.mule.connector.akv.provider.internal.connection.AkvConnection;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.MediaType;


/**
 * This class is a container for operations, every public method in this class will be taken as an
 * extension operation.
 */
public class AkvOperations {

  @Throws(AzureKeyVaultErrorTypeProvider.class)
  @MediaType(value = ANY, strict = false)
  public Secret getSecret(@Config AkvConfiguration configuration,
      @Connection AkvConnection connection, String secretName) throws MuleException {
    return connection.getSecret(secretName);
  }

  @Throws(AzureKeyVaultErrorTypeProvider.class)
  @MediaType(value = ANY, strict = false)
  public Key getKey(@Config AkvConfiguration configuration, @Connection AkvConnection connection,
      String keyName) throws MuleException {
    return connection.getKey(keyName);
  }

  @Throws(AzureKeyVaultErrorTypeProvider.class)
  @MediaType(value = ANY, strict = false)
  public Certificate getCertificate(@Config AkvConfiguration configuration,
      @Connection AkvConnection connection, String certificateName) throws MuleException {
    return connection.getCertificate(certificateName);
  }

  @Throws(AzureKeyVaultErrorTypeProvider.class)
  @MediaType(value = ANY, strict = false)
  public Encrypt encryptKey(@Config AkvConfiguration configuration, @Connection AkvConnection connection,
                            String keyName, String alg, String value) throws MuleException {
    return connection.encryptKey(keyName, alg, value);
  }

  @Throws(AzureKeyVaultErrorTypeProvider.class)
  @MediaType(value = ANY, strict = false)
  public Decrypt decryptKey(@Config AkvConfiguration configuration, @Connection AkvConnection connection,
                            String keyName, String alg, String value) throws MuleException {
    return connection.decryptKey(keyName, alg, value);
  }
}
