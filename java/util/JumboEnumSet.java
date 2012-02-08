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

package java.util;

/** {@collect.stats} 
 * {@description.open}
 * Private implementation class for EnumSet, for "jumbo" enum types
 * (i.e., those with more than 64 elements).
 * {@description.close}
 *
 * @author Josh Bloch
 * @since 1.5
 * @serial exclude
 */
class JumboEnumSet<E extends Enum<E>> extends EnumSet<E> {
    /** {@collect.stats} 
     * {@description.open}
     * Bit vector representation of this set.  The ith bit of the jth
     * element of this array represents the  presence of universe[64*j +i]
     * in this set.
     * {@description.close}
     */
    private long elements[];

    // Redundant - maintained for performance
    private int size = 0;

    JumboEnumSet(Class<E>elementType, Enum[] universe) {
        super(elementType, universe);
        elements = new long[(universe.length + 63) >>> 6];
    }

    void addRange(E from, E to) {
        int fromIndex = from.ordinal() >>> 6;
        int toIndex = to.ordinal() >>> 6;

        if (fromIndex == toIndex) {
            elements[fromIndex] = (-1L >>>  (from.ordinal() - to.ordinal() - 1))
                            << from.ordinal();
        } else {
            elements[fromIndex] = (-1L << from.ordinal());
            for (int i = fromIndex + 1; i < toIndex; i++)
                elements[i] = -1;
            elements[toIndex] = -1L >>> (63 - to.ordinal());
        }
        size = to.ordinal() - from.ordinal() + 1;
    }

    void addAll() {
        for (int i = 0; i < elements.length; i++)
            elements[i] = -1;
        elements[elements.length - 1] >>>= -universe.length;
        size = universe.length;
    }

    void complement() {
        for (int i = 0; i < elements.length; i++)
            elements[i] = ~elements[i];
        elements[elements.length - 1] &= (-1L >>> -universe.length);
        size = universe.length - size;
    }

    /** {@collect.stats} 
     * {@description.open}
     * Returns an iterator over the elements contained in this set.  The
     * iterator traverses the elements in their <i>natural order</i> (which is
     * the order in which the enum constants are declared).
     * {@description.close}
     * {@property.open formal:java.util.Collection_UnsafeIterator}
     * The returned
     * Iterator is a "weakly consistent" iterator that will never throw {@link
     * ConcurrentModificationException}.
     * {@property.close}
     *
     * @return an iterator over the elements contained in this set
     */
    public Iterator<E> iterator() {
        return new EnumSetIterator<E>();
    }

    private class EnumSetIterator<E extends Enum<E>> implements Iterator<E> {
        /** {@collect.stats} 
         * {@description.open}
         * A bit vector representing the elements in the current "word"
         * of the set not yet returned by this iterator.
         * {@description.close}
         */
        long unseen;

        /** {@collect.stats} 
         * {@description.open}
         * The index corresponding to unseen in the elements array.
         * {@description.close}
         */
        int unseenIndex = 0;

        /** {@collect.stats} 
         * {@description.open}
         * The bit representing the last element returned by this iterator
         * but not removed, or zero if no such element exists.
         * {@description.close}
         */
        long lastReturned = 0;

        /** {@collect.stats} 
         * {@description.open}
         * The index corresponding to lastReturned in the elements array.
         * {@description.close}
         */
        int lastReturnedIndex = 0;

        EnumSetIterator() {
            unseen = elements[0];
        }

        public boolean hasNext() {
            while (unseen == 0 && unseenIndex < elements.length - 1)
                unseen = elements[++unseenIndex];
            return unseen != 0;
        }

        public E next() {
            if (!hasNext())
                throw new NoSuchElementException();
            lastReturned = unseen & -unseen;
            lastReturnedIndex = unseenIndex;
            unseen -= lastReturned;
            return (E) universe[(lastReturnedIndex << 6)
                                + Long.numberOfTrailingZeros(lastReturned)];
        }

        public void remove() {
            if (lastReturned == 0)
                throw new IllegalStateException();
            elements[lastReturnedIndex] -= lastReturned;
            size--;
            lastReturned = 0;
        }
    }

    /** {@collect.stats} 
     * {@description.open}
     * Returns the number of elements in this set.
     * {@description.close}
     *
     * @return the number of elements in this set
     */
    public int size() {
        return size;
    }

    /** {@collect.stats} 
     * {@description.open}
     * Returns <tt>true</tt> if this set contains no elements.
     * {@description.close}
     *
     * @return <tt>true</tt> if this set contains no elements
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /** {@collect.stats} 
     * {@description.open}
     * Returns <tt>true</tt> if this set contains the specified element.
     * {@description.close}
     *
     * @param e element to be checked for containment in this collection
     * @return <tt>true</tt> if this set contains the specified element
     */
    public boolean contains(Object e) {
        if (e == null)
            return false;
        Class eClass = e.getClass();
        if (eClass != elementType && eClass.getSuperclass() != elementType)
            return false;

        int eOrdinal = ((Enum)e).ordinal();
        return (elements[eOrdinal >>> 6] & (1L << eOrdinal)) != 0;
    }

    // Modification Operations

    /** {@collect.stats} 
     * {@description.open}
     * Adds the specified element to this set if it is not already present.
     * {@description.close}
     *
     * @param e element to be added to this set
     * @return <tt>true</tt> if the set changed as a result of the call
     *
     * @throws NullPointerException if <tt>e</tt> is null
     */
    public boolean add(E e) {
        typeCheck(e);

        int eOrdinal = e.ordinal();
        int eWordNum = eOrdinal >>> 6;

        long oldElements = elements[eWordNum];
        elements[eWordNum] |= (1L << eOrdinal);
        boolean result = (elements[eWordNum] != oldElements);
        if (result)
            size++;
        return result;
    }

