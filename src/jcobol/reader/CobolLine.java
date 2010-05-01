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

/**
 * A interfaz for fixed & free format cobol lines.
 * @author Andr&eacute;s Gri&ntilde;&oacute; Brandt <agrinob@hotmail.com>
 */
public interface CobolLine {
    /**
     * Extract the indicator from the source code line.
     * <p>For fixed cobol format, indicator is extracted from column 7;
     * for free format, is extracted from column 1 (if any).
     * @return The indicator (space by default);
     */
    public char getIndicator();
    /**
     * Return the column where getSource() content starts.
     * @return The first column
     */
    public int getFirstColumn();

    /**
     * Get the source code content, minus sequence number/indicator/id area.
     * @return The source code
     */
    public String getSource();

    /**
     * The line number of this line.
     * @return 1..n
     */
    public int getLineNumber();

    /**
     * Evaluate fixed/free format.
     * @return true if this is a fixed format line.
     */
    public boolean isFixed();
    }
