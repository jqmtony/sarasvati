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
package com.googlecode.sarasvati.impl;

import java.util.Date;

import com.googlecode.sarasvati.GuardAction;
import com.googlecode.sarasvati.GuardResult;

public enum AcceptTokenGuardResult implements GuardResult
{
  INSTANCE;

  @Override
  public String getExitArcForSkip()
  {
    throw new UnsupportedOperationException( "getExitArcsForSkip should never be called on a GuardResult with action of AcceptToken" );
  }

  /**
   * @see com.googlecode.sarasvati.GuardResult#getDelayTillTime()
   */
  @Override
  public Date getDelayTillTime()
  {
    throw new UnsupportedOperationException( "getDelayUntilDate should never be called on a GuardResult with action of AcceptToken" );
  }

  @Override
  public GuardAction getGuardAction()
  {
    return GuardAction.AcceptToken;
  }

  @Override
  public String toString()
  {
    return "AcceptTokenResponse";
  }
}