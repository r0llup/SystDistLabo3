/**
 * Game
 *
 * Copyright (C) 2012 Sh1fT
 *
 * This file is part of EchecsJavaLibrary.
 *
 * EchecsJavaLibrary is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * EchecsJavaLibrary is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchecsJavaLibrary; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */

package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Manage a {@link Game}
 * @author Sh1fT
 */
@Entity
@Table(name = "GAME")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Game.findAll", query = "SELECT g FROM Game g"),
    @NamedQuery(name = "Game.findByIdgame", query = "SELECT g FROM Game g WHERE g.idgame = :idgame"),
    @NamedQuery(name = "Game.findByPlayer1", query = "SELECT g FROM Game g WHERE g.player1 = :player1"),
    @NamedQuery(name = "Game.findByPlayer2", query = "SELECT g FROM Game g WHERE g.player2 = :player2"),
    @NamedQuery(name = "Game.findByStarted", query = "SELECT g FROM Game g WHERE g.started = :started"),
    @NamedQuery(name = "Game.findByWhostart", query = "SELECT g FROM Game g WHERE g.whostart = :whostart")})
public class Game implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IDGAME")
    @GeneratedValue
    private Integer idgame;
    @Size(max = 255)
    @Column(name = "PLAYER1")
    private String player1;
    @Size(max = 255)
    @Column(name = "PLAYER2")
    private String player2;
    @Column(name = "STARTED")
    private Integer started;
    @Column(name = "WHOSTART")
    private Integer whostart;

    public Game() {
    }

    public Game(Integer idgame) {
        this.idgame = idgame;
    }

    public Integer getIdgame() {
        return idgame;
    }

    public void setIdgame(Integer idgame) {
        this.idgame = idgame;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public Integer getStarted() {
        return started;
    }

    public void setStarted(Integer started) {
        this.started = started;
    }

    public Integer getWhostart() {
        return whostart;
    }

    public void setWhostart(Integer whostart) {
        this.whostart = whostart;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idgame != null ? idgame.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Game)) {
            return false;
        }
        Game other = (Game) object;
        if ((this.idgame == null && other.idgame != null) || (this.idgame != null && !this.idgame.equals(other.idgame))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Game[ idgame=" + idgame + " ]";
    }
    
}
