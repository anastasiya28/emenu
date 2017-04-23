package applicationTest;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;


public class TestUtils {
    private static final Logger logger = Logger.getLogger(TestUtils.class);

    public static void assertNotNull(Object obj, String message) {
        logger.info("assertNotNull: " + message);
        TestCase.assertNotNull(obj);
    }

    public static void assertNull(Object obj, String message) {
        logger.info("assertNull: " + message);
        TestCase.assertNull(obj);
    }

    public static void assertThat(Object obj, Matcher matcher, String message) {
        logger.info("assertThat: " + message);
        MatcherAssert.assertThat(obj, matcher);
    }

    public static void assertEquals(Object expectedObj, Object actualObj, String message) {
        logger.info("assertEquals: " + message);
        logger.debug("assertEquals: Expected: " + expectedObj + " Actual: " + actualObj);
        Assert.assertEquals(expectedObj, actualObj);
    }

    public static void assertNotEquals(Object expectedObj, Object actualObj, String message) {
        logger.info("assertNotEquals: " + message);
        logger.debug("assertNotEquals: Expected: " + expectedObj + " Actual: " + actualObj);
        Assert.assertNotEquals(expectedObj, actualObj);
    }

    public static void assertArrayEquals(Object[] expecteds, Object[] actuals, String message) {
        logger.info("assertEquals: " + message);
        logger.debug("assertEquals: Expected: " + expecteds + " Actual: " + actuals);
        Assert.assertArrayEquals(expecteds, actuals);
    }

}
