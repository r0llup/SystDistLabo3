/**
 * CustomTableModel
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

package models;

import echecsapplicationclient.EchecsApplicationClient;
import interfaces.Reorderable;
import java.util.ArrayList;
import javax.jms.JMSException;
import javax.swing.table.DefaultTableModel;
import pieces.Bishop;
import pieces.Empty;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

/**
 * Manage a {@link CustomTableModel}
 * @author Sh1fT
 */
public class CustomTableModel extends DefaultTableModel implements Reorderable {
    private EchecsApplicationClient parent;
    private Boolean firstMove;

    /**
     * Create a new {@link CustomTableModel} instance
     * @param parent
     * @param rowCount
     * @param columnCount 
     */
    public CustomTableModel(EchecsApplicationClient parent,
            int rowCount, int columnCount) {
        super(rowCount, columnCount);
        this.setParent(parent);
        this.setFirstMove(true);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public EchecsApplicationClient getParent() {
        return parent;
    }

    public void setParent(EchecsApplicationClient parent) {
        this.parent = parent;
    }

    public Boolean getFirstMove() {
        return firstMove;
    }

    public void setFirstMove(Boolean firstMove) {
        this.firstMove = firstMove;
    }

    /**
     * Receive a Message
     * @throws JMSException 
     */
    public void receiveMessage() throws JMSException {
        String msg = this.getParent().receiveJMSMessageToMyQueue();
        System.out.println("msg: " + msg);
        String[] msgs = msg.split(":");
        Integer[] oldIndex = {0, 0};
        Integer[] newIndex = {0, 0};
        if (msgs.length == 5) {
            this.getParent().setWhoStart(Integer.parseInt(msgs[0]));
            oldIndex[0] = Integer.parseInt(msgs[1]);
            oldIndex[1] = Integer.parseInt(msgs[2]);
            newIndex[0] = Integer.parseInt(msgs[3]);
            newIndex[1] = Integer.parseInt(msgs[4]);
            this.setValueAt(this.getValueAt(oldIndex[0], oldIndex[1]), newIndex[0], newIndex[1]);
            this.setValueAt(new Empty(), oldIndex[0], oldIndex[1]);
            this.getParent().updateTitle();
        }
    }

    /**
     * Reorder the cases
     * @param fromIndex
     * @param toIndex 
     */
    @Override
    public void reorder(ArrayList<Integer> fromIndex, ArrayList<Integer> toIndex) {
        try {
            Piece fromPiece = (Piece) this.getValueAt(fromIndex.get(0), fromIndex.get(1));
            Piece toPiece = (Piece) this.getValueAt(toIndex.get(0), toIndex.get(1));
            if (!this.getParent().getWhoStart().equals(this.getParent().getPlayerId()))
                this.receiveMessage();
            else if (this.getParent().getPlayerColor().equals(fromPiece.getColor()) &&
                    fromPiece.commonCheck(toPiece) &&
                    !fromPiece.getColor().equals(toPiece.getColor())) {
                Boolean validMovement = false;
                if (fromPiece instanceof Bishop) {
                    Bishop fromBishop = (Bishop) fromPiece;
                    validMovement = fromBishop.commonCheck(fromIndex, toIndex);
                } else if (fromPiece instanceof King) {
                    King fromKing = (King) fromPiece;
                    validMovement = fromKing.commonCheck(fromIndex, toIndex);
                } else if (fromPiece instanceof Knight) {
                    Knight fromKnight = (Knight) fromPiece;
                    validMovement = fromKnight.commonCheck(fromIndex, toIndex);
                } else if (fromPiece instanceof Pawn) {
                    Pawn fromPawn = (Pawn) fromPiece;
                    fromPawn.setFirstMove(this.getFirstMove());
                    validMovement = fromPawn.commonCheck(fromIndex, toIndex);
                    if (validMovement)
                        this.setFirstMove(false);
                } else if (fromPiece instanceof Queen) {
                    Queen fromQueen = (Queen) fromPiece;
                    validMovement = fromQueen.commonCheck(fromIndex, toIndex);
                } else if (fromPiece instanceof Rook) {
                    Rook fromRook = (Rook) fromPiece;
                    validMovement = fromRook.commonCheck(fromIndex, toIndex);
                }
                if (validMovement) {
                    this.setValueAt(fromPiece, toIndex.get(0), toIndex.get(1));
                    this.setValueAt(new Empty(), fromIndex.get(0), fromIndex.get(1));
                    this.getParent().getEchecsSessionBean().toNextPlayer(this.getParent().getPlayerId());
                    this.getParent().setWhoStart(this.getParent().getEchecsSessionBean().whoIsPlaying());
                    this.getParent().sendJMSMessageToMyQueue(
                            (this.getParent().getPlayerId().equals(1) ? 2 : 1) + ":" +
                            fromIndex.get(0) + ":" + fromIndex.get(1) + ":" +
                            toIndex.get(0) + ":" + toIndex.get(1));
                    this.getParent().updateTitle();
                }
            }
        } catch (JMSException ex) {
            System.out.println("Error: " + ex.getLocalizedMessage());
            System.exit(1);
        }
    }
}