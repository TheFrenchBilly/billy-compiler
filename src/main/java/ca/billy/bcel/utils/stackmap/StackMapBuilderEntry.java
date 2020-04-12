package ca.billy.bcel.utils.stackmap;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.StackMapEntry;
import org.apache.bcel.classfile.StackMapType;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Type;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class StackMapBuilderEntry {

    protected BranchHandle branchHandle;

    protected Type[] typesOfLocals;

    protected Type[] typesOfStackItems;

    public StackMapEntry build(StackMapBuilderEntry previous, ConstantPoolGen cp, StackMapBuilder stackMapBuilder) {
        int localDiff = stackMapBuilder.adjustLocals(typesOfLocals);
        int offset = getOffset(previous);
        if (localDiff == 0) {
            if (typesOfStackItems.length == 0) {
                return buildSameFrame(offset, cp);
            } else if (typesOfStackItems.length == 1) {
                return buildSameLocal1StackFrame(offset, cp);
            }
        } else if (localDiff <= 3 && typesOfStackItems.length == 0) {
            return buildAppendFrame(offset, localDiff, cp);
        }

        /** FIXME */
        return buildFullFrame(offset, cp);

        // throw new IllegalArgumentException("Unsuported stack map frame");
    }

    int getTargetPosition() {
        return branchHandle.getTarget().getPosition();
    }

    private StackMapEntry buildAppendFrame(int offset, int localDiff, ConstantPoolGen cp) {
        Type[] typesOfLocals = new Type[localDiff];
        for (int i = localDiff - 1; i > -1; --i) {
            typesOfLocals[i] = this.typesOfLocals[(this.typesOfLocals.length - localDiff) + i];
        }
        return new StackMapEntry(Const.APPEND_FRAME + localDiff - 1, offset, getStackMapType(typesOfLocals, cp), new StackMapType[0], cp.getConstantPool());
    }

    private StackMapEntry buildSameLocal1StackFrame(int offset, ConstantPoolGen cp) {
        StackMapType[] typesOfStackItems = getStackMapType(this.typesOfStackItems, cp);
        if (offset > 63) {
            return new StackMapEntry(Const.SAME_LOCALS_1_STACK_ITEM_FRAME_EXTENDED, offset, new StackMapType[0], typesOfStackItems, cp.getConstantPool());
        }
        return new StackMapEntry(Const.SAME_LOCALS_1_STACK_ITEM_FRAME + offset, 0, new StackMapType[0], typesOfStackItems, cp.getConstantPool());
    }

    private StackMapEntry buildSameFrame(int offset, ConstantPoolGen cp) {
        if (offset > 63) {
            return new StackMapEntry(Const.SAME_FRAME_EXTENDED, offset, new StackMapType[0], new StackMapType[0], cp.getConstantPool());
        }
        return new StackMapEntry(Const.SAME_FRAME + offset, 0, new StackMapType[0], new StackMapType[0], cp.getConstantPool());
    }

    private StackMapEntry buildFullFrame(int offset, ConstantPoolGen cp) {
        StackMapType[] typesOfLocals = getStackMapType(this.typesOfLocals, cp);
        StackMapType[] typesOfStackItems = getStackMapType(this.typesOfStackItems, cp);

        return new StackMapEntry(Const.FULL_FRAME, offset, typesOfLocals, typesOfStackItems, cp.getConstantPool());
    }

    private int getOffset(StackMapBuilderEntry previous) {
        return previous == null ? getTargetPosition()  : getTargetPosition() - 1 - previous.getTargetPosition();
    }

    private StackMapType[] getStackMapType(Type[] types, ConstantPoolGen cp) {
        StackMapType[] stackMapTypes = new StackMapType[types.length];
        for (int i = 0; i < types.length; ++i) {
            stackMapTypes[i] = getStackMapType(types[i], cp);
        }
        return stackMapTypes;
    }

    private StackMapType getStackMapType(Type type, ConstantPoolGen cp) {

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
