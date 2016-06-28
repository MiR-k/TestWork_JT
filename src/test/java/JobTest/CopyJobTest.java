package JobTest;

import apicore.BaseTest;
import pages.JobsAPI;

public class CopyJobTest extends BaseTest {

    public void runTest() {

        logStep(0);
        JobsAPI jobsAPI = new JobsAPI();
        jobsAPI.create(props.getProperty("jobnamefirst"));

        logStep(1,2);
        jobsAPI.checkNum();

        logStep(3);
        jobsAPI.copy(props.getProperty("jobnamefirst"),props.getProperty("jobnamesecond"));

        logStep(4,5);
        jobsAPI.check(props.getProperty("jobnamesecond"),1);

        info("завершние работы");
        jobsAPI.delete(props.getProperty("jobnamesecond"));
        jobsAPI.delete(props.getProperty("jobnamefirst"));
    }
}
