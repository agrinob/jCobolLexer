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

import java.io.StringReader;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andr&eacute;s Gri&ntilde;&oacute; Brandt <agrinob@hotmail.com>
 */
public class CobolLexerTest {

    public CobolLexerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testZeroLength() throws Exception {
        String card = "";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        assertEquals(0, tokens.size());
    }

    @Test
    public void test6Length() throws Exception {
        String card = "123456";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        assertEquals(0, tokens.size());
    }

    @Test
    public void test7Length() throws Exception {
        String card = "123456 ";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        assertEquals(0, tokens.size());
    }

    @Test
    public void test7ILength() throws Exception {
        String card = "123456-";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 7, "-", CobolType.CONTINUATION));
        assertEquals(expected, tokens);
    }

    @Test
    public void testIDDivision() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456 ID DIVISION.";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 8, "ID"));
        expected.add(new CobolToken(1, 11, "DIVISION"));
        expected.add(new CobolToken(1, 19, ".", CobolType.SEPARATOR));
        assertEquals(expected, tokens);
    }

    @Test
    public void testIDDivisionFree() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "$ set sourceformat\"free\"\nID DIVISION.";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 1, "$ set sourceformat\"free\"", CobolType.SPECIAL_LINE));
        expected.add(new CobolToken(2, 1, "ID"));
        expected.add(new CobolToken(2, 4, "DIVISION"));
        expected.add(new CobolToken(2, 12, ".", CobolType.SEPARATOR));
        assertEquals(expected, tokens);
    }

    @Test
    public void testIDDivisionFree2() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "      $ set sourceformat\"free\"\n" + "" +
                      "IDENTIFICATION DIVISION.\n" +
                      "PROGRAM-ID. BookshopRpt91.Cbl.\n" +
                      "AUTHOR.  MICHAEL COUGHLAN.\n" +
                      "*Originally written for VAX COBOL 1991\n" +
                      "ENVIRONMENT DIVISION.\n" +
                      "INPUT-OUTPUT SECTION.\n" +
                      "DATA DIVISION.\n";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 7, "$ set sourceformat\"free\"", CobolType.SPECIAL_LINE));
        expected.add(new CobolToken(2, 1, "IDENTIFICATION"));
        expected.add(new CobolToken(2, 16, "DIVISION"));
        expected.add(new CobolToken(2, 24, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(3, 1, "PROGRAM-ID"));
        expected.add(new CobolToken(3, 11, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(3, 13, "BookshopRpt91.Cbl"));
        expected.add(new CobolToken(3, 30, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(4, 1, "AUTHOR"));
        expected.add(new CobolToken(4, 7, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(4, 10, "MICHAEL COUGHLAN.", CobolType.TEXT));
        expected.add(new CobolToken(5, 1, "*Originally written for VAX COBOL 1991", CobolType.COMMENT));
        expected.add(new CobolToken(6, 1, "ENVIRONMENT"));
        expected.add(new CobolToken(6, 13, "DIVISION"));
        expected.add(new CobolToken(6, 21, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(7, 1, "INPUT-OUTPUT"));
        expected.add(new CobolToken(7, 14, "SECTION"));
        expected.add(new CobolToken(7, 21, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(8, 1, "DATA"));
        expected.add(new CobolToken(8, 6, "DIVISION"));
        expected.add(new CobolToken(8, 14, ".", CobolType.SEPARATOR));

        assertEquals(expected, tokens);
    }

    @Test
    public void testIDDivisionCommentEntry() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "000001 ID DIVISION.\n"
                + "000002 program-id. asde.\n"
                + "000003* COMMENT.\n"
                + "000004/     perez.\n"
                + "000005 installation.\n"
                + "000006      ibm center (1, 2).\n"
                + "000007 environment division.\n"
                + "000009 source-computer. pc.\n";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 8, "ID"));
        expected.add(new CobolToken(1, 11, "DIVISION"));
        expected.add(new CobolToken(1, 19, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(2, 8, "program-id"));
        expected.add(new CobolToken(2, 18, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(2, 20, "asde", CobolType.WORD));
        expected.add(new CobolToken(2, 24, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(3, 7, "* COMMENT.", CobolType.COMMENT));
        expected.add(new CobolToken(4, 7, "/     perez.", CobolType.NEW_PAGE));
        expected.add(new CobolToken(5, 8, "installation"));
        expected.add(new CobolToken(5, 20, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(6, 13, "ibm center (1, 2).", CobolType.TEXT));
        expected.add(new CobolToken(7, 8, "environment"));
        expected.add(new CobolToken(7, 20, "division"));
        expected.add(new CobolToken(7, 28, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(8, 8, "source-computer"));
        expected.add(new CobolToken(8, 23, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(8, 25, "pc"));
        expected.add(new CobolToken(8, 27, ".", CobolType.SEPARATOR));

        if (!expected.equals(tokens)) {
            for (int i = 0; i < expected.size() && i < tokens.size(); i++) {
                CobolToken e = expected.get(i);
                CobolToken t = tokens.get(i);
                if (!e.equals(t)) {
                    System.out.println(e + " != " + t);
                }
            }
        }
        assertEquals(expected, tokens);
    }

    @Test
    public void testIDDivisionProgramCommentEntry() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "000001 ID DIVISION.\n"
                    + "000002 program-id. test is initial program.\n"
                    + "000003 installation. ibm center (1, 2).";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 8, "ID"));
        expected.add(new CobolToken(1, 11, "DIVISION"));
        expected.add(new CobolToken(1, 19, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(2, 8, "program-id"));
        expected.add(new CobolToken(2, 18, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(2, 20, "test"));
        expected.add(new CobolToken(2, 25, "is"));
        expected.add(new CobolToken(2, 28, "initial"));
        expected.add(new CobolToken(2, 36, "program"));
        expected.add(new CobolToken(2, 43, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(3, 8, "installation"));
        expected.add(new CobolToken(3, 20, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(3, 22, "ibm center (1, 2).", CobolType.TEXT));

        if (!expected.equals(tokens)) {
            for (int i = 0; i < expected.size() && i < tokens.size(); i++) {
                CobolToken e = expected.get(i);
                CobolToken t = tokens.get(i);
                if (!e.equals(t)) {
                    System.out.println(e + " != " + t);
                }
            }
        }
        assertEquals(expected, tokens);
    }

    @Test
    public void testIDDivisionProgramCommentEntryMulti() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "000001 ID DIVISION.\n"
                    + "000002 program-id.\n" 
                    + "000003     test is initial program.\n"
                    + "000004 author.\n"
                    + "000005     juan\n"
                    + "000006     perez.\n"
                    + "000007 environment division.";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 8, "ID"));
        expected.add(new CobolToken(1, 11, "DIVISION"));
        expected.add(new CobolToken(1, 19, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(2, 8, "program-id"));
        expected.add(new CobolToken(2, 18, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(3, 12, "test"));
        expected.add(new CobolToken(3, 17, "is"));
        expected.add(new CobolToken(3, 20, "initial"));
        expected.add(new CobolToken(3, 28, "program"));
        expected.add(new CobolToken(3, 35, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(4,  8, "author"));
        expected.add(new CobolToken(4, 14, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(5, 12, "juan", CobolType.TEXT));
        expected.add(new CobolToken(6, 12, "perez.", CobolType.TEXT));
        expected.add(new CobolToken(7, 8, "environment"));
        expected.add(new CobolToken(7, 20, "division"));
        expected.add(new CobolToken(7, 28, ".", CobolType.SEPARATOR));

        if (!expected.equals(tokens)) {
            for (int i = 0; i < expected.size() && i < tokens.size(); i++) {
                CobolToken e = expected.get(i);
                CobolToken t = tokens.get(i);
                if (!e.equals(t)) {
                    System.out.println(e + " != " + t);
                }
            }
        }
        assertEquals(expected, tokens);
    }

    @Test
    public void testIDDivisionProgramCommentEntryFree() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "$SET SOURCEFORMAT\"FREE\"\n" +
                      " ID DIVISION. program-id. test is initial program.";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 1, "$SET SOURCEFORMAT\"FREE\"", CobolType.SPECIAL_LINE));
        expected.add(new CobolToken(2, 2, "ID"));
        expected.add(new CobolToken(2, 5, "DIVISION"));
        expected.add(new CobolToken(2, 13, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(2, 15, "program-id"));
        expected.add(new CobolToken(2, 25, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(2, 27, "test"));
        expected.add(new CobolToken(2, 32, "is"));
        expected.add(new CobolToken(2, 35, "initial"));
        expected.add(new CobolToken(2, 43, "program"));
        expected.add(new CobolToken(2, 50, ".", CobolType.SEPARATOR));

        if (!expected.equals(tokens)) {
            for (int i = 0; i < expected.size() && i < tokens.size(); i++) {
                CobolToken e = expected.get(i);
                CobolToken t = tokens.get(i);
                if (!e.equals(t)) {
                    System.out.println(e + " != " + t);
                }
            }
        }
        assertEquals(expected, tokens);
    }

    @Test
    public void testOperator() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     + - * / ** < <= > >= =";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, "+"));
        expected.add(new CobolToken(1, 14, "-"));
        expected.add(new CobolToken(1, 16, "*"));
        expected.add(new CobolToken(1, 18, "/"));
        expected.add(new CobolToken(1, 20, "**"));
        expected.add(new CobolToken(1, 23, "<"));
        expected.add(new CobolToken(1, 25, "<="));
        expected.add(new CobolToken(1, 28, ">"));
        expected.add(new CobolToken(1, 30, ">="));
        expected.add(new CobolToken(1, 33, "="));
        assertEquals(expected, tokens);
    }

    @Test
    public void testStatement() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     STOP.";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, "STOP"));
        expected.add(new CobolToken(1, 16, ".", CobolType.SEPARATOR));
        assertEquals(expected, tokens);
    }

    @Test
    public void testComment() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456* NEW PAGE";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 7, "* NEW PAGE", CobolType.COMMENT));
        assertEquals(expected, tokens);
    }

    @Test
    public void testDebuggingFree() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "d          STOP RUN.";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens('d');
        TokenList expected = new TokenList();
//        expected.add(new CobolToken(1, 1, "d", CobolType.START_DEBUG));
        expected.add(new CobolToken(1, 12, "STOP", CobolType.WORD));
        expected.add(new CobolToken(1, 17, "RUN", CobolType.WORD));
        expected.add(new CobolToken(1, 20, ".", CobolType.SEPARATOR));
//        expected.add(new CobolToken(1, 1, "", CobolType.END_DEBUG));
        assertEquals(expected, tokens);
    }

    @Test
    public void testNoDebuggingFree() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "$SET SOURCEFORMAT\"FREE\"\nmove x";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 1, "$SET SOURCEFORMAT\"FREE\"", CobolType.SPECIAL_LINE));
        expected.add(new CobolToken(2, 1, "move", CobolType.WORD));
        expected.add(new CobolToken(2, 6, "x", CobolType.WORD));
        assertEquals(expected, tokens);
    }

    @Test
    public void testDebugging() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456D    STOP RUN.";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens('d');
        TokenList expected = new TokenList();
//        expected.add(new CobolToken(1, 7, "D", CobolType.START_DEBUG));
        expected.add(new CobolToken(1, 12, "STOP", CobolType.WORD));
        expected.add(new CobolToken(1, 17, "RUN", CobolType.WORD));
        expected.add(new CobolToken(1, 20, ".", CobolType.SEPARATOR));
//        expected.add(new CobolToken(1, 7, "", CobolType.END_DEBUG));
        assertEquals(expected, tokens);
    }

    @Test
    public void testDebuggingWithOtherLetter() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456a    STOP RUN.";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens('a');
        TokenList expected = new TokenList();
//        expected.add(new CobolToken(1, 7, "a", CobolType.START_DEBUG));
        expected.add(new CobolToken(1, 12, "STOP", CobolType.WORD));
        expected.add(new CobolToken(1, 17, "RUN", CobolType.WORD));
        expected.add(new CobolToken(1, 20, ".", CobolType.SEPARATOR));
//        expected.add(new CobolToken(1, 7, "", CobolType.END_DEBUG));
        assertEquals(expected, tokens);
    }

    @Test
    public void testNewPage() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456/ NEW PAGE";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 7, "/ NEW PAGE", CobolType.NEW_PAGE));
        assertEquals(expected, tokens);
    }

    @Test
    public void testFullSizeCard() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456                                                                  COBOL   ";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        assertEquals(0, tokens.size());
    }

    @Test
    public void testFullSizeCard2() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456- PROCEDURE DIVISION. STOP.                                       COBOL   ";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 7, "-", CobolType.CONTINUATION));
        expected.add(new CobolToken(1, 9, "PROCEDURE"));
        expected.add(new CobolToken(1, 19, "DIVISION"));
        expected.add(new CobolToken(1, 27, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(1, 29, "STOP"));
        expected.add(new CobolToken(1, 33, ".", CobolType.SEPARATOR));
        assertEquals(expected, tokens);
    }

    @Test
    public void testFullSizeCard3() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "086900             MOVE   \"SOURCE FILE WILL BE SUPPRESSED\" TO PRINT-DATAEXEC84.2";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 20, "MOVE"));
        expected.add(new CobolToken(1, 27, "\"SOURCE FILE WILL BE SUPPRESSED\"", CobolType.STRING));
        expected.add(new CobolToken(1, 60, "TO"));
        expected.add(new CobolToken(1, 63, "PRINT-DATA"));
        assertEquals(expected, tokens);
    }

    @Test
    public void testMultiLines() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "\n";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        assertEquals(0, tokens.size());
    }

    @Test
    public void testMultiLines2() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456\n123456";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        assertEquals(0, tokens.size());
    }

    @Test
    public void testMultiLines3() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456-\n123456 A";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 7, "-", CobolType.CONTINUATION));
        expected.add(new CobolToken(2, 8, "A"));
        assertEquals(expected, tokens);
    }

    @Test
    public void testString() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     'A'";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, "'A'", CobolType.STRING));
        assertEquals(expected, tokens);
    }

    @Test
    public void test2LinesString() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*12
        String card = "123456     'A\n123456-    'B'";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*12
        expected.add(new CobolToken(1, 12, "'A                                                           ", CobolType.STRING));
        expected.add(new CobolToken(2, 7, "-", CobolType.CONTINUATION));
        expected.add(new CobolToken(2, 12, "'B'", CobolType.STRING));
        assertEquals(expected, tokens);
    }

    @Test
    public void testString2() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     'AB'''";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, "'AB'''", CobolType.STRING));
        assertEquals(expected, tokens);
    }

    @Test
    public void testString3() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     'AB'''.";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, "'AB'''", CobolType.STRING));
        expected.add(new CobolToken(1, 18, ".", CobolType.SEPARATOR));
        assertEquals(expected, tokens);
    }

    @Test
    public void testString4() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     'AB'' C'";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, "'AB'' C'", CobolType.STRING));
        assertEquals(expected, tokens);
    }

    @Test
    public void testManyQuotesString() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     'AB '''' C'";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, "'AB '''' C'", CobolType.STRING));
        assertEquals(expected, tokens);
    }

    @Test
    public void testContinuationNoString() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     ABC\n123456-       DEF";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        //                       123456789*123456789*123456789*123456789*123456789*123456789*123456789*12
        expected.add(new CobolToken(1, 12, "ABCDEF", CobolType.WORD));
        assertEquals(expected, tokens);
    }

    @Test
    public void testContinuationString() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     'AB\n123456-       'HOLA'";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        //                       123456789*123456789*123456789*123456789*123456789*123456789*123456789*12
        expected.add(new CobolToken(1, 12, "'AB                                                          HOLA'", CobolType.STRING));
        assertEquals(expected, tokens);
    }

    @Test
    public void testContinuationString2() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     'AB                                                          \n123456-       ''.";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        //                       123456789*123456789*123456789*123456789*123456789*123456789*123456789*12
        expected.add(new CobolToken(1, 12, "'AB                                                          '", CobolType.STRING));
        expected.add(new CobolToken(2, 17, ".", CobolType.SEPARATOR));
        assertEquals(expected, tokens);
    }

    @Test
    public void testSpeciaContinuationStringTrue() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*12  123456789*123456789*1
        String card = "123456     'AB                                                         '\n123456-       ''ABC'.";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        //                       123456789*123456789*123456789*123456789*123456789*123456789*123456789*12
        expected.add(new CobolToken(1, 12, "'AB                                                         ''ABC'", CobolType.STRING));
        expected.add(new CobolToken(2, 21, ".", CobolType.SEPARATOR));

        assertEquals(expected, tokens);
    }

    @Test
    public void testStringHexH() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*12  123456789*123456789*1
        String card = "123456     H'ff00'";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        //                       123456789*123456789*123456789*123456789*123456789*123456789*123456789*12
        expected.add(new CobolToken(1, 12, "H'ff00'", CobolType.STRING));

        assertEquals(expected, tokens);
    }

    @Test
    public void testStringHexX() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*12  123456789*123456789*1
        String card = "123456     X\"ff00\"";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        //                       123456789*123456789*123456789*123456789*123456789*123456789*123456789*12
        expected.add(new CobolToken(1, 12, "X\"ff00\"", CobolType.STRING));

        assertEquals(expected, tokens);
    }

    @Test
    public void testSpeciaContinuationStringFalse() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*12  123456789*123456789*1
        String card = "123456     'AB                                                          \n123456-       ''.";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        //                       123456789*123456789*123456789*123456789*123456789*123456789*123456789*12
        expected.add(new CobolToken(1, 12, "'AB                                                          ", CobolType.STRING));
        expected.add(new CobolToken(2, 7, "-", CobolType.CONTINUATION));
        expected.add(new CobolToken(2, 15, "''", CobolType.STRING));
        expected.add(new CobolToken(2, 17, ".", CobolType.SEPARATOR));

        assertEquals(expected, tokens);
    }

    @Test
    public void testSpeciaContinuationStringMixedFalse() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*12  123456789*123456789*1
        String card = "123456     'AB                                                        ' \n123456-       ''.";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        //                       123456789*123456789*123456789*123456789*123456789*123456789*123456789*12
        expected.add(new CobolToken(1, 12, "'AB                                                        '", CobolType.STRING));
        expected.add(new CobolToken(2, 7, "-", CobolType.CONTINUATION));
        expected.add(new CobolToken(2, 15, "''", CobolType.STRING));
        expected.add(new CobolToken(2, 17, ".", CobolType.SEPARATOR));

        assertEquals(expected, tokens);
    }

    @Test
    public void testNoContinuationString3() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     'AB                                                         '\n123456        '' . ID";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        //                       123456789*123456789*123456789*123456789*123456789*123456789*123456789*12
        expected.add(new CobolToken(1, 12, "'AB                                                         '", CobolType.STRING));
        expected.add(new CobolToken(2, 15, "''", CobolType.STRING));
        expected.add(new CobolToken(2, 18, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(2, 20, "ID"));
        assertEquals(expected, tokens);
    }

    @Test
    public void testNoContinuationString2() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     'AB                                                         '\n123456        '' . ID";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        //                       123456789*123456789*123456789*123456789*123456789*123456789*123456789*12
        expected.add(new CobolToken(1, 12, "'AB                                                         '", CobolType.STRING));
        expected.add(new CobolToken(2, 15, "''", CobolType.STRING));
        expected.add(new CobolToken(2, 18, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(2, 20, "ID"));
        assertEquals(expected, tokens);
    }

    @Test
    public void testComma() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     ,";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, ",", CobolType.SEPARATOR));
        assertEquals(expected, tokens);
    }

    @Test
    public void testCommaSeparated() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     A, 'B'";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, "A"));
        expected.add(new CobolToken(1, 13, ",", CobolType.SEPARATOR));
        expected.add(new CobolToken(1, 15, "'B'", CobolType.STRING));
        assertEquals(expected, tokens);
    }

    @Test
    public void testCommaSeparatedString() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     'A, B'";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, "'A, B'", CobolType.STRING));
        assertEquals(expected, tokens);
    }

    @Test
    public void testSemiColon() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     ;";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, ";", CobolType.SEPARATOR));
        assertEquals(expected, tokens);
    }

    @Test
    public void testParens() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     PROCEDURE DIVISION.\n123457     9(9)";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, "PROCEDURE", CobolType.WORD));
        expected.add(new CobolToken(1, 22, "DIVISION", CobolType.WORD));
        expected.add(new CobolToken(1, 30, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(2, 12, "9", CobolType.WORD));
        expected.add(new CobolToken(2, 13, "(", CobolType.LEFT_PAREN));
        expected.add(new CobolToken(2, 14, "9", CobolType.WORD));
        expected.add(new CobolToken(2, 15, ")", CobolType.RIGHT_PAREN));
        assertEquals(expected, tokens);
    }

    @Test
    public void testSemiColonSeparated() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     A;  'B'";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, "A"));
        expected.add(new CobolToken(1, 13, ";", CobolType.SEPARATOR));
        expected.add(new CobolToken(1, 16, "'B'", CobolType.STRING));
        assertEquals(expected, tokens);
    }

    @Test
    public void testPeriod() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     .";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, ".", CobolType.SEPARATOR));
        assertEquals(expected, tokens);
    }

    @Test
    public void testPicturePeriod() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     99.";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, "99"));
        expected.add(new CobolToken(1, 14, ".", CobolType.SEPARATOR));
        assertEquals(expected, tokens);
    }

    @Test
    public void testInsidePeriod() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     99.99";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, "99.99"));
        assertEquals(expected, tokens);
    }

    @Test
    public void testPeriodPeriod() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     99.99..";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, "99.99."));
        expected.add(new CobolToken(1, 18, ".", CobolType.SEPARATOR));
        assertEquals(expected, tokens);
    }

    @Test
    public void testPeriod72() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456                                                                9.";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 71, "9"));
        expected.add(new CobolToken(1, 72, ".", CobolType.SEPARATOR));
        assertEquals(expected, tokens);
    }

    @Test
    public void testPictureString() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     PIC X(80).";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, "PIC",   CobolType.WORD));
        expected.add(new CobolToken(1, 16, "X(80)", CobolType.WORD));
        expected.add(new CobolToken(1, 21, ".", CobolType.SEPARATOR));
        assertEquals(expected, tokens);
    }

    @Test
    public void testPictureStringContinuation() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     PI\n" +
                      "123456-    CTURE X(\n"+
                      "123457-             80).";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, "PICTURE",   CobolType.WORD));
        expected.add(new CobolToken(2, 18, "X(80)",     CobolType.WORD));
        expected.add(new CobolToken(3, 24, ".",         CobolType.SEPARATOR));
        assertEquals(expected, tokens);
    }

    @Test
    public void testPictureStringContinuation2() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     77 WDATA PIC S9(\n" +
                      "123457-             6)V9(6).";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, "77"));
        expected.add(new CobolToken(1, 15, "WDATA"));
        expected.add(new CobolToken(1, 21, "PIC"));
        expected.add(new CobolToken(1, 25, "S9(6)V9(6)"));
        expected.add(new CobolToken(2, 28, ".",  CobolType.SEPARATOR));
        assertEquals(expected, tokens);
    }


    @Test
    public void testOpenParentesis() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     PROCEDURE DIVISION. (";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, "PROCEDURE", CobolType.WORD));
        expected.add(new CobolToken(1, 22, "DIVISION", CobolType.WORD));
        expected.add(new CobolToken(1, 30, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(1, 32, "(", CobolType.LEFT_PAREN));
        assertEquals(expected, tokens);
    }

    @Test
    public void testCloseParentesis() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     PROCEDURE DIVISION. )";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, "PROCEDURE", CobolType.WORD));
        expected.add(new CobolToken(1, 22, "DIVISION", CobolType.WORD));
        expected.add(new CobolToken(1, 30, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(1, 32, ")", CobolType.RIGHT_PAREN));
        assertEquals(expected, tokens);
    }

    @Test
    public void testParentesis() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     (A)";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, "(", CobolType.LEFT_PAREN));
        expected.add(new CobolToken(1, 13, "A", CobolType.WORD));
        expected.add(new CobolToken(1, 14, ")", CobolType.RIGHT_PAREN));
        assertEquals(expected, tokens);
    }

    @Test
    public void testPicture() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     PIC X(9).";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, "PIC", CobolType.WORD));
        expected.add(new CobolToken(1, 16, "X(9)", CobolType.WORD));
        expected.add(new CobolToken(1, 20, ".",   CobolType.SEPARATOR));
        assertEquals(expected, tokens);
    }

    @Test
    public void testPictureiS() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     PIC IS X(9).";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, "PIC"));
        expected.add(new CobolToken(1, 16, "IS"));
        expected.add(new CobolToken(1, 19, "X(9)"));
        expected.add(new CobolToken(1, 23, ".",   CobolType.SEPARATOR));
        assertEquals(expected, tokens);
    }

    @Test
    public void testArgList() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     PROCEDURE DIVISION. (a, (b));";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, "PROCEDURE", CobolType.WORD));
        expected.add(new CobolToken(1, 22, "DIVISION", CobolType.WORD));
        expected.add(new CobolToken(1, 30, ".", CobolType.SEPARATOR));
        expected.add(new CobolToken(1, 32, "(", CobolType.LEFT_PAREN));
        expected.add(new CobolToken(1, 33, "a"));
        expected.add(new CobolToken(1, 34, ",", CobolType.SEPARATOR));
        expected.add(new CobolToken(1, 36, "(", CobolType.LEFT_PAREN));
        expected.add(new CobolToken(1, 37, "b"));
        expected.add(new CobolToken(1, 38, ")", CobolType.RIGHT_PAREN));
        expected.add(new CobolToken(1, 39, ")", CobolType.RIGHT_PAREN));
        expected.add(new CobolToken(1, 40, ";", CobolType.SEPARATOR));
        assertEquals(expected, tokens);
    }

    @Test
    public void testPseudoText() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        //       0123456789*123
        String card = "123456     == A B C ==";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, "==", CobolType.START_PSEUDO_TEXT));
        expected.add(new CobolToken(1, 14, " A B C ", CobolType.PSEUDO_TEXT));
        expected.add(new CobolToken(1, 21, "==", CobolType.END_PSEUDO_TEXT));
        assertEquals(expected, tokens);
    }

    @Test
    public void testPseudoTextMultiLine() throws Exception {
        //             123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
        String card = "123456     == A B C\n123456        ==";
        System.out.println("Card='" + card + "'");
        StringReader reader = new StringReader(card);
        CobolLexer cardReader = new CobolLexer(reader);
        TokenList tokens = cardReader.getTokens();
        TokenList expected = new TokenList();
        expected.add(new CobolToken(1, 12, "==", CobolType.START_PSEUDO_TEXT));
        // 456789*123456789*123456789*123456789*123456789*123456789*12
        expected.add(new CobolToken(1, 14, " A B C                                                     ", CobolType.PSEUDO_TEXT));
        expected.add(new CobolToken(2, 8, "       ", CobolType.PSEUDO_TEXT));
        expected.add(new CobolToken(2, 15, "==", CobolType.END_PSEUDO_TEXT));
        assertEquals(expected, tokens);
    }
}
