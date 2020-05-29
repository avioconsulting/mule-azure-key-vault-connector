package akv;

import com.avioconsulting.mule.connector.akv.provider.api.client.model.Certificate;
import com.avioconsulting.mule.connector.akv.provider.api.client.model.Key;
import com.avioconsulting.mule.connector.akv.provider.api.client.model.Secret;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.http.policy.HttpLogOptions;
import com.azure.core.http.rest.Response;
import com.azure.core.util.Context;
import com.azure.core.util.polling.LongRunningOperationStatus;
import com.azure.core.util.polling.PollResponse;
import com.azure.core.util.polling.SyncPoller;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.certificates.CertificateClient;
import com.azure.security.keyvault.certificates.CertificateClientBuilder;
import com.azure.security.keyvault.certificates.models.*;
import com.azure.security.keyvault.keys.KeyClient;
import com.azure.security.keyvault.keys.KeyClientBuilder;

import com.azure.security.keyvault.keys.models.CreateRsaKeyOptions;
import com.azure.security.keyvault.keys.models.DeletedKey;
import com.azure.security.keyvault.keys.models.KeyVaultKey;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.DeletedSecret;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


public class AkvIntegrationTestCase extends MuleArtifactFunctionalTestCase {
    private static String keyName;
    private static String secretName;
    private static String secretValue;
    private static String certificateName;

    @Override
    protected String getConfigFile() {
        return "integration-test-mule-config.xml";
    }

    private static void setGlobals(){
        System.setProperty("AZURE_CLIENT_ID", System.getProperty("azure.client.id"));
        System.setProperty("AZURE_CLIENT_SECRET", System.getProperty("azure.client.secret"));
        System.setProperty("AZURE_TENANT_ID", System.getProperty("azure.tenant.id"));
        System.setProperty("AZURE_VAULT_NAME", System.getProperty("azure.vault.name"));
        System.setProperty("akv.test.key.name", getKeyName());
        System.setProperty("akv.test.secret.name", getSecretName());
        System.setProperty("akv.test.secret.value", getSecretValue());
        System.setProperty("akv.test.certificate.name", getCertificateName());
        //Not using it in Integration test case
        System.setProperty("AKV_TEST_AUTH_URL", "");
        System.setProperty("AKV_TEST_URL", "");
        System.out.println("Cleared the Auth Url");
    }

    public static String getKeyName() {
        if (keyName == null || keyName.length() == 0){
            keyName = "akvtest" + System.currentTimeMillis();
        }
        return keyName;
    }

    public static String getSecretName() {
        if (secretName == null || secretName.length() == 0) {
            secretName = "akvtest" + System.currentTimeMillis();
        }
        return secretName;
    }

    public static String getSecretValue() {
        if (secretValue == null || secretValue.length() == 0) {
            secretValue = "akvtest" + System.currentTimeMillis();
        }
        return secretValue;
    }

    public static String getCertificateName() {
        if (certificateName == null || certificateName.length() == 0) {
            certificateName = "akvtestCert" + System.currentTimeMillis();
        }
        return certificateName;
    }

    @BeforeClass public static void initializeVault() {
        setGlobals();
        createKey();
        createSecret();
        createCertificate();
    }

    @AfterClass public static void cleanUpVault(){
        setGlobals();
        deleteKey();
        deleteSecret();
        deleteCertificate();
    }

    @Test
    public void getKeyTest() throws Exception {
        Object payloadValue = flowRunner("getKeyTest")
                .run()
                .getMessage()
                .getPayload()
                .getValue();
        Key key = (Key)payloadValue;
        System.out.println(key);
        assertThat(key.getKey().getKeyType(), is("RSA"));
    }

    @Test
    public void getSecretTest() throws Exception {
        Object payloadValue = flowRunner("getSecretTest")
                .run()
                .getMessage()
                .getPayload()
                .getValue();
        Secret secret = (Secret) payloadValue;
        System.out.println(secret);
        assertThat(secret.getValue(), is(secretValue));
    }

    @Test
    public void getCertificateTest() throws Exception {
        Object payloadValue = flowRunner("getCertificateTest")
                .run()
                .getMessage()
                .getPayload()
                .getValue();
        Certificate certificate = (Certificate) payloadValue;
        System.out.println(certificate);
        assertThat(certificate.getPolicy().getId(), is("https://akv-mule-integration-key.vault.azure.net/certificates/" + certificateName + "/policy"));
    }

