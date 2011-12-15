/*
 * Copyright (c) 1996, 1999, Oracle and/or its affiliates. All rights reserved.
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

package java.io;

/** {@collect.stats}
 * {@description.open}
 * ObjectOutput extends the DataOutput interface to include writing of objects.
 * DataOutput includes methods for output of primitive types, ObjectOutput
 * extends that interface to include objects, arrays, and Strings.
 * {@description.close}
 *
 * @author  unascribed
 * @see java.io.InputStream
 * @see java.io.ObjectOutputStream
 * @see java.io.ObjectInputStream
 * @since   JDK1.1
 */
public interface ObjectOutput extends DataOutput {
    /** {@collect.stats}
     * {@description.open}
     * Write an object to the underlying storage or stream.  The
     * class that implements this interface defines how the object is
     * written.
     * {@description.close}
     *
     * @param obj the object to be written
     * @exception IOException Any of the usual Input/Output related exceptions.
     */
    public void writeObject(Object obj)
      throws IOException;

    /** {@collect.stats}
     * {@description.open}
     * Writes a byte.
     * {@description.close}
     * {@property.open blocking}
     * This method will block until the byte is actually
     * written.
     * {@property.close}
     * @param b the byte
     * @exception IOException If an I/O error has occurred.
     */
    public void write(int b) throws IOException;

    /** {@collect.stats}
     * {@description.open}
     * Writes an array of bytes.
     * {@description.close}
     * {@property.open blocking}
     * This method will block until the bytes
     * are actually written.
     * {@property.close}
     * @param b the data to be written
     * @exception IOException If an I/O error has occurred.
     */
    public void write(byte b[]) throws IOException;

    /** {@collect.stats}
     * {@description.open}
     * Writes a sub array of bytes.
     * {@description.close}
     * @param b the data to be written
     * @param off       the start offset in the data
     * @param len       the number of bytes that are written
     * @exception IOException If an I/O error has occurred.
     */
    public void write(byte b[], int off, int len) throws IOException;

    /** {@collect.stats}
     * {@description.open}
     * Flushes the stream. This will write any buffered
     * output bytes.
     * {@description.close}
     * @exception IOException If an I/O error has occurred.
     */
    public void flush() throws IOException;

    /** {@collect.stats}
     * {@description.open}
     * Closes the stream.
     * {@description.close}
     * {@property.open runtime formal:java.io.ObjectOutput_Close}
     * This method must be called
     * to release any resources associated with the
     * stream.
     * {@property.close}
     * @exception IOException If an I/O error has occurred.
     */
    public void close() throws IOException;
}
