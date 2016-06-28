package JobTest;


import apicore.BaseTest;
import org.testng.annotations.Test;
import pages.JobsAPI;

public class BuildJobTest extends BaseTest {

    @Test
    public void runTest() {
        int i = 0;
        System.out.println(++i);
        JobsAPI jobsAPI = new JobsAPI();
        jobsAPI.create("JobTestTo011");

        System.out.println(++i);
        jobsAPI.disable("toJobTest011");
    }
}

