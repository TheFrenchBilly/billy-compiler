package ca.billy.line.method;

public abstract class AbstractMethodWithoutParameterLine extends AbstractParentheseLine {

    @Override
    protected int expectedNbParameter() {
        return 0;
    }
}
