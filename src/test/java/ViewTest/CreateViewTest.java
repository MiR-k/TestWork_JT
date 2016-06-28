package ViewTest;

import apicore.BaseTest;
import org.testng.annotations.Test;
import pages.ViewsAPI;

/**
 * This test check the response api Jenkins
 *
 */
public class CreateViewTest extends BaseTest {

    @Test
    public void runTest() {

        ViewsAPI viewsAPI = new ViewsAPI();
        viewsAPI.create("ViewTest01");

        logStep(2);
        viewsAPI.delete("ViewTest01");
    }
}
