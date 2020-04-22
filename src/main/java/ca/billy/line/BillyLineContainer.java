package ca.billy.line;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.billy.line.attribute.StaticAttributeDefinitionAssignementLine;
import ca.billy.line.attribute.StaticAttributeDefinitionLine;
import ca.billy.line.control.ElseIfLine;
import ca.billy.line.control.ElseLine;
import ca.billy.line.control.IfLine;
import ca.billy.line.control.loop.BreakLine;
import ca.billy.line.control.loop.ForEachLine;
import ca.billy.line.control.loop.ForLine;
import ca.billy.line.control.loop.WhileLine;
import ca.billy.line.method.MainStaticMethodLine;
import ca.billy.line.method.StaticMethodCallLine;
import ca.billy.line.method.build.in.MainMethodLine;
import ca.billy.line.method.build.in.PrintLineMethodLine;
import ca.billy.line.method.build.in.PrintMethodLine;
import ca.billy.line.method.build.in.ReadLineMethodLine;
import ca.billy.line.variable.VariableArrayAssignementLine;
import ca.billy.line.variable.VariableAssignementLine;
import ca.billy.line.variable.VariableDefinitionAssignementLine;
import ca.billy.line.variable.VariableDefinitionLine;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BillyLineContainer {

    public static enum LineContext {
        // @formatter:off
        NONE, 
        MAIN, 
        DEFINITION, 
        ASSIGNEMENT, 
        DEFINITION_OR_ASSIGNEMENT, 
        CODE, 
        LOOP_CODE,
        END_IF;
        // @formatter:on
    }

    private static Map<LineContext, List<BillyLine>> map = new HashMap<>();

    static {
        add(LineContext.MAIN, new MainMethodLine(), new MainStaticMethodLine(), new StaticAttributeDefinitionLine(), new StaticAttributeDefinitionAssignementLine());

        add(LineContext.DEFINITION, new VariableDefinitionLine(), new VariableDefinitionAssignementLine());
        add(LineContext.ASSIGNEMENT, new VariableArrayAssignementLine(), new VariableAssignementLine());
        add(LineContext.DEFINITION_OR_ASSIGNEMENT, LineContext.DEFINITION, LineContext.ASSIGNEMENT);

        add(LineContext.CODE, new PrintLineMethodLine(), new PrintMethodLine(), new ReadLineMethodLine(), new IfLine(), new WhileLine(), new ForLine(), new ForEachLine());
        add(LineContext.CODE, LineContext.DEFINITION_OR_ASSIGNEMENT);
        add(LineContext.CODE, new StaticMethodCallLine());

        add(LineContext.LOOP_CODE, new BreakLine());
        add(LineContext.LOOP_CODE, LineContext.CODE);

        add(LineContext.END_IF, new ElseLine(), new ElseIfLine());
    }

    public static List<BillyLine> get(LineContext lineContext) {
        List<BillyLine> r = map.get(lineContext);
        return r == null ? Arrays.asList() : r;
    }

    public static void add(LineContext lineContext, BillyLine... billyLines) {
        List<BillyLine> list = map.get(lineContext);
        if (list == null) {
            map.put(lineContext, list = new ArrayList<>());
        }
        list.addAll(Arrays.asList(billyLines));
    }

    public static void add(LineContext lineContext, List<BillyLine> billyLines) {
        List<BillyLine> list = map.get(lineContext);
        if (list == null) {
            map.put(lineContext, list = new ArrayList<>());
        }
        list.addAll(billyLines);

    }

    public static void add(LineContext lineContext, LineContext... subLinesContext) {
        for (LineContext subLineContext : subLinesContext) {
            add(lineContext, map.get(subLineContext));
        }
    }
}
