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
 * The complete list of lexical's types.
 *
 * @author Andr&eacute;s Gri&ntilde;&oacute; Brandt <agrinob@hotmail.com>
 */
public enum CobolType {
    /** Line continuation indicator (a '-' in col 7) */
    CONTINUATION,
    /** New page (a '/' in col 7) */
    NEW_PAGE,
    /** Comment line (a '*' in col 7) */
    COMMENT,
    /** Special line (a '$' in col 7) */
    SPECIAL_LINE,
    /** String */
    STRING,
    /** Start of pseudo-text. Read next tokens for recovering text. */
    START_PSEUDO_TEXT,
    /** Content of pseudo-text (minus '=='). */
    PSEUDO_TEXT,
    /** End of pseudo-text. */
    END_PSEUDO_TEXT,
    /** A comma, period or semicolon separator. */
    SEPARATOR,
    /** Colon. */
    COLON,
    /** Left parentesis. */
    LEFT_PAREN,
    /** Right parentesis. */
    RIGHT_PAREN,
    /** Unparsed text. */
    TEXT,
    /** The concatenation operator. */
    AMPERSAND,
    /** A cobol word */
    WORD,
    /** Undefined */
    UNDEFINED
}


