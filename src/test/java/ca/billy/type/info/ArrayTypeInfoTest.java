package ca.billy.type.info;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.Type;
import org.junit.Test;

import ca.billy.type.EnumType;
import ca.billy.type.info.ArrayTypeInfo;
import ca.billy.type.info.BooleanTypeInfo;
import ca.billy.type.info.FloatTypeInfo;
import ca.billy.type.info.IntegerTypeInfo;
import ca.billy.type.info.StringTypeInfo;

public class ArrayTypeInfoTest {

    @Test
    public void testIsValidValue() {
        assertFalse(new ArrayTypeInfo<>(new BooleanTypeInfo(), EnumType.BOOLEAN).isValidValue("{\"test\", \"\"}"));
        assertTrue(new ArrayTypeInfo<>(new StringTypeInfo(), EnumType.STRING).isValidValue("{\"test\", \"\"}"));
    }
    
    @Test
    public void testIsValidValueWithDefaultValue() {
        assertFalse(new ArrayTypeInfo<>(new StringTypeInfo(), EnumType.STRING).isValidValue("String[1"));
        assertFalse(new ArrayTypeInfo<>(new StringTypeInfo(), EnumType.STRING).isValidValue("Boolean[1]"));
        assertTrue(new ArrayTypeInfo<>(new FloatTypeInfo(), EnumType.FLOAT).isValidValue("Float[2]"));
    }

    @Test
    public void testDefaultValue() {
        Boolean[] defaultValue = new ArrayTypeInfo<>(new BooleanTypeInfo(), EnumType.BOOLEAN).getDefaultValue();
        assertEquals(Boolean[].class, defaultValue.getClass());
        assertArrayEquals(new Boolean[0], defaultValue);
    }

    @Test
    public void testGetValue() {
        Float[] floats = new ArrayTypeInfo<>(new FloatTypeInfo(), EnumType.FLOAT).getValue(" { 12.3, 123.0,  3f } ");
        assertEquals(Float[].class, floats.getClass());
        assertArrayEquals(new Float[] { 12.3f, 123f, 3f }, floats);

        String[] strings = new ArrayTypeInfo<>(new StringTypeInfo(), EnumType.STRING).getValue(" { \"test1\",   \"test2\"} ");
        assertEquals(String[].class, strings.getClass());
        assertArrayEquals(new String[] { "test1", "test2" }, strings);

        Integer[] ints = new ArrayTypeInfo<>(new IntegerTypeInfo(), EnumType.INTEGER).getValue("{}");
        assertEquals(Integer[].class, ints.getClass());
        assertArrayEquals(new Integer[] {}, ints);
    }
    
    @Test
    public void testGetValueWithDefaultValue() {
        Float[] floats = new ArrayTypeInfo<>(new FloatTypeInfo(), EnumType.FLOAT).getValue("Float[3]");
        assertEquals(Float[].class, floats.getClass());
        assertArrayEquals(new Float[] {  0.0f, 0f, 0.000f }, floats);
        
        Integer[] integers = new ArrayTypeInfo<>(new IntegerTypeInfo(), EnumType.INTEGER).getValue("Integer[5]");
        assertEquals(Integer[].class, integers.getClass());
        assertArrayEquals(new Integer[] { 0, 0, 0, 0 ,0 }, integers);
        
        Boolean[] booleans = new ArrayTypeInfo<>(new BooleanTypeInfo(), EnumType.BOOLEAN).getValue("Boolean[0]");
        assertEquals(Boolean[].class, booleans.getClass());
        assertArrayEquals(new Boolean[] {}, booleans);
    }

    @Test
    public void testGetName() {
        assertEquals("Integer[]", new ArrayTypeInfo<>(new IntegerTypeInfo(), EnumType.INTEGER).getName());
    }

    @Test
    public void testGetBcelType() {
        assertEquals(new ArrayType(Type.STRING, 1), new ArrayTypeInfo<>(new StringTypeInfo(), EnumType.STRING).getBcelType());
    }

}
