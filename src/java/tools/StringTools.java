/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alianos
 */
public class StringTools {

    public static int numberOfDigits( String text ) {
        int count = 0;
        for( int i = 0; i < text.length(); i++ ) {
            char c = text.toCharArray()[i];
            if( Character.isDigit( c ) ) {
                count++;
            }
        }
        return count;
    }

    public static int numberOfLetters( String text ) {
        int count = 0;
        for( int i = 0; i < text.length(); i++ ) {
            char c = text.toCharArray()[i];
            if( Character.isLetter( c ) ) {
                count++;
            }
        }
        return count;
    }

    public static int numberOfSymbols( String text ) {
        int count = 0;
        for( int i = 0; i < text.length(); i++ ) {
            char c = text.toCharArray()[i];
            if( isSymbol( c ) ) {
                count++;
            }
        }
        return count;
    }

    public static boolean isSymbol( Character c ) {
        if( !Character.isWhitespace( c ) && !Character.isDigit( c ) && !Character.isLetter( c ) ) {
            return true;
        }
        return false;
    }

    public static int numberOfChar( String text, Character character ) {
        int count = 0;
        for( int i = 0; i < text.length(); i++ ) {
            char c = text.toCharArray()[i];
            if( c == character ) {
                count++;
            }
        }
        return count;
    }

    public static Boolean isNumber( String value ) {
        if( value.length() == numberOfDigits( value )
                || (value.length() == numberOfDigits( value ) + 1
                && (value.contains( "." ) || value.contains( "," ))) ) {
            return true;
        }
        return false;
    }

    /**
     * Looks for chars in a sensible way for classification.
     *
     * @param value
     * @param c
     * @return
     */
    public static boolean startsWithNumber( String text ) {
        boolean result = false;
        if( text != null && text.length() >= 1 ) {
            char c = text.toCharArray()[0];
            if( Character.isDigit( c ) ) {
                result = true;
            }
        }
        return result;
    }

    public static boolean startsWithSymbol( String text ) {
        boolean result = false;
        if( text != null && text.length() >= 1 ) {
            char c = text.toCharArray()[0];
            if( !Character.isWhitespace( c ) && !Character.isDigit( c ) && !Character.isLetter( c ) ) {
                result = true;
            }
        }
        return result;
    }

    public static boolean endsWithSymbol( String text ) {
        boolean result = false;
        if( text != null && text.length() >= 1 ) {
            char c = text.toCharArray()[text.length() - 1];
            if( !Character.isWhitespace( c ) && !Character.isDigit( c ) && !Character.isLetter( c ) ) {
                result = true;
            }
        }
        return result;
    }
    

    /**
     * get the n-th symbol of the text (apart from whitespaces). Starts from 1.
     *
     * @param text
     * @param n
     * @return the symbol, null if no symbol was found
     */
    public static String getSymbol( String text, int n ) {
        int found = 0;
        for( char c : text.toCharArray() ) {
            if( isSymbol( c ) ) {
                found++;
                if( found == n ) {
                    return c + "";
                }
            }
        }

        return null;
    }

    public static int numberOfUpperCase( String text ) {
        int count = 0;
        for( int i = 0; i < text.length(); i++ ) {
            char c = text.toCharArray()[i];
            if( Character.isUpperCase( c ) ) {
                count++;
            }
        }
        return count;
    }

    public static int numberOfLowerCase( String text ) {
        int count = 0;
        for( int i = 0; i < text.length(); i++ ) {
            char c = text.toCharArray()[i];
            if( Character.isLowerCase( c ) ) {
                count++;
            }
        }
        return count;
    }

    public static double avgContinousUppercase( String text ) {
        int count = 0;
        int totalLetters = 0;
        int totalWords = 0;
        for( int i = 0; i < text.length(); i++ ) {
            char c = text.toCharArray()[i];
            if( Character.isUpperCase( c ) ) {
                count++;
            }
            else {
                totalLetters += count;
                totalWords++;
                count = 0;
            }
        }
        return (double) totalLetters / (double) totalWords;
    }

    /**
     * Splits the given string at spaces, then looks for parts containing only literal characters, and returns
     * them. Duplicates are allowed.
     *
     * @param value
     * @return
     */
    public static List<String> getWords( String value ) {
        List<String> words = new ArrayList<String>();
        String[] posWords = value.split( " " );
        for( String posWord : posWords ) {
            if( posWord.matches( "^[a-zA-Z]+$" ) ) {
                words.add( posWord );
            }
        }
        return words;
    }

