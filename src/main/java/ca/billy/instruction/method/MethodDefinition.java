package ca.billy.instruction.method;

import org.apache.bcel.Const;
import org.apache.bcel.generic.Type;

import ca.billy.instruction.modifiers.AccessModifiers;
import ca.billy.type.EnumType;
import lombok.Getter;

public class MethodDefinition {

    // TODO Not working now
    @Getter
    private AccessModifiers accessModifiers;

    @Getter
    private String name;

    @Getter
    private EnumType returnEnumType;

    // TODO Not working now
    @Getter
    private Type[] args;

    private boolean isStatic;

    public MethodDefinition(String name) {
        this(name, null, new EnumType[0]);
    }

    public MethodDefinition(String name, EnumType returnType, EnumType... args) {
        this.name = name;
        this.returnEnumType = returnType;
        this.args = convertType(args);
        this.accessModifiers = AccessModifiers.PUBLIC;
    }

    public Type getReturnType() {
        return returnEnumType == null ? Type.VOID : returnEnumType.getBcelType();
    }

    public MethodDefinition setStatic() {
        isStatic = true;
        return this;
    }

    int getAccessFlags() {
        if (isStatic) {
            return accessModifiers.getFlag() | Const.ACC_STATIC;
        }

        return accessModifiers.getFlag();
    }

    MethodDefinition withArgs(Type[] args) {
        this.args = args;
        return this;
    }

    private Type[] convertType(EnumType[] args) {
        Type[] types = new Type[args.length];
        for (int i = 0; i < args.length; ++i) {
            types[i] = args[i].getBcelType();
        }
        return types;
    }

}
