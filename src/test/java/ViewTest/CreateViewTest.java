package ViewTest;

import apicore.BaseTest;
import apicore.helpers.Procedure;
import pages.ViewsAPI;

/**
 * This test check the response api Jenkins
 *
 */
public class CreateViewTest extends BaseTest {

    public void runTest() {

        logStep(1,2);
        ViewsAPI viewsAPI = new ViewsAPI();
        viewsAPI.checkNum();

        logStep(3);
        viewsAPI.create(props.getProperty("viewname"));

        logStep(4,5);
        viewsAPI.check(props.getProperty("viewname"), Procedure.creation.getValue());

        info(">>>завершние работы<<<");
        viewsAPI.delete(props.getProperty("viewname"));
    }
}
