/*
 * Copyright (c) 1998, 2009, Oracle and/or its affiliates. All rights reserved.
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

package javax.swing.plaf.metal;

import sun.awt.AppContext;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;

/** {@collect.stats}
 * Implements the bumps used throughout the Metal Look and Feel.
 *
 * @author Tom Santos
 * @author Steve Wilson
 */


class MetalBumps implements Icon {

    static final Color ALPHA = new Color(0, 0, 0, 0);

    protected int xBumps;
    protected int yBumps;
    protected Color topColor;
    protected Color shadowColor;
    protected Color backColor;

    private static final Object METAL_BUMPS = new Object();
    protected BumpBuffer buffer;

    /** {@collect.stats}
     * Creates MetalBumps of the specified size with the specified colors.
     * If <code>newBackColor</code> is null, the background will be
     * transparent.
     */
    public MetalBumps( int width, int height,
                       Color newTopColor, Color newShadowColor, Color newBackColor ) {
        setBumpArea( width, height );
        setBumpColors( newTopColor, newShadowColor, newBackColor );
    }

    private static BumpBuffer createBuffer(GraphicsConfiguration gc,
                                           Color topColor, Color shadowColor, Color backColor) {
        AppContext context = AppContext.getAppContext();
        List<BumpBuffer> buffers = (List<BumpBuffer>) context.get(METAL_BUMPS);
        if (buffers == null) {
            buffers = new ArrayList<BumpBuffer>();
            context.put(METAL_BUMPS, buffers);
        }
        for (BumpBuffer buffer : buffers) {
            if (buffer.hasSameConfiguration(gc, topColor, shadowColor, backColor)) {
                return buffer;
            }
        }
        BumpBuffer buffer = new BumpBuffer(gc, topColor, shadowColor, backColor);
        buffers.add(buffer);
        return buffer;
    }

    public void setBumpArea( Dimension bumpArea ) {
        setBumpArea( bumpArea.width, bumpArea.height );
    }

    public void setBumpArea( int width, int height ) {
        xBumps = width / 2;
        yBumps = height / 2;
    }

    public void setBumpColors( Color newTopColor, Color newShadowColor, Color newBackColor ) {
        topColor = newTopColor;
        shadowColor = newShadowColor;
        if (newBackColor == null) {
            backColor = ALPHA;
        }
        else {
            backColor = newBackColor;
        }
    }

    public void paintIcon( Component c, Graphics g, int x, int y ) {
        GraphicsConfiguration gc = (g instanceof Graphics2D) ?
                                     (GraphicsConfiguration)((Graphics2D)g).
                                     getDeviceConfiguration() : null;

        if ((buffer == null) || !buffer.hasSameConfiguration(gc, topColor, shadowColor, backColor)) {
            buffer = createBuffer(gc, topColor, shadowColor, backColor);
        }

        int bufferWidth = BumpBuffer.IMAGE_SIZE;
        int bufferHeight = BumpBuffer.IMAGE_SIZE;
        int iconWidth = getIconWidth();
        int iconHeight = getIconHeight();
        int x2 = x + iconWidth;
        int y2 = y + iconHeight;
        int savex = x;

        while (y < y2) {
            int h = Math.min(y2 - y, bufferHeight);
            for (x = savex; x < x2; x += bufferWidth) {
                int w = Math.min(x2 - x, bufferWidth);
                g.drawImage(buffer.getImage(),
                            x, y, x+w, y+h,
                            0, 0, w, h,
                            null);
            }
            y += bufferHeight;
        }
    }

    public int getIconWidth() {
        return xBumps * 2;
    }

    public int getIconHeight() {
        return yBumps * 2;
    }
}


class BumpBuffer {

    static final int IMAGE_SIZE = 64;

    transient Image image;
    Color topColor;
    Color shadowColor;
    Color backColor;
    private GraphicsConfiguration gc;

    public BumpBuffer(GraphicsConfiguration gc, Color aTopColor,
                      Color aShadowColor, Color aBackColor) {
        this.gc = gc;
        topColor = aTopColor;
        shadowColor = aShadowColor;
        backColor = aBackColor;
        createImage();
        fillBumpBuffer();
    }

    public boolean hasSameConfiguration(GraphicsConfiguration gc,
                                        Color aTopColor, Color aShadowColor,
                                        Color aBackColor) {
        if (this.gc != null) {
            if (!this.gc.equals(gc)) {
                return false;
            }
        }
        else if (gc != null) {
            return false;
        }
        return topColor.equals( aTopColor )       &&
               shadowColor.equals( aShadowColor ) &&
               backColor.equals( aBackColor );
    }

    /** {@collect.stats}
     * Returns the Image containing the bumps appropriate for the passed in
     * <code>GraphicsConfiguration</code>.
     */
    public Image getImage() {
        return image;
    }

    /** {@collect.stats}
     * Paints the bumps into the current image.
     */
    private void fillBumpBuffer() {
        Graphics g = image.getGraphics();

        g.setColor( backColor );
        g.fillRect( 0, 0, IMAGE_SIZE, IMAGE_SIZE );

        g.setColor(topColor);
        for (int x = 0; x < IMAGE_SIZE; x+=4) {
            for (int y = 0; y < IMAGE_SIZE; y+=4) {
                g.drawLine( x, y, x, y );
                g.drawLine( x+2, y+2, x+2, y+2);
            }
        }

        g.setColor(shadowColor);
        for (int x = 0; x < IMAGE_SIZE; x+=4) {
            for (int y = 0; y < IMAGE_SIZE; y+=4) {
                g.drawLine( x+1, y+1, x+1, y+1 );
                g.drawLine( x+3, y+3, x+3, y+3);
            }
        }
        g.dispose();
    }

    /** {@collect.stats}
     * Creates the image appropriate for the passed in
     * <code>GraphicsConfiguration</code>, which may be null.
     */
    private void createImage() {
        if (gc != null) {
            image = gc.createCompatibleImage(IMAGE_SIZE, IMAGE_SIZE,
                       (backColor != MetalBumps.ALPHA) ? Transparency.OPAQUE :
                       Transparency.BITMASK);
        }
        else {
            int cmap[] = { backColor.getRGB(), topColor.getRGB(),
                           shadowColor.getRGB() };
            IndexColorModel icm = new IndexColorModel(8, 3, cmap, 0, false,
                      (backColor == MetalBumps.ALPHA) ? 0 : -1,
                      DataBuffer.TYPE_BYTE);
            image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE,
                                      BufferedImage.TYPE_BYTE_INDEXED, icm);
        }
    }
}
