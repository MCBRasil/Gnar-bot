package xyz.gnarbot.gnar.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Expression
{
    private final String exp;
    private int pos = -1;
    private char current;
    
    public Expression(String exp)
    {
        this.exp = exp;
    }
    
    // Evaluates the expression.
    public double eval()
    {
        next();
        double value = parseBoolean();
        if (pos < exp.length()) throw new RuntimeException("Unhandled character: `"+current+"`.");
        pos = -1; //reset for re-evaluation
        return value;
    }
    
    // Get the next character and increase the position variable.
    private char next()
    {
        return current = ++pos < exp.length() ? exp.charAt(pos) : (char) -1;
    }
    
    // Returns if the next MEANINGFUL character is the prompt.
    //
    // If the next meaningful char is prompt, then skip that character
    // as we already know what it is.
    private boolean consume(char prompt)
    {
        while (current == ' ') next();
        if (current == prompt)
        {
            next();
            return true;
        }
        return false;
    }
    
    // Returns if the next character is the prompt.
    // Functions similar to consume except takes in account
    // for space.
    private boolean strictConsume(char prompt)
    {
        if (current == prompt)
        {
            next();
            return true;
        }
        return false;
    }
    
    private boolean nextIs(char prompt)
    {
        while (current == ' ') next();
        return current == prompt;
    }
    
    // Parse boolean if detect boolean statement.
    private double parseBoolean()
    {
        double value = parseExpression();
        
        while (true)
        {
            if (consume('>'))
            {
                if (strictConsume('='))
                {
                    value = value >= parseExpression() ? 1 : 0;
                    continue;
                }
                value = value > parseExpression() ? 1 : 0;
            }
            else if (consume('<'))
            {
                if (strictConsume('='))
                {
                    continue;
                }
                value = value <= parseExpression() ? 1 : 0;
            }
            else if (consume('='))
            {
                if (strictConsume('='))
                {
                    value = value == parseExpression() ? 1 : 0;
                    continue;
                }
                throw new RuntimeException("Expected '==', found '='.");
            }
            else if (consume('!'))
            {
                if (strictConsume('='))
                {
                    value = value != parseExpression() ? 1 : 0;
                    continue;
                }
                throw new RuntimeException("Expected '!=', found '!'.");
            }
            else break;
        }
        return value;
    }
    
    // Parse and sums up terms.
    private double parseExpression()
    {
        // Unary NOT.
        if (consume('!')) return parseExpression() == 0? 1 : 0;
        
        double value = parseTerm();
        while (true)
        {
            if      (consume('+')) value += parseTerm(); // Addition
            else if (consume('-')) value -= parseTerm(); // Subtraction
            else break;
        }
        return value;
    }
    
    // If next operand was + or -, this denotes
    // a new term and then parse multiplication
    // and division.
    private double parseTerm()
    {
        double value = parseFactor();
        while (true)
        {
            if      (consume('*')) value *= parseFactor(); // Multiplication
            else if (consume('/')) value /= parseFactor(); // Division
            else if (consume('%')) value %= parseFactor(); // Modulus
            else break;
        }
        return value;
    }
    
    // Parse the next numerical value/expressions.
    //
    // Will stop going to the next character when an
    // unhandled character is found. ie: '1.23L4' will parse into '1.23'
    private double parseFactor()
    {
        // Unary operators. Important to do this recursively.
        if (consume('+')) return parseFactor(); // Unary plus term.
        if (consume('-')) return -parseFactor(); // Unary minus term.
        
        // Stack value.
        double value;
        
        // Starting position variable when using the method.
        int start = pos;
        
        // GROUPINGS
        // - PARENTHESES
        if (consume('('))
        {
            value = parseBoolean();
            
            // Multiplication by (i)(i)
            while(consume(')')) // If it is cancelled.
            {
                if (consume('(')) value *= parseBoolean();
            }
        }
        // - ABSOLUTE VALUE
        else if (consume('|'))
        {
            value = Math.abs(parseBoolean());
            if (!consume('|')) // Requires a closing absolute value bracket.
            {
                throw new RuntimeException("Absolute value not closed at index " + start + ".");
            }
        }
        
        // DEFINE VALUES
        // - NUMERICAL PARSING
        else if (current >= '0' && current <= '9' || current == '.')
        {
            // Continuously increase the position variable until
            // it's not a numeric character anymore.
            while (current >= '0' && current <= '9' || current == '.') next();
            value = Double.parseDouble(exp.substring(start, pos));
            
            // Multiplication by x(i)
            if (consume('('))
            {
                value *= parseBoolean();
                while(consume(')'))
                {
                    if (consume('(')) value *= parseBoolean();
                }
            }
        }
        
        // FUNCTION AND CONSTANTS
        else func: if (Character.isLetter(current))
        {
            while (Character.isLetter(current) || Character.isDigit(current) || current == '_') next();
            
            String ref = exp.substring(start, pos);
    
            switch (ref)
            {
                case "true":
                    value = 1;
                    break func;
                case "false":
                    value = 0;
                    break func;
                case "pi":
                    value = Math.PI;
                    break func;
                case "e":
                    value = Math.E;
                    break func;
                case "random":
                    value = Math.random();
                    break func;
                case "infinity":
                    value = Double.POSITIVE_INFINITY;
                    break func;
            }
            
            // - FUNCTIONS LOOKUP
            List<Double> argList = new ArrayList<>();
            
            boolean grouped = nextIs('(');
            
            argList.add(parseTerm()); // 1st argument.
            
            while (consume(','))
            {
                if (grouped)
                {
                    argList.add(parseBoolean());
                    
                    if (consume(')')) break;
                    continue;  // Must use parentheses to escape mutliargument functions.
                }
                // Secondary arguments w/o groupings.
                argList.add(parseTerm());
            }
            
            Object[] args = new Object[argList.size()];
            argList.toArray(args);
            
            Class[] types = new Class[argList.size()];
            for (int i = 0; i < argList.size(); i++) types[i] = Double.TYPE;
            
            try
            {
                Method method = Math.class.getDeclaredMethod(ref, types);
                
                value = Double.parseDouble(method.invoke(null, args).toString());
            }
            catch (NoSuchMethodException e)
            {
                throw new RuntimeException("Unknown function/ref: `" + ref + Arrays.toString(types) + "`.");
            }
            catch (InvocationTargetException e)
            {
                throw new RuntimeException("Could not invoke: `" + ref + "`.");
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException("Could not access: `" + ref + "`.");
            }
        }
        else
        {
            throw new RuntimeException("Unexpected character: "+current);
        }
        
        if (consume('E'))
        {
            value *= Math.pow(10, parseFactor());
        }
        
        // SUFFIXES
        if (Character.isLetter(current))
        {
            start = pos;
            while (Character.isLetter(current)) next();
            
            String suffix = exp.substring(start, pos);
    
            switch (suffix)
            {
                case "deg":
                    value = Math.toRadians(value);
                    break;
                case "k":
                    value *= 1000;
                    break;
                case "mil":
                    value *= 1000000;
                    break;
                case "pi": // Multiply value by constant.
                    value *= Math.PI;
                    break;
                case "e":
                    value *= Math.E;
                    break;
                default:
                    throw new RuntimeException("Unexpected suffix: "+suffix);
            }
        }
        
        if (consume('^'))
        {
            value = Math.pow(value, parseFactor());
        }
    
        return value;
    }
}