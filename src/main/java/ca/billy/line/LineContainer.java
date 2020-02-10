package ca.billy.line;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.billy.line.attribute.StaticAttributeDefinitionAssignementLine;
import ca.billy.line.attribute.StaticAttributeDefinitionLine;
import ca.billy.line.control.ElseIfLine;
import ca.billy.line.control.ElseLine;
import ca.billy.line.control.IfLine;
import ca.billy.line.method.MainMethodLine;
import ca.billy.line.method.MainStaticMethodLine;
import ca.billy.line.method.PrintLineMethodLine;
import ca.billy.line.method.PrintMethodLine;
import ca.billy.line.method.ReadLineMethodLine;
import ca.billy.line.method.StaticMethodCallLine;
import ca.billy.line.variable.VariableAssignementLine;
import ca.billy.line.variable.VariableDefinitionAssignementLine;
import ca.billy.line.variable.VariableDefinitionLine;

public class LineContainer {

    private Map<LineContext, List<BillyLine>> map;

    public LineContainer() {
        super();
        map = new HashMap<>();
        map.put(LineContext.NONE, Arrays.asList());

        map.put(
                LineContext.MAIN,
                Arrays.asList(new MainMethodLine(), new MainStaticMethodLine(), new StaticAttributeDefinitionLine(), new StaticAttributeDefinitionAssignementLine()));

        map.put(
                LineContext.CODE,
                Arrays.asList(
                        new PrintLineMethodLine(),
                        new PrintMethodLine(),
                        new ReadLineMethodLine(),
                        new IfLine(),
                        new VariableDefinitionLine(),
                        new VariableDefinitionAssignementLine(),
                        new VariableAssignementLine(),
                        new StaticMethodCallLine()));

        map.put(LineContext.END_IF, Arrays.asList(new ElseLine(), new ElseIfLine()));
    }

    public List<BillyLine> get(LineContext lineContext) {
        return map.get(lineContext);
    }

    public static enum LineContext {
        NONE, MAIN, CODE, END_IF;
    }
}
