package ch.zhaw.anliksim.program;

import ch.zhaw.anliksim.Scanner;
import ch.zhaw.anliksim.Token;
import de.inetsoftware.jwebassembly.JWebAssembly;
import de.inetsoftware.jwebassembly.module.*;

class ProgramEmitter implements Emitter {

    private static final String VALUE = "value";

    @Override
    public void emit() {
        try {
            program();
            JWebAssembly.il.add(new WasmLoadStoreInstruction(true, JWebAssembly.local(ValueType.f64, VALUE), 0));
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
            JWebAssembly.il.add(new WasmLoadStoreInstruction(true, JWebAssembly.local(ValueType.f64, Scanner.token.str), 0));
        }
    }

    private static void assignment() throws Exception {
        Scanner.check(Token.IDENT);
        int slot = JWebAssembly.local(ValueType.f64, Scanner.token.str);
        Scanner.check(Token.EQUAL);
        expr();
        Scanner.check(Token.SCOLON);
        JWebAssembly.il.add(new WasmLoadStoreInstruction(false, slot, 0));
    }

    private static void statement() throws Exception {
        assignment();
    }

    private static void statementSequence() throws Exception {
        do {
            statement();
        } while (Scanner.la != Token.EOF);
    }

    private static void program() throws Exception {
        statementSequence();
    }

    public static void main(String[] args) throws Exception {
        Scanner.init(
                "x = $arg0;\n" +
                        "a = 1;\n" +
                        "b = 2;\n" +
                        "c = 3;\n" +
                        "value = a*x*x + b*x + c;\n"
        );
        Scanner.scan();
        JWebAssembly.emitCode(Program.class, new ProgramEmitter());
    }
}
