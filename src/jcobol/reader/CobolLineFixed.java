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
 * A fixed format (80 columns) cobol source line.
 * @author Andr&eacute;s Gri&ntilde;&oacute; Brandt <agrinob@hotmail.com>
 */
public class CobolLineFixed implements CobolLine {
    private final int lineNumber;
    /** Source line, truncated to 72 columns, rigth-padded with spaces. */
    private final String source;
    private static final int[] tabs = new int[] {7, 12, 20, 28, 36, 42, 50, 58, 64, 72};

    /**
     * Constructor.
     * @param lineNumber The line number of the line.
     * @param source The complete 80 columns text of the line.
     */
    public CobolLineFixed(final int lineNumber, final String source) {
        assert source != null;
        assert lineNumber > 0;

        if (source.length() > 72) {
            this.source = source.substring(0, 72);
        }
        else {
            this.source = String.format("%1$-" + 72 + "s", source);
        }
        this.lineNumber = lineNumber;
    }

    @Override
    public int getLineNumber() {
        return this.lineNumber;
    }

    @Override
    public char getIndicator() {
        return source.charAt(6);
    }

    @Override
    public int getFirstColumn() {
        return 8;
    }

    @Override
    public String getSource() {
        return source.substring(7);
    }

    @Override
    public boolean isFixed() {
        return true;
    }

    /**
     * Default tabs for source line
     * @return The columns (1..n) for tab stops
     */
    public static int[] getTabs() {
        return tabs;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj == null) {
            return result;
        }
        if (obj instanceof CobolLineFixed) {
            CobolLineFixed other = (CobolLineFixed) obj;
            result = (this.lineNumber == other.lineNumber) &&
                     (this.source.equals(other.source));
        }
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.lineNumber;
        hash = 97 * hash + (this.source != null ? this.source.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return source;
    }
}
