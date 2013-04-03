/*
 * Copyright (c) 1999, 2002, Oracle and/or its affiliates. All rights reserved.
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

package javax.sound.midi;

/** {@collect.stats}
 * MIDI events contain a MIDI message and a corresponding time-stamp
 * expressed in ticks, and can represent the MIDI event information
 * stored in a MIDI file or a <code>{@link Sequence}</code> object.  The
 * duration of a tick is specified by the timing information contained
 * in the MIDI file or <code>Sequence</code> object.
 * <p>
 * In Java Sound, <code>MidiEvent</code> objects are typically contained in a
 * <code>{@link Track}</code>, and <code>Tracks</code> are likewise
 * contained in a <code>Sequence</code>.
 *
 *
 * @author David Rivas
 * @author Kara Kytle
 */
public class MidiEvent {


    // Instance variables

    /** {@collect.stats}
     * The MIDI message for this event.
     */
    private final MidiMessage message;


    /** {@collect.stats}
     * The tick value for this event.
     */
    private long tick;


    /** {@collect.stats}
     * Constructs a new <code>MidiEvent</code>.
     * @param message the MIDI message contained in the event
     * @param tick the time-stamp for the event, in MIDI ticks
     */
    public MidiEvent(MidiMessage message, long tick) {

        this.message = message;
        this.tick = tick;
    }

    /** {@collect.stats}
     * Obtains the MIDI message contained in the event.
     * @return the MIDI message
     */
    public MidiMessage getMessage() {
        return message;
    }


    /** {@collect.stats}
     * Sets the time-stamp for the event, in MIDI ticks
     * @param tick the new time-stamp, in MIDI ticks
     */
    public void setTick(long tick) {
        this.tick = tick;
    }


    /** {@collect.stats}
     * Obtains the time-stamp for the event, in MIDI ticks
     * @return the time-stamp for the event, in MIDI ticks
     */
    public long getTick() {
        return tick;
    }
}