    public static void createKey() {
        System.out.println("beforeclass start");
        KeyClient keyClient = new KeyClientBuilder()
                .vaultUrl("https://akv-mule-integration-key.vault.azure.net/")
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
        System.out.println(keyClient);
        Response<KeyVaultKey> createKeyResponse = keyClient.createRsaKeyWithResponse(new CreateRsaKeyOptions(keyName)
                .setKeySize(2048), new Context("key1", "value1"));
        System.out.printf("Create Key operation succeeded with status code %s \n", createKeyResponse.getStatusCode());
        System.out.println("key created successfully");
    }

    public static void deleteKey(){
        System.out.println("afterclass start");
        KeyClient keyClient = new KeyClientBuilder()
                .vaultUrl("https://akv-mule-integration-key.vault.azure.net/")
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
        SyncPoller<DeletedKey, Void> deletedKeyPoller = keyClient.beginDeleteKey(keyName);
        PollResponse<DeletedKey> deletedKeyPollResponse = deletedKeyPoller.poll();
        DeletedKey deletedKey = deletedKeyPollResponse.getValue();
        System.out.println("Deleted Date  %s" + deletedKey.getDeletedOn().toString());
        System.out.println("key Deleted successfully");
    }

    public static void createSecret() {
        System.out.println("beforeclass start");
        SecretClient secretClient = new SecretClientBuilder()
                .vaultUrl("https://akv-mule-integration-key.vault.azure.net/")
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
        System.out.println(secretClient);
        KeyVaultSecret secret = secretClient.setSecret(secretName, secretValue);
        System.out.printf("Secret is created with name %s and value %s \n", secret.getName(), secret.getValue());
        System.out.println("Secret created successfully");
    }

    public static void deleteSecret(){
        System.out.println("afterclass start");
        SecretClient secretClient = new SecretClientBuilder()
                .vaultUrl("https://akv-mule-integration-key.vault.azure.net/")
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
        SyncPoller<DeletedSecret, Void> deletedSecretPoller = secretClient.beginDeleteSecret(secretName);
        PollResponse<DeletedSecret> deletedSecretPollResponse = deletedSecretPoller.poll();
        DeletedSecret deletedSecret = deletedSecretPollResponse.getValue();
        System.out.println("Deleted Date  %s" + deletedSecretPollResponse.getValue().getDeletedOn().toString());
        System.out.println("Secret Deleted successfully");
    }

    public static void createCertificate()  {
        System.out.println("beforeclass start");
        CertificateClient certificateClient = new CertificateClientBuilder()
                .credential(new DefaultAzureCredentialBuilder().build())
                .vaultUrl("https://akv-mule-integration-key.vault.azure.net/")
                .httpLogOptions(new HttpLogOptions().setLogLevel(HttpLogDetailLevel.BODY_AND_HEADERS))
                .buildClient();
        CertificatePolicy certificatePolicy = new CertificatePolicy("Self",
                "CN=SelfSignedJavaPkcs12");
        SyncPoller<CertificateOperation, KeyVaultCertificateWithPolicy> certificatePoller = certificateClient
                .beginCreateCertificate(certificateName, CertificatePolicy.getDefault());
        certificatePoller.waitUntil(LongRunningOperationStatus.SUCCESSFULLY_COMPLETED);
        KeyVaultCertificate certificate = certificatePoller.getFinalResult();
        System.out.printf("Certificate created with name %s", certificate.getName());
        System.out.println("Certificate created successfully");
    }

    public static void deleteCertificate(){
        System.out.println("afterclass start");
        CertificateClient certificateClient = new CertificateClientBuilder()
                .credential(new DefaultAzureCredentialBuilder().build())
                .vaultUrl("https://akv-mule-integration-key.vault.azure.net/")
                .httpLogOptions(new HttpLogOptions().setLogLevel(HttpLogDetailLevel.BODY_AND_HEADERS))
                .buildClient();
        SyncPoller<DeletedCertificate, Void> deleteCertificatePoller =
                certificateClient.beginDeleteCertificate(certificateName);
        PollResponse<DeletedCertificate> pollResponse = deleteCertificatePoller.poll();
        System.out.printf("Deleted certificate with name %s and recovery id %s", pollResponse.getValue().getName(),
                pollResponse.getValue().getRecoveryId());
        System.out.println("Certificate Deleted successfully");
    }
}