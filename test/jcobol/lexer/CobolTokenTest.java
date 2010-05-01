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
public class CobolTokenTest {

    public CobolTokenTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testGetCol() {
        System.out.println("getCol");
        CobolToken instance = new CobolToken(1, 2, "A");
        int expResult = 2;
        int result = instance.getCol();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetRow() {
        System.out.println("getRow");
        CobolToken instance = new CobolToken(2, 3, "B");
        int expResult = 2;
        int result = instance.getRow();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetText() {
        System.out.println("getText");
        CobolToken instance = new CobolToken(4, 5, "ABC");
        String expResult = "ABC";
        String result = instance.getText();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetFirstChar() {
        System.out.println("getFirstChar");
        CobolToken instance = new CobolToken(6, 7, "12345");
        String expResult = "1";
        String result = instance.getFirstChar();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetLastChar() {
        System.out.println("getLastChar");
        CobolToken instance = new CobolToken(8, 9, "abcde");
        String expResult = "e";
        String result = instance.getLastChar();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetLastCol() {
        System.out.println("getLastCol");
        CobolToken instance = new CobolToken(1, 70, "ABC");
        int expResult = 72;
        int result = instance.getLastCol();
        assertEquals(expResult, result);
    }

    @Test
    public void testEquals() {
        System.out.println("equals(null)");
        Object obj = null;
        CobolToken instance = new CobolToken(1, 2, "A");
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
}