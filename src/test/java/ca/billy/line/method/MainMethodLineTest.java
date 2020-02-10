package ca.billy.line.method;

import org.junit.Assert;
import org.junit.Test;

public class MainMethodLineTest {

    @Test
    public void testIsValid() {
        MainMethodLine mainMethodLine = new MainMethodLine();

        Assert.assertFalse(mainMethodLine.isValid("", null));
        Assert.assertFalse(mainMethodLine.isValid("main(", null));
        Assert.assertFalse(mainMethodLine.isValid("main)", null));
        Assert.assertFalse(mainMethodLine.isValid("mai()", null));
        Assert.assertTrue(mainMethodLine.isValid("main()", null));
    }

}
