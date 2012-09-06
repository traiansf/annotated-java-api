/*
 * Copyright (c) 2003, 2006, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package javax.sql.rowset.serial;

import java.sql.*;
import java.io.*;
import java.util.Map;
import java.net.URL;


/** {@collect.stats}
 * A serialized version of an <code>Array</code>
 * object, which is the mapping in the Java programming language of an SQL
 * <code>ARRAY</code> value.
 * <P>
 * The <code>SerialArray</code> class provides a constructor for creating
 * a <code>SerialArray</code> instance from an <code>Array</code> object,
 * methods for getting the base type and the SQL name for the base type, and
 * methods for copying all or part of a <code>SerialArray</code> object.
 * <P>
 * Note: In order for this class to function correctly, a connection to the
 * data source
 * must be available in order for the SQL <code>Array</code> object to be
 * materialized (have all of its elements brought to the client server)
 * if necessary. At this time, logical pointers to the data in the data source,
 * such as locators, are not currently supported.
 */
public class SerialArray implements Array, Serializable, Cloneable {

        /** {@collect.stats}
         * A serialized array in which each element is an <code>Object</code>
         * in the Java programming language that represents an element
         * in the SQL <code>ARRAY</code> value.
         * @serial
         */
    private Object[] elements;

        /** {@collect.stats}
         * The SQL type of the elements in this <code>SerialArray</code> object.  The
         * type is expressed as one of the constants from the class
         * <code>java.sql.Types</code>.
         * @serial
         */
    private int baseType;

        /** {@collect.stats}
         * The type name used by the DBMS for the elements in the SQL <code>ARRAY</code>
         * value that this <code>SerialArray</code> object represents.
         * @serial
         */
    private String baseTypeName;

        /** {@collect.stats}
         * The number of elements in this <code>SerialArray</code> object, which
         * is also the number of elements in the SQL <code>ARRAY</code> value
         * that this <code>SerialArray</code> object represents.
         * @serial
         */
    private int len;

