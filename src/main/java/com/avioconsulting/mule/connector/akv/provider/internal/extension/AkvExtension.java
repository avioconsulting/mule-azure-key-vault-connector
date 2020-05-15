package com.avioconsulting.mule.connector.akv.provider.internal.extension;

import com.avioconsulting.mule.connector.akv.provider.api.AkvConfiguration;
import com.avioconsulting.mule.connector.akv.provider.api.error.AzureKeyVaultErrorType;
import org.mule.runtime.extension.api.annotation.Configurations;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.mule.runtime.extension.api.annotation.error.ErrorTypes;


/**
 * This is the main class of an extension, is the entry point from which configurations, connection
 * providers, operations and sources are going to be declared.
 */
@Xml(prefix = "akv")
@Extension(name = "Akv")
@ErrorTypes(AzureKeyVaultErrorType.class)
@Configurations(AkvConfiguration.class)
public class AkvExtension {

}
