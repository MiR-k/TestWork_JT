package JobTest;

import apicore.BaseTest;
import pages.JobsAPI;

public class BuildJobTest extends BaseTest {

    public void runTest() {

        JobsAPI jobsAPI = new JobsAPI();
        jobsAPI.create(props.getProperty("jobnamefirst"));

        logStep(1,2);
        jobsAPI.checkBuild(props.getProperty("jobnamefirst"));

        logStep(3);
        jobsAPI.build(props.getProperty("jobnamefirst"));

        logStep(4);
        jobsAPI.checkBuild(props.getProperty("jobnamefirst"));

        info(">>>завершние работы<<<");
        jobsAPI.delete(props.getProperty("jobnamefirst"));
    }
}

