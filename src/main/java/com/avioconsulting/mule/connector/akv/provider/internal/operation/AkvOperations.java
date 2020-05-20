package com.avioconsulting.mule.connector.akv.provider.internal.operation;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import com.avioconsulting.mule.connector.akv.provider.api.AkvConfiguration;
import com.avioconsulting.mule.connector.akv.provider.api.error.AzureKeyVaultErrorTypeProvider;
import com.avioconsulting.mule.connector.akv.provider.api.client.model.Certificate;
import com.avioconsulting.mule.connector.akv.provider.api.client.model.Key;
import com.avioconsulting.mule.connector.akv.provider.api.client.model.Secret;
import com.avioconsulting.mule.connector.akv.provider.internal.connection.AkvConnection;
import org.mule.runtime.api.exception.DefaultMuleException;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.MediaType;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;


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
}
