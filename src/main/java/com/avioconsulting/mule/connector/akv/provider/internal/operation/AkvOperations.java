package com.avioconsulting.mule.connector.akv.provider.internal.operation;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import com.avioconsulting.mule.connector.akv.provider.api.AkvConfiguration;
import com.avioconsulting.mule.connector.akv.provider.api.client.model.*;
import com.avioconsulting.mule.connector.akv.provider.api.error.AzureKeyVaultErrorTypeProvider;
import com.avioconsulting.mule.connector.akv.provider.internal.connection.AkvConnection;
import org.mule.runtime.api.exception.DefaultMuleException;
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
      @Connection AkvConnection connection, String secretName) throws DefaultMuleException {
    return connection.getSecret(secretName);
  }

  @Throws(AzureKeyVaultErrorTypeProvider.class)
  @MediaType(value = ANY, strict = false)
  public Key getKey(@Config AkvConfiguration configuration, @Connection AkvConnection connection,
      String keyName) throws DefaultMuleException {
    return connection.getKey(keyName);
  }

  @Throws(AzureKeyVaultErrorTypeProvider.class)
  @MediaType(value = ANY, strict = false)
  public Certificate getCertificate(@Config AkvConfiguration configuration,
      @Connection AkvConnection connection, String certificateName) throws DefaultMuleException {
    return connection.getCertificate(certificateName);
  }

  @Throws(AzureKeyVaultErrorTypeProvider.class)
  @MediaType(value = ANY, strict = false)
  public Encrypt encryptKey(@Config AkvConfiguration configuration, @Connection AkvConnection connection,
                            String keyName, String alg, String value) throws DefaultMuleException {
    return connection.encryptKey(keyName, alg, value);
  }

  @Throws(AzureKeyVaultErrorTypeProvider.class)
  @MediaType(value = ANY, strict = false)
  public Decrypt decryptKey(@Config AkvConfiguration configuration, @Connection AkvConnection connection,
                            String keyName, String alg, String value) throws DefaultMuleException {
    return connection.decryptKey(keyName, alg, value);
  }
}
