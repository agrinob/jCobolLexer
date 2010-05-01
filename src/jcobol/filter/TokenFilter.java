/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jcobol.filter;

import jcobol.lexer.CobolToken;

/**
 * Interfaz for selecting tokens.
 * @author Andr&eacute;s Gri&ntilde;&oacute; Brandt <agrinob@hotmail.com>
 */
public interface TokenFilter {
    /**
     * Evaluate a cobol token.
     * @param token The token to be evaluated.
     * @return True if the token is accepted by this filter.
     */
    public boolean accept(final CobolToken token);
}
