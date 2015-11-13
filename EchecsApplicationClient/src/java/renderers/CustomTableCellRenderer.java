/**
 * CustomTableCellRenderer
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

package renderers;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import pieces.Piece;

/**
 * Manage a {@link CustomTableCellRenderer}
 * @author Sh1fT
 */
public class CustomTableCellRenderer extends DefaultTableCellRenderer {
    /**
     * Create a new {@link CustomTableCellRenderer} instance
     */
    public CustomTableCellRenderer() {
        super();
    }

    /**
     * Retrieve the component from the table
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     * @return 
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel l = (JLabel) super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);
        if (l instanceof JLabel) {
            this.createChessboard(l, row, column);
            if (value instanceof Piece) {
                l.setText(null);
                l.setIcon(((Piece) value).getIcon());
                return l;
            }
        }
        return null;
    }

    /**
     * Create the Chessboard
     * @param l
     * @param row
     * @param column 
     */
    public void createChessboard(JLabel l, Integer row, Integer column) {
        if ((row % 2) == 0) {
            if ((column % 2) == 0)
                l.setBackground(new Color(255, 206, 158));
            else
                l.setBackground(new Color(209, 139, 71));
        } else {
            if ((column % 2) != 0)
                l.setBackground(new Color(255, 206, 158));
            else
                l.setBackground(new Color(209, 139, 71));
        }
    }
}