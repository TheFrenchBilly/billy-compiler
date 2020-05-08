package ca.billy.bcel.utils;

import java.util.List;

import org.apache.bcel.generic.Type;

import ca.billy.type.EnumType;

public class TypeUtil {

    public static Type[] convertType(EnumType[] args) {
        Type[] types = new Type[args.length];
        for (int i = 0; i < args.length; ++i) {
            types[i] = args[i].getBcelType();
        }
        return types;
    }

    public static Type[] convertType(List<EnumType> args) {
        return args.stream().map(t -> t.getBcelType()).toArray(Type[]::new);
    }

    public static Type[] merge(List<EnumType> args1, EnumType[] args2) {
        Type[] types = new Type[args1.size() + args2.length];
        for (int i = 0; i < args1.size(); ++i) {
            types[i] = args1.get(i).getBcelType();
        }
        for (int i = 0; i < args2.length; ++i) {
            types[i + args1.size()] = args2[i].getBcelType();
        }
        return types;
    }

    public static Type[] merge(EnumType[] args1, EnumType[] args2) {
        Type[] types = new Type[args1.length + args2.length];
        for (int i = 0; i < args1.length; ++i) {
            types[i] = args1[i].getBcelType();
        }
        for (int i = 0; i < args2.length; ++i) {
            types[i + args1.length] = args2[i].getBcelType();
        }
        return types;
    }

}
