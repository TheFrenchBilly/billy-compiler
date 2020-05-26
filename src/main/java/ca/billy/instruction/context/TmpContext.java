package ca.billy.instruction.context;

import java.util.ArrayList;
import java.util.List;

import ca.billy.type.EnumType;

public class TmpContext extends CodeInstructionContext {

    private List<EnumType> stackTypes;

    public TmpContext(BillyInstructionContext parent, EnumType... stackTypes) {
        super(parent);
        this.stackTypes = new ArrayList<>();
        this.stackTypes.addAll(parent.getStackTypes());
        for (EnumType stackType : stackTypes) {
            this.stackTypes.add(stackType);
        }
    }

    @Override
    public List<EnumType> getStackTypes() {
        return stackTypes;
    }

}
