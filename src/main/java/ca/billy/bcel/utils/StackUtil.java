package ca.billy.bcel.utils;

import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.InstructionList;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StackUtil {

    public static void swap(InstructionList il, int topSize, int belowSize) {
        if (topSize == 1) {
            if (belowSize == 1) {
                il.append(InstructionConst.SWAP);
            } else {
                il.append(InstructionConst.DUP_X2);
                il.append(InstructionConst.POP);
            }
        } else {
            if (belowSize == 1) {
                il.append(InstructionConst.DUP2_X1);
            } else {
                il.append(InstructionConst.DUP2_X2);
            }
            il.append(InstructionConst.POP2);
        }
    }
}
