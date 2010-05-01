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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andr&eacute;s Gri&ntilde;&oacute; Brandt <agrinob@hotmail.com>
 */
public class TokenListTest {

    public TokenListTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    @Test
    public void testGetFromLastEmptyList() {
        System.out.println("getFromLast(0) from an empty list");
        int pos = 0;
        TokenList instance = new TokenList();
        CobolToken expResult = null;
        CobolToken result = instance.getFromLast(pos);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetFromNonEmptyList0() {
        System.out.println("getFromLast(0) from an non-empty list");
        int pos = 0;
        TokenList instance = new TokenList();
        CobolToken expResult = new CobolToken(1, 12, "ID");
        instance.add(expResult);
        CobolToken result = instance.getFromLast(pos);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetFromNonEmptyList1() {
        System.out.println("getFromLast(1) from an non-empty list");
        int pos = 1;
        TokenList instance = new TokenList();
        CobolToken expResult = new CobolToken(1, 12, "ID");
        instance.add(expResult);
        instance.add(new CobolToken(1, 20, "DIVISION"));
        CobolToken result = instance.getFromLast(pos);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetFromNonEmptyCommentList0() {
        System.out.println("getFromLast(0) from an non-empty list with comments");
        int pos = 0;
        TokenList instance = new TokenList();
        CobolToken expResult = new CobolToken(1, 12, "ID");
        instance.add(expResult);
        instance.add(new CobolToken(1, 8, "/    COMMENT", CobolType.COMMENT));
        CobolToken result = instance.getFromLast(pos);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetFromNonEmptyCommentList1() {
        System.out.println("getFromLast(1) from an non-empty list with comments");
        int pos = 1;
        TokenList instance = new TokenList();
        CobolToken expResult = new CobolToken(1, 12, "ID");
        instance.add(expResult);
        instance.add(new CobolToken(1, 20, "DIVISION"));
        instance.add(new CobolToken(1, 7,  "/    COMMENT", CobolType.COMMENT));
        CobolToken result = instance.getFromLast(pos);
        assertEquals(expResult, result);
    }

    @Test
    public void testJoinPicture() {
        System.out.println("Join tokens for PICTURE 9(5)V99.");

        TokenList instance = new TokenList();
        instance.add(new CobolToken(1, 1, "9"));
        instance.add(new CobolToken(1, 2, "(", CobolType.LEFT_PAREN));
        instance.add(new CobolToken(1, 3, "5"));
        instance.add(new CobolToken(1, 4, ")", CobolType.RIGHT_PAREN));
        instance.add(new CobolToken(1, 5, "V99"));
        instance.add(new CobolToken(1, 8, ".", CobolType.SEPARATOR));

        String result = instance.joinContiguousText(0);
        String expected = "9(5)V99";
        assertEquals(expected, result);
    }

    @Test
    public void testJoinSeparator() {
        System.out.println("Join separtor tokens in PICTURE 9(5)V99.");

        TokenList instance = new TokenList();
        instance.add(new CobolToken(1, 1, "9"));
        instance.add(new CobolToken(1, 2, "(", CobolType.LEFT_PAREN));
        instance.add(new CobolToken(1, 3, "5"));
        instance.add(new CobolToken(1, 4, ")", CobolType.RIGHT_PAREN));
        instance.add(new CobolToken(1, 5, "V99"));
        instance.add(new CobolToken(1, 8, ".", CobolType.SEPARATOR));

        String result = instance.joinContiguousText(5);
        String expected = ".";
        assertEquals(expected, result);
    }

    @Test
    public void testGetFromContinuation() {
        System.out.println("Special continuation for strings");
        CobolToken st = new CobolToken(1, 70, "'A'");
        CobolToken ind= new CobolToken(2, 7, "-", CobolType.CONTINUATION);

        TokenList instance = new TokenList();
        instance.add(st);
        instance.add(ind);

        CobolToken result = instance.getFromLast(0);
        assertEquals(ind, result);
        result = instance.getFromLast(1);
        assertEquals(st, result);
    }

    @Test
    public void testGetFromContinuationWithComments() {
        System.out.println("Special continuation for strings");
        CobolToken st = new CobolToken(1, 70, "'A'");
        CobolToken ind= new CobolToken(3, 7, "-", CobolType.CONTINUATION);

        TokenList instance = new TokenList();
        instance.add(st);
        instance.add(new CobolToken(2, 7, "/ COMMENT", CobolType.COMMENT));
        instance.add(ind);

        CobolToken result = instance.getFromLast(0);
        assertEquals(ind, result);
        result = instance.getFromLast(1);
        assertEquals(st, result);
    }

    @Test
    public void testEqualsEmpty() {
        System.out.println("a == a for empty a");
        TokenList instance = new TokenList();
        boolean expResult = true;
        boolean result = instance.equals(instance);
        assertEquals(expResult, result);        
    }

    @Test
    public void testEqualsEmpty2() {
        System.out.println("a == b for empty a, b");
        TokenList instance = new TokenList();
        boolean expResult = true;
        boolean result = instance.equals(new TokenList());
        assertEquals(expResult, result);
    }

    @Test
    public void testEqualsNonEmpty() {
        System.out.println("a == a for non empty a");
        TokenList instance = new TokenList();
        instance.add(new CobolToken(1, 12, "ID"));
        boolean expResult = true;
        boolean result = instance.equals(instance);
        assertEquals(expResult, result);
    }

    @Test
    public void testEqualsNonEmpty2() {
        System.out.println("a == b for non empty a and b");
        TokenList instance = new TokenList();
        instance.add(new CobolToken(1, 12, "ID"));
        TokenList instance2 = new TokenList();
        instance2.add(new CobolToken(1, 12, "ID"));

        boolean expResult = true;
        boolean result = instance.equals(instance2);
        assertEquals(expResult, result);
    }

    @Test
    public void testNonEqualsNonEmpty() {
        System.out.println("a != b for non empty a and b");
        TokenList instance = new TokenList();
        instance.add(new CobolToken(1, 12, "ID"));
        TokenList instance2 = new TokenList();
        instance2.add(new CobolToken(1, 12, "DIVISION"));

        boolean expResult = false;
        boolean result = instance.equals(instance2);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetIndex() {
        System.out.println("getIndex for existing unique element");
        TokenList instance = new TokenList();
        instance.add(new CobolToken(1, 12, "ID"));
        instance.add(new CobolToken(1, 15, "DIVISION"));
        instance.add(new CobolToken(1, 30, ".", CobolType.SEPARATOR));

        int expResult = 1;
        int result = instance.getIndex(0, "DIVISION");
        assertEquals(expResult, result);
    }

    @Test
    public void testGetIndexDuplicate() {
        System.out.println("getIndex for existing duplicate element");
        TokenList instance = new TokenList();
        instance.add(new CobolToken(1, 12, "ID"));
        instance.add(new CobolToken(1, 15, "DIVISION"));
        instance.add(new CobolToken(1, 30, ".", CobolType.SEPARATOR));
        instance.add(new CobolToken(2, 12, "PROCEDURE"));
        instance.add(new CobolToken(2, 15, "DIVISION"));

        int expResult = 4;
        int result = instance.getIndex(2, "DIVISION");
        assertEquals(expResult, result);
    }

    @Test
    public void testGetIndexPast() {
        System.out.println("getIndex for existing unique element not to be found");
        TokenList instance = new TokenList();
        instance.add(new CobolToken(1, 12, "ID"));
        instance.add(new CobolToken(1, 15, "DIVISION"));
        instance.add(new CobolToken(1, 30, ".", CobolType.SEPARATOR));

        int expResult = -1;
        int result = instance.getIndex(2, "DIVISION");
        assertEquals(expResult, result);
    }

    @Test
    public void testJoinContinuation() {
        System.out.println("join continued tokens");
        TokenList instance = new TokenList();
        instance.add(new CobolToken(1, 12, "PIC"));
        instance.add(new CobolToken(2,  7, "-", CobolType.CONTINUATION));
        instance.add(new CobolToken(2, 15, "TURE"));

        //  Token must been joined.
        int expResult = 1;
        int result = instance.size();
        assertEquals(expResult, result);

        //  One token
        CobolToken token = new CobolToken(1, 12, "PICTURE");
        assertEquals(token, instance.get(0));
    }

    @Test
    public void testJoinContinuation2() {
        System.out.println("join continued tokens across comments");
        TokenList instance = new TokenList();
        instance.add(new CobolToken(1, 12, "PIC"));
        instance.add(new CobolToken(2,  7, "*----", CobolType.COMMENT));
        instance.add(new CobolToken(3,  7, "-", CobolType.CONTINUATION));
        instance.add(new CobolToken(3, 25, "TU"));
        instance.add(new CobolToken(4,  7, "-", CobolType.CONTINUATION));
        instance.add(new CobolToken(4, 20, "RE"));

        //  Token must been joined.
        int expResult = 2;
        int result = instance.size();
        assertEquals(expResult, result);

        //  One token
        CobolToken token = new CobolToken(1, 12, "PICTURE");
        assertEquals(token, instance.get(0));
    }
}