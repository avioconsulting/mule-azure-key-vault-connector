package com.avioconsulting.mule.connector.akv.provider.api;

import com.avioconsulting.mule.connector.akv.provider.internal.connection.AkvConnectionProvider;
import com.avioconsulting.mule.connector.akv.provider.internal.operation.AkvOperations;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;

/**
 * This class represents an extension configuration, values set in this class are commonly used
 * across multiple operations since they represent something core from the extension.
 */
@Operations(AkvOperations.class)
@ConnectionProviders(AkvConnectionProvider.class)
public class AkvConfiguration {

}
