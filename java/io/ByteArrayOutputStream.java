/*
 * Copyright (c) 1994, 2006, Oracle and/or its affiliates. All rights reserved.
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

import java.util.Arrays;

/** {@collect.stats}
 * {@property.name properties java.io.ByteArrayOutputStream_Close}
 * {@property.name properties java.io.ByteArrayOutputStream_FlushBeforeRetrieve}
 * {@description.open}
 * This class implements an output stream in which the data is
 * written into a byte array. The buffer automatically grows as data
 * is written to it.
 * The data can be retrieved using <code>toByteArray()</code> and
 * <code>toString()</code>.
 * {@description.close}
 * <p>
 * {@property.open Property:java.io.ByteArrayOutputStream_Close}
 * Closing a <tt>ByteArrayOutputStream</tt> has no effect. The methods in
 * this class can be called after the stream has been closed without
 * generating an <tt>IOException</tt>.
 * {@property.close}
 *
 * @author  Arthur van Hoff
 * @since   JDK1.0
 */

public class ByteArrayOutputStream extends OutputStream {

    /** {@collect.stats}
     * {@description.open}
     * The buffer where data is stored.
     * {@description.close}
     */
    protected byte buf[];

    /** {@collect.stats}
     * {@description.open}
     * The number of valid bytes in the buffer.
     * {@description.close}
     */
    protected int count;

    /** {@collect.stats}
     * {@description.open}
     * Creates a new byte array output stream. The buffer capacity is
     * initially 32 bytes, though its size increases if necessary.
     * {@description.close}
     */
    public ByteArrayOutputStream() {
        this(32);
    }

