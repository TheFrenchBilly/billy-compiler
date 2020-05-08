package ca.billy.type;

import org.junit.Assert;
import org.junit.Test;


public class EnumTypeTest {
    
    @Test
    public void testTypeMatch() {
        Assert.assertFalse(EnumType.BOOLEAN.typeMatch(null));   
        Assert.assertFalse(EnumType.STRING.typeMatch(EnumType.INTEGER));
        Assert.assertTrue(EnumType.FLOAT_ARRAY.typeMatch(EnumType.FLOAT_ARRAY));
    }
    
    @Test
    public void testTypeMatchAny() {
        Assert.assertFalse(EnumType.ANY.typeMatch(null));        
        Assert.assertTrue(EnumType.ANY.typeMatch(EnumType.STRING_ARRAY));
        Assert.assertTrue(EnumType.ANY_ARRAY.typeMatch(EnumType.ANY));
    }
    
    @Test
    public void testTypeMatchAnyArray() {
        Assert.assertFalse(EnumType.ANY_ARRAY.typeMatch(null));
        Assert.assertFalse(EnumType.ANY_ARRAY.typeMatch(EnumType.BOOLEAN));
        Assert.assertFalse(EnumType.FLOAT.typeMatch(EnumType.ANY_ARRAY));
        Assert.assertTrue(EnumType.ANY_ARRAY.typeMatch(EnumType.INTEGER_ARRAY));
        Assert.assertTrue(EnumType.BOOLEAN_ARRAY.typeMatch(EnumType.ANY_ARRAY));
        Assert.assertTrue(EnumType.ANY_ARRAY.typeMatch(EnumType.ANY_ARRAY));
    }

}
