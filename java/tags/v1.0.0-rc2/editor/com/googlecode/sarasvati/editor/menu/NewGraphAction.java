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

    Copyright 2008 Paul Lorenz
*/
package com.googlecode.sarasvati.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import com.googlecode.sarasvati.editor.GraphEditor;

public class NewGraphAction extends AbstractAction
{
  private static final long serialVersionUID = 1L;

  protected GraphEditor editor;

  public NewGraphAction (GraphEditor editor)
  {
    super( "New Process Definition" );
    this.editor = editor;

    putValue( Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke( KeyEvent.VK_N, KeyEvent.CTRL_MASK ) );
    putValue( Action.MNEMONIC_KEY, KeyEvent.VK_N );
  }

  @Override
  public void actionPerformed (ActionEvent e)
  {
    editor.createNewProcessDefinition();
  }
}