package NodeTest;

import apicore.BaseTest;
import apicore.Procedure;
import pages.NodeAPI;
import java.io.IOException;

public class CreateNodeTest extends BaseTest {

    public void runTest(){

        logStep(1,2);
        NodeAPI nodeAPI = new NodeAPI();
        nodeAPI.checkNum();

        logStep(3);
        nodeAPI.create(props.getProperty("nodename"));

        logStep(4,5);
        nodeAPI.check(props.getProperty("nodename"), Procedure.creation.getValue());

        info("завершние работы");
        nodeAPI.delete(props.getProperty("nodename"));
    }
}
