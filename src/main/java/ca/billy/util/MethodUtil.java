package ca.billy.util;

import java.util.ArrayList;
import java.util.List;

import ca.billy.BillyException;
import ca.billy.Const;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MethodUtil {

    public static String extractMethodName(String line) {
        int index = line.indexOf(Const.START_PARENTHESES);
        return line.substring(0, index).trim();
    }

    public static String[] extractParameters(String line) {
        int index = line.indexOf(Const.START_PARENTHESES);
        line = line.substring(index + 1, line.length() - 1).trim();

        if (Const.EMPTY.equals(line)) {
            return new String[0];
        }

        List<String> args = new ArrayList<>();
        do {
            int comma = line.indexOf(Const.COMMA);
            int parenthese = line.indexOf(Const.START_PARENTHESES);
            int brakets = line.indexOf(Const.START_CURLY_BRACKETS);

            if (comma == -1) {
                args.add(line.trim());
                line = Const.EMPTY;
            } else if ((parenthese == -1 || comma < parenthese) && (brakets == -1 || comma < brakets)) {
                args.add(line.substring(0, comma).trim());
                line = line.substring(comma + 1);
            } else if (brakets == -1 || (parenthese != -1 && parenthese < brakets)) {
                line = extractArgumentIgnoringCommaUntilEnd(line, args, Const.END_PARENTHESES);
            } else {
                line = extractArgumentIgnoringCommaUntilEnd(line, args, Const.END_CURLY_BRACKETS);
            }

        } while (!Const.EMPTY.equals(line));

        return args.toArray(new String[args.size()]);
    }

    private static String extractArgumentIgnoringCommaUntilEnd(String line, List<String> args, String end) {
        int endPos = line.indexOf(end);
        if (endPos == -1)
            throw new BillyException("Expected a " + end);
        int nextComma = line.substring(endPos).indexOf(Const.COMMA);
        if (nextComma == -1) {
            args.add(line.trim());
            return Const.EMPTY;
        } else {
            args.add(line.substring(0, endPos + nextComma).trim());
            return line.substring(endPos + nextComma + 1);
        }
    }
}
