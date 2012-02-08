/*
 * Copyright (c) 1996, 2007, Oracle and/or its affiliates. All rights reserved.
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

package java.beans;

import java.beans.*;

/** {@collect.stats} 
 * This is a support class to help build property editors.
 * <p>
 * It can be used either as a base class or as a delagatee.
 */

public class PropertyEditorSupport implements PropertyEditor {

    /** {@collect.stats} 
     * Constructs a <code>PropertyEditorSupport</code> object.
     *
     * @since 1.5
     */
    public PropertyEditorSupport() {
        setSource(this);
    }

    /** {@collect.stats} 
     * Constructs a <code>PropertyEditorSupport</code> object.
     *
     * @param source the source used for event firing
     * @since 1.5
     */
    public PropertyEditorSupport(Object source) {
        if (source == null) {
           throw new NullPointerException();
        }
        setSource(source);
    }

    /** {@collect.stats} 
     * Returns the bean that is used as the
     * source of events. If the source has not
     * been explicitly set then this instance of
     * <code>PropertyEditorSupport</code> is returned.
     *
     * @return the source object or this instance
     * @since 1.5
     */
    public Object getSource() {
        return source;
    }

    /** {@collect.stats} 
     * Sets the source bean.
     * <p>
     * The source bean is used as the source of events
     * for the property changes. This source should be used for information
     * purposes only and should not be modified by the PropertyEditor.
     *
     * @param source source object to be used for events
     * @since 1.5
     */
    public void setSource(Object source) {
        this.source = source;
    }

    /** {@collect.stats} 
     * Set (or change) the object that is to be edited.
     *
     * @param value The new target object to be edited.  Note that this
     *     object should not be modified by the PropertyEditor, rather
     *     the PropertyEditor should create a new object to hold any
     *     modified value.
     */
    public void setValue(Object value) {
        this.value = value;
        firePropertyChange();
    }

    /** {@collect.stats} 
     * Gets the value of the property.
     *
     * @return The value of the property.
     */
    public Object getValue() {
        return value;
    }

    //----------------------------------------------------------------------

    /** {@collect.stats} 
     * Determines whether the class will honor the paintValue method.
     *
     * @return  True if the class will honor the paintValue method.
     */

    public boolean isPaintable() {
        return false;
    }

    /** {@collect.stats} 
     * Paint a representation of the value into a given area of screen
     * real estate.  Note that the propertyEditor is responsible for doing
     * its own clipping so that it fits into the given rectangle.
     * <p>
     * If the PropertyEditor doesn't honor paint requests (see isPaintable)
     * this method should be a silent noop.
     *
     * @param gfx  Graphics object to paint into.
     * @param box  Rectangle within graphics object into which we should paint.
     */
    public void paintValue(java.awt.Graphics gfx, java.awt.Rectangle box) {
    }

    //----------------------------------------------------------------------

    /** {@collect.stats} 
     * This method is intended for use when generating Java code to set
     * the value of the property.  It should return a fragment of Java code
     * that can be used to initialize a variable with the current property
     * value.
     * <p>
     * Example results are "2", "new Color(127,127,34)", "Color.orange", etc.
     *
     * @return A fragment of Java code representing an initializer for the
     *          current value.
     */
    public String getJavaInitializationString() {
        return "???";
    }

    //----------------------------------------------------------------------

    /** {@collect.stats} 
     * Gets the property value as a string suitable for presentation
     * to a human to edit.
     *
     * @return The property value as a string suitable for presentation
     *       to a human to edit.
     * <p>   Returns "null" is the value can't be expressed as a string.
     * <p>   If a non-null value is returned, then the PropertyEditor should
     *       be prepared to parse that string back in setAsText().
     */
    public String getAsText() {
        return (this.value != null)
                ? this.value.toString()
                : "null";
    }

    /** {@collect.stats} 
     * Sets the property value by parsing a given String.  May raise
     * java.lang.IllegalArgumentException if either the String is
     * badly formatted or if this kind of property can't be expressed
     * as text.
     *
     * @param text  The string to be parsed.
     */
    public void setAsText(String text) throws java.lang.IllegalArgumentException {
        if (value instanceof String) {
            setValue(text);
            return;
        }
        throw new java.lang.IllegalArgumentException(text);
    }

    //----------------------------------------------------------------------

    /** {@collect.stats} 
     * If the property value must be one of a set of known tagged values,
     * then this method should return an array of the tag values.  This can
     * be used to represent (for example) enum values.  If a PropertyEditor
     * supports tags, then it should support the use of setAsText with
     * a tag value as a way of setting the value.
     *
     * @return The tag values for this property.  May be null if this
     *   property cannot be represented as a tagged value.
     *
     */
    public String[] getTags() {
        return null;
    }

    //----------------------------------------------------------------------

    /** {@collect.stats} 
     * A PropertyEditor may chose to make available a full custom Component
     * that edits its property value.  It is the responsibility of the
     * PropertyEditor to hook itself up to its editor Component itself and
     * to report property value changes by firing a PropertyChange event.
     * <P>
     * The higher-level code that calls getCustomEditor may either embed
     * the Component in some larger property sheet, or it may put it in
     * its own individual dialog, or ...
     *
     * @return A java.awt.Component that will allow a human to directly
     *      edit the current property value.  May be null if this is
     *      not supported.
     */

    public java.awt.Component getCustomEditor() {
        return null;
    }

    /** {@collect.stats} 
     * Determines whether the propertyEditor can provide a custom editor.
     *
     * @return  True if the propertyEditor can provide a custom editor.
     */
    public boolean supportsCustomEditor() {
        return false;
    }

    //----------------------------------------------------------------------

    /** {@collect.stats} 
     * Register a listener for the PropertyChange event.  The class will
     * fire a PropertyChange value whenever the value is updated.
     *
     * @param listener  An object to be invoked when a PropertyChange
     *          event is fired.
     */
    public synchronized void addPropertyChangeListener(
                                PropertyChangeListener listener) {
        if (listeners == null) {
            listeners = new java.util.Vector();
        }
        listeners.addElement(listener);
    }

    /** {@collect.stats} 
     * Remove a listener for the PropertyChange event.
     *
     * @param listener  The PropertyChange listener to be removed.
     */
    public synchronized void removePropertyChangeListener(
                                PropertyChangeListener listener) {
        if (listeners == null) {
            return;
        }
        listeners.removeElement(listener);
    }

    /** {@collect.stats} 
     * Report that we have been modified to any interested listeners.
     */
    public void firePropertyChange() {
        java.util.Vector targets;
        synchronized (this) {
            if (listeners == null) {
                return;
            }
            targets = (java.util.Vector) listeners.clone();
        }
        // Tell our listeners that "everything" has changed.
        PropertyChangeEvent evt = new PropertyChangeEvent(source, null, null, null);

        for (int i = 0; i < targets.size(); i++) {
            PropertyChangeListener target = (PropertyChangeListener)targets.elementAt(i);
            target.propertyChange(evt);
        }
    }

    //----------------------------------------------------------------------

    private Object value;
    private Object source;
    private java.util.Vector listeners;
}
