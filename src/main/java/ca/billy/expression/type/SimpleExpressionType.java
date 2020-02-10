package ca.billy.expression.type;

import ca.billy.expression.instruction.builder.ExpressionBuilder;
import ca.billy.type.EnumType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class SimpleExpressionType implements ExpressionType {

    EnumType left;

    EnumType right;

    EnumType out;

    ExpressionBuilder builder;

}