    /** {@collect.stats}
     * Constructs a new <code>SerialArray</code> object from the given
     * <code>Array</code> object, using the given type map for the custom
     * mapping of each element when the elements are SQL UDTs.
     * <P>
     * This method does custom mapping if the array elements are a UDT
     * and the given type map has an entry for that UDT.
     * Custom mapping is recursive,
     * meaning that if, for instance, an element of an SQL structured type
     * is an SQL structured type that itself has an element that is an SQL
     * structured type, each structured type that has a custom mapping will be
     * mapped according to the given type map.
     * <P>
     * The new <code>SerialArray</code>
     * object contains the same elements as the <code>Array</code> object
     * from which it is built, except when the base type is the SQL type
     * <code>STRUCT</code>, <code>ARRAY</code>, <code>BLOB</code>,
     * <code>CLOB</code>, <code>DATALINK</code> or <code>JAVA_OBJECT</code>.
     * In this case, each element in the new
     * <code>SerialArray</code> object is the appropriate serialized form,
     * that is, a <code>SerialStruct</code>, <code>SerialArray</code>,
     * <code>SerialBlob</code>, <code>SerialClob</code>,
     * <code>SerialDatalink</code>, or <code>SerialJavaObject</code> object.
     * <P>
     * Note: (1) The <code>Array</code> object from which a <code>SerialArray</code>
     * object is created must have materialized the SQL <code>ARRAY</code> value's
     * data on the client before it is passed to the constructor.  Otherwise,
     * the new <code>SerialArray</code> object will contain no data.
     * <p>
     * Note: (2) If the <code>Array</code> contains <code>java.sql.Types.JAVA_OBJECT</code>
     * types, the <code>SerialJavaObject</code> constructor is called where checks
     * are made to ensure this object is serializable.
     * <p>
     * Note: (3) The <code>Array</code> object supplied to this constructor cannot
     * return <code>null</code> for any <code>Array.getArray()</code> methods.
     * <code>SerialArray</code> cannot serialize null array values.
     *
     *
     * @param array the <code>Array</code> object to be serialized
     * @param map a <code>java.util.Map</code> object in which
     *        each entry consists of 1) a <code>String</code> object
     *        giving the fully qualified name of a UDT (an SQL structured type or
     *        distinct type) and 2) the
     *        <code>Class</code> object for the <code>SQLData</code> implementation
     *        that defines how the UDT is to be mapped. The <i>map</i>
     *        parameter does not have any effect for <code>Blob</code>,
     *        <code>Clob</code>, <code>DATALINK</code>, or
     *        <code>JAVA_OBJECT</code> types.
     * @throws SerialException if an error occurs serializing the
     *        <code>Array</code> object
     * @throws SQLException if a database access error occurs or if the
     *        <i>array</i> or the <i>map</i> values are <code>null</code>
     */
     public SerialArray(Array array, Map<String,Class<?>> map)
         throws SerialException, SQLException
     {

        if ((array == null) || (map == null)) {
            throw new SQLException("Cannot instantiate a SerialArray " +
            "object with null parameters");
        }

        if ((elements = (Object[])array.getArray()) == null) {
             throw new SQLException("Invalid Array object. Calls to Array.getArray() " +
                 "return null value which cannot be serialized");
         }

        elements = (Object[])array.getArray(map);
        baseType = array.getBaseType();
        baseTypeName = array.getBaseTypeName();
        len = elements.length;

        switch (baseType) {
            case java.sql.Types.STRUCT:
                for (int i = 0; i < len; i++) {
                    elements[i] = new SerialStruct((Struct)elements[i], map);
                }
            break;

            case java.sql.Types.ARRAY:
                for (int i = 0; i < len; i++) {
                    elements[i] = new SerialArray((Array)elements[i], map);
                }
            break;

            case java.sql.Types.BLOB:
            for (int i = 0; i < len; i++) {
                elements[i] = new SerialBlob((Blob)elements[i]);
            }
            break;

            case java.sql.Types.CLOB:
                for (int i = 0; i < len; i++) {
                    elements[i] = new SerialClob((Clob)elements[i]);
                }
            break;

            case java.sql.Types.DATALINK:
                for (int i = 0; i < len; i++) {
                    elements[i] = new SerialDatalink((URL)elements[i]);
                }
            break;

            case java.sql.Types.JAVA_OBJECT:
                for (int i = 0; i < len; i++) {
                elements[i] = new SerialJavaObject((Object)elements[i]);
            }
        default:
            ;
        }
  }

