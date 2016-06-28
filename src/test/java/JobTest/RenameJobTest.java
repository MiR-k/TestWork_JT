package JobTest;

import apicore.BaseTest;
import pages.JobsAPI;

/**
 * This test check the response api Jenkins
 * rename Job
 */
public class RenameJobTest extends BaseTest {

    public void runTest() {

        JobsAPI jobsAPI = new JobsAPI();
        jobsAPI.create(props.getProperty("jobnamefirst"));

        logStep(1,2);
        jobsAPI.checkNum();

        logStep(3);
        jobsAPI.rename(props.getProperty("jobnamefirst"), props.getProperty("jobremame"));

        logStep(4);
        jobsAPI.check(props.getProperty("jobremame"),0);

        info("завершние работы");
        jobsAPI.delete(props.getProperty("jobremame"));

    }
}
