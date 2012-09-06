/*
 * Copyright (c) 2004, Oracle and/or its affiliates. All rights reserved.
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

package java.util;

/** {@collect.stats} 
 * {@description.open}
 * FomattableFlags are passed to the {@link Formattable#formatTo
 * Formattable.formatTo()} method and modify the output format for {@linkplain
 * Formattable Formattables}.  Implementations of {@link Formattable} are
 * responsible for interpreting and validating any flags.
 * {@description.close}
 *
 * @since  1.5
 */
public class FormattableFlags {

    // Explicit instantiation of this class is prohibited.
    private FormattableFlags() {}

    /** {@collect.stats} 
     * {@description.open}
     * Left-justifies the output.  Spaces (<tt>'&#92;u0020'</tt>) will be added
     * at the end of the converted value as required to fill the minimum width
     * of the field.  If this flag is not set then the output will be
     * right-justified.
     *
     * <p> This flag corresponds to <tt>'-'</tt> (<tt>'&#92;u002d'</tt>) in
     * the format specifier.
     * {@description.close}
     */
    public static final int LEFT_JUSTIFY = 1<<0; // '-'

    /** {@collect.stats} 
     * {@description.open}
     * Converts the output to upper case according to the rules of the
     * {@linkplain java.util.Locale locale} given during creation of the
     * <tt>formatter</tt> argument of the {@link Formattable#formatTo
     * formatTo()} method.  The output should be equivalent the following
     * invocation of {@link String#toUpperCase(java.util.Locale)}
     *
     * <pre>
     *     out.toUpperCase() </pre>
     *
     * <p> This flag corresponds to <tt>'^'</tt> (<tt>'&#92;u005e'</tt>) in
     * the format specifier.
     * {@description.close}
     */
    public static final int UPPERCASE = 1<<1;    // '^'

    /** {@collect.stats} 
     * {@description.open}
     * Requires the output to use an alternate form.  The definition of the
     * form is specified by the <tt>Formattable</tt>.
     *
     * <p> This flag corresponds to <tt>'#'</tt> (<tt>'&#92;u0023'</tt>) in
     * the format specifier.
     * {@description.close}
     */
    public static final int ALTERNATE = 1<<2;    // '#'
}
