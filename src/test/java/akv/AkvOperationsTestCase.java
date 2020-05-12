package akv;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.avioconsulting.mule.connector.akv.provider.client.model.Secret;
import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;
import org.junit.Test;

public class AkvOperationsTestCase extends MuleArtifactFunctionalTestCase {

  /**
   * Specifies the mule config xml with the flows that are going to be executed in the tests, this file lives in the test resources.
   */
  @Override
  protected String getConfigFile() {
    return "test-mule-config.xml";
  }

  @Test
  public void executeAuth() throws Exception {
   Object payloadValue = flowRunner("authFlow")
            .run()
            .getMessage()
            .getPayload()
            .getValue();
    Secret secret = (Secret)payloadValue;
    assertThat(secret.getValue(), is("letsgobills!"));
  }
}
