/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
public class CobolLineFactoryTest {

    public CobolLineFactoryTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testEmpty() {
        System.out.println("empty line");
        String sourceLine = "";
        int lineNumber = 1;
        CobolLineFactory instance = new CobolLineFactory();
        CobolLine expResult = new CobolLineFixed(1, "");
        CobolLine result = instance.getCobolLine(sourceLine, lineNumber);
        assertEquals(expResult, result);
    }

    @Test
    public void testSetSourceAutoFree() {
        System.out.println("set source format (free)");
        String sourceLine = "$set sourceformat\"free\"";
        int lineNumber = 1;
        CobolLineFactory instance = new CobolLineFactory();
        CobolLine expResult = new CobolLineFree(1, "$set sourceformat\"free\"");
        CobolLine result = instance.getCobolLine(sourceLine, lineNumber);
        assertEquals(expResult, result);
    }

    @Test
    public void testSetSourceAutoFixed() {
        System.out.println("set source format (fixed)");
        String sourceLine = "000001 $set sourceformat\"fixed\"";
        int lineNumber = 1;
        CobolLineFactory instance = new CobolLineFactory();
        CobolLine expResult = new CobolLineFixed(1, "000001 $set sourceformat\"fixed\"");
        CobolLine result = instance.getCobolLine(sourceLine, lineNumber);
        assertEquals(expResult, result);
    }

    @Test
    public void testTabsFree() {
        System.out.println("free format with tabs");
        String sourceLine = "\t9\t17";
        int lineNumber = 1;
        CobolLineFactory instance = new CobolLineFactory();
        instance.getCobolLine("$set sourceformat\"free\"", lineNumber++);
                                                          // 123456789*123456789*
        CobolLine expResult = new CobolLineFree(lineNumber, "        9       17");
        CobolLine result    = instance.getCobolLine(sourceLine, lineNumber);
        assertEquals(expResult, result);
    }

    @Test
    public void testTabsFree2() {
        System.out.println("free format with tabs");
        String sourceLine = "   88 End-Of-ODF\t\tVALUE HIGH-VALUES.";
        int lineNumber = 1;
        CobolLineFactory instance = new CobolLineFactory();
        instance.getCobolLine("$set sourceformat\"free\"", lineNumber++);
                                                          // 123456789*123456789*123456789*123456789
        CobolLine expResult = new CobolLineFree(lineNumber, "   88 End-Of-ODF                VALUE HIGH-VALUES.");
        CobolLine result    = instance.getCobolLine(sourceLine, lineNumber);
        assertEquals(expResult, result);
    }

    @Test
    public void testTabsFree3() {
        System.out.println("free format with tabs");
        String sourceLine = "\t02\tPr-Number\t\tPIC 9(4).";
        int lineNumber = 1;
        CobolLineFactory instance = new CobolLineFactory();
        instance.getCobolLine("$set sourceformat\"free\"", lineNumber++);
                                                          // 123456789*123456789*123456789*123456789*123456789*
        CobolLine expResult = new CobolLineFree(lineNumber, "        02      Pr-Number               PIC 9(4).");
        CobolLine result    = instance.getCobolLine(sourceLine, lineNumber);
        assertEquals(expResult, result);
    }
}