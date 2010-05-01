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

import jcobol.reader.CobolLineFixed;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andr&eacute;s Gri&ntilde;&oacute; Brandt <agrinob@hotmail.com>
 */
public class CobolLineFixedTest {

    public CobolLineFixedTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testGetIndicator1() {
        System.out.println("line=000001*");
        CobolLineFixed instance = new CobolLineFixed(1, "000001*");
        char expResult = '*';
        char result = instance.getIndicator();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetIndicator2() {
        System.out.println("line=000001/");
        CobolLineFixed instance = new CobolLineFixed(1, "000001/");
        char expResult = '/';
        char result = instance.getIndicator();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetIndicator3() {
        System.out.println("line=000001D");
        CobolLineFixed instance = new CobolLineFixed(1, "000001D");
        char expResult = 'D';
        char result = instance.getIndicator();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetIndicatorSpaces() {
        System.out.println("line=000001 id division.");
        CobolLineFixed instance = new CobolLineFixed(1, "000001 id division.");
        char expResult = ' ';
        char result = instance.getIndicator();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetIndicatorEmpty() {
        System.out.println("line=");
        CobolLineFixed instance = new CobolLineFixed(1, "");
        char expResult = ' ';
        char result = instance.getIndicator();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetSource1() {
        System.out.println("line=000001/ 'HOLA'");
        CobolLineFixed instance = new CobolLineFixed(1, "000001/ 'HOLA'");
        String expResult = pad72(" 'HOLA'");
        String result = instance.getSource();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetSource2() {
        System.out.println("line=000001      'HOLA'");
        CobolLineFixed instance = new CobolLineFixed(1, "000001      'HOLA'");
        String expResult = pad72("     'HOLA'");
        String result = instance.getSource();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetSourceEmpty() {
        System.out.println("line= ''");
        CobolLineFixed instance = new CobolLineFixed(1, "");
        String expResult = pad72("");
        String result = instance.getSource();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetSourceAlmostEmpty() {
        System.out.println("line= '0123456'");
        CobolLineFixed instance = new CobolLineFixed(1, "123456");
        String expResult =  pad72("");
        String result = instance.getSource();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetFullLine() {
        final String card="086900             MOVE   \"SOURCE FILE WILL BE SUPPRESSED\" TO PRINT-DATAEXEC84.2";
        System.out.println("line= '" + card + "'");
        CobolLineFixed instance = new CobolLineFixed(1, card);
        String expResult = "            MOVE   \"SOURCE FILE WILL BE SUPPRESSED\" TO PRINT-DATA";
        String result = instance.getSource();
        assertEquals(expResult, result);
    }

        @Test
    public void testGetSource3() {
        System.out.println("line=      $ SET SOURCEFORMAT\"FREE\"");
        CobolLineFixed instance = new CobolLineFixed(1, "      $ SET SOURCEFORMAT\"FREE\"");
        String expResult = pad72(" SET SOURCEFORMAT\"FREE\"");
        String result = instance.getSource();
        assertEquals(expResult, result);
    }

    private String pad72(final String s) {
        return String.format("%1$-" + 65 + "s", s);
    }
}