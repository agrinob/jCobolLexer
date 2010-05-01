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

import java.io.StringReader;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andr&eacute;s Gri&ntilde;&oacute; Brandt <agrinob@hotmail.com>
 */
public class CardReaderTest {
    private final String lineFixed = "000001 ID DIVISION.                                                     EXEC84";
    private final String option = "$ SET SOURCEFORMAT\"FREE\"";
    private final String lineFree =  "program-id. exec84";
    private final String option2 = "$set SOURCEFORMAT\"fixed\"";
    private final String lineFixed2 = "000002 installation.                                                    EXEC84";
    private final String lineFreeLong = "123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*";
    private final String empty = "";

    private final String test =
          // 123456789*123456789*123456789*123456789*123456789*123456789*123456789*123456789*
            lineFixed + "\n" +
            option + "\n" +
            lineFree + "\n" +
            option2 + "\n" +
            lineFixed2 + "\n" +
            option + "\n" +
            lineFreeLong + "\n" +
            empty + "\n";

    public CardReaderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testReadLineFixed() throws Exception {
        System.out.println("readLine Fixed");
        CardReader instance = new CardReader(new StringReader(test));
        CobolLine expResult = new CobolLineFixed(1, lineFixed);
        CobolLine result = instance.readLine();
        assertEquals(expResult, result);
    }

    @Test
    public void testReadLineFree() throws Exception {
        System.out.println("readLine Free");
        CardReader instance = new CardReader(new StringReader(test));
        CobolLine expResult = new CobolLineFree(3, lineFree);
        CobolLine result = instance.readLine();
        result = instance.readLine();
        result = instance.readLine();
        assertEquals(expResult, result);
    }

    @Test
    public void testReadLineFreeLong() throws Exception {
        System.out.println("readLine Free extra long");
        CardReader instance = new CardReader(new StringReader(test));
        CobolLine expResult = new CobolLineFree(7, lineFreeLong);
        CobolLine result = null;

        for (int i=0; i < 7; i++) {
            result = instance.readLine();
        }
        assertEquals(expResult, result);
    }

    @Test
    public void testReadLineFreeEmpty() throws Exception {
        System.out.println("readLine Empty Free line");
        CardReader instance = new CardReader(new StringReader(test));
        CobolLine expResult = new CobolLineFree(8, empty);
        CobolLine result = null;

        for (int i=0; i < 8; i++) {
            result = instance.readLine();
        }
        assertEquals(expResult, result);
    }

    @Test
    public void testReadLineOption() throws Exception {
        System.out.println("readLine compiler option");
        CardReader instance = new CardReader(new StringReader(test));
        CobolLine expResult = new CobolLineFree(2, option);
        CobolLine result = instance.readLine();
        result = instance.readLine();
        assertEquals(expResult, result);
    }

    @Test
    public void testReadLineBack2Fixed() throws Exception {
        System.out.println("readLine compiler option back to Fixed Format");
        CardReader instance = new CardReader(new StringReader(test));
        CobolLine expResult = new CobolLineFixed(5, lineFixed2);
        CobolLine result = null;

        for (int i=0; i < 5; i++) {
            result = instance.readLine();
        }
        assertEquals(expResult, result);
    }

    @Test
    public void testReadLineEmpty() throws Exception {
        System.out.println("readLine empty input");
        CardReader instance = new CardReader(new StringReader(""));
        CobolLine expResult = null;
        CobolLine result = instance.readLine();
        assertEquals(expResult, result);
    }
}