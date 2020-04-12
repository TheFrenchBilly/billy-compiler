package ca.billy.line;

import java.util.Optional;

import org.apache.bcel.generic.ClassGen;

import ca.billy.BillyException;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.context.InstructionContainer;
import ca.billy.line.BillyLineContainer.LineContext;

public class LineProcessor {

    private int indent;

    private BillyInstructionContext billyInstructionContext;

    private LineContext lineEndContext;

    public LineProcessor() {
        super();
        lineEndContext = LineContext.NONE;
        indent = 0;
        billyInstructionContext = new InstructionContainer();
    }

    public ClassGen build() {
        if (billyInstructionContext instanceof InstructionContainer)
            return ((InstructionContainer) billyInstructionContext).build();
        billyInstructionContext = billyInstructionContext.getParent();
        return build();
    }

    public void process(String line) {
        process(new LineWrapper(line));
    }

    private void process(LineWrapper lineWrapper) {
        try {
            if (lineWrapper.isEmpty())
                return;

            if (lineWrapper.getIndent() < indent) {
                for (; indent != lineWrapper.getIndent(); --indent) {
                    lineEndContext = billyInstructionContext.getLineEndContext();
                    billyInstructionContext = billyInstructionContext.getParent();
                }
            } else if (lineWrapper.getIndent() > indent) {
                throw new BillyException("To much indentation");
            }

            Optional<BillyLine> billyLineOptional = getBillyLine(lineWrapper);

            if (billyLineOptional.isPresent()) {
                BillyLine billyLine = billyLineOptional.get();

                BillyInstruction instruction = billyLine.createBillyInstruction(lineWrapper, billyInstructionContext);
                billyInstructionContext.add(instruction);

                if (instruction instanceof BillyInstructionContext) {
                    ++indent;
                    billyInstructionContext = (BillyInstructionContext) instruction;
                }
            } else {
                throw new BillyException("Unexpected line");
            }
        } catch (BillyException billyException) {
            throw new BillyException(billyException.getMessage(), lineWrapper, billyException);
        }
    }

    private Optional<BillyLine> getBillyLine(final LineWrapper line) {
        Optional<BillyLine> billyLineOptional = BillyLineContainer.get(lineEndContext).stream().filter(billyLine -> billyLine.isValid(line, billyInstructionContext)).findFirst();

        if (!billyLineOptional.isPresent()) {
            billyLineOptional = BillyLineContainer
                    .get(billyInstructionContext.getLineContext())
                    .stream()
                    .filter(billyLine -> billyLine.isValid(line, billyInstructionContext))
                    .findFirst();
        }

        return billyLineOptional;
    }

}
