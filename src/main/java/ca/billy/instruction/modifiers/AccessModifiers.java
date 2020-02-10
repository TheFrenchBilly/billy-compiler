package ca.billy.instruction.modifiers;

import java.util.stream.Stream;

import org.apache.bcel.*;
import lombok.Getter;

public enum AccessModifiers {
    PUBLIC("public", Const.ACC_PUBLIC), PROTECTED("protected", Const.ACC_PROTECTED), PRIVATE("private" , Const.ACC_PRIVATE);

    @Getter
    private String name;
    
    @Getter
    private short flag;

    AccessModifiers(String name, short flag) {
        this.name = name;
        this.flag = flag;
    }
    
    public static AccessModifiers getAccessModifiers(String type) {
        return Stream.of(AccessModifiers.values()).filter(e -> e.name.equals(type)).findFirst().orElse(null);
    }

}
