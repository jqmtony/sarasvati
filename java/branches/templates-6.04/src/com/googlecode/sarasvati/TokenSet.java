/*
    This file is part of Sarasvati.

    Sarasvati is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Sarasvati is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Sarasvati.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2009 Paul Lorenz
*/

package com.googlecode.sarasvati;

import java.util.List;

/**
 * A token set is way of associating tokens together and allowing them to have
 * state relative to one another. This takes two forms:
 *
 * <ul>
 *   <li>Each token has an index in the token set. This allows a token in the
 *       set to be distinguished from other (useful for template style nodes).
 *   <li>The token set has it's own environment. The token set environment may
 *       contain attributes visible to all tokens in the set, or just tokens
 *       with a specific index.
 * </ul>
 *
 * @author Paul Lorenz
 */
public interface TokenSet
{
  /**
   * Returns the process that this token set belongs to.
   *
   * @return The process that this token set belongs to.
   */
  GraphProcess getProcess ();

  /**
   * Returns the name given to this token set.
   *
   * @return The name given to this token set.
   */
  String getName ();

  /**
   * Returns the list of active (non-completed) arc tokens which
   * are members of this token set.
   *
   * @param engine The engine executing the current process. May
   *               be required in order to load the token list.
   *
   * @return the list of active (non-completed) arc tokens which
   *         are members of this token set.
   */
  List<ArcToken> getActiveArcTokens (Engine engine);

  /**
   * Returns the list of active (non-completed) node tokens which
   * are members of this token set.
   *
   * @param engine The engine executing the current process. May
   *               be required in order to load the token list.
   *
   * @return the list of active (non-completed) node tokens which
   *         are members of this token set.
   */
  List<NodeToken> getActiveNodeTokens (Engine engine);

  /**
   * Returns true if the token set has been join on and contains no more
   * active tokens, and false otherwise.
   *
   * @return True if the token set has been join on and contains no more
   *         active tokens, and false otherwise.
   */
  boolean isComplete ();

  /**
   * Marks this token set as being complete, in the sense that a
   * token set join has been performed on the token set.
   */
  void markComplete (Engine engine);
}