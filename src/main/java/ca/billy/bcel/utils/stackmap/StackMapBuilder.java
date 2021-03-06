package ca.billy.bcel.utils.stackmap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.bcel.classfile.StackMap;
import org.apache.bcel.classfile.StackMapEntry;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Type;

import ca.billy.bcel.utils.Branch;

/**
 * https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.7.4
 * 
 * @author cedric.bilodeau
 */
public class StackMapBuilder {

    private ConstantPoolGen cp;

    private List<Branch> branchs;

    public StackMapBuilder(ConstantPoolGen cp) {
        this.cp = cp;
        branchs = new ArrayList<>();
    }

    public StackMapBuilder addFrame(Branch branch) {
        branchs.add(branch);
        return this;
    }

    /**
     * Build the {@link StackMap}
     * 
     * @param locals The initial local
     * @return The {@link StackMap}
     */
    public StackMap build(Type... locals) {
        int stackMapTableEntry = cp.addUtf8("StackMapTable");

        Set<Integer> targetSet = new HashSet<>();
        branchs = branchs.stream().filter(b -> targetSet.add(b.getTargetPosition())).sorted(Comparator.comparingInt(Branch::getTargetPosition)).collect(Collectors.toList());

        StackMapEntry[] map = new StackMapEntry[branchs.size()];
        for (int i = 0; i < map.length; ++i) {
            int offset = i == 0 ? branchs.get(i).getTargetPosition() : branchs.get(i).getTargetPosition() - 1 - branchs.get(i - 1).getTargetPosition();

            if (sameType(locals, branchs.get(i).getLocals())) {
                int localDiff = branchs.get(i).getLocals().length - locals.length;
                map[i] = StackMapEntryFactory.create(branchs.get(i).getLocals(), branchs.get(i).getStacks(), localDiff, offset, cp);
            } else {
                map[i] = StackMapEntryFactory.createFullFrame(branchs.get(i).getLocals(), branchs.get(i).getStacks(), offset, cp);
            }

            locals = branchs.get(i).getLocals();
        }

        StackMap stackMap = new StackMap(stackMapTableEntry, 0, null, cp.getConstantPool());
        stackMap.setStackMap(map);
        return stackMap;
    }

    private boolean sameType(Type[] t1, Type[] t2) {
        for (int i = 0; i < Math.min(t1.length, t2.length); ++i) {
            if (!t1[i].equals(t2[i])) {
                return false;
            }
        }
        return true;
    }

}
