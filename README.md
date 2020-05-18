# Azure Key Vault Extension

This connector allows the easy integration with Azure Key Vault.  All operations use the [Azure Key Vault REST API](https://docs.microsoft.com/en-us/rest/api/keyvault/) being invoked with a HTTP Client. 

## Operations

### Get Secret
Retrieves the specified secret.  
Link to official documentation.  [Azure - Get Secret](https://docs.microsoft.com/en-us/rest/api/keyvault/getsecret/getsecret)

### Get Key
Retrieves the public portion of a stored key.  
Link to official documentation.  [Azure - Get Key](https://docs.microsoft.com/en-us/rest/api/keyvault/getkey/getkey) 

### Get Certificate
This operation retrieves data about a specific certificate.  
Link to official documentation.  [Azure - Get Certificate](https://docs.microsoft.com/en-us/rest/api/keyvault/getcertificate/getcertificate])


## Dependency
Add this dependency to your application pom.xml

```
<groupId>com.avioconsulting.mule.connector</groupId>
<artifactId>mule-azure-key-vault-connector</artifactId>
<version>0.1.0-SNAPSHOT</version>
<classifier>mule-plugin</classifier>
```

## Usage
Sample calls to the key vault.  The `secretName`/`keyName`/`certificateName` attribute's are appropriate name of the secret/key/certificate.
```
<akv:get-secret config-ref="config" secretName="test-secret"/>

<akv:get-key config-ref="config" keyName="test-key"/>

<akv:get-certificate config-ref="config" certificateName="test-certificate"/>
```

### Configuration
|Parameter|Sample|Description|
|---|---|---|
|Azure OAuth Base URI|https://login.microsoftonline.com/|The base url of the login server.|
|Azure Tenant Id|`xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx`|The Tenant ID of the Azure account.|
|Service account client id|`xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx`|The account client id.|
|Service account client secret|`password1`|The account client secret.|
|Time for API calls|15000|Defaults to 30000 (30 seconds)|


## Deploying to Exchange
The Mule Azure Key Connector can be deployed to an Exchange with a few small modifications.
> Shamelessly stolen from Manik Mager's [blog post](https://javastreets.com/blog/publish-connectors-to-anypoint-exchange.html)
1. Update the connector pom.xml file
    * Change the `groupId` value to the Organization Id 
        * (Id found in Anypoint -> Access Management -> Organization -> You're Org)
        ```
           <modelVersion>4.0.0</modelVersion>
           <groupId>xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx</groupId>
           <artifactId>mule-azure-key-vault-connector</artifactId>
           <version>0.1.0-SNAPSHOT</version>
           <packaging>mule-extension</packaging>
           <name>Akv Extension</name>
        ```
    * Update `distributionManagement` to point to the Exchange Repository (Uncomment these lines)
        ```
        <distributionManagement>
          <snapshotRepository>
            <id>exchange-repository</id>
            <name>Exchange Repository</name>
            <url>https://maven.anypoint.mulesoft.com/api/v1/organizations/${pom.groupId}/maven</url>
            <layout>default</layout>
          </snapshotRepository>
          <repository>
            <id>exchange-repository</id>
            <name>Exchange Repository</name>
            <url>https://maven.anypoint.mulesoft.com/api/v1/organizations/${pom.groupId}/maven</url>
            <layout>default</layout>
          </repository>
        </distributionManagement>
        ```
1. Configure your `~/.m2/settings.xml` file with your Exchange credentials
    ```
    <servers>
     <server>
       <id>exchange-repository</id>
       <username>USERNAME</username>
       <password>PASSWORD</password>
     </server>
    </servers>
    ```
1. Execute `mvn deploy` to publish to Exchange

Enjoy!