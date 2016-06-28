package ViewTest;

import apicore.BaseTest;
import apicore.helpers.Procedure;
import pages.ViewsAPI;

/**
 * This test check the response api Jenkins
 *
 */
public class DeleteViewTest extends BaseTest{

    public void runTest() {

        ViewsAPI viewsAPI = new ViewsAPI();
        viewsAPI.create(props.getProperty("viewname"));

        logStep(1,2);
        viewsAPI.checkNum();

        logStep(3);
        viewsAPI.delete(props.getProperty("viewname"));

        logStep(4,5);
        viewsAPI.check(props.getProperty("viewname"), Procedure.removal.getValue());

    }
}