    /** {@collect.stats}
     * This method frees the <code>Array</code> object and releases the resources that
     * it holds. The object is invalid once the <code>free</code>
     * method is called.
     *<p>
     * After <code>free</code> has been called, any attempt to invoke a
     * method other than <code>free</code> will result in a <code>SQLException</code>
     * being thrown.  If <code>free</code> is called multiple times, the subsequent
     * calls to <code>free</code> are treated as a no-op.
     *<p>
     *
     * @throws SQLException if an error occurs releasing
     * the Array's resources
     * @exception SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    public void free() throws SQLException {
         throw new SQLFeatureNotSupportedException("Feature not supported");
    }

    /** {@collect.stats}
     * Constructs a new <code>SerialArray</code> object from the given
     * <code>Array</code> object.
     * <P>
     * This constructor does not do custom mapping.  If the base type of the array
     * is an SQL structured type and custom mapping is desired, the constructor
     * <code>SerialArray(Array array, Map map)</code> should be used.
     * <P>
     * The new <code>SerialArray</code>
     * object contains the same elements as the <code>Array</code> object
     * from which it is built, except when the base type is the SQL type
     * <code>BLOB</code>,
     * <code>CLOB</code>, <code>DATALINK</code> or <code>JAVA_OBJECT</code>.
     * In this case, each element in the new
     * <code>SerialArray</code> object is the appropriate serialized form,
     * that is, a <code>SerialBlob</code>, <code>SerialClob</code>,
     * <code>SerialDatalink</code>, or <code>SerialJavaObject</code> object.
     * <P>
     * Note: (1) The <code>Array</code> object from which a <code>SerialArray</code>
     * object is created must have materialized the SQL <code>ARRAY</code> value's
     * data on the client before it is passed to the constructor.  Otherwise,
     * the new <code>SerialArray</code> object will contain no data.
     * <p>
     * Note: (2) The <code>Array</code> object supplied to this constructor cannot
     * return <code>null</code> for any <code>Array.getArray()</code> methods.
     * <code>SerialArray</code> cannot serialize <code>null</code> array values.
     *
     * @param array the <code>Array</code> object to be serialized
     * @throws SerialException if an error occurs serializing the
     *     <code>Array</code> object
     * @throws SQLException if a database access error occurs or the
     *     <i>array</i> parameter is <code>null</code>.
     */
     public SerialArray(Array array) throws SerialException, SQLException {
         if (array == null) {
             throw new SQLException("Cannot instantiate a SerialArray " +
                 "object with a null Array object");
         }

         if ((elements = (Object[])array.getArray()) == null) {
             throw new SQLException("Invalid Array object. Calls to Array.getArray() " +
                 "return null value which cannot be serialized");
         }

         //elements = (Object[])array.getArray();
         baseType = array.getBaseType();
         baseTypeName = array.getBaseTypeName();
         len = elements.length;

        switch (baseType) {

        case java.sql.Types.BLOB:
            for (int i = 0; i < len; i++) {
                elements[i] = new SerialBlob((Blob)elements[i]);
            }
            break;

        case java.sql.Types.CLOB:
            for (int i = 0; i < len; i++) {
                elements[i] = new SerialClob((Clob)elements[i]);
            }
            break;

        case java.sql.Types.DATALINK:
            for (int i = 0; i < len; i++) {
                elements[i] = new SerialDatalink((URL)elements[i]);
            }
            break;

        case java.sql.Types.JAVA_OBJECT:
            for (int i = 0; i < len; i++) {
                elements[i] = new SerialJavaObject((Object)elements[i]);
            }

        default:
            ;
        }


    }

        /** {@collect.stats}
         * Returns a new array that is a copy of this <code>SerialArray</code>
         * object.
         *
         * @return a copy of this <code>SerialArray</code> object as an
         *         <code>Object</code> in the Java programming language
         * @throws SerialException if an error occurs retrieving a copy of
     *         this <code>SerialArray</code> object
         */
    public Object getArray() throws SerialException {
        Object dst = new Object[len];
        System.arraycopy((Object)elements, 0, dst, 0, len);
        return dst;
    }

 //[if an error occurstype map used??]
        /** {@collect.stats}
         * Returns a new array that is a copy of this <code>SerialArray</code>
         * object, using the given type map for the custom
         * mapping of each element when the elements are SQL UDTs.
         * <P>
         * This method does custom mapping if the array elements are a UDT
         * and the given type map has an entry for that UDT.
     * Custom mapping is recursive,
         * meaning that if, for instance, an element of an SQL structured type
         * is an SQL structured type that itself has an element that is an SQL
         * structured type, each structured type that has a custom mapping will be
         * mapped according to the given type map.
         *
     * @param map a <code>java.util.Map</code> object in which
     *        each entry consists of 1) a <code>String</code> object
     *        giving the fully qualified name of a UDT and 2) the
     *        <code>Class</code> object for the <code>SQLData</code> implementation
     *        that defines how the UDT is to be mapped
         * @return a copy of this <code>SerialArray</code> object as an
         *         <code>Object</code> in the Java programming language
         * @throws SerialException if an error occurs
         */
    public Object getArray(Map<String, Class<?>> map) throws SerialException {
        Object dst[] = new Object[len];
        System.arraycopy((Object)elements, 0, dst, 0, len);
        return dst;
    }

