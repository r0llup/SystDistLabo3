/**
 * Pawn
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

import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 * Manage a {@link Pawn}
 * @author Sh1fT
 */
public class Pawn extends Piece {
    private Boolean firstMove;

    /**
     * Create a new {@link Pawn} instance
     */
    public Pawn() {
        super(null, null);
    }

    public Boolean getFirstMove() {
        return firstMove;
    }

    public void setFirstMove(Boolean firstMove) {
        this.firstMove = firstMove;
    }

    /**
     * Create a new {@link Pawn} instance
     * @param color
     * @param icon 
     */
    public Pawn(String color, ImageIcon icon) {
        super(color, icon);
        this.setFirstMove(true);
    }

    /**
     * do common check
     * @param fromIndex
     * @param toIndex
     * @return 
     */
    public Boolean commonCheck(ArrayList<Integer> fromIndex,
            ArrayList<Integer> toIndex) {
        if (((this.getColor().compareToIgnoreCase("b") == 0) &&
             ((this.getFirstMove() && ((toIndex.get(0) == fromIndex.get(0)+1) ||
                (toIndex.get(0) == fromIndex.get(0)+2))) ||
                (!this.getFirstMove() && (toIndex.get(0) == fromIndex.get(0)+1)))) ||
                ((this.getColor().compareToIgnoreCase("w") == 0) &&
                ((this.getFirstMove() && ((toIndex.get(0) == fromIndex.get(0)-1) ||
                (toIndex.get(0) == fromIndex.get(0)-2))) ||
                (!this.getFirstMove() && (toIndex.get(0) == fromIndex.get(0)-1)))) &&
                (toIndex.get(1) == fromIndex.get(1)))
            return true;
        return false;
    }
}