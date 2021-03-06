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
package com.googlecode.sarasvati.visual.common;

import java.awt.Point;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.Path;
import org.eclipse.draw2d.graph.ShortestPathRouter;
import org.netbeans.api.visual.router.Router;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Widget;

import com.googlecode.sarasvati.visual.util.ConvertUtil;

public class ShortestPathRouterAdapter implements Router
{
  protected GraphSceneImpl<?, ?> scene;
  protected ShortestPathRouter router;
  protected boolean dirty = false;

  public ShortestPathRouterAdapter (GraphSceneImpl<?,?> scene)
  {
    this.scene = scene;
    this.router = new ShortestPathRouter();
    router.setSpacing( 5 );
  }

  public void addNodeWidget (Widget w)
  {
    w.addDependency( new WidgetListener( w ) );
  }

  public void removeNodeWidget (Widget w)
  {
    WidgetListener widgetListener = null;
    for (Widget.Dependency dependency : w.getDependencies() )
    {
      if ( dependency instanceof WidgetListener )
      {
        widgetListener = (WidgetListener)dependency;
      }
    }

    if ( widgetListener != null )
    {
      w.removeDependency( widgetListener );
      widgetListener.cleanup();
    }
  }

  public void addPath (Path path)
  {
    router.addPath( path );
    dirty = true;
  }

  public void removePath (Path path)
  {
    router.removePath( path );
    dirty = true;
  }

  @Override
  public List<Point> routeConnection (ConnectionWidget conn)
  {
    PathTrackingConnectionWidget pathTrackingCW = (PathTrackingConnectionWidget)conn;
    pathTrackingCW.ensurePathCurrent();

    while ( dirty )
    {
      router.solve();
      dirty = false;
      redrawConnections( conn );
    }

    return pathTrackingCW.getRoute();
  }

  public void redrawConnections (Widget source)
  {
    for ( Widget widget : scene.getConnectionLayer().getChildren() )
    {
      if ( widget != source && widget instanceof PathTrackingConnectionWidget)
      {
        PathTrackingConnectionWidget conn = (PathTrackingConnectionWidget)widget;
        conn.reroute();
      }
    }
  }

  public void setDirty ()
  {
    this.dirty = true;
  }

  public class WidgetListener implements Widget.Dependency
  {
    Widget widget;
    Rectangle bounds;

    public Rectangle getNewBounds ()
    {
      java.awt.Rectangle newBounds = widget.getBounds();
      if (newBounds == null )
      {
        return null;
      }
      newBounds.setLocation( widget.getLocation() );
      return ConvertUtil.awtToSwt( newBounds );
    }

    public WidgetListener (Widget widget)
    {
      this.widget = widget;
      this.bounds = getNewBounds();
      dirty = true;
    }

    @Override
    public void revalidateDependency ()
    {
      if (bounds == null )
      {
        bounds = getNewBounds();
        if (bounds != null )
        {
          router.addObstacle( bounds );
        }
      }
      else
      {
        Rectangle newBounds = getNewBounds();
        if ( newBounds == null )
        {
          router.removeObstacle( bounds );
        }
        else
        {
          router.updateObstacle( bounds, newBounds );
        }
        bounds = newBounds;
      }

      dirty = true;
    }

    public void cleanup ()
    {
      if ( bounds != null )
      {
        router.removeObstacle( bounds );
        dirty = true;
      }
    }
  }
}