        /** {@collect.stats}
         * Returns a new array that is a copy of a slice
         * of this <code>SerialArray</code> object, starting with the
         * element at the given index and containing the given number
         * of consecutive elements.
         *
         * @param index the index into this <code>SerialArray</code> object
         *              of the first element to be copied;
         *              the index of the first element is <code>0</code>
         * @param count the number of consecutive elements to be copied, starting
         *              at the given index
         * @return a copy of the designated elements in this <code>SerialArray</code>
         *         object as an <code>Object</code> in the Java programming language
         * @throws SerialException if an error occurs
         */
    public Object getArray(long index, int count) throws SerialException {
        Object dst = new Object[count];
        System.arraycopy((Object)elements, (int)index, dst, 0, count);
        return dst;
    }

        /** {@collect.stats}
         * Returns a new array that is a copy of a slice
         * of this <code>SerialArray</code> object, starting with the
         * element at the given index and containing the given number
         * of consecutive elements.
         * <P>
         * This method does custom mapping if the array elements are a UDT
         * and the given type map has an entry for that UDT.
     * Custom mapping is recursive,
         * meaning that if, for instance, an element of an SQL structured type
         * is an SQL structured type that itself has an element that is an SQL
         * structured type, each structured type that has a custom mapping will be
         * mapped according to the given type map.
         *
         * @param index the index into this <code>SerialArray</code> object
         *              of the first element to be copied; the index of the
         *              first element in the array is <code>0</code>
         * @param count the number of consecutive elements to be copied, starting
         *              at the given index
     * @param map a <code>java.util.Map</code> object in which
     *        each entry consists of 1) a <code>String</code> object
     *        giving the fully qualified name of a UDT and 2) the
     *        <code>Class</code> object for the <code>SQLData</code> implementation
     *        that defines how the UDT is to be mapped
         * @return a copy of the designated elements in this <code>SerialArray</code>
         *         object as an <code>Object</code> in the Java programming language
         * @throws SerialException if an error occurs
         */
    public Object getArray(long index, int count, Map<String,Class<?>> map)
        throws SerialException
    {
        Object dst = new Object[count];
        System.arraycopy((Object)elements, (int)index, dst, 0, count);
        return dst;
    }

        /** {@collect.stats}
         * Retrieves the SQL type of the elements in this <code>SerialArray</code>
         * object.  The <code>int</code> returned is one of the constants in the class
         * <code>java.sql.Types</code>.
         *
         * @return one of the constants in <code>java.sql.Types</code>, indicating
         *         the SQL type of the elements in this <code>SerialArray</code> object
         * @throws SerialException if an error occurs
         */
    public int getBaseType() throws SerialException {
        return baseType;
    }

        /** {@collect.stats}
         * Retrieves the DBMS-specific type name for the elements in this
         * <code>SerialArray</code> object.
         *
         * @return the SQL type name used by the DBMS for the base type of this
     *         <code>SerialArray</code> object
         * @throws SerialException if an error occurs
         */
    public String getBaseTypeName() throws SerialException {
        return baseTypeName;
    }

    /** {@collect.stats}
     * Retrieves a <code>ResultSet</code> object holding the elements of
     * the subarray that starts at
     * index <i>index</i> and contains up to <i>count</i> successive elements.
     * This method uses the connection's type map to map the elements of
     * the array if the map contains
     * an entry for the base type. Otherwise, the standard mapping is used.
     *
     * @param index the index into this <code>SerialArray</code> object
     *         of the first element to be copied; the index of the
     *         first element in the array is <code>0</code>
     * @param count the number of consecutive elements to be copied, starting
     *         at the given index
     * @return a <code>ResultSet</code> object containing the designated
     *         elements in this <code>SerialArray</code> object, with a
     *         separate row for each element
     * @throws SerialException, which in turn throws an
     *         <code>UnsupportedOperationException</code>, if this method is called
     */
    public ResultSet getResultSet(long index, int count) throws SerialException {
        throw new UnsupportedOperationException();
    }

