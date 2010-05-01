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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Read a cobol source program.
 * <p>Can process fixed format, free format and mixed fixed-free format cobol
 * source program.
 * @author Andr&eacute;s Gri&ntilde;&oacute; Brandt <agrinob@hotmail.com>
 */
public class CardReader {

    /** The source program. */
    private final BufferedReader sourcePgma;
    /** How many lines readed so far. */
    private int lineNumber = 0;
    private CobolLineFactory cblLineFac;

    /**
     * Constructor.
     * @param source The cobol source program.
     */
    public CardReader(Reader source) {
        assert source != null;
        this.sourcePgma = new BufferedReader(source);
        this.cblLineFac = new CobolLineFactory();
    }

    /**
     * Read the next line from cobol source program.
     * @return a CobolLine instance or null if the end of the stream
     * has been reached.
     * @throws IOException If an I/O error occurs.
     */
    public CobolLine readLine() throws IOException {
        final String srcLine = sourcePgma.readLine();
        if (srcLine == null) {
            return null;
            }
        lineNumber++;
        return cblLineFac.getCobolLine(srcLine, lineNumber);
    }
}
