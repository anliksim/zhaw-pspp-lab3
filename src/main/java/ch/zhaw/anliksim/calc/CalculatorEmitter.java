package ch.zhaw.anliksim.calc;

import ch.zhaw.anliksim.Scanner;
import ch.zhaw.anliksim.Token;
import de.inetsoftware.jwebassembly.*;
import de.inetsoftware.jwebassembly.module.*;

class CalculatorEmitter implements Emitter {

    @Override
    public void emit() {
        try {
            expr();
            JWebAssembly.il.add(new WasmBlockInstruction(WasmBlockOperator.RETURN, null, 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void expr() throws Exception {
        term();
        while (Scanner.la == Token.PLUS || Scanner.la == Token.MINUS) {
            Scanner.scan();
            int op = Scanner.token.kind;
            term();

            if (op == Token.PLUS) {
                JWebAssembly.il.add(new WasmNumericInstruction(NumericOperator.add, ValueType.f64, 0));
            } else if (op == Token.MINUS) {
                JWebAssembly.il.add(new WasmNumericInstruction(NumericOperator.sub, ValueType.f64, 0));
            }
        }
    }

    private static void term() throws Exception {
        factor();
        while (Scanner.la == Token.TIMES || Scanner.la == Token.SLASH) {
            Scanner.scan();
            int op = Scanner.token.kind;
            factor();

            if (op == Token.TIMES) {
                JWebAssembly.il.add(new WasmNumericInstruction(NumericOperator.mul, ValueType.f64, 0));
            } else if (op == Token.SLASH) {
                JWebAssembly.il.add(new WasmNumericInstruction(NumericOperator.div, ValueType.f64, 0));
            }
        }
    }

    private static void factor() throws Exception {
        if (Scanner.la == Token.LBRACK) {
            Scanner.scan();
            expr();
            Scanner.check(Token.RBRACK);
        } else if (Scanner.la == Token.NUMBER) {
            Scanner.scan();
            JWebAssembly.il.add(new WasmConstInstruction(Scanner.token.val, 0));
        } else if (Scanner.la == Token.IDENT) {
            Scanner.scan();
            JWebAssembly.il.add(new WasmConstInstruction(Scanner.token.val, 0));
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner.init("4.2 + 3.2 * 2");
        Scanner.scan();
        JWebAssembly.emitCode(Calculator.class, new CalculatorEmitter());
    }
}
