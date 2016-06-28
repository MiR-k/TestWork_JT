package JobTest;

import apicore.BaseTest;
import apicore.Procedure;
import pages.JobsAPI;

/**
 * This test check the response api Jenkins
 *
 */
public class DeleteJobTest extends BaseTest {

    public void runTest() {

        JobsAPI jobsAPI = new JobsAPI();
        jobsAPI.create(props.getProperty("jobnamefirst"));

        logStep(1,2);
        jobsAPI.checkNum();

        logStep(3);
        jobsAPI.delete(props.getProperty("jobnamefirst"));

        logStep(4,5);
        jobsAPI.check(props.getProperty("jobnamefirst"), Procedure.removal.getValue());
    }
}
