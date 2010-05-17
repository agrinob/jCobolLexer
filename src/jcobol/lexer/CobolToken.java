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

/**
 * A indivisible piece of cobol source code.
 *
 * <p>A CobolToken keep the source text and row/col where it begin. You can
 * reconstruct the original source code from their CobolToken representation.
 *
 * <p>Note: for multiline strings (joined by continuation character), we register
 * only the line and column of the string's segment.
 *
 * @author Andr&eacute;s Gri&ntilde;&oacute; Brandt <agrinob@hotmail.com>
 */
public class CobolToken {
    /** The actual content (from cobol source line) */
    private String text;
    /** Start column of the text (1..n) */
    private int col;
    /** Line number of the text (1..n) */
    private int row;
    /** Content type */
    private CobolType type;

    /**
     * Constructor.
     * @param row Source code line number (1..).
     * @param col Source code column number (1, ...)
     * @param text Token text, not null.
     * @param type The type of this token, not null.
     */
    public CobolToken(final int row, final int col, final String text, final CobolType type) {
        assert row > 0;
        assert col > 0;
        assert text != null;
        assert type != null;

        this.col = col;
        this.row = row;
        this.text = text;
        this.type = type;
    }

    /**
     * Constructor for a token of type <code>WORD</code>
     * @param row Source code line number.
     * @param col Source code column number (1, ...)
     * @param text Token text.
     */
    public CobolToken(final int row, final int col, final String text) {
        this(row, col, text, CobolType.WORD);
    }


    /**
     * Getter
     * @return Token text starting column number.
     */
    public int getCol() {
        return col;
    }

    /**
     * Getter
     * @return Token text line number.
     */
    public int getRow() {
        return row;
    }

    /**
     * Getter
     * @return Token text.
     */
    public String getText() {
        return text;
    }

    /**
     * Getter
     * @return The type of this token.
     */
    public CobolType getType() {
        return type;
    }

    /**
     * Return the first character of this token.
     * @return The first character, or null if the token is empty.
     */
    public String getFirstChar() {
        return (text.length() > 0) ? new String(new char[] {text.charAt(0)}) : null;
        }

    /**
     * Return the last character of this token.
     * @return The last character, or null if the token is empty.
     */
    public String getLastChar() {
        int len = text.length();
        return (len > 0) ? new String(new char[] {text.charAt(len-1)}) : null;
        }

    /**
     * Return the end column of the token.
     * @return The end column.
     */
    public int getLastCol() {
        return col + text.length() - 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof CobolToken)) {
            return false;
        }
        CobolToken other = (CobolToken) obj;
        return other.col == this.col && other.row == this.row && other.text.equals(this.text) && other.type.equals(this.type);
    }

    @Override
    public int hashCode() {
        return this.row * 80 + this.col;
    }

    @Override
    public String toString() {
        return type + " (" + row + ", " + col + ", '" + text + "'/" + text.length() + ")";
    }
}
