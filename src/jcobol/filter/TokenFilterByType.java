/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jcobol.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jcobol.lexer.CobolToken;
import jcobol.lexer.CobolType;

/**
 * Filter tokens by type.
 * <p>After creating the filter, you must give one or more pairs (CobolType, boolean).
 * For each token, the pairs are processed in order, until a match is found
 * and the token is therefore accepted or rejected.
 * <p>If no match is found, the token is accepted.
 *
 * @author Andr&eacute;s Gri&ntilde;&oacute; Brandt <agrinob@hotmail.com>
 */
public class TokenFilterByType implements TokenFilter {
    private final List<FilterEntry> list;

    /**
     * Constructor
     */
    public TokenFilterByType() {
        list = new ArrayList<FilterEntry>();
    }

    /**
     * Add a type to filter.
     * @param type The type to be accepted/rejected.
     * @param accept True if the type is to be accepted, false if the type
     * is to be rejected.
     */
    public void addType(final CobolType type, final boolean accept) {
        list.add(new FilterEntry(type, accept));
    }

    @Override
    public boolean accept(CobolToken token) {
        Iterator<FilterEntry> ite = list.iterator();
        while (ite.hasNext()) {
            FilterEntry filter = ite.next();
            if (token.getType() == filter.type) {
                return filter.accept;
            }
        }
        return true;
    }

    private class FilterEntry {
        public CobolType type;
        public boolean accept;

        public FilterEntry(final CobolType type, final boolean accept) {
            this.type = type;
            this.accept = accept;
        }
    }
}
