/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.mockito.internal.creation.ClassNameFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;

/**
 * Used by default by every Mockito mock.
 * <ul>
 * <li>
 *  Returns appropriate primitive for primitive-returning methods
 * </li>
 * <li>
 *  Returns empty collection for collection-returning methods (works for most commonly used collection types)
 * </li>
 * <li>
 *  Returns description of mock for toString() method
 * </li>
 * <li>
 *  Returns null for everything else
 * </li>
 * </ul>
 */
public class DefaultReturnValues implements ReturnValues {
    
    /* (non-Javadoc)
     * @see org.mockito.configuration.ReturnValues#valueFor(org.mockito.invocation.InvocationOnMock)
     */
    public Object valueFor(InvocationOnMock invocation) {
        if (Invocation.isToString(invocation)) {
            Object mock = invocation.getMock();
            String mockDescription = "Mock for " + ClassNameFinder.classNameForMock(mock) + ", hashCode: " + mock.hashCode();
            return mockDescription;
        }
        
        Class<?> returnType = invocation.getMethod().getReturnType();
        return returnValueFor(returnType);
    }
    
    protected Object returnValueFor(Class<?> type) {
        if (type.isPrimitive()) {
            return primitiveOf(type);
        } else if (isPrimitiveWrapper(type)) {
            return primitiveWrapperOf(type);
        //new instances are used instead of Collections.emptyList(), etc.
        //to avoid UnsupportedOperationException if code under test modifies returned collection
        } else if (type == Collection.class) {
            return new LinkedList<Object>();
        } else if (type == Set.class) {
            return new HashSet<Object>();
        } else if (type == HashSet.class) {
            return new HashSet<Object>();
        } else if (type == SortedSet.class) {
            return new TreeSet<Object>();
        } else if (type == TreeSet.class) {
            return new TreeSet<Object>();
        } else if (type == LinkedHashSet.class) {
            return new LinkedHashSet<Object>();
        } else if (type == List.class) {
            return new LinkedList<Object>();
        } else if (type == LinkedList.class) {
            return new LinkedList<Object>();
        } else if (type == ArrayList.class) {
            return new ArrayList<Object>();
        } else if (type == Map.class) {
            return new HashMap<Object, Object>();
        } else if (type == HashMap.class) {
            return new HashMap<Object, Object>();
        } else if (type == SortedMap.class) {
            return new TreeMap<Object, Object>();
        } else if (type == TreeMap.class) {
            return new TreeMap<Object, Object>();
        } else if (type == LinkedHashMap.class) {
            return new LinkedHashMap<Object, Object>();
        }       
        //Let's not care about the rest of collections.
        return null;
    }

    private Object primitiveOf(Class<?> type) {
        if (type == Boolean.TYPE) {
            return false;
        } else if (type == Character.TYPE) {
            return (char) 0;
        } else {
            return 0;
        } 
    }
    
    private boolean isPrimitiveWrapper(Class<?> type) {
        return wrapperReturnValues.containsKey(type);
    }
    
    private Object primitiveWrapperOf(Class<?> type) {
        return wrapperReturnValues.get(type);
    }
    
    private static Map<Class<?>, Object> wrapperReturnValues = new HashMap<Class<?>, Object>();
    
    static {
        wrapperReturnValues.put(Boolean.class, Boolean.FALSE);
        wrapperReturnValues.put(Character.class, new Character((char) 0));
        wrapperReturnValues.put(Byte.class, new Byte((byte) 0));
        wrapperReturnValues.put(Short.class, new Short((short) 0));
        wrapperReturnValues.put(Integer.class, new Integer(0));
        wrapperReturnValues.put(Long.class, new Long(0));
        wrapperReturnValues.put(Float.class, new Float(0));
        wrapperReturnValues.put(Double.class, new Double(0));
    }
}