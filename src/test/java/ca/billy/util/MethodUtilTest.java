package ca.billy.util;

import org.junit.Assert;
import org.junit.Test;

import ca.billy.BillyException;

public class MethodUtilTest {

    @Test
    public void testExtractMethodName() {
        Assert.assertEquals("test", MethodUtil.extractMethodName("test()"));
    }

    @Test
    public void testExtractParameters() {

        Assert.assertArrayEquals(new String[] { "1", "2", "3", "4", "5", "6" }, MethodUtil.extractParameters("test(1, 2, 3,4 ,5,6)"));

        Assert.assertArrayEquals(new String[] {}, MethodUtil.extractParameters("test()"));
        Assert.assertArrayEquals(new String[] {}, MethodUtil.extractParameters("test( )"));
    }
    
    @Test
    public void testExtractParametersWithArray() {
        Assert.assertArrayEquals(new String[] { "1", "p(2, 3,4 ,5,6)" }, MethodUtil.extractParameters("test(1, p(2, 3,4 ,5,6))"));
        Assert.assertArrayEquals(new String[] { "1", "w(2, 3,4 ,5,6  )","a(12,21)", "args" }, MethodUtil.extractParameters("test(1, w(2, 3,4 ,5,6  ), a(12,21), args)")); 
    }
        
    @Test
    public void testExtractParametersWithFunction() {       
        Assert.assertArrayEquals(new String[] { "1", "{2, 1231}" }, MethodUtil.extractParameters("test(1, {2, 1231})"));
        Assert.assertArrayEquals(new String[] { "1", "{2, 1231}", "{ 2 , 1 }" }, MethodUtil.extractParameters("test(1, {2, 1231}, { 2 , 1 } )"));
    }
    
    @Test(expected = BillyException.class)
    public void testExtractParametersWithArrayException() {
         MethodUtil.extractParameters("test({2, 1231)");
    }
    
    @Test(expected = BillyException.class)
    public void testExtractParametersWithFunctionException() {
         MethodUtil.extractParameters("test(a(2, 1231)");
    }

}
