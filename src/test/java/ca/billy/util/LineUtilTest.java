package ca.billy.util;

import org.junit.Assert;
import org.junit.Test;

import ca.billy.BillyException;

public class LineUtilTest {
    
    @Test
    public void testGetIndent() {
        LineUtil.setSpaceByTab(4);
        Assert.assertEquals(0, LineUtil.getIndent(""));
        Assert.assertEquals(1, LineUtil.getIndent("\t"));
        Assert.assertEquals(1, LineUtil.getIndent("    Interger i"));
        Assert.assertEquals(2, LineUtil.getIndent("\t    "));
        Assert.assertEquals(2, LineUtil.getIndent("    \t"));
        Assert.assertEquals(3, LineUtil.getIndent("\t    \t"));
    }
    
    @Test
    public void testGetIndentWithOneSpace() {
        LineUtil.setSpaceByTab(1);
        Assert.assertEquals(0, LineUtil.getIndent(""));
        Assert.assertEquals(1, LineUtil.getIndent("\t"));
        Assert.assertEquals(1, LineUtil.getIndent(" for ;;i<1;:"));
        Assert.assertEquals(2, LineUtil.getIndent("\t "));
        Assert.assertEquals(2, LineUtil.getIndent(" \t"));
        Assert.assertEquals(4, LineUtil.getIndent("\t  \t"));
    }
    
    @Test(expected = BillyException.class)
    public void testGetIndentException() {
        LineUtil.setSpaceByTab(2);
       LineUtil.getIndent(" ");
    }
}
