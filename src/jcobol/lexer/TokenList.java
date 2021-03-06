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

import java.util.ArrayList;
import java.util.List;
import jcobol.filter.TokenFilter;

/**
 * A list of CobolToken, plus some support methods.
 * @author Andr&eacute;s Gri&ntilde;&oacute; Brandt <agrinob@hotmail.com>
 */
public class TokenList {

    /** The list itself. */
    private List<CobolToken> tokens;

    /**
     * Constructor
     */
    public TokenList() {
        tokens = new ArrayList<CobolToken>();
    }

    /**
     * The number of tokens in the list.
     * @return The number of tokens in the list.
     */
    public int size() {
        return tokens.size();
    }

    /**
     * Append a token to the end of the list.
     * @param token Token to be added, not null.
     * @throws IllegalArgumentException If argument is null.
     */
    public void add(final CobolToken token) throws IllegalArgumentException {
        if (token == null) {
            throw new IllegalArgumentException("Null argument");
        }
        CobolType lastType = CobolType.UNDEFINED;
        int last = tokens.size() - 1;
        if (last >= 0) {
            lastType = tokens.get(last).getType();
        }
        if (lastType == CobolType.CONTINUATION) {
            joinTokens(token);
        } else {
            tokens.add(token);
        }
    }

    /**
     * Join new token to previous token in list.
     * <p>The operation skip non-code lines (comment, new pages and special lines).
     * @param token The new token.
     */
    private void joinTokens(final CobolToken token) {
        boolean codeToken = false;
        CobolToken prevToken = null;
        CobolType type = CobolType.UNDEFINED;

        //  Skips last (continuation), and comments and other non-code tokens.
        int index = tokens.size() - 2;
        while (index >= 0 && !codeToken) {
            prevToken = tokens.get(index);
            type = prevToken.getType();
            if (type == CobolType.COMMENT || type == CobolType.NEW_PAGE || type == CobolType.SPECIAL_LINE) {
                index--;
            } else {
                codeToken = true;
            }
        }

        if (codeToken && type == token.getType()) {
            //  Delete last token (of type CobolType.CONTINUATION);
            tokens.remove(tokens.size() - 1);
            //  Join two tokens.
            int row = prevToken.getRow();
            int col = prevToken.getCol();
            String text = prevToken.getText();
            String text2= token.getText();
            if (type == CobolType.STRING) {
                //  Delete the initial quote/semiquote.
                text2 = text2.substring(1);
                }
            CobolToken joinedToken = new CobolToken(row, col, text + text2, type);
            tokens.set(index, joinedToken);
        }
        else {
            //  Source code is bad; just append the token.
            tokens.add(token);
        }
    }

    /**
     * Get a token from the list.
     * @param index The token's position (0..n).
     * @return A non-null token.
     * @throws IllegalArgumentException Index out of range.
     */
    public CobolToken get(final int index) throws IllegalArgumentException {
        if (index < 0 || index >= tokens.size()) {
            throw new IllegalArgumentException("Index out of range");
        }

        return tokens.get(index);
    }

    /**
     * Select tokens.
     * @param filter Filters to be applied to the actual list.
     * @return A new list with the selected tokens.
     */
    public TokenList get(final TokenFilter... filter) {
        TokenList newList = new TokenList();
        for (CobolToken token : tokens) {
            boolean accept = true;
            for (TokenFilter f : filter) {
                if (!f.accept(token)) {
                    accept = false;
                    break;
                }
            }
            if (accept) {
                newList.add(token);
            }
        }
        return newList;
    }

    /**
     * Search token by content.
     * @param text The text of the token to search.
     * @param start Index in the list to start the search.
     * @return The index of found token, or -1.
     */
    public int getIndex(final int start, final String text) {
        int index = start;
        while (index < tokens.size() && !tokens.get(index).getText().equals(text)) {
            index++;
        }
        return (index < tokens.size()) ? index : -1;
    }

    /**
     * Search token by content ignoring case.
     * @param text The text of the token to search.
     * @param start Index in the list to start the search.
     * @return The index of found token, or -1.
     */
    public int getIndexIgnoreCase(final int start, final String text) {
        int index = start;
        while (index < tokens.size() && !tokens.get(index).getText().equalsIgnoreCase(text)) {
            index++;
        }
        return (index < tokens.size()) ? index : -1;
    }

    /**
     * Returns a view of the portion of this list between the specified fromIndex,
     * inclusive, and toIndex, exclusive.
     * (If fromIndex and toIndex are equal, the returned list is empty.)
     * The returned list is backed by this list, so non-structural changes in
     * the returned list are reflected in this list, and vice-versa.
     * @param start Index of first element.
     * @param stop Index of last element (exclusive).
     * @return A list, posibly empty.
     */
    public List<CobolToken> subList(final int start, final int stop) {
        assert start >= 0;
        assert start <= stop;

        return tokens.subList(start, stop);
    }

    /**
     * Search backward for a token that's not a comment, compiler directive, text or empty line.
     * <p>The following {@link CobolType} are excluded from the search: <code>
     * COMMENT, NEW_PAGE, SPECIAL_LINE</code> and <code>TEXT</code>.
     * @param pos Token position counting from the list's end:
     * 0 = last non-comment token; 1 = next to last non-comment token.
     * @return The found token or null.
     */
    public CobolToken getFromLast(final int pos) {
        if (pos < 0 || pos >= tokens.size()) {
            return null;
        }

        CobolToken token = null;
        int count = pos;
        int index = tokens.size() - 1;

        if (index >= 0) {
            do {
                token = tokens.get(index);
                index--;

                switch (token.getType()) {
                    case COMMENT:
                    case NEW_PAGE:
                    case SPECIAL_LINE:
                    case TEXT:
                        break;
                    default:
                        count--;
                }
            } while (index >= 0 && count >= 0);
        }
        return (count < 0) ? token : null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TokenList)) {
            return false;
        }
        return tokens.equals(((TokenList) obj).tokens);
    }

    @Override
    public int hashCode() {
        return tokens.hashCode();
    }

    @Override
    public String toString() {
        return tokens.toString();
    }
}
