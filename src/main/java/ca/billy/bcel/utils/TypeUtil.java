package ca.billy.bcel.utils;

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
    
}
