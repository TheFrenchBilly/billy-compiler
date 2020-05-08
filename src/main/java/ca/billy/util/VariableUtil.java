package ca.billy.util;

import ca.billy.Const;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VariableUtil {

    private static String VAR_REGEX = "[a-zA-Z]+";

    private static String ARRAY_ACCESS_REGEX = VAR_REGEX + "\\s*" + "\\[.+\\]";

    public static boolean isValidName(String name) {
        return name.matches(VAR_REGEX);
    }

    public static boolean isValidArrayExpression(String access) {
        return access.matches(ARRAY_ACCESS_REGEX);
    }

    // {variableName, string expression for the index}
    public static String[] splitArrayAccess(String access) {
        int startBrackets = access.indexOf(Const.START_SQUARE_BRACKETS);
        return new String[] { access.substring(0, startBrackets).trim(), access.substring(startBrackets + 1, access.length() - 1).trim() };
    }

    // dont support multiple =
    // { variable name, the value to assigne}
    public static String[] splitAssignement(String assignementString) {
        int index = assignementString.indexOf(Const.OPT_ASSIGNEMENT);
        return new String[] { assignementString.substring(0, index).trim(), assignementString.substring(index + 1).trim() };
    }
}
