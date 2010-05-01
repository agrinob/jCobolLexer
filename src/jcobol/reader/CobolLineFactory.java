/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jcobol.reader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Andr&eacute;s Gri&ntilde;&oacute; Brandt <agrinob@hotmail.com>
 */
public class CobolLineFactory {
    /** Compiler directive that choose line format: $SET SOURCEFORMAT"FREE" or $SET SOURCEFORMAT"FIXED"*/
    private static final Pattern sourceFormat = Pattern.compile("^\\s*\\$\\s*SET\\s+\\SOURCEFORMAT\\s*\"(\\S+)\"", Pattern.CASE_INSENSITIVE);
    /** Format in use (free/fixed) */
    private boolean fixedFormat = true;

    public CobolLine getCobolLine(final String sourceLine, final int lineNumber) {
        final Matcher m         = sourceFormat.matcher(sourceLine);
        final boolean fixedLine = (m.matches() && sourceLine.startsWith("$")) ? false : fixedFormat;
        final int[] tabs        = (fixedLine) ? CobolLineFixed.getTabs() : CobolLineFree.getTabs();
        final String line       = expandTabs(sourceLine, tabs);
        CobolLine cobolLine;

        if (m.matches()) {
            // $set formatsource must be accepted in both format.
            cobolLine = (line.startsWith("$")) ? new CobolLineFree(lineNumber, line)
                                               : new CobolLineFixed(lineNumber, line);
           String type = m.group(1);
           fixedFormat = type.equalsIgnoreCase("FIXED");
        }
        else {
            //  Any other card except $set formatsource
            cobolLine = (fixedFormat) ? new CobolLineFixed(lineNumber, line)
                                      : new CobolLineFree(lineNumber, line);
        }

        return cobolLine;
    }

    /**
     * Expands tabs in source line.
     * @param line The line to be expanded.
     * @return The line with tabs replaced by spaces.
     */
    private String expandTabs(final String line, final int[] tabs) {
        final char[] cLine = line.toCharArray();
        StringBuffer buffer = new StringBuffer(250);

        for (int index = 0; index < cLine.length; index++) {
            if (cLine[index] == '\t') {
                int spaces = getSpacesToNextTab(buffer.length() + 1, tabs);
                while (spaces > 0) {
                    buffer.append(' ');
                    spaces--;
                }
            } else {
                buffer.append(cLine[index]);
            }
        }

        return buffer.toString();
    }

    /**
     * Calculate spaces needed to reach next tab position.
     * @param col Starting column.
     * @return Number of spaces needed (1..n). At least 1, because tab itself
     * must be replaces with spaces.
     */
    private int getSpacesToNextTab(final int col, final int[] tabs) {
        int tabIndex = 0;
        while (tabIndex < tabs.length && tabs[tabIndex] <= col) {
            tabIndex++;
        }
        return (tabIndex < tabs.length && tabs[tabIndex] > col) ? tabs[tabIndex] - col : 0;
    }
}
