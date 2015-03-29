package cn.liutils.ripple.impl.compiler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.google.common.collect.Lists;

import cn.liutils.ripple.IFunction;
import cn.liutils.ripple.Path;
import cn.liutils.ripple.ScriptProgram;
import cn.liutils.ripple.RippleException.ScriptCompilerException;

public class Parser {
    public static class ScriptObject {
        public Object value;
        public String path;
        public IFunction func;
        public int funcArgNum;
    }
    
    private enum TokenType {
        EOS,
        IDENTIFIER,
        INTEGER,
        DOUBLE,
        SINGLE_SYMBOL,
        MULTI_SYMBOL,
        KEY_WORD,
    }
    
    private enum MultiCharSymbol {
        S_GE,
        S_LE,
        S_NE,
        
        S_AND,
        S_OR,
    }
    
    private static HashSet<String> keywords = new HashSet(Arrays.asList(
            "switch",
            "when",
            "true",
            "false"
            ));
    
    
    private static class Token {
        TokenType type;
        char sSymbol;
        MultiCharSymbol mSymbol;
        String str;
        int integerValue;
        double doubleValue;
        
        public boolean isSingleChar(char c) {
            return type == TokenType.SINGLE_SYMBOL && c == sSymbol;
        }
        
        public boolean isMultiChar(MultiCharSymbol m) {
            return type == TokenType.MULTI_SYMBOL && m == mSymbol;
        }
        
        public boolean isIdentifier() {
            return type == TokenType.IDENTIFIER;
        }
        
        public boolean isKeyword(String val) {
            return type == TokenType.KEY_WORD && str.equals(val);
        }
        
        public boolean isInteger() {
            return type == TokenType.INTEGER;
        }
        
        public boolean isDouble() {
            return type == TokenType.DOUBLE;
        }
        
        public boolean isEOS() {
            return type == TokenType.EOS;
        }
        
        public void setSingleChar(char c) {
            type = TokenType.SINGLE_SYMBOL;
            sSymbol = c;
        }
        
        public void setMultiChar(MultiCharSymbol m) {
            type = TokenType.MULTI_SYMBOL;
            mSymbol = m;
        }
        
        public void setInteger(int val) {
            type = TokenType.INTEGER;
            integerValue = val;
        }
        
        public void setDouble(double val) {
            type = TokenType.DOUBLE;
            doubleValue = val;
        }
        
        public void setString(String val) {
            if (keywords.contains(val)) {
                this.type = TokenType.KEY_WORD;
            } else {
                this.type = TokenType.IDENTIFIER;
            }
            this.str = val;
        }
        
        public void setEOS() {
            type = TokenType.EOS;
        }
        
        public BinaryOperator toBinOp() {
            if (this.type == TokenType.SINGLE_SYMBOL) {
                switch (this.sSymbol) {
                case '+': return BinaryOperator.ADD;
                case '-': return BinaryOperator.SUBSTRACT;
                case '*': return BinaryOperator.MULTIPLY;
                case '/': return BinaryOperator.DIVIDE;
                case '=': return BinaryOperator.EQUAL;
                case '>': return BinaryOperator.GREATER;
                case '<': return BinaryOperator.LESSER;
                }
            } else if (this.type == TokenType.MULTI_SYMBOL) {
                switch (this.mSymbol) {
                case S_AND: return BinaryOperator.AND;
                case S_GE: return BinaryOperator.GREATER_EQUAL;
                case S_LE: return BinaryOperator.LESSER_EQUAL;
                case S_NE: return BinaryOperator.NOT_EQUAL;
                case S_OR: return BinaryOperator.OR;
                }
            }
            return BinaryOperator.UNKNOWN;
        }
        
        public UnaryOperator toUnOp() {
            if (this.type == TokenType.SINGLE_SYMBOL) {
                switch (this.sSymbol) {
                case '-': return UnaryOperator.MINUS;
                case '!': return UnaryOperator.NOT;
                case '=': return UnaryOperator.U_EQUAL;
                case '>': return UnaryOperator.U_GREATER;
                case '<': return UnaryOperator.U_LESSER;
                }
            } else if (this.type == TokenType.MULTI_SYMBOL){
                switch (this.mSymbol) {
                case S_GE: return UnaryOperator.U_GREATER_EQUAL;
                case S_LE: return UnaryOperator.U_LESSER_EQUAL;
                case S_NE: return UnaryOperator.U_NOT_EQUAL;
                default:
                }
            }
            return UnaryOperator.UNKNOWN;
        }
    }
    
    public ScriptProgram program;
    private FunctionClassLoader loader;
    
    private LineNumberReader lineNumberReader;
    private PushbackReader reader;
    private Token currentToken;
    
