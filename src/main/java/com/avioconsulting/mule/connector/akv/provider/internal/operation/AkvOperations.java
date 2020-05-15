package com.avioconsulting.mule.connector.akv.provider.internal.operation;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import com.avioconsulting.mule.connector.akv.provider.api.AkvConfiguration;
import com.avioconsulting.mule.connector.akv.provider.api.error.AzureKeyVaultErrorTypeProvider;
import com.avioconsulting.mule.connector.akv.provider.client.model.Certificate;
import com.avioconsulting.mule.connector.akv.provider.client.model.Key;
import com.avioconsulting.mule.connector.akv.provider.client.model.Secret;
import com.avioconsulting.mule.connector.akv.provider.internal.connection.AkvConnection;
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
      @Connection AkvConnection connection, String path) {
    return connection.getSecret(path);
  }

  @Throws(AzureKeyVaultErrorTypeProvider.class)
  @MediaType(value = ANY, strict = false)
  public Key getKey(@Config AkvConfiguration configuration, @Connection AkvConnection connection,
      String path) {
    return connection.getKey(path);
  }

  @Throws(AzureKeyVaultErrorTypeProvider.class)
  @MediaType(value = ANY, strict = false)
  public Certificate getCertificate(@Config AkvConfiguration configuration,
      @Connection AkvConnection connection, String path) {
    return connection.getCertificate(path);
  }
}
