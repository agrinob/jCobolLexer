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
 * A free format source cobol line.
 * @author Andr&eacute;s Gri&ntilde;&oacute; Brandt <agrinob@hotmail.com>
 */
public class CobolLineFree implements CobolLine {
    private final String source;
    private final int lineNumber;
    private static final int[] tabs = new int[] {9, 17, 25, 33, 41, 49, 57, 65, 73, 81, 89};

    /**
     * Constructor.
     * @param lineNumber The line number of the line.
     * @param source The complete text of the line.
     */
    public CobolLineFree(final int lineNumber, final String source) {
        assert source != null;
        assert lineNumber > 0;
        this.source = source;
        this.lineNumber = lineNumber;
    }

    @Override
    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public char getIndicator() {
        final int len = source.length();
        char ind = (len > 0) ? source.charAt(0) : ' ';
        switch (ind) {
            case '*':
            case '/':
            case '$': break;
            case 'D': //    Debugging line is marked by a 'D'/'d' followed
            case 'd': //    by space.
                char next = (len > 1) ? source.charAt(1) : ' ';
                ind = (next == ' ') ? ind : ' ';
                break;
            default : ind = ' ';
        }
        return ind;
    }

    @Override
    public int getFirstColumn() {
        return (getIndicator() == ' ') ? 1 : 2;
    }

    @Override
    public String getSource() {
        final String src;
        if (this.getIndicator() == ' ') {
            src = source;
        }
        else {
            src = (source.length() > 1) ? source.substring(1) : "";
        }
        return src;
    }

    @Override
    public boolean isFixed() {
        return false;
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
        if (obj instanceof CobolLineFree) {
            CobolLineFree other = (CobolLineFree) obj;
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
