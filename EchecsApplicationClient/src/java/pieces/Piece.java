/**
 * Piece
 *
 * Copyright (C) 2012 Sh1fT
 *
 * This file is part of EchecsApplicationClient.
 *
 * EchecsApplicationClient is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * EchecsApplicationClient is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchecsApplicationClient; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */

package pieces;

import javax.swing.ImageIcon;

/**
 * Manage a {@link Piece}
 * @author Sh1fT
 */
public class Piece implements interfaces.Piece {
    private String color;
    private ImageIcon icon;

    /**
     * Create a new {@link Piece} instance
     */
    public Piece() {
        this(null, null);
    }

    /**
     * Create a new {@link Piece} instance
     * @param color
     * @param icon 
     */
    public Piece(String color, ImageIcon icon) {
        this.setColor(color);
        this.setIcon(icon);
    }

    @Override
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    /**
     * do common check
     * @param toPiece
     * @return 
     */
    public Boolean commonCheck(Piece toPiece) {
        if (!(this instanceof Empty) && !(toPiece instanceof King))
            return true;
        return false;
    }
}