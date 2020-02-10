package ca.billy.expression.type;

import ca.billy.expression.OperatorEnum;
import ca.billy.expression.instruction.builder.ExpressionBuilder;
import ca.billy.expression.instruction.builder.NotDeterminedExpressionBuilder;
import ca.billy.type.EnumType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class NotDeterminedExpressionType implements ExpressionType {

    @Setter
    @Getter
    EnumType left;

    @Getter
    EnumType right;

    @Setter
    @Getter
    EnumType out = EnumType.ANY;

    @Getter
    OperatorEnum operatorEnum;

    private NotDeterminedExpressionBuilder builder = new NotDeterminedExpressionBuilder(this);

    @Override
    public ExpressionBuilder getBuilder() {
        return builder;
    }
}
