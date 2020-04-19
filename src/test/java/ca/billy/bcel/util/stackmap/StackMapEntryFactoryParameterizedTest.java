package ca.billy.bcel.util.stackmap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.StackMapEntry;
import org.apache.bcel.classfile.StackMapType;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Type;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import ca.billy.bcel.utils.stackmap.StackMapEntryFactory;

@RunWith(Parameterized.class)
public class StackMapEntryFactoryParameterizedTest {

    @Parameters(name = "{0}")
    public static Iterable<Object[]> data() {
        List<Object[]> list = new ArrayList<>();
        list.add(new Object[] { "Same Frame - 1 offset", getTypes(0), getTypes(0), 0, 1, createExpected(Const.SAME_FRAME + 1, 0, 0, 0, 0) });
        list.add(new Object[] { "Same Frame - 63 offset", getTypes(0), getTypes(0), 0, 63, createExpected(Const.SAME_FRAME + 63, 0, 0, 0, 0) });
        list.add(new Object[] { "Same Frame - 64 offset", getTypes(0), getTypes(0), 0, 64, createExpected(Const.SAME_FRAME_EXTENDED, 64, 0, 0, 0) });
        list.add(new Object[] { "Same Frame - 120 offset", getTypes(2), getTypes(0), 0, 120, createExpected(Const.SAME_FRAME_EXTENDED, 120, 0, 0, 0) });

        list.add(new Object[] { "Same Local 1 stack - 1 offset", getTypes(0), getTypes(1), 0, 1, createExpected(Const.SAME_LOCALS_1_STACK_ITEM_FRAME + 1, 0, 0, 1, 0) });
        list.add(new Object[] { "Same Local 1 stack - 63 offset", getTypes(0), getTypes(1), 0, 63, createExpected(Const.SAME_LOCALS_1_STACK_ITEM_FRAME + 63, 0, 0, 1, 0) });
        list.add(new Object[] { "Same Local 1 stack - 64 offset", getTypes(0), getTypes(1), 0, 64, createExpected(Const.SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED, 64, 0, 1, 0) });
        list.add(new Object[] { "Same Local 1 stack - 120 offset", getTypes(0), getTypes(1), 0, 120, createExpected(Const.SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED, 120, 0, 1, 0) });

        list.add(new Object[] { "Append Frame - 1 - 233 offset", getTypes(5), getTypes(0), 1, 233, createExpected(Const.APPEND_FRAME, 233, 1, 0, 4) });
        Stream.of(1, 2, 3).forEach(i -> {
            list.add(new Object[] { "Append Frame - " + i, getTypes(i), getTypes(0), i, i * 3, createExpected(Const.APPEND_FRAME + i - 1, i * 3, i, 0, 0) });
        });

        list.add(new Object[] { "Chop Frame - 1 - 233 offset", getTypes(1), getTypes(0), -1, 233, createExpected(Const.CHOP_FRAME + 2, 233, 0, 0, 0) });
        Stream.of(1, 2, 3).forEach(i -> {
            list.add(new Object[] { "Chop Frame - " + i, getTypes(5 - i), getTypes(0), -i, i * 2, createExpected(Const.CHOP_FRAME + 3 - i, i * 2, 0, 0, 0) });
        });

        list.add(new Object[] { "Full Frame - 0 Locals - 2 Stack", getTypes(0), getTypes(2), 0, 999, createExpected(Const.FULL_FRAME, 999, 0, 2, 0) });
        list.add(new Object[] { "Full Frame - 4 Locals - 0 Stack", getTypes(4), getTypes(0), 4, 123, createExpected(Const.FULL_FRAME, 123, 4, 0, 0) });
        list.add(new Object[] { "Full Frame - 5 Locals - 0 Stack", getTypes(5), getTypes(0), 4, 321, createExpected(Const.FULL_FRAME, 321, 5, 0, 0) });
        for (int l : new int[] { 1, 2, 3, 4, 5 }) {
            for (int s : new int[] { 1, 2, 3, 4, 5 }) {
                int offset = l * 2 + s * 3;
                list.add(
                        new Object[] { "Full Frame - " + l + " Locals - " + s + " Stack", getTypes(l), getTypes(s), 1, offset, createExpected(Const.FULL_FRAME, offset, l, s, 0) });
            }
        }

        return list;
    }

    private static Type[] getTypes(int i) {
        Type[] types = new Type[i];
        for (int y = 0; y < i; ++y) {
            types[y] = staticTypes[y];
        }
        return types;
    }

    private static StackMapEntry createExpected(int frameType, int byteCodeOffset, int nbLocals, int nbStacks, int start) {
        return new StackMapEntry(frameType, byteCodeOffset, getStackMapType(nbLocals, start), getStackMapType(nbStacks, 0), null);
    }

    private static StackMapType[] getStackMapType(int i, int start) {
        StackMapType[] types = new StackMapType[i];
        for (int y = 0; y < i; ++y) {
            types[y] = staticEntryTypes[y + start];
        }
        return types;
    }

    private static Type[] staticTypes = { Type.INT, Type.BOOLEAN, Type.FLOAT, Type.STRING, new ArrayType(Type.INT, 1) };
    private static StackMapType[] staticEntryTypes = { new StackMapType(Const.ITEM_Integer, 0, null), new StackMapType(Const.ITEM_Integer, 1, null),
            new StackMapType(Const.ITEM_Float, 2, null), new StackMapType(Const.ITEM_Object, 3, null), new StackMapType(Const.ITEM_Object, 4, null) };

    private Type[] locals;
    private Type[] stacks;
    private int localDiff;
    private int offset;
    private StackMapEntry expected;

    public StackMapEntryFactoryParameterizedTest(String name, Type[] locals, Type[] stacks, int localDiff, int offset, StackMapEntry expected) {
        this.locals = locals;
        this.stacks = stacks;
        this.localDiff = localDiff;
        this.offset = offset;
        this.expected = expected;
    }

    @Test
    public void testBuild() {
        StackMapEntry entry = StackMapEntryFactory.create(locals, stacks, localDiff, offset, new ConstantPoolGen());

        Assert.assertEquals("Invalid frame type", expected.getFrameType(), entry.getFrameType());
        Assert.assertEquals("Invalid bytecode offset", expected.getByteCodeOffset(), entry.getByteCodeOffset());

        Assert.assertEquals("Invalid locals length", expected.getTypesOfLocals().length, entry.getTypesOfLocals().length);
        for (int i = 0; i < expected.getTypesOfLocals().length; ++i) {
            Assert.assertEquals("Invalid local type " + i, expected.getTypesOfLocals()[i].getType(), entry.getTypesOfLocals()[i].getType());
        }

        Assert.assertEquals("Invalid stack length", expected.getTypesOfStackItems().length, entry.getTypesOfStackItems().length);
        for (int i = 0; i < expected.getTypesOfStackItems().length; ++i) {
            Assert.assertEquals("Invalid stack type " + i, expected.getTypesOfStackItems()[i].getType(), entry.getTypesOfStackItems()[i].getType());
        }
    }

}