    /** {@collect.stats}
     * {@description.open}
     * Creates a new byte array output stream, with a buffer capacity of
     * the specified size, in bytes.
     * {@description.close}
     *
     * @param   size   the initial size.
     * @exception  IllegalArgumentException if size is negative.
     */
    public ByteArrayOutputStream(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Negative initial size: "
                                               + size);
        }
        buf = new byte[size];
    }

    /** {@collect.stats}
     * {@description.open}
     * Writes the specified byte to this byte array output stream.
     * {@description.close}
     *
     * @param   b   the byte to be written.
     */
    public synchronized void write(int b) {
        int newcount = count + 1;
        if (newcount > buf.length) {
            buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
        }
        buf[count] = (byte)b;
        count = newcount;
    }

    /** {@collect.stats}
     * {@description.open}
     * Writes <code>len</code> bytes from the specified byte array
     * starting at offset <code>off</code> to this byte array output stream.
     * {@description.close}
     *
     * @param   b     the data.
     * @param   off   the start offset in the data.
     * @param   len   the number of bytes to write.
     */
    public synchronized void write(byte b[], int off, int len) {
        if ((off < 0) || (off > b.length) || (len < 0) ||
            ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        int newcount = count + len;
        if (newcount > buf.length) {
            buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
        }
        System.arraycopy(b, off, buf, count, len);
        count = newcount;
    }

    /** {@collect.stats}
     * {@description.open}
     * Writes the complete contents of this byte array output stream to
     * the specified output stream argument, as if by calling the output
     * stream's write method using <code>out.write(buf, 0, count)</code>.
     * {@description.close}
     *
     * {@property.open Property:java.io.ByteArrayOutputStream_FlushBeforeRetrieve}
     * When an OutputStream (or its subclass) instance is built on top of an
     * underlying ByteArrayOutputStream instance, it should be flushed or closed
     * before the contents of the ByteArrayOutputStream instance is retrieved.
     * {@property.close}
     *
     * @param      out   the output stream to which to write the data.
     * @exception  IOException  if an I/O error occurs.
     */
    public synchronized void writeTo(OutputStream out) throws IOException {
        out.write(buf, 0, count);
    }

    /** {@collect.stats}
     * {@description.open}
     * Resets the <code>count</code> field of this byte array output
     * stream to zero, so that all currently accumulated output in the
     * output stream is discarded. The output stream can be used again,
     * reusing the already allocated buffer space.
     * {@description.close}
     *
     * @see     java.io.ByteArrayInputStream#count
     */
    public synchronized void reset() {
        count = 0;
    }

    /** {@collect.stats}
     * {@description.open}
     * Creates a newly allocated byte array. Its size is the current
     * size of this output stream and the valid contents of the buffer
     * have been copied into it.
     * {@description.close}
     *
     * {@property.open Property:java.io.ByteArrayOutputStream_FlushBeforeRetrieve}
     * {@property.close}
     *
     * @return  the current contents of this output stream, as a byte array.
     * @see     java.io.ByteArrayOutputStream#size()
     */
    public synchronized byte toByteArray()[] {
        return Arrays.copyOf(buf, count);
    }

    /** {@collect.stats}
     * {@description.open}
     * Returns the current size of the buffer.
     * {@description.close}
     *
     * @return  the value of the <code>count</code> field, which is the number
     *          of valid bytes in this output stream.
     * @see     java.io.ByteArrayOutputStream#count
     */
    public synchronized int size() {
        return count;
    }

    /** {@collect.stats}
     * {@description.open}
     * Converts the buffer's contents into a string decoding bytes using the
     * platform's default character set. The length of the new <tt>String</tt>
     * is a function of the character set, and hence may not be equal to the
     * size of the buffer.
     *
     * <p> This method always replaces malformed-input and unmappable-character
     * sequences with the default replacement string for the platform's
     * default character set. The {@linkplain java.nio.charset.CharsetDecoder}
     * class should be used when more control over the decoding process is
     * required.
     * {@description.close}
     *
     * {@property.open Property:java.io.ByteArrayOutputStream_FlushBeforeRetrieve}
     * {@property.close}
     *
     * @return String decoded from the buffer's contents.
     * @since  JDK1.1
     */
    public synchronized String toString() {
        return new String(buf, 0, count);
    }

    /** {@collect.stats}
     * {@description.open}
     * Converts the buffer's contents into a string by decoding the bytes using
     * the specified {@link java.nio.charset.Charset charsetName}. The length of
     * the new <tt>String</tt> is a function of the charset, and hence may not be
     * equal to the length of the byte array.
     *
     * <p> This method always replaces malformed-input and unmappable-character
     * sequences with this charset's default replacement string. The {@link
     * java.nio.charset.CharsetDecoder} class should be used when more control
     * over the decoding process is required.
     * {@description.close}
     *
     * {@property.open Property:java.io.ByteArrayOutputStream_FlushBeforeRetrieve}
     * {@property.close}
     *
     * @param  charsetName  the name of a supported
     *              {@linkplain java.nio.charset.Charset </code>charset<code>}
     * @return String decoded from the buffer's contents.
     * @exception  UnsupportedEncodingException
     *             If the named charset is not supported
     * @since   JDK1.1
     */
    public synchronized String toString(String charsetName)
        throws UnsupportedEncodingException
    {
        return new String(buf, 0, count, charsetName);
    }

    /** {@collect.stats}
     * {@description.open}
     * Creates a newly allocated string. Its size is the current size of
     * the output stream and the valid contents of the buffer have been
     * copied into it. Each character <i>c</i> in the resulting string is
     * constructed from the corresponding element <i>b</i> in the byte
     * array such that:
     * <blockquote><pre>
     *     c == (char)(((hibyte &amp; 0xff) &lt;&lt; 8) | (b &amp; 0xff))
     * </pre></blockquote>
     * {@description.close}
     *
     * {@property.open Property:java.io.ByteArrayOutputStream_FlushBeforeRetrieve}
     * {@property.close}
     *
     * @deprecated This method does not properly convert bytes into characters.
     * As of JDK&nbsp;1.1, the preferred way to do this is via the
     * <code>toString(String enc)</code> method, which takes an encoding-name
     * argument, or the <code>toString()</code> method, which uses the
     * platform's default character encoding.
     *
     * @param      hibyte    the high byte of each resulting Unicode character.
     * @return     the current contents of the output stream, as a string.
     * @see        java.io.ByteArrayOutputStream#size()
     * @see        java.io.ByteArrayOutputStream#toString(String)
     * @see        java.io.ByteArrayOutputStream#toString()
     */
    @Deprecated
    public synchronized String toString(int hibyte) {
        return new String(buf, hibyte, 0, count);
    }

    /** {@collect.stats}
     * {@description.open}
     * Closing a <tt>ByteArrayOutputStream</tt> has no effect.
     * {@description.close}
     * {@property.open Property:java.io.ByteArrayOutputStream_Close}
     * The methods in
     * this class can be called after the stream has been closed without
     * generating an <tt>IOException</tt>.
     * {@property.close}
     * <p>
     */
    public void close() throws IOException {
    }

}
