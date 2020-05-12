package akv;

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
        System.setProperty("akvUrl", akvUrl);

        mockClient
//                .withSecure(true)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/" + TENANT_ID + "/oauth2/v2.0/token")
//                                .withHeader("X-Vault-Token", "MOCK_TOKEN")
                ).respond(
                response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(LOGIN_RESPONSE)
        );

        mockClient
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/mysecret")
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
                                .withPath("/mysecretnotfound")
                                .withQueryStringParameter("api-version", "7.0")
                                .withHeader("Authorization", "Bearer MOCK_TOKEN")
                ).respond(
                response()
                        .withStatusCode(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody(SECRET_NOT_FOUND_RESPONSE)
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
    public void getSecretNotFound() throws Exception {
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
}
