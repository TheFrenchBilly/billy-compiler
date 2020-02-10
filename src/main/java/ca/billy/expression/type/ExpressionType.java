package ca.billy.expression.type;

import ca.billy.expression.instruction.builder.ExpressionBuilder;
import ca.billy.type.EnumType;

/**
 * "left" OPERATOR "right"<br>
 * "out" is the resultType<br>
 * 
 * @author cedric.bilodeau
 */
public interface ExpressionType {

    /**
     * "left" OPERATOR "right"
     * 
     * @return "left"
     */
    EnumType getLeft();

    /**
     * "left" OPERATOR "right"
     * 
     * @return "right"
     */
    EnumType getRight();

    /**
     * "left" OPERATOR "right"<br>
     * "out" is the resultType<br>
     * 
     * @return "out"
     */
    EnumType getOut();

    /**
     * The {@link ExpressionBuilder} to construct the bytecode for this expression
     * 
     * @return The {@link ExpressionBuilder}
     */
    ExpressionBuilder getBuilder();

}
