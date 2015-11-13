/**
 * TableRowTransferHandler
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

package handlers;

import interfaces.Reorderable;
import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSource;
import java.util.ArrayList;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

/**
 * Manage a {@link TableRowTransferHandler}
 * @author Sh1fT
 */
public class TableRowTransferHandler extends TransferHandler {
    private final DataFlavor localObjectFlavor = new ActivationDataFlavor(
            ArrayList.class, DataFlavor.javaJVMLocalObjectMimeType,
            "Integer Row Column Index");
    private JTable table = null;

   /**
    * Create a new {@link TableRowTransferHandler} instance
    * @param table 
    */
    public TableRowTransferHandler(JTable table) {
        this.table = table;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        assert (c == table);
        ArrayList<Integer> row = new ArrayList<Integer>();
        row.add(0, table.getSelectedRow());
        row.add(1, table.getSelectedColumn());
        return new DataHandler(row, localObjectFlavor.getMimeType());
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport info) {
        boolean b = info.getComponent() == table && info.isDrop() &&
                info.isDataFlavorSupported(localObjectFlavor);
        table.setCursor(b ? DragSource.DefaultMoveDrop : DragSource.DefaultMoveNoDrop);
        return b;
    }

    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY_OR_MOVE;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport info) {
        JTable target = (JTable) info.getComponent();
        JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
        ArrayList<Integer> toIndex = new ArrayList<Integer>();
        toIndex.add(0, dl.getRow());
        toIndex.add(1, dl.getColumn());
        int toIndexRowCount = table.getModel().getRowCount();
        int toIndexColCount = table.getModel().getColumnCount();
        if (toIndex.get(0) < 0 || toIndex.get(0) > toIndexRowCount)
            toIndex.set(0, toIndexRowCount);
        if (toIndex.get(1) < 0 || toIndex.get(1) > toIndexColCount)
            toIndex.set(1, toIndexColCount);
        target.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        try {
            ArrayList<Integer> fromIndex = (ArrayList<Integer>)
                    info.getTransferable().getTransferData(localObjectFlavor);
            if (fromIndex.get(0) != -1 && fromIndex.get(1) != -1 &&
                    (fromIndex.get(0) != toIndex.get(0) || fromIndex.get(1) != toIndex.get(1))) {
                ((Reorderable) table.getModel()).reorder(fromIndex, toIndex);
             if (toIndex.get(0) > fromIndex.get(0))
                toIndex.set(0, toIndex.get(0)-1);
             if (toIndex.get(1) > fromIndex.get(1))
                toIndex.set(1, toIndex.get(1)-1);
             target.getSelectionModel().addSelectionInterval(toIndex.get(0), toIndex.get(0));
             return true;
          }
       } catch (Exception e) {
          e.printStackTrace();
       }
       return false;
    }

   @Override
   protected void exportDone(JComponent c, Transferable t, int act) {
      if (act == TransferHandler.MOVE)
         table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
   }
}