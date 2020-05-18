package akv;

import com.avioconsulting.mule.connector.akv.provider.client.model.Certificate;
import com.avioconsulting.mule.connector.akv.provider.client.model.Key;
import com.avioconsulting.mule.connector.akv.provider.client.model.Secret;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.fail;
import static org.hamcrest.core.Is.is;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class AkvOperationsTestCase extends MuleArtifactFunctionalTestCase {
    private static final Logger logger = LoggerFactory.getLogger(AkvOperationsTestCase.class);

    private static String LOGIN_RESPONSE = "{\"token_type\":\"Bearer\",\"expires_in\":3599,\"ext_expires_in\":3599,\"access_token\":\"MOCK_TOKEN\"}";
    private static String GET_SECRET_SUCCESS_RESPONSE = "{\"value\":\"letsgobills!\",\"id\":\"https://vault.azure.net/secrets/mysecret/abc123id\",\"attributes\":{\"enabled\":true,\"created\":1588608909,\"updated\":1588608909,\"recoveryLevel\":\"Recoverable+Purgeable\"}}";
    private static String SECRET_NOT_FOUND_RESPONSE = "{\"error\":{\"code\":\"SecretNotFound\",\"message\":\"A secret with (name/id) mysecretnotfound was not found in this key vault.\"}}";
    private static String TENANT_ID = "MOCK_TENANT";
    private static String GET_KEY_SUCCESS_RESPONSE = "{\"key\":{\"kid\":\"https://test-keyvault-poc.vault.azure.net/keys/test-poc-key-avio/7500c4c4a2f041aea1b997ab1d922f78\",\"kty\":\"RSA\",\"key_ops\":[\"sign\",\"verify\",\"wrapKey\",\"unwrapKey\",\"encrypt\",\"decrypt\"],\"n\":\"mockKey\",\"e\":\"AQAB\"},\"attributes\":{\"enabled\":true,\"created\":1588608850,\"updated\":1588608850,\"recoveryLevel\":\"Recoverable+Purgeable\"}}";
    private static String KEY_NOT_FOUND_RESPONSE = "{\"error\":{\"code\":\"KeyNotFound\",\"message\":\"A key with (name/id) test-poc-key-avio1 was not found in this key vault. If you recently deleted this key you may be able to recover it using the correct recovery command. For help resolving this issue, please see https://go.microsoft.com/fwlink/?linkid=2125182\"}}";
    private static String GET_CERT_SUCCESS_RESPONSE = "{\"id\":\"https://avioconsulting.vault.azure.net/certificates/mockcertificate/42c2dd8101a34b9f93abe3d59ac72c5a\",\"kid\":\"https://avioconsulting.vault.azure.net/keys/mockcertificate/42c2dd8101a34b9f93abe3d59ac72c5a\",\"sid\":\"https://avioconsulting.vault.azure.net/secrets/mockcertificate/42c2dd8101a34b9f93abe3d59ac72c5a\",\"x5t\":\"Q8V35BLaOifkjMA-EBLhtskgpmQ\",\"cer\":\"MOCK_CERT\",\"attributes\":{\"enabled\":true,\"nbf\":1589301357,\"exp\":1620837957,\"created\":1589301957,\"updated\":1589301957,\"recoveryLevel\":\"Recoverable+Purgeable\"},\"policy\":{\"id\":\"https://avioconsulting.vault.azure.net/certificates/mockcertificate/policy\",\"key_props\":{\"exportable\":true,\"kty\":\"RSA\",\"key_size\":2048,\"reuse_key\":false},\"secret_props\":{\"contentType\":\"application/x-pkcs12\"},\"x509_props\":{\"subject\":\"CN=*.microsoft.com\",\"sans\":{\"dns_names\":[\"onedrive.microsoft.com\",\"xbox.microsoft.com\"]},\"ekus\":[\"1.3.6.1.5.5.7.3.1\",\"1.3.6.1.5.5.7.3.2\"],\"key_usage\":[\"digitalSignature\",\"keyEncipherment\"],\"validity_months\":12,\"basic_constraints\":{\"ca\":false}},\"lifetime_actions\":[{\"trigger\":{\"lifetime_percentage\":80},\"action\":{\"action_type\":\"AutoRenew\"}}],\"issuer\":{\"name\":\"Self\"},\"attributes\":{\"enabled\":true,\"created\":1589301942,\"updated\":1589301942}},\"pending\":{\"id\":\"https://avioconsulting.vault.azure.net/certificates/mockcertificate/pending\"}}";
    private static String CERT_NOT_FOUND_RESPONSE = "{\"error\":{\"code\":\"CertificateNotFound\",\"message\":\"A certificate with (name/id) mockcertificatenotfound was not found in this key vault. If you recently deleted this certificate you may be able to recover it using the correct recovery command. For help resolving this issue, please see https://go.microsoft.com/fwlink/?linkid=2125182\"}}";

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this);
    private MockServerClient mockClient;

    /**
    * Specifies the mule config xml with the flows that are going to be executed in the tests, this file lives in the test resources.
    */
    @Override
    protected String getConfigFile() {
        String akvUrl = String.format("http://%s:%d/", mockServerRule.getClient().remoteAddress().getHostString(), mockServerRule.getClient().remoteAddress().getPort());
        logger.info("getConfigFile:: AKVault Url: " + akvUrl);
        System.setProperty("AKV_TEST_URL", akvUrl);

        // Auth
        mockClient
//                .withSecure(true)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/" + TENANT_ID + "/oauth2/v2.0/token")
                ).respond(
                response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(LOGIN_RESPONSE)
        );

        // Secret Success
        mockClient
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/test-secret")
                                .withQueryStringParameter("api-version", "7.0")
                                .withHeader("Authorization", "Bearer MOCK_TOKEN")
                ).respond(
                response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(GET_SECRET_SUCCESS_RESPONSE)
        );

        // Secret Not Found
        mockClient
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/test-secret-notfound")
                                .withQueryStringParameter("api-version", "7.0")
                                .withHeader("Authorization", "Bearer MOCK_TOKEN")
                ).respond(
                response()
                        .withStatusCode(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody(SECRET_NOT_FOUND_RESPONSE)
        );

        //mock success Get Key
        mockClient
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/test-key")
                                .withQueryStringParameter("api-version", "7.0")
                                .withHeader("Authorization", "Bearer MOCK_TOKEN")
                ).respond(
                response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(GET_KEY_SUCCESS_RESPONSE)
        );

        //mock Key Not Found
        mockClient
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/test-key-notfound")
                                .withQueryStringParameter("api-version", "7.0")
                                .withHeader("Authorization", "Bearer MOCK_TOKEN")
                ).respond(
                response()
                        .withStatusCode(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody(KEY_NOT_FOUND_RESPONSE)
        );

        //mock success Get Certificate
        mockClient
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/test-certificate/completed")
                                .withQueryStringParameter("api-version", "7.0")
                                .withHeader("Authorization", "Bearer MOCK_TOKEN")
                ).respond(
                response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(GET_CERT_SUCCESS_RESPONSE)
        );

        //mock Certificate Not Found
        mockClient
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/test-certificate-notfound/completed")
                                .withQueryStringParameter("api-version", "7.0")
                                .withHeader("Authorization", "Bearer MOCK_TOKEN")
                ).respond(
                response()
                        .withStatusCode(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody(CERT_NOT_FOUND_RESPONSE)
        );
        return "test-mule-config.xml";
    }

    @Test
    public void getSecretSuccess() throws Exception {
    Object payloadValue = flowRunner("getSecretSuccess")
            .run()
            .getMessage()
            .getPayload()
            .getValue();
    Secret secret = (Secret)payloadValue;
    assertThat(secret.getValue(), is("letsgobills!"));
    }

    @Test
    public void SecretNotFoundget() throws Exception {
        try {
            Object payloadValue = flowRunner("getSecretNotFound")
                    .run()
                    .getMessage()
                    .getPayload()
                    .getValue();
            fail("Exception not thrown.");
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("was not found in this key vault."));
        }
    }

    @Test
    public void getKeySuccess() throws Exception {
        Object payloadValue = flowRunner("getKeySuccess")
                .run()
                .getMessage()
                .getPayload()
                .getValue();
        Key key = (Key)payloadValue;
        System.out.println(key);
        assertThat(key.getKey().getN(), is("mockKey"));
    }

    @Test
    public void getKeyNotFound() throws Exception {
        try {
            Object payloadValue = flowRunner("getKeyNotFound")
                    .run()
                    .getMessage()
                    .getPayload()
                    .getValue();
            fail("Exception not thrown.");
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("was not found in this key vault."));
        }
    }

    @Test
    public void getCertSuccess() throws Exception {
        Object payloadValue = flowRunner("getCertificateSuccess")
                .run()
                .getMessage()
                .getPayload()
                .getValue();
        Certificate cert = (Certificate) payloadValue;
        System.out.println(cert);
        assertThat(cert.getCertificate(), is("MOCK_CERT"));
    }

    @Test
    public void getCertNotFound() throws Exception {
        try {
            Object payloadValue = flowRunner("getCertificateNotFound")
                    .run()
                    .getMessage()
                    .getPayload()
                    .getValue();
            fail("Exception not thrown.");
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("was not found in this key vault."));
        }
    }
}
