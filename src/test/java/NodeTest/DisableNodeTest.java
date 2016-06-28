package NodeTest;

import apicore.BaseTest;
import pages.NodeAPI;

/**
 * This test check the response api Jenkins
 * when the master node is paused
 */
public class DisableNodeTest extends BaseTest{

    public void runTest() {
        NodeAPI nodeAPI = new NodeAPI();
        nodeAPI.create(props.getProperty("nodename"));

        logStep(1,2);
        nodeAPI.checkSatus(props.getProperty("nodename"));

        logStep(3);
        nodeAPI.disable(props.getProperty("nodename"));

        logStep(4,5);
        nodeAPI.checkSatus(props.getProperty("nodename"));

        info("Завершение работы");
        nodeAPI.delete(props.getProperty("nodename"));
    }
}
