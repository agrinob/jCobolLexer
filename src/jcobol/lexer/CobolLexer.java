/*
 *  Copyright (C) 2010 Andres Grino Brandt <agrinob@hotmail.com>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jcobol.lexer;

import java.io.IOException;
import java.io.Reader;
import jcobol.reader.CardReader;
import jcobol.reader.CobolLine;
import org.apache.log4j.Logger;

/**
 * Read a cobol readerSource program and parse it in lexical tokens.
 * @version 1.01 Comment entry in ID DIVISION are tokens of type TEXT.
 * @author Andr&eacute;s Gri&ntilde;&oacute; Brandt <agrinob@hotmail.com>
 */
public class CobolLexer {

    private final static Logger logger = Logger.getLogger(CobolLexer.class);
    private static final int finalCol = 72;
    private final Reader readerSource;
    /** Switch for multi-line pseudo-text */
    private boolean inPseudoText;
    /** List of all tokens, always updated. */
    private TokenList allTokens;
    /** Name of actual DIVISION where are in. */
    private String lastDivision;
    /** Name of actual paragraph in ID DIVISION where are in. */
    private String lastParagraph;
    /**
     * true is rest of the line must be taken as a unparsed text.
     * false means that rest of the line must be parsed.
     */
    private boolean modeText;

    /**
     * Constructor.
     * @param source The readerSource program, free or fixed format.
     */
    public CobolLexer(Reader source) {
        assert source != null;
        this.readerSource = source;
    }

    /**
     * Get the readerSource program as a list of tokens.
     * @param debug Zero or more (case insensitive) characters. Used to selected
     * which debug lines from source code to parse. Debug lines not selected are
     * discarded.
     * @return A list of tokens.
     * @throws IOException In case of I/O error reading the readerSource program.
     */
    public TokenList getTokens(final char... debug) throws IOException {
        CardReader cardReader = new CardReader(readerSource);
        this.lastDivision = "";
        this.lastParagraph = "";
        this.modeText = false;

        allTokens = new TokenList();
        inPseudoText = false;
        CobolLine line = cardReader.readLine();
        int lineNumber = 1;
        while (line != null) {
            if (isDebugLineIncluded(line, debug)) {
                parseLine(line);
            }
            line = cardReader.readLine();
            lineNumber++;
        }

        return allTokens;
    }

