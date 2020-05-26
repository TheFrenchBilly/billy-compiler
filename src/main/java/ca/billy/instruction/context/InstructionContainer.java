package ca.billy.instruction.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.bcel.Const;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.InstructionConst;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;

import ca.billy.BillyException;
import ca.billy.instruction.BillyCodeInstruction.BillyCodeInstructionArgs;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.attribute.MainAttributeDefinitionInstruction;
import ca.billy.instruction.method.MainInstruction;
import ca.billy.instruction.method.MethodDefinition;
import ca.billy.instruction.method.MethodInstruction;
import ca.billy.instruction.variable.VariableDefinitionInstruction;
import ca.billy.line.BillyLineContainer.LineContext;
import ca.billy.type.EnumType;
import lombok.Getter;

public class InstructionContainer implements BillyInstructionContext {

    private MainInstruction mainInstruction;

    @Getter
    private List<MethodInstruction> staticMethods;

    private List<MethodDefinition> defaultMethods;

    @Getter
    private List<MainAttributeDefinitionInstruction> staticAttributes;

    public InstructionContainer() {
        staticMethods = new ArrayList<>();
        staticAttributes = new ArrayList<>();
        defaultMethods = new ArrayList<>();
        defaultMethods.add(new MethodDefinition(ca.billy.Const.READ_LINE, EnumType.STRING));
        defaultMethods.add(new MethodDefinition(ca.billy.Const.ARRAY_LENGTH_LINE, EnumType.INTEGER, EnumType.ANY_ARRAY));
    }

    @Override
    public void add(BillyInstruction instruction) {
        if (instruction instanceof MainInstruction) {
            if (mainInstruction != null)
                throw new BillyException("two main() method");
            mainInstruction = (MainInstruction) instruction;
        } else if (instruction instanceof MethodInstruction) {
            staticMethods.add((MethodInstruction) instruction);
        } else if (instruction instanceof MainAttributeDefinitionInstruction) {
            staticAttributes.add((MainAttributeDefinitionInstruction) instruction);
        }
    }

    @Override
    public List<BillyInstruction> getInstructions() {
        List<BillyInstruction> list = Arrays.asList(mainInstruction);
        list.addAll(staticMethods);
        return list;
    }

    @Override
    public VariableDefinitionInstruction findVariable(String variableName) {
        return findLocalVariable(variableName);
    }

    @Override
    public List<EnumType> getFrameVariables() {
        return new ArrayList<>();
    }
    
    @Override
    public List<EnumType> getStackTypes() {
        return new ArrayList<>();
    }

    @Override
    public boolean isExistingVariable(String variableName) {
        return isExistingLocalVariable(variableName);
    }

    @Override
    public VariableDefinitionInstruction findLocalVariable(String variableName) {
        return staticAttributes.stream().filter(a -> a.getName().equals(variableName)).findFirst().orElse(null);
    }

    @Override
    public boolean isExistingLocalVariable(String variableName) {
        return findLocalVariable(variableName) != null;
    }

    @Override
    public MethodDefinition findDefaultMethod(String methodName) {
        return defaultMethods.stream().filter(m -> m.getName().equals(methodName)).findFirst().orElse(null);
    }

    @Override
    public MethodDefinition findMethod(String methodName) {
        return findLocalMethod(methodName);
    }

    @Override
    public MethodDefinition findLocalMethod(String methodName) {
        MethodDefinition methodDefinition = findDefaultMethod(methodName);
        if (methodDefinition == null) {
            return staticMethods.stream().map(MethodInstruction::getMethodDefinition).filter(m -> m.getName().equals(methodName)).findFirst().orElse(null);
        }
        return methodDefinition;
    }

    @Override
    public boolean isExistingMethod(String methodName) {
        return isExistingLocalMethod(methodName);
    }

    @Override
    public boolean isExistingLocalMethod(String methodName) {
        return findLocalMethod(methodName) != null;
    }

    @Override
    public LineContext getLineContext() {
        return LineContext.MAIN;
    }

    @Override
    public LineContext getLineEndContext() {
        return LineContext.NONE;
    }

    @Override
    public BillyInstructionContext getParent() {
        return null;
    }

    public ClassGen build() {
        if (mainInstruction == null)
            throw new BillyException("no main()");

        ClassGen cg = new ClassGen("Main", "java.lang.Object", "<generated>", Const.ACC_PUBLIC | Const.ACC_SUPER, null);
        cg.setMajor(52);
        cg.setMinor(0);
        InstructionList il = new InstructionList();
        MethodGen staticMg = new MethodGen(
                Const.ACC_STATIC, // access flags
                Type.VOID, // return type
                Type.NO_ARGS, // argument types
                new String[0], // arg names
                "<clinit>",
                cg.getClassName(), // method, class
                il,
                cg.getConstantPool());

        InstructionFactory factory = new InstructionFactory(cg);
        BillyCodeInstructionArgs args = BillyCodeInstructionArgs.builder().cg(cg).cp(cg.getConstantPool()).il(il).factory(factory).context(this).build();
        staticAttributes.forEach(sa -> sa.build(args));
        il.append(InstructionConst.RETURN);
        staticMg.setMaxLocals();
        staticMg.setMaxStack();

        cg.addMethod(staticMg.getMethod());
        cg.addMethod(mainInstruction.build(cg, factory));
        staticMethods.forEach(sm -> cg.addMethod(sm.build(cg, factory)));

        return cg;
    }
}
