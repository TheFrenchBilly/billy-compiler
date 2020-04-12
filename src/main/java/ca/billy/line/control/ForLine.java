package ca.billy.line.control;

import ca.billy.BillyException;
import ca.billy.Const;
import ca.billy.expression.Expression;
import ca.billy.instruction.BillyCodeInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.context.InstructionContainer;
import ca.billy.instruction.context.VariableInstructionContext;
import ca.billy.instruction.control.ForInstruction;
import ca.billy.line.BillyLineContainer;
import ca.billy.line.BillyLineContainer.LineContext;
import ca.billy.line.LineWrapper;
import ca.billy.type.EnumType;

public class ForLine extends AbstractForLine {

    protected int getLength() {
        return 3;
    }

    @Override
    public VariableInstructionContext createBillyInstruction(LineWrapper line, BillyInstructionContext instructionContext) {
        int index = line.getLine().indexOf(Const.SPACE);
        String[] exps = line.getLine().substring(index + 1, line.getLine().length() - 1).split(";");
        
        // ugh ? should i create a empty container ?
        InstructionContainer instructionContainer = new InstructionContainer();

        BillyCodeInstruction ins1 = null, ins3 = null;
        if (!exps[0].trim().isEmpty()) {
            LineWrapper subLine = new LineWrapper(exps[0], line.getLineNumber());
            ins1 = (BillyCodeInstruction) BillyLineContainer
                    .get(LineContext.DEFINITION_OR_ASSIGNEMENT)
                    .stream()
                    .filter(billyLine -> billyLine.isValid(subLine, instructionContainer))
                    .findFirst()
                    .orElseThrow(() -> new BillyException("Unexpected expression :" + exps[0]))
                    .createBillyInstruction(subLine, instructionContainer);
        }

        if (exps.length == 3 && !exps[2].trim().isEmpty()) {
            LineWrapper subLine = new LineWrapper(exps[2], line.getLineNumber());
            ins3 = (BillyCodeInstruction) BillyLineContainer
                    .get(LineContext.ASSIGNEMENT)
                    .stream()
                    .filter(billyLine -> billyLine.isValid(subLine, instructionContainer))
                    .findFirst()
                    .orElseThrow(() -> new BillyException("Unexpected expression :" + exps[2]))
                    .createBillyInstruction(subLine, instructionContainer);
        }

        return new ForInstruction(instructionContext, ins1, new Expression(exps[1], EnumType.BOOLEAN, line.getLineNumber()), ins3);
    }

}