    private ArrayList<ScriptObject> parsedObject = new ArrayList();
    private Path currentPath = new Path(null);
    
    private Parser(ScriptProgram program, Reader input, FunctionClassLoader loader) {
        this.program = program;
        this.loader = loader;
        this.lineNumberReader = new LineNumberReader(input);
        this.reader = new PushbackReader(input);//TODO use linenumberreader
        
        this.currentToken = new Token();
    }
    
    //parse
    
    private void parseProgram() throws IOException {
        this.readToken();
        this.parseNamespace();
        if (!this.currentToken.isEOS()) {
            throw new ScriptCompilerException("Invalid token. Should be end of stream", this);
        }
    }
    
    private void parseNamespace() throws IOException {
        while (currentToken.isIdentifier()) {
            String name = currentToken.str;
            this.readToken();
            if (currentToken.isSingleChar('{')) {
                this.readToken();
                Path parentPath = currentPath;
                currentPath = new Path(parentPath, name);
                
                this.parseNamespace();
                
                this.currentPath = parentPath;
                if (!currentToken.isSingleChar('}')) {
                    throw new ScriptCompilerException("Invalid token. Should be '}'", this);
                }
                this.readToken();
            } else if (currentToken.isSingleChar('(')) {
                this.parseFunction(new Path(currentPath, name));
            }
        }
    }
    
    private void parseFunction(Path functionPath) throws IOException {
        CodeGenerator gen = new CodeGenerator(this, this.loader, functionPath);
        
        //param list
        this.readToken();
        int nargs = 0;
        if (!currentToken.isSingleChar(')')) {
            //has params
            while (true) {
                if (!currentToken.isIdentifier()) {
                    throw new ScriptCompilerException("Invalid token. Should be parameter name", this);
                }
                gen.addParameter(currentToken.str);
                ++nargs;
                this.readToken();
                if (currentToken.isSingleChar(')')) {
                    break;
                }
                if (!currentToken.isSingleChar(',')) {
                    throw new ScriptCompilerException("Invalid token. Should be ','", this);
                }
                this.readToken();
            }
        }
        this.readToken(); //skip ')'
        if (!currentToken.isSingleChar('{')) {
            throw new ScriptCompilerException("Invalid token. Should be '{'", this);
        }
        this.readToken(); //skip '{'
        gen.functionBodyBegin();
        this.parseExpression(gen);
        IFunction f = gen.functionBodyEnd();
        if (!currentToken.isSingleChar('}')) {
            throw new ScriptCompilerException("Invalid token. Should be '}'", this);
        }
        this.readToken();
        
        ScriptObject obj = new ScriptObject();
        obj.path = functionPath.path;
        obj.func = f;
        obj.funcArgNum = nargs;
        this.parsedObject.add(obj);
    }
    
    private void parseExpression(CodeGenerator gen) throws IOException {
        this.parseSubExpr(gen, 0);
    }
    
    private void parseSubExpr(CodeGenerator gen, int priority) throws IOException {
        UnaryOperator unop = currentToken.toUnOp();
        if (unop != UnaryOperator.UNKNOWN) {
            this.readToken();
            this.parseSubExpr(gen, 100); //only read following prefix unary operators
            gen.calcUnary(unop);
        } else {
            this.parseSimpleExpr(gen);
        }
        BinaryOperator binop = currentToken.toBinOp();
        while (binop.priority > priority) {
            this.readToken();
            this.parseSubExpr(gen, binop.priority);
            gen.calcBinary(binop);
            binop = currentToken.toBinOp();
        }
    }
    
    private void parseSimpleExpr(CodeGenerator gen) throws IOException {
        if (currentToken.isKeyword("true")) {
            gen.pushBooleanConst(true);
            this.readToken();
        } else if (currentToken.isKeyword("false")) {
            gen.pushBooleanConst(false);
            this.readToken();
        } else if (currentToken.isInteger()) {
            gen.pushIntegerConst(currentToken.integerValue);
            this.readToken();
        } else if (currentToken.isDouble()) {
            gen.pushDoubleConst(currentToken.doubleValue);
            this.readToken();
        } else if (currentToken.isSingleChar('(')) {
            this.readToken();
            this.parseExpression(gen);
            if (!currentToken.isSingleChar(')')) {
                throw new ScriptCompilerException("Invalid token. Should be ')'", this);
            }
            this.readToken();
        } else if (currentToken.isKeyword("switch")) {
            throw new ScriptCompilerException("Switch expression is not supported", this);
        } else if (currentToken.isIdentifier()) {
            //TODO in a separated function
            String name = currentToken.str;
            this.readToken();
            if (currentToken.isSingleChar('.')) {
                //path
                StringBuilder sb = new StringBuilder(name);
                sb.append('.');
                this.readToken();
                if (!currentToken.isIdentifier()) {
                    throw new ScriptCompilerException("Invalid path", this);
                }
                sb.append(currentToken.str);
                this.readToken();
                while (currentToken.isSingleChar('.')) {
                    sb.append('.');
                    this.readToken();
                    if (!currentToken.isIdentifier()) {
                        throw new ScriptCompilerException("Invalid path", this);
                    }
                    sb.append(currentToken.str);
                    this.readToken();
                }
                Path path = new Path(sb.toString());
                if (currentToken.isSingleChar('(')) {
                    //func
                    throw new ScriptCompilerException("Function call is not supported", this);
                } else {
                    //namespace value
                    throw new ScriptCompilerException("Value reference is not supported", this);
                }
            } else if (currentToken.isSingleChar('(')) {
                //func
                throw new ScriptCompilerException("Function call is not supported", this);
            } else {
                //param
                gen.pushParameter(name);
                //the name has been skipped, no readToken here
            }
        }
    }
    
