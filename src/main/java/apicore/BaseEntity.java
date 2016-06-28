package apicore;

import apicore.helpers.Logger;

import static org.testng.AssertJUnit.assertTrue;

public abstract class BaseEntity {

    protected static int stepNumber = 1;
    protected static Logger logger = Logger.getInstance();

    /**
     * Format message.
     *
     * @param message
     *            message
     * @return null
     */
    protected abstract String formatLogMsg(String message);

    /**
     * Informative message.
     *
     * @param message
     *            Message
     */
    protected void info(final String message) {
        logger.info(formatLogMsg(message));
    }

    /**
     * Warning.
     *
     * @param message
     *            Message
     */
    protected void warn(final String message) {
        logger.warn(formatLogMsg(message));
    }

    /**
     * Error message without stopping the test.
     *
     * @param message
     *            Message
     */
    protected void error(final String message) {
        logger.error(formatLogMsg(message));
    }

    protected static String getLoc(final String key) {
        return Logger.getLoc(key);
    }

    /**
     * Fatal error message.
     *
     * @param message
     *            Message
     */
    protected void fatal(final String message) {
        logger.fatal(formatLogMsg(message));
        assertTrue(formatLogMsg(message), false);
    }

    /**
     * Logging a step number.
     *
     * @param step
     *            - step number
     */
    public static void logStep(final int step) {
        logger.step(step);
    }

    /**
     * Logging a several steps in a one action
     *
     * @param fromStep
     *            - the first step number to be logged
     * @param toStep
     *            - the last step number to be logged
     */
    public void logStep(final int fromStep, final int toStep) {
        logger.step(fromStep, toStep);
    }

    // ==============================================================================================
    // Asserts

    /**
     * Universal method
     *
     * @param isTrue
     *            Condition
     * @param passMsg
     *            Positive message
     * @param failMsg
     *            Negative message
     */
    public void doAssert(final Boolean isTrue, final String passMsg,
                         final String failMsg) {
        if (isTrue) {
            info(passMsg);
        } else {
            fatal(failMsg);
        }
    }
}
