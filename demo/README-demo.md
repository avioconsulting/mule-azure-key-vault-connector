# Executing the Azure Key Vault Connector Demo

### Creating the key vault in Azure

Create a key vault in azure portal to get the clientId, clientSecret, and tenantId values and add it to local.properties before deploying the Mule application.

`Note:` Make sure vault names have to be globally unique across all of Azure

### Installing the connector

checkout the code from `https://github.com/avioconsulting/mule-azure-key-vault-connector` and do a `mvn install` to install the connector locally.

### Execute the Demo application

Open the demo application (Azure-key-vault-connector-demo) with Anypoint Studio and start the application as a Mule Application

### Executing get-key-flow

Execute the following to test the Get Key component. change the keyName parameter to get required key.

curl GET 'http://localhost:8081/api/key?keyName=akv-mule-key'

### Executing get-secret-flow

Execute the following to test the Get Secret component. change the secretName parameter to get required secret.

curl GET 'http://localhost:8081/api/secret?secretName=akv-mule-secret'

### Executing get-certificate-flow

Execute the following to test the Get Certificate component. change the certName parameter to get required certificate


curl GET 'http://localhost:8081/api/certificate?certName=akv-mule-integration-cert'

### Note

Pass clientId, clientSecret and tenantId as arguments while deploying the application

to deploy the application, use mvn clean deploy -DclientId=`xxxxxxxxxxxxxxx` -DclientSecret=`xxxxxxxxxxxxxxx` 
-DtenantId=`xxxxxxxxxxxxxxx`

