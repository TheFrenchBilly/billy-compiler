package ca.billy.instruction.method;

import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.StackMap;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;

import ca.billy.bcel.utils.stackmap.StackMapBuilder;
import ca.billy.instruction.BillyCodeInstruction;
import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.context.VariableInstructionContext;
import lombok.Getter;

public class MethodInstruction extends VariableInstructionContext {

    @Getter
    private MethodDefinition methodDefinition;

    public MethodInstruction(BillyInstructionContext parent, MethodDefinition methodDefinition) {
        super(parent);
        this.methodDefinition = methodDefinition;
    }

    public Method build(ClassGen cg, InstructionFactory factory) {
        InstructionList il = new InstructionList();
        MethodGen mg = new MethodGen(
                methodDefinition.getAccessFlags(), // access flags
                methodDefinition.getReturnType(), // return type
                methodDefinition.getArgs(), // argument types
                generateArgsName(methodDefinition.getArgs().length), // arg names
                methodDefinition.getName(),
                cg.getClassName(), // method, class
                il,
                cg.getConstantPool());
        StackMapBuilder stackMapBuilder = new StackMapBuilder(cg.getConstantPool());

        BillyCodeInstructionArgs args = BillyCodeInstructionArgs
                .builder()
                .cp(cg.getConstantPool())
                .factory(factory)
                .il(il)
                .mg(mg)
                .context(this)
                .stackMapBuilder(stackMapBuilder)
                .build();

        for (BillyInstruction ins : getInstructions()) {
            ins.valid(args.getContext());
            ((BillyCodeInstruction) ins).build(args);
        }

        // FIXME not always return void
        il.append(InstructionConst.RETURN);

        mg.setMaxLocals();
        mg.setMaxStack();
        mg.removeNOPs();

        Method m = mg.getMethod();
        StackMap stackMap = stackMapBuilder.build();
        if (stackMap.getLength() > 0) {
            Code code = m.getCode();
            Attribute[] oldAttributes = code.getAttributes();
            Attribute[] attributes = new Attribute[oldAttributes.length + 1];
            for (int i = 0; i < oldAttributes.length; ++i) {
                attributes[i] = oldAttributes[i];
            }
            attributes[oldAttributes.length] = stackMap;
            code.setAttributes(attributes);
        }
        return m;
    }

    // TODO modifiy to get real args name
    private String[] generateArgsName(int size) {
        String[] s = new String[size];
        for (Integer i = 0; i < size; ++i) {
            s[i] = "args" + i.toString();
        }
        return s;
    }

}