    /** {@collect.stats} 
     * {@description.open}
     * Removes the specified element from this set if it is present.
     * {@description.close}
     *
     * @param e element to be removed from this set, if present
     * @return <tt>true</tt> if the set contained the specified element
     */
    public boolean remove(Object e) {
        if (e == null)
            return false;
        Class eClass = e.getClass();
        if (eClass != elementType && eClass.getSuperclass() != elementType)
            return false;
        int eOrdinal = ((Enum)e).ordinal();
        int eWordNum = eOrdinal >>> 6;

        long oldElements = elements[eWordNum];
        elements[eWordNum] &= ~(1L << eOrdinal);
        boolean result = (elements[eWordNum] != oldElements);
        if (result)
            size--;
        return result;
    }

    // Bulk Operations

    /** {@collect.stats} 
     * {@description.open}
     * Returns <tt>true</tt> if this set contains all of the elements
     * in the specified collection.
     * {@description.close}
     *
     * @param c collection to be checked for containment in this set
     * @return <tt>true</tt> if this set contains all of the elements
     *        in the specified collection
     * @throws NullPointerException if the specified collection is null
     */
    public boolean containsAll(Collection<?> c) {
        if (!(c instanceof JumboEnumSet))
            return super.containsAll(c);

        JumboEnumSet es = (JumboEnumSet)c;
        if (es.elementType != elementType)
            return es.isEmpty();

        for (int i = 0; i < elements.length; i++)
            if ((es.elements[i] & ~elements[i]) != 0)
                return false;
        return true;
    }

    /** {@collect.stats} 
     * {@description.open}
     * Adds all of the elements in the specified collection to this set.
     * {@description.close}
     *
     * @param c collection whose elements are to be added to this set
     * @return <tt>true</tt> if this set changed as a result of the call
     * @throws NullPointerException if the specified collection or any of
     *     its elements are null
     */
    public boolean addAll(Collection<? extends E> c) {
        if (!(c instanceof JumboEnumSet))
            return super.addAll(c);

        JumboEnumSet es = (JumboEnumSet)c;
        if (es.elementType != elementType) {
            if (es.isEmpty())
                return false;
            else
                throw new ClassCastException(
                    es.elementType + " != " + elementType);
        }

        for (int i = 0; i < elements.length; i++)
            elements[i] |= es.elements[i];
        return recalculateSize();
    }

    /** {@collect.stats} 
     * {@description.open}
     * Removes from this set all of its elements that are contained in
     * the specified collection.
     * {@description.close}
     *
     * @param c elements to be removed from this set
     * @return <tt>true</tt> if this set changed as a result of the call
     * @throws NullPointerException if the specified collection is null
     */
    public boolean removeAll(Collection<?> c) {
        if (!(c instanceof JumboEnumSet))
            return super.removeAll(c);

        JumboEnumSet es = (JumboEnumSet)c;
        if (es.elementType != elementType)
            return false;

        for (int i = 0; i < elements.length; i++)
            elements[i] &= ~es.elements[i];
        return recalculateSize();
    }

    /** {@collect.stats} 
     * {@description.open}
     * Retains only the elements in this set that are contained in the
     * specified collection.
     * {@description.close}
     *
     * @param c elements to be retained in this set
     * @return <tt>true</tt> if this set changed as a result of the call
     * @throws NullPointerException if the specified collection is null
     */
    public boolean retainAll(Collection<?> c) {
        if (!(c instanceof JumboEnumSet))
            return super.retainAll(c);

        JumboEnumSet<?> es = (JumboEnumSet<?>)c;
        if (es.elementType != elementType) {
            boolean changed = (size != 0);
            clear();
            return changed;
        }

        for (int i = 0; i < elements.length; i++)
            elements[i] &= es.elements[i];
        return recalculateSize();
    }

    /** {@collect.stats} 
     * {@description.open}
     * Removes all of the elements from this set.
     * {@description.close}
     */
    public void clear() {
        Arrays.fill(elements, 0);
        size = 0;
    }

    /** {@collect.stats} 
     * {@description.open}
     * Compares the specified object with this set for equality.  Returns
     * <tt>true</tt> if the given object is also a set, the two sets have
     * the same size, and every member of the given set is contained in
     * this set.
     * {@description.close}
     *
     * @param e object to be compared for equality with this set
     * @return <tt>true</tt> if the specified object is equal to this set
     */
    public boolean equals(Object o) {
        if (!(o instanceof JumboEnumSet))
            return super.equals(o);

        JumboEnumSet es = (JumboEnumSet)o;
        if (es.elementType != elementType)
            return size == 0 && es.size == 0;

        return Arrays.equals(es.elements, elements);
    }

    /** {@collect.stats} 
     * {@description.open}
     * Recalculates the size of the set.  Returns true if it's changed.
     * {@description.close}
     */
    private boolean recalculateSize() {
        int oldSize = size;
        size = 0;
        for (long elt : elements)
            size += Long.bitCount(elt);

        return size != oldSize;
    }

    public EnumSet<E> clone() {
        JumboEnumSet<E> result = (JumboEnumSet<E>) super.clone();
        result.elements = (long[]) result.elements.clone();
        return result;
    }
}
