/*
 * Copyright (c) 2000, 2006, Oracle and/or its affiliates. All rights reserved.
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

package java.net;


/** {@collect.stats} 
 * {@description.open}
 * Checked exception thrown to indicate that a string could not be parsed as a
 * URI reference.
 * {@description.close}
 *
 * @author Mark Reinhold
 * @see URI
 * @since 1.4
 */

public class URISyntaxException
    extends Exception
{
    private String input;
    private int index;

    /** {@collect.stats} 
     * {@description.open}
     * Constructs an instance from the given input string, reason, and error
     * index.
     * {@description.close}
     *
     * @param  input   The input string
     * @param  reason  A string explaining why the input could not be parsed
     * @param  index   The index at which the parse error occurred,
     *                 or <tt>-1</tt> if the index is not known
     *
     * @throws  NullPointerException
     *          If either the input or reason strings are <tt>null</tt>
     *
     * @throws  IllegalArgumentException
     *          If the error index is less than <tt>-1</tt>
     */
    public URISyntaxException(String input, String reason, int index) {
        super(reason);
        if ((input == null) || (reason == null))
            throw new NullPointerException();
        if (index < -1)
            throw new IllegalArgumentException();
        this.input = input;
        this.index = index;
    }

    /** {@collect.stats} 
     * {@description.open}
     * Constructs an instance from the given input string and reason.  The
     * resulting object will have an error index of <tt>-1</tt>.
     * {@description.close}
     *
     * @param  input   The input string
     * @param  reason  A string explaining why the input could not be parsed
     *
     * @throws  NullPointerException
     *          If either the input or reason strings are <tt>null</tt>
     */
    public URISyntaxException(String input, String reason) {
        this(input, reason, -1);
    }

    /** {@collect.stats} 
     * {@description.open}
     * Returns the input string.
     * {@description.close}
     *
     * @return  The input string
     */
    public String getInput() {
        return input;
    }

    /** {@collect.stats} 
     * {@description.open}
     * Returns a string explaining why the input string could not be parsed.
     * {@description.close}
     *
     * @return  The reason string
     */
    public String getReason() {
        return super.getMessage();
    }

    /** {@collect.stats} 
     * {@description.open}
     * Returns an index into the input string of the position at which the
     * parse error occurred, or <tt>-1</tt> if this position is not known.
     * {@description.close}
     *
     * @return  The error index
     */
    public int getIndex() {
        return index;
    }

    /** {@collect.stats} 
     * {@description.open}
     * Returns a string describing the parse error.  The resulting string
     * consists of the reason string followed by a colon character
     * (<tt>':'</tt>), a space, and the input string.  If the error index is
     * defined then the string <tt>" at index "</tt> followed by the index, in
     * decimal, is inserted after the reason string and before the colon
     * character.
     * {@description.close}
     *
     * @return  A string describing the parse error
     */
    public String getMessage() {
        StringBuffer sb = new StringBuffer();
        sb.append(getReason());
        if (index > -1) {
            sb.append(" at index ");
            sb.append(index);
        }
        sb.append(": ");
        sb.append(input);
        return sb.toString();
    }

}
