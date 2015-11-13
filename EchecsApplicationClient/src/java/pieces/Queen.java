/**
 * Queen
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
 * Manage a {@link Queen}
 * @author Sh1fT
 */
public class Queen extends Piece {
    /**
     * Create a new {@link Queen} instance
     */
    public Queen() {
        super(null, null);
    }

    /**
     * Create a new {@link Queen} instance
     * @param color
     * @param icon 
     */
    public Queen(String color, ImageIcon icon) {
        super(color, icon);
    }

    /**
     * do common check
     * @param fromIndex
     * @param toIndex
     * @return 
     */
    public Boolean commonCheck(ArrayList<Integer> fromIndex,
            ArrayList<Integer> toIndex) {
        if (((toIndex.get(0) == fromIndex.get(0)) ||
                (toIndex.get(1) == fromIndex.get(1))) ||
                (Math.abs(fromIndex.get(0) - toIndex.get(0)) ==
                Math.abs(fromIndex.get(1) - toIndex.get(1))))
            return true;
        return false;
    }
}