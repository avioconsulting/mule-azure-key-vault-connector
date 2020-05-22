# Executing the Azure Key Vault Connector Demo


## Step 2: Execute the Demo application

Open the demo application (Azure-key-vault-connector-demo) with Anypoint Studio and start the application as a Mule Application

### Executing get-key-flow

Execute the following to test the Get Key component

curl GET 'http://localhost:8081/api/key'

### Executing get-secret-flow

Execute the following to test the Get Secret component

curl GET 'http://localhost:8081/api/secret'

### Executing get-certificate-flow

Execute the following to test the Get Certificate component

curl GET 'http://localhost:8081/api/certificate'

### Note

pass the clientId, ClientSecret and tenantId as arguments while deploying the application
