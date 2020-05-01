package ca.billy.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import ca.billy.BillyException;
import ca.billy.Const;
import ca.billy.expression.instruction.IExpressionInstruction;
import ca.billy.expression.instruction.ReplaceWrapperExpression;
import ca.billy.expression.instruction.leaf.LeafExpressionFactory;
import ca.billy.expression.instruction.node.NodeExpression;
import ca.billy.expression.type.ExpressionType;
import ca.billy.instruction.context.BillyInstructionContext;
import ca.billy.type.EnumType;
import lombok.AllArgsConstructor;

// Careful, this not some easy code to read and understand
// and not the more optimal code

// No fast failing so it's more easy
public class ExpressionProcessor {

    @AllArgsConstructor
    private static class OperatorIndex {
        int index;
        OperatorEnum op;
    }

    @AllArgsConstructor
    private static class StackInfo {
        int index;
        int methodIndexCounter;
        boolean ignore;
    }

    public static IExpressionInstruction parse(String expressionString, EnumType expectedReturn, BillyInstructionContext instructionContext) {
        // TODO Move to user input ?
//        if (expressionString.contains(Const.UNDERSCORE)) {
//            throw new BillyException("Invalid character");
//        }

        List<ReplaceWrapperExpression> replaceList = new ArrayList<>();
        String res = expressionString;

        // First, we replace all parentheses and method call but _X_ where x s the index of replaceList
        // We don't do it if we don't find '('
        if (expressionString.contains(Const.START_PARENTHESES)) {
            res = prepareString(expressionString, replaceList, instructionContext);
        }

        IExpressionInstruction expressionInstruction = parse(res, replaceList, instructionContext);

        if (!expressionInstruction.getResultType().typeMatch(expectedReturn)) {
            throw new BillyException("The type " + expectedReturn.getName() + " is not assignable for the type " + expressionInstruction.getResultType().getName());
        }

        return expressionInstruction;
    }

    /**
     * Replace all parentheses and method call but _X_ where x s the index of replaceList
     * 
     * @param expressionString The {@link String} input
     * @param instructionContext The {@link BillyInstructionContext} for the exp
     * @param replaceList The {@link list} of {@link ReplaceWrapperExpression}
     * @Return res the {@link String} result
     */
    private static String prepareString(String expressionString, List<ReplaceWrapperExpression> replaceList, BillyInstructionContext instructionContext) {
        Stack<StackInfo> stack = new Stack<>();
        String res = expressionString;

        int methodIndexCounter = 0;
        for (int i = 0; i < expressionString.length(); ++i) {
            if (expressionString.charAt(i) == '(') {
                StackInfo si = new StackInfo(i, methodIndexCounter, stack.size() > 0 && stack.peek().methodIndexCounter != 0);
                stack.add(si);

            } else if (expressionString.charAt(i) == ')') {
                if (stack.size() == 0) {
                    throw new BillyException("Unexpected ')'");
                }
                StackInfo si = stack.pop();
                if (!si.ignore) {
                    res = addToReplaceList(i, si, res, expressionString, replaceList, instructionContext);
                }
            } else if ((expressionString.charAt(i) >= 'A' && expressionString.charAt(i) <= 'Z') || (expressionString.charAt(i) >= 'a' && expressionString.charAt(i) <= 'z')) {
                ++methodIndexCounter;
            } else if (expressionString.charAt(i) == ' ' || expressionString.charAt(i) == '\t') {
                if (methodIndexCounter != 0) {
                    ++methodIndexCounter;
                }
            } else {
                methodIndexCounter = 0;
            }
        }

        if (stack.size() != 0) {
            throw new BillyException("Missing one ')'");
        }
        
        return res;
    }

    private static String addToReplaceList(Integer index, StackInfo si, String res, String expressionString, List<ReplaceWrapperExpression> replaceList,
            BillyInstructionContext instructionContext) {
        String replace = expressionString.substring(si.index - si.methodIndexCounter, index + 1);
        for (int y = 0; y < replaceList.size(); ++y) {
            replace = replace.replace(replaceList.get(y).getReplaceString(), Const.UNDERSCORE + y + Const.UNDERSCORE);
        }
        res = res.replace(replace, Const.UNDERSCORE + replaceList.size() + Const.UNDERSCORE);
        IExpressionInstruction expressionInstruction = null;
        if (si.methodIndexCounter == 0) {
            expressionInstruction = parse(replace.substring(1, replace.length() - 1), replaceList, instructionContext);
        } else {
            expressionInstruction = LeafExpressionFactory.createLeafExpression(replace.substring(0, replace.length()), instructionContext);
        }
        replaceList.add(new ReplaceWrapperExpression(expressionInstruction, replace));
        return res;
    }

    public static IExpressionInstruction parse(String s, List<ReplaceWrapperExpression> replaceList, BillyInstructionContext instructionContext) {

        s = s.trim();
        OperatorIndex operatorIndex = getOperatorIndex(s);

        if (operatorIndex.index != -1) {
            IExpressionInstruction left = parse(s.substring(0, operatorIndex.index), replaceList, instructionContext);
            IExpressionInstruction right = parse(s.substring(operatorIndex.index + operatorIndex.op.getOperator().length(), s.length()), replaceList, instructionContext);
            ExpressionType expressionType = operatorIndex.op.retrieveExpressionType(left.getResultType(), right.getResultType());
            return new NodeExpression(left, expressionType, right);
        }

        if (s.startsWith(Const.UNDERSCORE) && s.endsWith(Const.UNDERSCORE)) {
            return replaceList.get(Integer.parseInt(s.substring(1, s.length() - 1)));
        }

        return LeafExpressionFactory.createLeafExpression(s, instructionContext);
    }

    private static OperatorIndex getOperatorIndex(String stringExpression) {
        OperatorIndex operatorIndex = null;

        for (OperatorEnum[] ops : OperatorEnum.getValuesByPriority()) {
            operatorIndex = getOperatorIndex(stringExpression, ops);
            if (operatorIndex.index != -1) {
                break;
            }
        }

        return operatorIndex;
    }

    private static OperatorIndex getOperatorIndex(String stringExpression, OperatorEnum[] ops) {
        int indexMax = -1;
        OperatorEnum opMin = null;

        for (OperatorEnum op : ops) {
            int index = stringExpression.lastIndexOf(op.getOperator());
            if (index != -1 && isNotInString(stringExpression, index) && index > indexMax) {
                indexMax = index;
                opMin = op;
            }
        }

        return new OperatorIndex(indexMax, opMin);
    }

    /**
     * Valid if the operator in not in a string<br>
     * TODO does it support /" ??
     */
    private static boolean isNotInString(String stringExpression, int index) {
        String substr = stringExpression.substring(0, index);
        if (!substr.contains(Const.QUOTE)) {
            return true;
        } else if (substr.startsWith(Const.QUOTE)) {
            return substr.split(Const.QUOTE).length % 2 == 1;
        } else {
            return substr.split(Const.QUOTE).length % 2 == 0;
        }
    }

}