    private boolean isDebugLineIncluded(final CobolLine line, final char... debug) {
        char lineDebug = Character.toUpperCase(line.getIndicator());
        boolean result = true;
        if (Character.isLetter(lineDebug)) {
            result = false;
            for (char d : debug) {
                if (Character.toUpperCase(d) == lineDebug) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Parse a single cobol line.
     * @param line The cobol line.
     */
    private void parseLine(final CobolLine line) {
        CobolToken token = parseIndicator(line);
        if (token == null
                || token.getType() == CobolType.CONTINUATION) {
//                || token.getType() == CobolType.START_DEBUG) {
            parseCode(line);
//            // After processing a debug line, mark the end of the debug.
//            if (token != null && token.getType() == CobolType.START_DEBUG) {
//                add(new CobolToken(token.getRow(), token.getCol(), "", CobolType.END_DEBUG));
//            }
        }
    }

    /**
     * Parse a proper cobol line. Proper cobol line are actual code line, i.e.
     * not compiler directive, new page, etc.
     * @param cobolLine The cobol line to be parsed.
     */
    private void parseCode(final CobolLine cobolLine) {
        String line = cobolLine.getSource();
        int index = 0;
        int last = line.length() - 1;

        modeText = isModeText(cobolLine);

        while (index <= last) {
            if (!inPseudoText) {
                //  Skip whitespaces
                while (index <= last && line.charAt(index) == ' ') {
                    index++;
                }
            }
            if (index <= last) {
                CobolToken token = null;
                if (modeText) {
                    token = parseCommentEntry(cobolLine, index);
                } else if (isPseudoText(line, index)) {
                    token = parsePseudoText(cobolLine, index);
                } else if (inPseudoText) {
                    token = parsePseudoTextContent(cobolLine, index);
                } else if (isString(line, index)) {
                    token = parseString(cobolLine, index);
                } else if (isSeparator(line, index)) {
                    token = parseSeparator(cobolLine, index);
                } else if (isOperator(line, index)) {
                    token = parseOperator(cobolLine, index);
                } else {
                    token = parseWord(cobolLine, index);
                }

                add(token);
                index += token.getText().length();
            }
        }
    }

    /**
     * Determine how to interprete a source line in ID DIVISION.
     * @param cobolLine The line to be examined.
     * @return true if the line must be take as a comment (not parsed word by word).
     */
    private boolean isModeText(final CobolLine cobolLine) {
        boolean result = false;
        if (lastDivision.equals("IDENTIFICATION")) {
            if (lastParagraph.equals("program-id")) {
                //  PROGRAM-ID and following lines must be parsed word by word.
                result = false;
            }
            else {
                //  Any other paragraph entry
                // In free format comment entries paragraph are one line only.
                result = (cobolLine.isFixed()) ? cobolLine.getSource().startsWith("    ") : false;
                }
            }
        return result;
    }

    private CobolToken parseOperator(final CobolLine line, final int index) {
        final String source = line.getSource();
        final int last = source.length() - 1;
        int i = index;
        // Catch ending separator.
        do {
            i++;
        } while (i <= last && !isSeparator(source, i));
        String srcOp = source.substring(index, i);
        return new CobolToken(line.getLineNumber(), line.getFirstColumn() + index, srcOp, getTypeForOperator(srcOp));
    }

    private CobolToken parseWord(final CobolLine line, final int index) {
        final String source = line.getSource();
        final int last = source.length() - 1;
        int i = index;
        // Catch ending separator.
        do {
            i++;
        } while (i <= last && !isSeparator(source, i));

        String text = source.substring(index, i);
        return new CobolToken(line.getLineNumber(), line.getFirstColumn() + index, text, CobolType.WORD);
    }

    /**
     * Evaluate last tokens added.
     * @return true if last tokens are a variation of "PICTURE IS".
     */
    private boolean isLastPicture() {
        int index = 0;
        CobolToken last = this.allTokens.getFromLast(index);
        if (last == null) {
            return false;
        }
        if (last.getType() == CobolType.CONTINUATION) {
            index++;
            last = this.allTokens.getFromLast(index);
        }

        String text = last.getText().toUpperCase();
        if (!text.equals("IS") && !text.equals("PIC") && !text.equals("PICTURE")) {
            //  Maybe we are in PICTURE IS some-partial-picture-string.
            index++;
            last = this.allTokens.getFromLast(index);
            if (last != null) {
                text = last.getText().toUpperCase();
                index++;
            }
        }

        if (text.equals("IS")) {
            index++;
            last = this.allTokens.getFromLast(index);
            if (last != null) {
                text = last.getText().toUpperCase();
                }
            }

        return (text.equals("PIC") || text.equals("PICTURE"));
    }


    private CobolToken parseSeparator(final CobolLine line, final int index) {
        String text = line.getSource().substring(index, index + 1);
        CobolType type = CobolType.SEPARATOR;
        if (text.equals("(")) {
            type = CobolType.LEFT_PAREN;
        } else {
            if (text.equals(")")) {
                type = CobolType.RIGHT_PAREN;
            }
        }
        return new CobolToken(line.getLineNumber(), line.getFirstColumn() + index, text, type);
    }

    private CobolToken parsePseudoText(final CobolLine cobolLine, final int index) {
        CobolToken token = new CobolToken(cobolLine.getLineNumber(),
                cobolLine.getFirstColumn() + index,
                "==",
                (inPseudoText) ? CobolType.END_PSEUDO_TEXT : CobolType.START_PSEUDO_TEXT);
        inPseudoText = !inPseudoText;
        return token;
    }

    private CobolToken parsePseudoTextContent(final CobolLine cobolLine, final int index) {
        final String line = cobolLine.getSource();
        final int last = line.length() - 1;

        int i = index;
        while (i <= last && !line.startsWith("==", i)) {
            i++;
        }
        return new CobolToken(cobolLine.getLineNumber(), cobolLine.getFirstColumn() + index, line.substring(index, i), CobolType.PSEUDO_TEXT);
    }

    private CobolToken parseContent(final CobolLine cobolLine) {
        final String line = cobolLine.getSource();
        int index = line.length() - 1;
        while (index > 0 && Character.isWhitespace(line.charAt(index))) {
            index--;
        }
        return new CobolToken(cobolLine.getLineNumber(), cobolLine.getFirstColumn(), line.substring(0, index + 1), CobolType.TEXT);
    }

    private CobolToken parseCommentEntry(final CobolLine cobolLine, final int index) {
        String line = cobolLine.getSource();
        return new CobolToken(cobolLine.getLineNumber(), cobolLine.getFirstColumn() + index, line.substring(index).trim(), CobolType.TEXT);
    }

    private CobolToken parseIndicator(final CobolLine line) {
        CobolToken token = null;
        char indicator = line.getIndicator();
        if (indicator != ' ' && !Character.isLetter(indicator)) {
            CobolType type = getTypeForIndicator(indicator);
            String text = new String(new char[]{indicator});
//            if (type != CobolType.CONTINUATION && type != CobolType.START_DEBUG) {
            if (type != CobolType.CONTINUATION) {
                text += line.getSource();
            }
            token = new CobolToken(line.getLineNumber(), line.getFirstColumn() - 1,
                    text.trim(), type);
            add(token);
        }
        return token;
    }

    private CobolType getTypeForOperator(final String operator) {
        CobolType type = CobolType.UNDEFINED;
        if (operator.equals("&")) {
            type = CobolType.AMPERSAND;
        }
        return type;
    }

    private CobolType getTypeForIndicator(final char indicator) {
        CobolType type = CobolType.CONTINUATION;
        switch (indicator) {
            case '-':
                type = CobolType.CONTINUATION;
                break;
            case '/':
                type = CobolType.NEW_PAGE;
                break;
            case '$':
                type = CobolType.SPECIAL_LINE;
                break;
            case '*':
                type = CobolType.COMMENT;
                break;
//            default:
//                //  A letter in indicator area means debugging line.
//                //  Standard is 'D'/'d', but some compilers allow 'A'-'Z'
//                //  and 'a'-'z'.
//                if (Character.isLetter(indicator)) {
//                    type = CobolType.START_DEBUG;
//                }
        }
        return type;
    }

    /**
     * Extract a string from cobol readerSource line.
     * <p>Recognize string delimited with quotes, semi-quotes and
     * hexadecimal string (H', X', H" and X").
     * @param lineNumber Line number of the cobol readerSource line.
     * @param card Cobol readerSource text.
     * @param start index where string start.
     * @return The string found.
     */
    private CobolToken parseString(final CobolLine cobolLine, final int start) {
        final String line = cobolLine.getSource();
        final int last = line.length() - 1;
        char startChar = line.charAt(start);
        int index = start + 1;

        //  Check hexadecimal strings starting with H or X.
        if (Character.isLetter(startChar)) {
            //  Advance index to see the initial quote/semi-quote.
            index++;
            startChar = line.charAt(start + 1);
        }
        // Was the previous non-comment/non-empty line a string ending in
        // column 72 with a quote?
        if (isSpecialContinuation()) {
            //  The string must begin with quote-quote.
            //  Do not confuse with a empty string.
            index++;
        } //  Inspect the string, accounting for quote-quote.
        int count = 1;  // Count the quotes found so far.
        do {
            while (index <= last && line.charAt(index) != startChar) {
                index++;
            }
            while (index <= last && line.charAt(index) == startChar) {
                count++;
                index++;
            }
        } while (index <= last && count % 2 != 0);

        return new CobolToken(cobolLine.getLineNumber(), cobolLine.getFirstColumn() + start, line.substring(start, index), CobolType.STRING);
    }

    private void add(final CobolToken token) {
        allTokens.add(token);
        checkLastDivision(token);
        if (lastDivision.equals("IDENTIFICATION")) {
            checkParagraphType(token);
        }
    }

    private void checkParagraphType(final CobolToken lastToken) {
        if (lastToken.getText().equals(".")) {
            CobolToken prevToken = allTokens.getFromLast(1);
            if (prevToken.getType() == CobolType.WORD) {
                String text = prevToken.getText().toLowerCase();
                if (text.equals("author") || text.equals("installation")
                        || text.equals("date-written") || text.equals("date-compiled")
                        || text.equals("security") || text.equals("program-id")) {
                    lastParagraph = text;
                    logger.debug("last paragraph=" + lastParagraph);
                    modeText = !text.equals("program-id");
                }
            }
        }

    }

    /**
     * Mantains the name of the DIVISION we are in.
     */
    private void checkLastDivision(final CobolToken lastToken) {

        if (lastToken.getType() == CobolType.WORD && lastToken.getText().equalsIgnoreCase("DIVISION")) {
            CobolToken prevToken = allTokens.getFromLast(1);
            if (prevToken.getType() == CobolType.WORD) {
                lastDivision = prevToken.getText().toUpperCase();
                if (lastDivision.equals("ID")) {
                    lastDivision = "IDENTIFICATION";
                    logger.debug("last division=" + lastDivision);
                }
            }
        }
    }

    /**
     * Evaluate if rest of cobol line is a comment entry in ID DIVISION.
     * @param line The readerSource program line.
     * @param index The position to begin looking.
     * @return
     */
//    private boolean isCommentEntry(final String line, final int index) {
//        String text = line.substring(index).toLowerCase().trim();
//
//        if (text.startsWith("environment ")) {
//            return false;
//        }
//
//        String paragraphName = getParagraphName(text);
//        if (paragraphName != null) {
//            lastParagraph = paragraphName;
//        }
//        boolean isCommentEntry = !lastParagraph.equals("program-id");
//        logger.debug("isCommentEntry '" + text + "'? -> " + isCommentEntry);
//        return isCommentEntry;
//    }
//
//    private String getParagraphName(final String text) {
//        String[] parts = text.split("\\s", 2);
//        String name = null;
//        if (parts.length > 0) {
//            if (parts[0].equals("program-id.") || parts[0].equals("author.") ||
//                   parts[0].equals("date-compiled.") || parts[0].equals("date-written.") ||
//                   parts[0].equals("installation.") || parts[0].equals("security.")) {
//                name = parts[0].substring(0, parts[0].length() - 1);
//            }
//        }
//        return name;
//    }
//
    /**
     * Check continuation line for quote-quote.
     * <p>If continuation line begins with quote-quote, like
     * <pre><code>
     *               1                             7 
     * Cols 1234567890123 ...                      012 
     *            -    ''????...????
     * </code><pre>
     * it can be interpreted as:
     * <ul>
     * <li>A continuation string that begins with a quote as its first character.</li>
     * <li>A continuation string that contains and end with an empty string (patological, but legal).</li>
     * </ul>
     * <p>We must look at the continued line to do the right interpretation:
     * <i>
     * If a nonnumeric literal that is to be continued on the next line has as its last
    character a quotation mark in column 72, the continuation line must start with two
    consecutive quotation marks. This will result in a single quotation mark as part of
    the value of the nonnumeric literal.
     * </i>
     * <p><b>Example</b>
     * This one must return <b>true</b>.
     * <code>
     * <pre>
     *               1                             7
     * Cols 1234567890123 ...                      012
     *                 'STRING                       '
     *            *     IRRELEVANT COMMENTS
     *            -    ''CONTINUATION  '.
     * </pre>
     * </code>
     * @return true if the last lastToken is a (double) quoted string ending in
     * col 72.
     */
    private boolean isSpecialContinuation() {
        boolean result = false;
        final CobolToken lastToken = allTokens.getFromLast(0);
        final CobolToken prevToken = allTokens.getFromLast(1);

        if (lastToken != null && prevToken != null) {
            if (lastToken.getType() == CobolType.CONTINUATION && lastToken.getText().equals("-")) {
                if (prevToken.getLastCol() == CobolLexer.finalCol) {
                    String first = prevToken.getFirstChar();
                    String last = prevToken.getLastChar();

                    if (first.equals("'") || first.equals("\"")) {
                        result = first.equals(last);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Check for a cobol separator.
     * <p>Cobol separator are:
     * <ul>
     * <li>White space.
     * <li>Period, comma or semicolon, followed by white space.
     * <li>Colon.
     * <li>Parentesis, except in PICTURE format string.
     * </ul>
     * @param line The line under examination.
     * @param index Position to check-out.
     * @return true is line[index] contains one of the standard cobol separator.
     */
    private boolean isSeparator(final String line, final int index) {
        boolean result = false;
        final int len = line.length();

        if (index < len) {
            char car = line.charAt(index);
            if ((car == '(' || car == ')') && !isLastPicture()) {
                result = true;
            } else {
                if (car == ' ' || car == ':') {
                    result = true;
                } else {
                    if (car == '.' || car == ',' || car == ';') {
                        //  past end of readerSource line => whitespace, always a separator.
                        result = (index + 1 < len) ? (line.charAt(index + 1) == ' ') : true;
                    }
                }
            }
        }
        return result;
    }

    private boolean isOperator(final String line, final int index) {
        char car = line.charAt(index);
        return (car == '&');
    }

    /**
     * Detect if a string starts at giving position.
     * @param line The line to be examined.
     * @param index The first position to be examined in the line.
     * @return true if line[index] is the start of an string.
     */
    private boolean isString(final String line, final int index) {
        boolean result = false;
        char car = Character.toUpperCase(line.charAt(index));
        if (car == 'H' || car == 'X') {
            if (index + 1 < line.length()) {
                car = line.charAt(index + 1);
            }
        }
        return (car == '"' || car == '\'');
    }

    private boolean isPseudoText(final String line, final int index) {
        return line.startsWith("==", index);
    }
}
