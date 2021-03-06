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
package com.googlecode.sarasvati.env;

import java.util.HashMap;
import java.util.Map;

/**
 * Static utility class for registring and using {@link AttributeConverter}s.
 * Note that registering converts is NOT threadsafe, and should be done at
 * startup.
 *
 * @author Paul Lorenz
 */
public class AttributeConverters
{
  private static Map<Class<?>, AttributeConverter> converters = new HashMap<Class<?>, AttributeConverter>();

  static
  {
    converters.put( String.class, new StringAttributeConverter() );

    converters.put( Byte.class, new ByteAttributeConverter() );
    converters.put( Byte.TYPE, new ByteAttributeConverter() );

    converters.put( Boolean.class, new ByteAttributeConverter() );
    converters.put( Boolean.TYPE, new ByteAttributeConverter() );

    converters.put( Short.class, new ShortAttributeConverter() );
    converters.put( Short.TYPE, new ShortAttributeConverter() );

    converters.put( Character.class, new CharacterAttributeConverter() );
    converters.put( Character.TYPE, new CharacterAttributeConverter() );

    converters.put( Integer.class, new IntegerAttributeConverter() );
    converters.put( Integer.TYPE, new IntegerAttributeConverter() );

    converters.put( Long.class, new LongAttributeConverter() );
    converters.put( Long.TYPE, new LongAttributeConverter() );

    converters.put( Float.class, new FloatAttributeConverter() );
    converters.put( Float.TYPE, new FloatAttributeConverter() );

    converters.put( Double.class, new DoubleAttributeConverter() );
    converters.put( Double.TYPE, new DoubleAttributeConverter() );
  }

  private static AttributeConverter defaultConverter = new AttributeConverter()
  {
    @Override
    public Object stringToObject (String string, Class<?> type)
    {
      throw new IllegalArgumentException( "No converter is set up to handle conversion attributes from string to " + type );
    }

    @Override
    public String objectToString (Object object)
    {
      throw new IllegalArgumentException( "No converter is set up to handle conversion of attributes from " + object.getClass() + " to string" );
    }
  };

  public static String objectToString (Object object)
  {
    if ( object == null )
    {
      return null;
    }

    AttributeConverter converter = converters.get( object.getClass() );
    return converter == null ? defaultConverter.objectToString( object ) : converter.objectToString( object );
  }

  @SuppressWarnings("unchecked")
  public static <T> T stringToObject (String string, Class<T> type)
  {
    if ( string == null )
    {
      return null;
    }

    AttributeConverter converter = converters.get( type );
    Object result = converter == null ? defaultConverter.stringToObject( string, type ) : converter.stringToObject( string, type );
    return (T)result;
  }

  public static void setDefaultConverter (AttributeConverter converter)
  {
    defaultConverter = converter;
  }

  public static AttributeConverter getConverterForType (Class<?> type)
  {
    return converters.get( type );
  }
}