    /**
     * Removes all symbols from the start of the text, and trailing whitespace chars.
     *
     * @param text
     * @return the trimmed string.
     */
    public static String trueTrim( String text ) {
        text = text.trim();
        while( StringTools.startsWithSymbol( text ) ) {
            text = text.substring( 1, text.length() );
            text = text.trim();
        }
        return text;
    }

    /**
     * Removes all symbols & whitespace from the start & end of the text
     *
     * @param text
     * @return the trimmed string.
     */
    public static String trueTrimBothEnds( String text ) {
        text = text.trim();
        while( StringTools.startsWithSymbol( text ) ) {
            text = text.substring( 1, text.length() );
            text = text.trim();
        }
        while( StringTools.endsWithSymbol( text ) ) {
            text = text.substring( 0, text.length() - 1 );
            text = text.trim();
        }
        return text;
    }

    public static boolean isYesOrNo( String value ) {
        value = trueTrim( value.toLowerCase() );
        if( value.equals( "yes" ) || value.equals( "no" ) ) {
            return true;
        }

        //Look for something like "Yes, with sound"
        if( value.length() > 2 ) {
            //character trailing a no
            Character c = value.toCharArray()[2];
            if( value.startsWith( "no" ) && (isSymbol( c ) || Character.isWhitespace( c )) ) {
                return true;
            }
        }
        if( value.length() > 3 ) {
            //character trailing a yes
            Character c = value.toCharArray()[3];
            if( value.startsWith( "yes" ) && (isSymbol( c ) || Character.isWhitespace( c )) ) {
                return true;
            }
        }

        return false;
    }

    /**
     * Removes pairs of symbols.
     *
     * @param brackets The kind of brackets to remove. Acceptable arguments are (, [, { for example "({"
     * @param value
     * @return the clear string in the first position of the array list. Strings in symbol pairs in
     * consecutive positions.
     */
    public static ArrayList<String> removePair( String brackets, String value ) {
        ArrayList<String> result = new ArrayList<String>();
        result.add( value );

        for( char c : brackets.toCharArray() ) {
            char openChar = '(';
            char closeChar = ')';

            if( c == '(' ) {
                openChar = '(';
                closeChar = ')';
            }
            else if( c == '[' ) {
                openChar = '[';
                closeChar = ']';
            }
            else if( c == '{' ) {
                openChar = '{';
                closeChar = '}';
            }

            while( value.contains( openChar + "" ) && value.contains( closeChar + "" ) ) {
                int from = value.indexOf( openChar );
                int to = value.indexOf( closeChar, from );
                String removed = value.substring( from, to + 1 );
                value = value.replace( removed, "" );
                removed = removed.substring( 1, removed.length() - 1 );//remove the symbols themselves
                result.add( removed );
                result.set( 0, value );
            }
        }
        return result;

    }

    public static String[] splitAt( char splitChar, String value, int occurenceToSplit ) {
        String part1 = "";
        String part2 = value;
        String[] result = new String[2];

        int occured = 0;
        for( char c : value.toCharArray() ) {
            part2 = part2.substring( 1 );//this is placed on top so the found symbol is no included in part2 when found
            if( c == splitChar ) {
                occured++;
                if( occured == occurenceToSplit ) {
                    result[0] = part1.trim();
                    result[1] = part2.trim();
                    return result;
                }
            }
            part1 += c;
        }

        result[0] = part1.trim();
        result[1] = part2.trim();//if we get here, part2 should be empty
        return result;
    }

    public static String[] splitAt( String splitSymbol, String questionableValue, int occurenceToSplit ) {
        //@@ensure splitSymbol is at least size 1
        return splitAt( splitSymbol.toCharArray()[0], questionableValue, occurenceToSplit );
    }

    /**
     * Strips the html and returns the text
     *
     * @param htmlText
     * @return
     */
    public static String getPlainText( String htmlText ) {
        if( htmlText != null ) {
          
            return "";
        }
        else {
            return "";
        }
    }

    /**
     * Removes characters from the start and end of value, if they are the same (e.g. enclosed in brackets or
     * quotes). Only removes symbols, not whitespaces, digits, or letters.
     *
     * @param value
     * @return The cleaned value
     */
    public static String removeStartEndSymbols( String value ) {        
        if( value == null ) {
            return null;
        }
        //that can only happen, if we have at least 2 characters
        if( value.length() <= 1 ) {
            return value;
        }
        
        char[] charArray = value.toCharArray();
        if( charArray[0] == charArray[value.length() - 1] && startsWithSymbol( value )) {
            value = value.substring( 1, value.length() - 1 );
        }
        return value;
    }
}
