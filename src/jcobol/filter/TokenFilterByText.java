/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jcobol.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jcobol.lexer.CobolToken;

/**
 * Filter tokens by his text (exact match, case sensitive).
 * <p>After creating the filter, you must give one or more pairs (text, boolean).
 * For each token, the pairs are processed in order, until a match is found
 * and the token is therefore accepted or rejected.
 * <p>If no match is found, the token is accepted.

 * @author Andr&eacute;s Gri&ntilde;&oacute; Brandt <agrinob@hotmail.com>
 */
public class TokenFilterByText implements TokenFilter {
    private final List<FilterEntry> list;

    /**
     * Constructor
     */
    public TokenFilterByText() {
        list = new ArrayList<FilterEntry>();
    }

    /**
     * Add a text to filter.
     * @param text The text to be accepted/rejected.
     * @param accept True if the text is to be accepted, false if the type
     * is to be rejected.
     */
    public void addText(final String text, final boolean accept) {
        list.add(new FilterEntry(text, accept));
    }

    @Override
    public boolean accept(CobolToken token) {
        Iterator<FilterEntry> ite = list.iterator();
        while (ite.hasNext()) {
            FilterEntry filter = ite.next();
            if (token.getText().equals(filter.text)) {
                return filter.accept;
            }
        }
        return true;
    }

    private class FilterEntry {
        public String text;
        public boolean accept;

        public FilterEntry(final String text, final boolean accept) {
            this.text = text;
            this.accept = accept;
        }
    }
}
