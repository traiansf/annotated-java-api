/*
 * Copyright (c) 1997, 2000, Oracle and/or its affiliates. All rights reserved.
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
package javax.swing.event;

import javax.swing.undo.*;
import javax.swing.text.*;

/** {@collect.stats}
 * Interface for document change notifications.  This provides
 * detailed information to Document observers about how the
 * Document changed.  It provides high level information such
 * as type of change and where it occurred, as well as the more
 * detailed structural changes (What Elements were inserted and
 * removed).
 *
 * @author  Timothy Prinzing
 * @see javax.swing.text.Document
 * @see DocumentListener
 */
public interface DocumentEvent {

    /** {@collect.stats}
     * Returns the offset within the document of the start
     * of the change.
     *
     * @return the offset >= 0
     */
    public int getOffset();

    /** {@collect.stats}
     * Returns the length of the change.
     *
     * @return the length >= 0
     */
    public int getLength();

    /** {@collect.stats}
     * Gets the document that sourced the change event.
     *
     * @return the document
     */
    public Document getDocument();

    /** {@collect.stats}
     * Gets the type of event.
     *
     * @return the type
     */
    public EventType getType();

    /** {@collect.stats}
     * Gets the change information for the given element.
     * The change information describes what elements were
     * added and removed and the location.  If there were
     * no changes, null is returned.
     * <p>
     * This method is for observers to discover the structural
     * changes that were made.  This means that only elements
     * that existed prior to the mutation (and still exist after
     * the mutatino) need to have ElementChange records.
     * The changes made available need not be recursive.
     * <p>
     * For example, if the an element is removed from it's
     * parent, this method should report that the parent
     * changed and provide an ElementChange implementation
     * that describes the change to the parent.  If the
     * child element removed had children, these elements
     * do not need to be reported as removed.
     * <p>
     * If an child element is insert into a parent element,
     * the parent element should report a change.  If the
     * child element also had elements inserted into it
     * (grandchildren to the parent) these elements need
     * not report change.
     *
     * @param elem the element
     * @return the change information, or null if the
     *   element was not modified
     */
    public ElementChange getChange(Element elem);

    /** {@collect.stats}
     * Enumeration for document event types
     */
    public static final class EventType {

        private EventType(String s) {
            typeString = s;
        }

        /** {@collect.stats}
         * Insert type.
         */
        public static final EventType INSERT = new EventType("INSERT");

        /** {@collect.stats}
         * Remove type.
         */
        public static final EventType REMOVE = new EventType("REMOVE");

        /** {@collect.stats}
         * Change type.
         */
        public static final EventType CHANGE = new EventType("CHANGE");

        /** {@collect.stats}
         * Converts the type to a string.
         *
         * @return the string
         */
        public String toString() {
            return typeString;
        }

        private String typeString;
    }

    /** {@collect.stats}
     * Describes changes made to a specific element.
     */
    public interface ElementChange {

        /** {@collect.stats}
         * Returns the element represented.  This is the element
         * that was changed.
         *
         * @return the element
         */
        public Element getElement();

        /** {@collect.stats}
         * Fetches the index within the element represented.
         * This is the location that children were added
         * and/or removed.
         *
         * @return the index >= 0
         */
        public int getIndex();

        /** {@collect.stats}
         * Gets the child elements that were removed from the
         * given parent element.  The element array returned is
         * sorted in the order that the elements used to lie in
         * the document, and must be contiguous.
         *
         * @return the child elements
         */
        public Element[] getChildrenRemoved();

        /** {@collect.stats}
         * Gets the child elements that were added to the given
         * parent element.  The element array returned is in the
         * order that the elements lie in the document, and must
         * be contiguous.
         *
         * @return the child elements
         */
        public Element[] getChildrenAdded();

    }
}
