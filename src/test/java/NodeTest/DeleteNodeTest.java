package NodeTest;

import apicore.BaseTest;
import apicore.Procedure;
import org.testng.annotations.Test;
import pages.NodeAPI;

/**
 * This test check the response api Jenkins
 *
 */
public class DeleteNodeTest extends BaseTest{


    public void runTest() {

        NodeAPI nodeAPI = new NodeAPI();
        nodeAPI.create(props.getProperty("nodename"));

        logStep(1,2);
        nodeAPI.checkNum();

        logStep(3);
        nodeAPI.delete(props.getProperty("nodename"));

        logStep(4,5);
        nodeAPI.check(props.getProperty("nodename"), Procedure.creation.getValue());

    }
}