    /** {@collect.stats}
     *
     * Retrieves a <code>ResultSet</code> object that contains all of
     * the elements of the SQL <code>ARRAY</code>
     * value represented by this <code>SerialArray</code> object. This method uses
     * the specified map for type map customizations unless the base type of the
     * array does not match a user-defined type (UDT) in <i>map</i>, in
     * which case it uses the
     * standard mapping. This version of the method <code>getResultSet</code>
     * uses either the given type map or the standard mapping; it never uses the
     * type map associated with the connection.
     *
     * @param map a <code>java.util.Map</code> object in which
     *        each entry consists of 1) a <code>String</code> object
     *        giving the fully qualified name of a UDT and 2) the
     *        <code>Class</code> object for the <code>SQLData</code> implementation
     *        that defines how the UDT is to be mapped
     * @return a <code>ResultSet</code> object containing all of the
     *         elements in this <code>SerialArray</code> object, with a
     *         separate row for each element
     * @throws SerialException, which in turn throws an
     *         <code>UnsupportedOperationException</code>, if this method is called
     */
    public ResultSet getResultSet(Map<String, Class<?>> map)
        throws SerialException
    {
        throw new UnsupportedOperationException();
    }

    /** {@collect.stats}
     * Retrieves a <code>ResultSet</code> object that contains all of
     * the elements in the <code>ARRAY</code> value that this
     * <code>SerialArray</code> object represents.
     * If appropriate, the elements of the array are mapped using the connection's
     * type map; otherwise, the standard mapping is used.
     *
     * @return a <code>ResultSet</code> object containing all of the
     *         elements in this <code>SerialArray</code> object, with a
     *         separate row for each element
     * @throws SerialException if called, which in turn throws an
     *         <code>UnsupportedOperationException</code>, if this method is called
     */
    public ResultSet getResultSet() throws SerialException {
        throw new UnsupportedOperationException();
    }


    /** {@collect.stats}
     * Retrieves a result set holding the elements of the subarray that starts at
     * Retrieves a <code>ResultSet</code> object that contains a subarray of the
     * elements in this <code>SerialArray</code> object, starting at
     * index <i>index</i> and containing up to <i>count</i> successive
     * elements. This method uses
     * the specified map for type map customizations unless the base type of the
     * array does not match a user-defined type (UDT) in <i>map</i>, in
     * which case it uses the
     * standard mapping. This version of the method <code>getResultSet</code> uses
     * either the given type map or the standard mapping; it never uses the type
     * map associated with the connection.
     *
     * @param index the index into this <code>SerialArray</code> object
     *              of the first element to be copied; the index of the
     *              first element in the array is <code>0</code>
     * @param count the number of consecutive elements to be copied, starting
     *              at the given index
     * @param map a <code>java.util.Map</code> object in which
     *        each entry consists of 1) a <code>String</code> object
     *        giving the fully qualified name of a UDT and 2) the
     *        <code>Class</code> object for the <code>SQLData</code> implementation
     *        that defines how the UDT is to be mapped
     * @return a <code>ResultSet</code> object containing the designated
     *         elements in this <code>SerialArray</code> object, with a
     *         separate row for each element
     * @throws SerialException if called, which in turn throws an
     *         <code>UnsupportedOperationException</code>
     */
    public ResultSet getResultSet(long index, int count,
                                  Map<String,Class<?>> map)
        throws SerialException
    {
        throw new UnsupportedOperationException();
    }

    /** {@collect.stats}
     * The identifier that assists in the serialization of this <code>SerialArray</code>
     * object.
     */
    static final long serialVersionUID = -8466174297270688520L;
}
