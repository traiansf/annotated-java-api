/*
 * Copyright (c) 2000, 2003, Oracle and/or its affiliates. All rights reserved.
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
package javax.swing.text;

import java.lang.reflect.*;
import java.text.*;
import java.util.*;
import javax.swing.text.*;

/** {@collect.stats}
 * <code>NumberFormatter</code> subclasses <code>InternationalFormatter</code>
 * adding special behavior for numbers. Among the specializations are
 * (these are only used if the <code>NumberFormatter</code> does not display
 * invalid nubers, eg <code>setAllowsInvalid(false)</code>):
 * <ul>
 *   <li>Pressing +/- (- is determined from the
 *       <code>DecimalFormatSymbols</code> associated with the
 *       <code>DecimalFormat</code>) in any field but the exponent
 *       field will attempt to change the sign of the number to
 *       positive/negative.
 *   <li>Pressing +/- (- is determined from the
 *       <code>DecimalFormatSymbols</code> associated with the
 *       <code>DecimalFormat</code>) in the exponent field will
 *       attemp to change the sign of the exponent to positive/negative.
 * </ul>
 * <p>
 * If you are displaying scientific numbers, you may wish to turn on
 * overwrite mode, <code>setOverwriteMode(true)</code>. For example:
 * <pre>
 * DecimalFormat decimalFormat = new DecimalFormat("0.000E0");
 * NumberFormatter textFormatter = new NumberFormatter(decimalFormat);
 * textFormatter.setOverwriteMode(true);
 * textFormatter.setAllowsInvalid(false);
 * </pre>
 * <p>
 * If you are going to allow the user to enter decimal
 * values, you should either force the DecimalFormat to contain at least
 * one decimal (<code>#.0###</code>), or allow the value to be invalid
 * <code>setAllowsInvalid(true)</code>. Otherwise users may not be able to
 * input decimal values.
 * <p>
 * <code>NumberFormatter</code> provides slightly different behavior to
 * <code>stringToValue</code> than that of its superclass. If you have
 * specified a Class for values, {@link #setValueClass}, that is one of
 * of <code>Integer</code>, <code>Long</code>, <code>Float</code>,
 * <code>Double</code>, <code>Byte</code> or <code>Short</code> and
 * the Format's <code>parseObject</code> returns an instance of
 * <code>Number</code>, the corresponding instance of the value class
 * will be created using the constructor appropriate for the primitive
 * type the value class represents. For example:
 * <code>setValueClass(Integer.class)</code> will cause the resulting
 * value to be created via
 * <code>new Integer(((Number)formatter.parseObject(string)).intValue())</code>.
 * This is typically useful if you
 * wish to set a min/max value as the various <code>Number</code>
 * implementations are generally not comparable to each other. This is also
 * useful if for some reason you need a specific <code>Number</code>
 * implementation for your values.
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running
 * the same version of Swing.  As of 1.4, support for long term storage
 * of all JavaBeans<sup><font size="-2">TM</font></sup>
 * has been added to the <code>java.beans</code> package.
 * Please see {@link java.beans.XMLEncoder}.
 *
 * @since 1.4
 */
public class NumberFormatter extends InternationalFormatter {
    /** {@collect.stats} The special characters from the Format instance. */
    private String specialChars;

    /** {@collect.stats}
     * Creates a <code>NumberFormatter</code> with the a default
     * <code>NumberFormat</code> instance obtained from
     * <code>NumberFormat.getNumberInstance()</code>.
     */
    public NumberFormatter() {
        this(NumberFormat.getNumberInstance());
    }

    /** {@collect.stats}
     * Creates a NumberFormatter with the specified Format instance.
     *
     * @param format Format used to dictate legal values
     */
    public NumberFormatter(NumberFormat format) {
        super(format);
        setFormat(format);
        setAllowsInvalid(true);
        setCommitsOnValidEdit(false);
        setOverwriteMode(false);
    }

    /** {@collect.stats}
     * Sets the format that dictates the legal values that can be edited
     * and displayed.
     * <p>
     * If you have used the nullary constructor the value of this property
     * will be determined for the current locale by way of the
     * <code>NumberFormat.getNumberInstance()</code> method.
     *
     * @param format NumberFormat instance used to dictate legal values
     */
    public void setFormat(Format format) {
        super.setFormat(format);

        DecimalFormatSymbols dfs = getDecimalFormatSymbols();

        if (dfs != null) {
            StringBuffer sb = new StringBuffer();

            sb.append(dfs.getCurrencySymbol());
            sb.append(dfs.getDecimalSeparator());
            sb.append(dfs.getGroupingSeparator());
            sb.append(dfs.getInfinity());
            sb.append(dfs.getInternationalCurrencySymbol());
            sb.append(dfs.getMinusSign());
            sb.append(dfs.getMonetaryDecimalSeparator());
            sb.append(dfs.getNaN());
            sb.append(dfs.getPercent());
            sb.append('+');
            specialChars = sb.toString();
        }
        else {
            specialChars = "";
        }
    }

