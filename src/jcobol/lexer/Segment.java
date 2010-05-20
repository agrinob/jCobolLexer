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
 * A pair of indexes.
 *
 * @author Andr&eacute;s Gri&ntilde;&oacute; Brandt <agrinob@hotmail.com>
 */
public class Segment {

    private final int first;
    private final int last;

    /**
     * Constructor.
     * @param first First index (&lt;= last).
     * @param last Last index
     */
    public Segment(final int first, final int last) {
        assert first < last;
        this.first = first;
        this.last = last;
    }

    /**
     * Getter
     * @return The first index.
     */
    public int getFirst() {
        return first;
    }

    /**
     * Getter
     * @return The last index.
     */
    public int getLast() {
        return last;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Segment other = (Segment) obj;
        if (this.first != other.first) {
            return false;
        }
        if (this.last != other.last) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + this.first;
        hash = 23 * hash + this.last;
        return hash;
    }

    @Override
    public String toString() {
        return "[" + first + ", " + last + "]";
    }
}
