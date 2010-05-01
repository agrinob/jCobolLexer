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

package jcobol.reader;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andr&eacute;s Gri&ntilde;&oacute; Brandt <agrinob@hotmail.com>
 */
public class CobolLineFreeTest {

    public CobolLineFreeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testGetIndicator1() {
        System.out.println("line=*");
        CobolLineFree instance = new CobolLineFree(1, "*");
        char expResult = '*';
        char result = instance.getIndicator();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetIndicator2() {
        System.out.println("line=/");
        CobolLineFree instance = new CobolLineFree(1, "/");
        char expResult = '/';
        char result = instance.getIndicator();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetIndicator3() {
        System.out.println("line=DATA DIVISION");
        CobolLineFree instance = new CobolLineFree(1, "DATA DIVISION");
        char expResult = ' ';
        char result = instance.getIndicator();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetIndicator4() {
        System.out.println("line=D move x to y");
        CobolLineFree instance = new CobolLineFree(1, "D move x to y");
        char expResult = 'D';
        char result = instance.getIndicator();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetIndicator5() {
        System.out.println("line=D");
        CobolLineFree instance = new CobolLineFree(1, "D");
        char expResult = 'D';
        char result = instance.getIndicator();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetIndicatorSpaces() {
        System.out.println("line=id division.");
        CobolLineFree instance = new CobolLineFree(1, "id division.");
        char expResult = ' ';
        char result = instance.getIndicator();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetIndicatorEmpty() {
        System.out.println("line=");
        CobolLineFree instance = new CobolLineFree(1, "");
        char expResult = ' ';
        char result = instance.getIndicator();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetFirstColumn2() {
        System.out.println("line=stop run.");
        CobolLineFree instance = new CobolLineFree(1, "stop run.");
        int expResult = 1;
        int result = instance.getFirstColumn();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetFirstColumn1() {
        System.out.println("line=*OPTIONS");
        CobolLineFree instance = new CobolLineFree(1, "*OPTIONS");
        int expResult = 2;
        int result = instance.getFirstColumn();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetFirstColumn3() {
        System.out.println("line= stop run.");
        CobolLineFree instance = new CobolLineFree(1, " stop run.");
        int expResult = 1;
        int result = instance.getFirstColumn();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetSource1() {
        System.out.println("line=/ 'HOLA'");
        CobolLineFree instance = new CobolLineFree(1, "/ 'HOLA'");
        String expResult = " 'HOLA'";
        String result = instance.getSource();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetSource2() {
        System.out.println("line= 'HOLA'");
        CobolLineFree instance = new CobolLineFree(1, " 'HOLA'");
        String expResult = " 'HOLA'";
        String result = instance.getSource();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetSourceEmpty() {
        System.out.println("line= ''");
        CobolLineFree instance = new CobolLineFree(1, "");
        String expResult = "";
        String result = instance.getSource();
        assertEquals(expResult, result);
    }
}