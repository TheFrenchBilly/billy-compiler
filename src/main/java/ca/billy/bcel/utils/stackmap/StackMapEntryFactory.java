package ca.billy.bcel.utils.stackmap;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.StackMapEntry;
import org.apache.bcel.classfile.StackMapType;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Type;

public class StackMapEntryFactory {

    public static StackMapEntry create(Type[] typesOfLocals, Type[] typesOfStacks, int localDiff, int offset, ConstantPoolGen cp) {

        if (localDiff == 0) {
            if (typesOfStacks.length == 0) {
                return buildSameFrame(offset, cp);
            } else if (typesOfStacks.length == 1) {
                return buildSameLocal1StackFrame(typesOfStacks, offset, cp);
            }
        } else if (localDiff > 0 && localDiff <= 3 && typesOfStacks.length == 0) {
            return buildAppendFrame(typesOfLocals, offset, localDiff, cp);
        } else if (localDiff < 0 && localDiff >= -3 && typesOfStacks.length == 0) {
            return buildChopeFrame(offset, localDiff, cp);
        }

        return buildFullFrame(typesOfLocals, typesOfStacks, offset, cp);
    }

    private static StackMapEntry buildSameFrame(int offset, ConstantPoolGen cp) {
        if (offset > 63) {
            return new StackMapEntry(Const.SAME_FRAME_EXTENDED, offset, new StackMapType[0], new StackMapType[0], cp.getConstantPool());
        }
        return new StackMapEntry(Const.SAME_FRAME + offset, 0, new StackMapType[0], new StackMapType[0], cp.getConstantPool());
    }

    private static StackMapEntry buildAppendFrame(Type[] typesOfLocals, int offset, int localDiff, ConstantPoolGen cp) {
        Type[] locals = new Type[localDiff];
        for (int i = localDiff - 1; i > -1; --i) {
            locals[i] = typesOfLocals[(typesOfLocals.length - localDiff) + i];
        }
        return new StackMapEntry(Const.APPEND_FRAME + localDiff - 1, offset, getStackMapType(locals, cp), new StackMapType[0], cp.getConstantPool());
    }

    private static StackMapEntry buildSameLocal1StackFrame(Type[] typesOfStacks, int offset, ConstantPoolGen cp) {
        StackMapType[] typesOfStackItems = getStackMapType(typesOfStacks, cp);
        if (offset > 63) {
            return new StackMapEntry(Const.SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED, offset, new StackMapType[0], typesOfStackItems, cp.getConstantPool());
        }
        return new StackMapEntry(Const.SAME_LOCALS_1_STACK_ITEM_FRAME + offset, 0, new StackMapType[0], typesOfStackItems, cp.getConstantPool());
    }

    private static StackMapEntry buildChopeFrame(int offset, int localDiff, ConstantPoolGen cp) {
        return new StackMapEntry(Const.CHOP_FRAME + 3 + localDiff, offset, new StackMapType[0], new StackMapType[0], cp.getConstantPool());
    }

    private static StackMapEntry buildFullFrame(Type[] typesOfLocals, Type[] typesOfStacks, int offset, ConstantPoolGen cp) {
        StackMapType[] locals = getStackMapType(typesOfLocals, cp);
        StackMapType[] stacks = getStackMapType(typesOfStacks, cp);

        return new StackMapEntry(Const.FULL_FRAME, offset, locals, stacks, cp.getConstantPool());
    }

    private static StackMapType[] getStackMapType(Type[] types, ConstantPoolGen cp) {
        StackMapType[] stackMapTypes = new StackMapType[types.length];
        for (int i = 0; i < types.length; ++i) {
            stackMapTypes[i] = getStackMapType(types[i], cp);
        }
        return stackMapTypes;
    }

    private static StackMapType getStackMapType(Type type, ConstantPoolGen cp) {

        if (type.equals(BasicType.BOOLEAN)) {
            return new StackMapType(Const.ITEM_Integer, cp.addUtf8(BasicType.BOOLEAN.getSignature()), cp.getConstantPool());
        } else if (type.equals(BasicType.INT)) {
            return new StackMapType(Const.ITEM_Integer, cp.addUtf8(BasicType.INT.getSignature()), cp.getConstantPool());
        } else if (type.equals(BasicType.FLOAT)) {
            return new StackMapType(Const.ITEM_Float, cp.addUtf8(BasicType.FLOAT.getSignature()), cp.getConstantPool());
        } else if (type.equals(Type.STRING)) {
            return new StackMapType(Const.ITEM_Object, cp.addClass(Type.STRING), cp.getConstantPool());
        } else if (type instanceof ArrayType) {
            return new StackMapType(Const.ITEM_Object, cp.addArrayClass((ArrayType) type), cp.getConstantPool());
        }

        throw new IllegalStateException("Should not happen");
    }
}
