package JobTest;

import apicore.BaseTest;
import apicore.helpers.Procedure;
import pages.JobsAPI;

/**
 * This test check the response api Jenkins
 * create
 */
public class CreateJobTest extends BaseTest{


    public void runTest() {

        logStep(1,2);
        JobsAPI jobsAPI = new JobsAPI();
        jobsAPI.checkNum();

        logStep(3);
        jobsAPI.create(props.getProperty("jobnamefirst"));

        logStep(4,5);
        jobsAPI.check(props.getProperty("jobnamefirst"), Procedure.creation.getValue());

        info(">>>завершние работы<<<");
        jobsAPI.delete(props.getProperty("jobnamefirst"));
    }
}
