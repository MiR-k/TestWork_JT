package apicore;

import apicore.helpers.PropertiesResourceManager;
import org.testng.annotations.Test;

public abstract class BaseTest extends BaseEntity{

    private static final String PROPERTIES_FILE = "testdata.properties";

    protected static PropertiesResourceManager props;
    /**
     * To override.
     */
    public abstract void runTest() throws Throwable;

    private void initProperty(){
        props = new PropertiesResourceManager(PROPERTIES_FILE);
    }
    /**
     * Test
     * @throws Throwable Throwable
     */
    @Test
    public void xTest() throws Throwable {
        Class<? extends BaseTest> currentClass = this.getClass();
        try {
            logger.logTestName(currentClass.getName());
            initProperty();
            runTest();
            logger.logTestEnd(currentClass.getName());
        } catch (Throwable e) {
            logger.warn("");
            logger.warnRed(getLoc("loc.test.failed"));
            logger.warn("");
            logger.fatal(e.getMessage());
        }
    }

    /**
     *
     * @param message String
     * @return String message
     */
    protected String formatLogMsg(String message) {
        return message;
    }
}
