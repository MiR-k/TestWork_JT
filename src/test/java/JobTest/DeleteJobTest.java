package JobTest;

import apicore.BaseTest;
import org.testng.annotations.Test;
import pages.JobsAPI;

import java.io.IOException;

/**
 * This test check the response api Jenkins
 *
 */
public class DeleteJobTest extends BaseTest {

    @Test
    public void runTest() throws IOException {

        JobsAPI jobsAPI = new JobsAPI();

        jobsAPI.delete("JobTestTo011");
        jobsAPI.delete("JobTestTo021");
    }
}
