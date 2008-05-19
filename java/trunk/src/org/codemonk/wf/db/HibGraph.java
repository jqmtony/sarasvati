/**
 * Created on Apr 25, 2008
 */
package org.codemonk.wf.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codemonk.wf.IArc;
import org.codemonk.wf.IGraph;
import org.codemonk.wf.INode;

@Entity
@Table (name="wf_graph")
public class HibGraph implements IGraph
{
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected Long   id;
  protected String name;
  protected int    version;

  @OneToMany (fetch=FetchType.EAGER, mappedBy="graph")
  protected List<HibNodeRef> nodeRefs;

  @OneToMany (fetch=FetchType.EAGER, mappedBy="graph")
  protected List<HibArc>     arcs;

  @Transient
  protected Map<HibNodeRef, List<IArc>> inputMap;

  @Transient
  protected Map<HibNodeRef, List<IArc>> outputMap;

  public Long getId ()
  {
    return id;
  }

  public void setId (Long id)
  {
    this.id = id;
  }

  @Override
  public String getName ()
  {
    return name;
  }

  public void setName (String name)
  {
    this.name = name;
  }

  public int getVersion ()
  {
    return version;
  }

  public void setVersion (int version)
  {
    this.version = version;
  }

  public List<HibNodeRef> getNodeRefs ()
  {
    return nodeRefs;
  }

  public void setNodeRefs (List<HibNodeRef> nodeRefs)
  {
    this.nodeRefs = nodeRefs;
  }

  public List<HibArc> getArcs ()
  {
    return arcs;
  }

  public void setArcs (List<HibArc> arcs)
  {
    this.arcs = arcs;
  }

  @Override
  public List<IArc> getInputArcs (INode node)
  {
    if ( inputMap == null )
    {
      initialize();
    }
    return inputMap.get( node );
  }

  @Override
  public List<IArc> getInputArcs (INode node, String arcName)
  {
    List<IArc> arcList = getInputArcs( node );
    List<IArc> result = new ArrayList<IArc>( arcList.size() );

    for ( IArc arc : arcList )
    {
      if ( arcName.equals( arc.getName() ) )
      {
        result.add( arc );
      }
    }
    return result;
  }

  @Override
  public List<IArc> getOutputArcs (INode node)
  {
    if (outputMap == null)
    {
      initialize();
    }
    return outputMap.get( node );
  }

  @Override
  public List<IArc> getOutputArcs (INode node, String arcName)
  {
    List<IArc> arcList = getOutputArcs( node );
    List<IArc> result = new ArrayList<IArc>( arcList.size() );

    for ( IArc arc : arcList )
    {
      if ( arcName.equals( arc.getName() ) )
      {
        result.add( arc );
      }
    }
    return result;
  }

  public void initialize ()
  {
    inputMap  = new HashMap<HibNodeRef, List<IArc>>();
    outputMap = new HashMap<HibNodeRef, List<IArc>>();

    for ( HibArc arc : arcs )
    {
      HibNodeRef node = arc.getStartNode();
      List<IArc> list = outputMap.get( node );

      if ( list == null )
      {
        list = new LinkedList<IArc>();
        outputMap.put( node, list );
      }

      list.add( arc );

      node = arc.getEndNode();
      list = inputMap.get( node );

      if ( list == null )
      {
        list = new LinkedList<IArc>();
        inputMap.put( node, list );
      }

      list.add( arc );
    }

    List<IArc> emptyList = Collections.emptyList();
    for (HibNodeRef node : nodeRefs )
    {
      if ( !inputMap.containsKey( node ) )
      {
        inputMap.put( node, emptyList );
      }
      if ( !outputMap.containsKey( node ) )
      {
        outputMap.put( node, emptyList );
      }
    }
  }

  @Override
  public List<INode> getStartNodes ()
  {
    List<INode> startNodes = new LinkedList<INode>();

    for ( HibNodeRef node : getNodeRefs() )
    {
      if ( "start".equals( node.getType() ) && node.getGraph().equals( this ) )
      {
        startNodes.add( node );
      }
    }

    return startNodes;
  }

  @Override
  public boolean hasArcInverse( IArc arc )
  {
    for (IArc tmpArc : arcs)
    {
      if ( arc.getStartNode().equals( tmpArc.getEndNode() ) &&
           arc.getEndNode().equals( tmpArc.getStartNode() ) )
      {
        return true;
      }
    }

    return false;
  }

  @Override
  public int hashCode ()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( id == null )
        ? 0 : id.hashCode() );
    return result;
  }

  @Override
  public boolean equals (Object obj)
  {
    if ( this == obj ) return true;
    if ( obj == null ) return false;
    if ( !( obj instanceof HibGraph ) ) return false;
    final HibGraph other = (HibGraph)obj;
    if ( id == null )
    {
      if ( other.id != null ) return false;
    }
    else if ( !id.equals( other.id ) ) return false;
    return true;
  }
}