    /** {@collect.stats}
     * Invokes <code>parseObject</code> on <code>f</code>, returning
     * its value.
     */
    Object stringToValue(String text, Format f) throws ParseException {
        if (f == null) {
            return text;
        }
        Object value = f.parseObject(text);

        return convertValueToValueClass(value, getValueClass());
    }

    /** {@collect.stats}
     * Converts the passed in value to the passed in class. This only
     * works if <code>valueClass</code> is one of <code>Integer</code>,
     * <code>Long</code>, <code>Float</code>, <code>Double</code>,
     * <code>Byte</code> or <code>Short</code> and <code>value</code>
     * is an instanceof <code>Number</code>.
     */
    private Object convertValueToValueClass(Object value, Class valueClass) {
        if (valueClass != null && (value instanceof Number)) {
            if (valueClass == Integer.class) {
                return new Integer(((Number)value).intValue());
            }
            else if (valueClass == Long.class) {
                return new Long(((Number)value).longValue());
            }
            else if (valueClass == Float.class) {
                return new Float(((Number)value).floatValue());
            }
            else if (valueClass == Double.class) {
                return new Double(((Number)value).doubleValue());
            }
            else if (valueClass == Byte.class) {
                return new Byte(((Number)value).byteValue());
            }
            else if (valueClass == Short.class) {
                return new Short(((Number)value).shortValue());
            }
        }
        return value;
    }

    /** {@collect.stats}
     * Returns the character that is used to toggle to positive values.
     */
    private char getPositiveSign() {
        return '+';
    }

    /** {@collect.stats}
     * Returns the character that is used to toggle to negative values.
     */
    private char getMinusSign() {
        DecimalFormatSymbols dfs = getDecimalFormatSymbols();

        if (dfs != null) {
            return dfs.getMinusSign();
        }
        return '-';
    }

    /** {@collect.stats}
     * Returns the character that is used to toggle to negative values.
     */
    private char getDecimalSeparator() {
        DecimalFormatSymbols dfs = getDecimalFormatSymbols();

        if (dfs != null) {
            return dfs.getDecimalSeparator();
        }
        return '.';
    }

    /** {@collect.stats}
     * Returns the DecimalFormatSymbols from the Format instance.
     */
    private DecimalFormatSymbols getDecimalFormatSymbols() {
        Format f = getFormat();

        if (f instanceof DecimalFormat) {
            return ((DecimalFormat)f).getDecimalFormatSymbols();
        }
        return null;
    }

    /** {@collect.stats}
     */
    private boolean isValidInsertionCharacter(char aChar) {
        return (Character.isDigit(aChar) || specialChars.indexOf(aChar) != -1);
    }


    /** {@collect.stats}
     * Subclassed to return false if <code>text</code> contains in an invalid
     * character to insert, that is, it is not a digit
     * (<code>Character.isDigit()</code>) and
     * not one of the characters defined by the DecimalFormatSymbols.
     */
    boolean isLegalInsertText(String text) {
        if (getAllowsInvalid()) {
            return true;
        }
        for (int counter = text.length() - 1; counter >= 0; counter--) {
            char aChar = text.charAt(counter);

            if (!Character.isDigit(aChar) &&
                           specialChars.indexOf(aChar) == -1){
                return false;
            }
        }
        return true;
    }

    /** {@collect.stats}
     * Subclassed to treat the decimal separator, grouping separator,
     * exponent symbol, percent, permille, currency and sign as literals.
     */
    boolean isLiteral(Map attrs) {
        if (!super.isLiteral(attrs)) {
            if (attrs == null) {
                return false;
            }
            int size = attrs.size();

            if (attrs.get(NumberFormat.Field.GROUPING_SEPARATOR) != null) {
                size--;
                if (attrs.get(NumberFormat.Field.INTEGER) != null) {
                    size--;
                }
            }
            if (attrs.get(NumberFormat.Field.EXPONENT_SYMBOL) != null) {
                size--;
            }
            if (attrs.get(NumberFormat.Field.PERCENT) != null) {
                size--;
            }
            if (attrs.get(NumberFormat.Field.PERMILLE) != null) {
                size--;
            }
            if (attrs.get(NumberFormat.Field.CURRENCY) != null) {
                size--;
            }
            if (attrs.get(NumberFormat.Field.SIGN) != null) {
                size--;
            }
            if (size == 0) {
                return true;
            }
            return false;
        }
        return true;
    }

    /** {@collect.stats}
     * Subclassed to make the decimal separator navigatable, as well
     * as making the character between the integer field and the next
     * field navigatable.
     */
    boolean isNavigatable(int index) {
        if (!super.isNavigatable(index)) {
            // Don't skip the decimal, it causes wierd behavior
            if (getBufferedChar(index) == getDecimalSeparator()) {
                return true;
            }
            return false;
        }
        return true;
    }