    //lex

    private char peekChar() throws IOException {
        char c = (char) reader.read();
        reader.unread(c);
        return c;
    }
    
    private void readNumber(char first) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(first);
        char c;
        while (Character.isDigit(c = peekChar())) {
            reader.read();
            sb.append(c);
        }
        if (peekChar() == '.') {
            reader.read();
            sb.append('.');
            while (Character.isDigit(c = peekChar())) {
                reader.read();
                sb.append(c);
            }
            String resultStr = sb.toString();
            if (resultStr.endsWith(".")) {
                throw new ScriptCompilerException("Invalid number format", this);
            }
            //double
            currentToken.setDouble(Double.parseDouble(resultStr));
        }
        //integer
        currentToken.setInteger(Integer.parseInt(sb.toString()));
    }
    
    private void readIdentifier(char first) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(first);
        char c;
        c = peekChar();
        while (Character.isLetterOrDigit(c) || c == '_') {
            reader.read();
            sb.append(c);
            c = peekChar();
        }
        currentToken.setString(sb.toString());
    }
    
    private void readToken() throws IOException {
        int charEnd = reader.read();
        char c;
        if (charEnd == -1) {
            currentToken.setEOS();
            return;
        }
        c = (char) charEnd;
        switch (c) {
        //single char
        case '+':
        case '-':
        case '*':
        case '/':
        case '(':
        case ')':
        case '{':
        case '}':
        case '.':
        case ',':
        case '=': //currently no ==
            currentToken.setSingleChar(c);
            break;
        //followed by '='
        case '>':
            if (peekChar() == '=') {
                currentToken.setMultiChar(MultiCharSymbol.S_GE);
            } else {
                currentToken.setSingleChar(c);
            }
            break;
        case '<':
            if (peekChar() == '=') {
                currentToken.setMultiChar(MultiCharSymbol.S_LE);
            } else {
                currentToken.setSingleChar(c);
            }
            break;
        case '!':
            if (peekChar() == '=') {
                currentToken.setMultiChar(MultiCharSymbol.S_NE);
            } else {
                currentToken.setSingleChar(c);
            }
            break;
        case '&':
            if (peekChar() == '&') {
                currentToken.setMultiChar(MultiCharSymbol.S_AND);
            } else {
                currentToken.setSingleChar(c);
            }
            break;
        case '|':
            if (peekChar() == '|') {
                currentToken.setMultiChar(MultiCharSymbol.S_OR);
            } else {
                currentToken.setSingleChar(c);
            }
        //number
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            readNumber(c);
            break;
        default:
            if (Character.isWhitespace(c)) {
                readToken(); //read again
                return;
            } else if (Character.isLetter(c) || c == '_') {
                readIdentifier(c);
                return;
            } else {
                throw new ScriptCompilerException("Unknown token", this);
            }
        }
    }
    
    //api
    public static List<ScriptObject> parse(ScriptProgram program, Reader input, FunctionClassLoader loader) {
        //try {
            //char c = (char) input.read();
            //char c1 = '今';
        //} catch (IOException e) {
            // TODO Auto-generated catch block
        //    e.printStackTrace();
        //}
        
        Parser p = new Parser(program, input, loader);
        try {
            p.parseProgram();
        } catch (IOException e) {
            throw new ScriptCompilerException(e, p);
        }
        /*
        ScriptObject o = new ScriptObject();
        CodeGenerator gen = new CodeGenerator(p, loader, new Path("main"));
        o.path = "main";
        o.func = gen.testCompile();
        o.funcArgNum = 1;
        return Arrays.asList(new ScriptObject[] { o });
        */
        return p.parsedObject;
    }
}
