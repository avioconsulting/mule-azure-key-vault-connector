package akv;

import com.avioconsulting.mule.connector.akv.provider.api.model.*;
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
import com.azure.security.keyvault.certificates.models.CertificatePolicy;
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
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.fail;


public class AkvIntegrationTestCase extends MuleArtifactFunctionalTestCase {
    private static String keyName;
    private static String secretName;
    private static String secretValue;
    private static String certificateName;
    private static String vaultName;
    private static String alg;
    private final static String ENCRYPT_VALUE= "AVIO";

    @Override
    protected String getConfigFile() {
        return "integration-test-mule-config.xml";
    }

    private static void setGlobals() {
        String azureClientId = System.getProperty("azure.client.id");
        if (azureClientId != null && !azureClientId.isEmpty()) {
            System.setProperty("AZURE_CLIENT_ID", azureClientId);
        }
        String azureClientSecret = System.getProperty("azure.client.secret");
        if (azureClientSecret != null && !azureClientSecret.isEmpty()) {
            System.setProperty("AZURE_CLIENT_SECRET", azureClientSecret);
        }
        String azureTenantId = System.getProperty("azure.tenant.id");
        if (azureTenantId != null && !azureTenantId.isEmpty()) {
            System.setProperty("AZURE_TENANT_ID", azureTenantId);
        }
        String azureVaultName = System.getProperty("azure.vault.name");
        if (azureVaultName != null && !azureVaultName.isEmpty()) {
            System.setProperty("AZURE_VAULT_NAME", azureVaultName);
        }
    }
    private static void setSystemProperties() {
        System.setProperty("akv.test.key.name", getKeyName());
        System.setProperty("akv.test.secret.name", getSecretName());
        System.setProperty("akv.test.secret.value", getSecretValue());
        System.setProperty("akv.test.certificate.name", getCertificateName());
        System.setProperty("akv.test.alg", getAlg());
        System.setProperty("akv.test.value", ENCRYPT_VALUE);

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

    public static String getAlg() {
        if (alg == null || alg.length() == 0) {
            alg = "RSA1_5";
        }
        return alg;
    }

    public static String getVaultName() {
        if (vaultName == null || vaultName.length() == 0) {
            vaultName = System.getProperty("AZURE_VAULT_NAME");
        }
        return vaultName;
    }


    @BeforeClass public static void initializeVault() {
        setGlobals();
        createKey();
        createSecret();
        createCertificate();
        setSystemProperties();
    }

    @AfterClass public static void cleanUpVault(){
//        setGlobals();
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
        assertThat(certificate.getPolicy().getId(), is("https://" + getVaultName() + ".vault.azure.net/certificates/" + certificateName + "/policy"));
    }

    @Test
    public void encryptKeyTest() throws Exception {
        Object payloadValue = flowRunner("encryptKeyTest")
                .run()
                .getMessage()
                .getPayload()
                .getValue();
        Encrypt encrypt = (Encrypt) payloadValue;
        System.out.println(encrypt);
        assertThat(encrypt.getKid(), containsString("https://" + getVaultName() + ".vault.azure.net/keys"));
    }
    @Test
    public void decryptKeyTest() throws Exception {
        Object payloadValue = flowRunner("decryptKeyTest")
                .run()
                .getMessage()
                .getPayload()
                .getValue();
        Decrypt decrypt = (Decrypt) payloadValue;
        System.out.println(decrypt);
        assertThat(decrypt.getValue(), is(ENCRYPT_VALUE));
    }

    @Test
    @Ignore
    public void validTlsTest() throws Exception {
        Object payloadValue = flowRunner("validTlsTest")
                .run()
                .getMessage()
                .getPayload()
                .getValue();
        Secret secret = (Secret) payloadValue;
        System.out.println(secret);
        assertThat(secret.getValue(), is(secretValue));
    }

    @Test
    public void invalidTlsTest() throws Exception {
        try {
            Object payloadValue = flowRunner("invalidTlsTest")
                    .run()
                    .getMessage()
                    .getPayload()
                    .getValue();
            fail("Exception should have been thrown.");
        } catch (Exception e) {
            System.out.println("Found error: " + e.getMessage());
            assertThat(e.getMessage(), containsString("Error retrieving secret"));
        }
    }

    public static void createKey() {
        KeyClient keyClient = new KeyClientBuilder()
                .vaultUrl("https://" + getVaultName() + ".vault.azure.net/")
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
        System.out.println(keyClient);
        Response<KeyVaultKey> createKeyResponse = keyClient.createRsaKeyWithResponse(new CreateRsaKeyOptions(getKeyName())
                .setKeySize(2048), new Context("key1", "value1"));
        System.out.printf("Create Key operation succeeded with status code %s \n", createKeyResponse.getStatusCode());
        System.out.println("key created successfully");
    }

    public static void deleteKey(){
        KeyClient keyClient = new KeyClientBuilder()
                .vaultUrl("https://" + getVaultName() + ".vault.azure.net/")
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
        SyncPoller<DeletedKey, Void> deletedKeyPoller = keyClient.beginDeleteKey(keyName);
        PollResponse<DeletedKey> deletedKeyPollResponse = deletedKeyPoller.poll();
        DeletedKey deletedKey = deletedKeyPollResponse.getValue();
        System.out.println("Deleted Date  %s" + deletedKey.getDeletedOn().toString());
        System.out.println("key Deleted successfully");
    }

    public static void createSecret() {
        SecretClient secretClient = new SecretClientBuilder()
                .vaultUrl("https://" + getVaultName() + ".vault.azure.net/")
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
        System.out.println(secretClient);
        KeyVaultSecret secret = secretClient.setSecret(getSecretName(), getSecretValue());
        System.out.printf("Secret is created with name %s and value %s \n", secret.getName(), secret.getValue());
        System.out.println("Secret created successfully");
    }

    public static void deleteSecret(){
        SecretClient secretClient = new SecretClientBuilder()
                .vaultUrl("https://" + getVaultName() + ".vault.azure.net/")
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
        SyncPoller<DeletedSecret, Void> deletedSecretPoller = secretClient.beginDeleteSecret(secretName);
        PollResponse<DeletedSecret> deletedSecretPollResponse = deletedSecretPoller.poll();
        DeletedSecret deletedSecret = deletedSecretPollResponse.getValue();
        System.out.println("Deleted Date  %s" + deletedSecretPollResponse.getValue().getDeletedOn().toString());
        System.out.println("Secret Deleted successfully");
    }

    public static void createCertificate()  {
        CertificateClient certificateClient = new CertificateClientBuilder()
                .credential(new DefaultAzureCredentialBuilder().build())
                .vaultUrl("https://" + getVaultName() + ".vault.azure.net/")
                .httpLogOptions(new HttpLogOptions().setLogLevel(HttpLogDetailLevel.BODY_AND_HEADERS))
                .buildClient();
        CertificatePolicy certificatePolicy = new CertificatePolicy("Self",
                "CN=SelfSignedJavaPkcs12");
        SyncPoller<CertificateOperation, KeyVaultCertificateWithPolicy> certificatePoller = certificateClient
                .beginCreateCertificate(getCertificateName(), CertificatePolicy.getDefault());
        certificatePoller.waitUntil(LongRunningOperationStatus.SUCCESSFULLY_COMPLETED);
        KeyVaultCertificate certificate = certificatePoller.getFinalResult();
        System.out.printf("Certificate created with name %s", certificate.getName());
        System.out.println("Certificate created successfully");
    }

    public static void deleteCertificate(){
        CertificateClient certificateClient = new CertificateClientBuilder()
                .credential(new DefaultAzureCredentialBuilder().build())
                .vaultUrl("https://" + getVaultName() + ".vault.azure.net/")
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