package ca.billy.bcel.utils.stackmap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.bcel.classfile.StackMap;
import org.apache.bcel.classfile.StackMapEntry;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Type;

/**
 * Only support SAME_FRAME,SAME_LOCALS_1_STACK_ITEM_FRAME,APPEND_FRAME for now and their extended version if needed
 * 
 * @author cedric.bilodeau
 */
public class StackMapBuilder {

    private ConstantPoolGen cp;

    private List<StackMapBuilderEntry> entries;

    private Type[] locals;

    public StackMapBuilder(ConstantPoolGen cp) {
        this.cp = cp;
        entries = new ArrayList<>();
        locals = new Type[0];
    }
    
    public StackMapBuilder addFrame(BranchHandle handle, Type[] locals, Type[] stacks) {
        entries.add(new StackMapBuilderEntry(handle, locals, stacks));
        return this;
    }
    
    public StackMap build() {
        int stackMapTableEntry = cp.addUtf8("StackMapTable");
        Set<Integer> targetSet = new HashSet<>();
        entries = entries.stream().filter(e ->  targetSet.add(e.getTargetPosition())).sorted(Comparator.comparingInt(StackMapBuilderEntry::getTargetPosition)).collect(Collectors.toList());
        StackMapEntry[] map = new StackMapEntry[entries.size()];
        for (int i = 0; i < map.length; ++i) {
            map[i] = entries.get(i).build(i == 0 ? null : entries.get(i - 1), cp, this);
        }

        StackMap stackMap = new StackMap(stackMapTableEntry, 0, null, cp.getConstantPool());
        stackMap.setStackMap(map);
        return stackMap;
    }
    
    int adjustLocals(Type[] locals) {
        int length = locals.length - this.locals.length;
        if (length == 0) {
            return 0;
        }
        Type[] newLocals = new Type[length];
        for (int i = this.locals.length; i < locals.length; ++i) {
            newLocals[i - this.locals.length] = locals[i];
        }
        this.locals = locals;
        return length;
    }

}
