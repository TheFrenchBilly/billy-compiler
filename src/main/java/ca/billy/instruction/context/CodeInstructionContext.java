package ca.billy.instruction.context;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ca.billy.instruction.method.MethodInstruction;
import ca.billy.type.EnumType;

public class CodeInstructionContext extends VariableInstructionContext {

    public CodeInstructionContext(BillyInstructionContext parent) {
        super(parent);
    }

    @Override
    public List<EnumType> getFrameVariables() {
        BillyInstructionContext billyInstructionContext = getParent();
        while (!(billyInstructionContext instanceof MethodInstruction)) {
            billyInstructionContext = billyInstructionContext.getParent();
        }

        return Stream.concat(Stream.of(((MethodInstruction) billyInstructionContext).getMethodDefinition().getArgs()), super.getFrameVariables().stream()).collect(
                Collectors.toList());
    }
}
