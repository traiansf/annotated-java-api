/*
 * Copyright (c) 1994, 2000, Oracle and/or its affiliates. All rights reserved.
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

package java.lang;

/** {@collect.stats} 
 * {@description.open}
 * Thrown by <code>String</code> methods to indicate that an index
 * is either negative or greater than the size of the string.  For
 * some methods such as the charAt method, this exception also is
 * thrown when the index is equal to the size of the string.
 * {@description.close}
 *
 * @author  unascribed
 * @see     java.lang.String#charAt(int)
 * @since   JDK1.0
 */
public
class StringIndexOutOfBoundsException extends IndexOutOfBoundsException {
    /** {@collect.stats}
     * {@description.open} 
     * Constructs a <code>StringIndexOutOfBoundsException</code> with no
     * detail message.
     * {@description.close}
     *
     * @since   JDK1.0.
     */
    public StringIndexOutOfBoundsException() {
        super();
    }

    /** {@collect.stats} 
     * {@description.open}
     * Constructs a <code>StringIndexOutOfBoundsException</code> with
     * the specified detail message.
     * {@description.close}
     *
     * @param   s   the detail message.
     */
    public StringIndexOutOfBoundsException(String s) {
        super(s);
    }

    /** {@collect.stats} 
     * {@description.open}
     * Constructs a new <code>StringIndexOutOfBoundsException</code>
     * class with an argument indicating the illegal index.
     * {@description.close}
     *
     * @param   index   the illegal index.
     */
    public StringIndexOutOfBoundsException(int index) {
        super("String index out of range: " + index);
    }
}
