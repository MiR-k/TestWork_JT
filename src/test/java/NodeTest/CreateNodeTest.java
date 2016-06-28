package NodeTest;

import apicore.BaseTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.NodeAPI;

import java.io.IOException;

public class CreateNodeTest extends BaseTest {

    @Test
    //@Parameters("TestNg")
    public void runTest() throws IOException {

        NodeAPI nodeAPI = new NodeAPI();
        nodeAPI.create("Node1");

        nodeAPI.disable("Node1");

        nodeAPI.delete("Node1");

    }
}
