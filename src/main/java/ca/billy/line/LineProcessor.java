package ca.billy.line;

import java.util.Optional;

import org.apache.bcel.generic.ClassGen;

import ca.billy.BillyException;
import ca.billy.Const;
import ca.billy.Log;
import ca.billy.expression.ExpressionProcessor;
import ca.billy.instruction.BillyInstruction;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.instruction.context.InstructionContainer;
import ca.billy.line.LineContainer.LineContext;

public class LineProcessor {

    private final static char INDENT = '\t';

    private int indent;

    private LineContainer lineContainer;
    
    private ExpressionProcessor expressionProcessor;

    private BillyInstructionContext billyInstructionContext;

    private LineContext lineEndContext;

    public LineProcessor() {
        super();
        lineEndContext = LineContext.NONE;
        indent = 0;
        lineContainer = new LineContainer();
        expressionProcessor = new ExpressionProcessor();
        billyInstructionContext = new InstructionContainer();        
    }
    
    public ClassGen build() {
        if (billyInstructionContext instanceof InstructionContainer)
            return ((InstructionContainer) billyInstructionContext).build();
        billyInstructionContext = billyInstructionContext.getParent();
        return build();
    }

    public void process(String line) {
        line = removeComment(line);
        if (line.trim().length() == 0)
            return;

        int lineIndent = getIndent(line);
        if (lineIndent < indent) {
            for (; indent != lineIndent;--indent) {
                lineEndContext = billyInstructionContext.getLineEndContext();
                billyInstructionContext = billyInstructionContext.getParent();
            }
        } else if (lineIndent > indent) {
            throw new BillyException("To much indentation");
        }

        final String lineWithoutIndent = line.substring(indent).trim();
        Optional<BillyLine> billyLineOptional = getBillyLine(lineWithoutIndent);

        Log.log(line);
        if (billyLineOptional.isPresent()) {
            BillyLine billyLine = billyLineOptional.get();

            BillyInstruction instruction = billyLine.createBillyInstruction(lineWithoutIndent, billyInstructionContext, expressionProcessor);
            billyInstructionContext.add(instruction);

            if (instruction instanceof BillyInstructionContext) {
                ++indent;
                billyInstructionContext = (BillyInstructionContext) instruction;
            }
        } else {
            throw new BillyException("Unexpected line :" + line);
        }
    }

    private Optional<BillyLine> getBillyLine(final String lineWithoutIndent) {
        Optional<BillyLine> billyLineOptional = lineContainer
                .get(lineEndContext)
                .stream()
                .filter(billyLine -> billyLine.isValid(lineWithoutIndent, billyInstructionContext))
                .findFirst();

        if (!billyLineOptional.isPresent()) {
            billyLineOptional = lineContainer
                    .get(billyInstructionContext.getLineContext())
                    .stream()
                    .filter(billyLine -> billyLine.isValid(lineWithoutIndent, billyInstructionContext))
                    .findFirst();
        }

        return billyLineOptional;
    }
    
    private int getIndent(String line) {
        int index = 0;
        while (INDENT == line.charAt(index))
            ++index;

        return index;
    }
    
    private String removeComment(String line) {
        int index = line.indexOf(Const.COMMENT);
        return index == -1 ? line : index == 0 ? Const.EMPTY :line.substring(0, index -1);
    }

}