    /** {@collect.stats}
     * Returns the first <code>NumberFormat.Field</code> starting
     * <code>index</code> incrementing by <code>direction</code>.
     */
    private NumberFormat.Field getFieldFrom(int index, int direction) {
        if (isValidMask()) {
            int max = getFormattedTextField().getDocument().getLength();
            AttributedCharacterIterator iterator = getIterator();

            if (index >= max) {
                index += direction;
            }
            while (index >= 0 && index < max) {
                iterator.setIndex(index);

                Map attrs = iterator.getAttributes();

                if (attrs != null && attrs.size() > 0) {
                    Iterator keys = attrs.keySet().iterator();

                    while (keys.hasNext()) {
                        Object key = keys.next();

                        if (key instanceof NumberFormat.Field) {
                            return (NumberFormat.Field)key;
                        }
                    }
                }
                index += direction;
            }
        }
        return null;
    }

    /** {@collect.stats}
     * Overriden to toggle the value if the positive/minus sign
     * is inserted.
     */
    void replace(DocumentFilter.FilterBypass fb, int offset, int length,
                String string, AttributeSet attr) throws BadLocationException {
        if (!getAllowsInvalid() && length == 0 && string != null &&
            string.length() == 1 &&
            toggleSignIfNecessary(fb, offset, string.charAt(0))) {
            return;
        }
        super.replace(fb, offset, length, string, attr);
    }

    /** {@collect.stats}
     * Will change the sign of the integer or exponent field if
     * <code>aChar</code> is the positive or minus sign. Returns
     * true if a sign change was attempted.
     */
    private boolean toggleSignIfNecessary(DocumentFilter.FilterBypass fb,
                                              int offset, char aChar) throws
                              BadLocationException {
        if (aChar == getMinusSign() || aChar == getPositiveSign()) {
            NumberFormat.Field field = getFieldFrom(offset, -1);
            Object newValue;

            try {
                if (field == null ||
                    (field != NumberFormat.Field.EXPONENT &&
                     field != NumberFormat.Field.EXPONENT_SYMBOL &&
                     field != NumberFormat.Field.EXPONENT_SIGN)) {
                    newValue = toggleSign((aChar == getPositiveSign()));
                }
                else {
                    // exponent
                    newValue = toggleExponentSign(offset, aChar);
                }
                if (newValue != null && isValidValue(newValue, false)) {
                    int lc = getLiteralCountTo(offset);
                    String string = valueToString(newValue);

                    fb.remove(0, fb.getDocument().getLength());
                    fb.insertString(0, string, null);
                    updateValue(newValue);
                    repositionCursor(getLiteralCountTo(offset) -
                                     lc + offset, 1);
                    return true;
                }
            } catch (ParseException pe) {
                invalidEdit();
            }
        }
        return false;
    }

    /** {@collect.stats}
     * Returns true if the range offset to length identifies the only
     * integer field.
     */
    private boolean isOnlyIntegerField(int offset, int length) {
        if (isValidMask()) {
            int start = getAttributeStart(NumberFormat.Field.INTEGER);

            if (start != -1) {
                AttributedCharacterIterator iterator = getIterator();

                iterator.setIndex(start);
                if (offset > start || iterator.getRunLimit(
                    NumberFormat.Field.INTEGER) > (offset + length)) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    /** {@collect.stats}
     * Invoked to toggle the sign. For this to work the value class
     * must have a single arg constructor that takes a String.
     */
    private Object toggleSign(boolean positive) throws ParseException {
        Object value = stringToValue(getFormattedTextField().getText());

        if (value != null) {
            // toString isn't localized, so that using +/- should work
            // correctly.
            String string = value.toString();

            if (string != null && string.length() > 0) {
                if (positive) {
                    if (string.charAt(0) == '-') {
                        string = string.substring(1);
                    }
                }
                else {
                    if (string.charAt(0) == '+') {
                        string = string.substring(1);
                    }
                    if (string.length() > 0 && string.charAt(0) != '-') {
                        string = "-" + string;
                    }
                }
                if (string != null) {
                    Class valueClass = getValueClass();

                    if (valueClass == null) {
                        valueClass = value.getClass();
                    }
                    try {
                        Constructor cons = valueClass.getConstructor(
                                              new Class[] { String.class });

                        if (cons != null) {
                            return cons.newInstance(new Object[]{string});
                        }
                    } catch (Throwable ex) { }
                }
            }
        }
        return null;
    }

    /** {@collect.stats}
     * Invoked to toggle the sign of the exponent (for scientific
     * numbers).
     */
    private Object toggleExponentSign(int offset, char aChar) throws
                             BadLocationException, ParseException {
        String string = getFormattedTextField().getText();
        int replaceLength = 0;
        int loc = getAttributeStart(NumberFormat.Field.EXPONENT_SIGN);

        if (loc >= 0) {
            replaceLength = 1;
            offset = loc;
        }
        if (aChar == getPositiveSign()) {
            string = getReplaceString(offset, replaceLength, null);
        }
        else {
            string = getReplaceString(offset, replaceLength,
                                      new String(new char[] { aChar }));
        }
        return stringToValue(string);
    }
}
