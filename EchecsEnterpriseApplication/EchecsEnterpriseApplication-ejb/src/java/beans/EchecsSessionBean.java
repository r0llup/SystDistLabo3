/**
 * EchecsSessionBean
 *
 * Copyright (C) 2012 Sh1fT
 *
 * This file is part of EchecsEnterpriseApplication.
 *
 * EchecsEnterpriseApplication is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * EchecsEnterpriseApplication is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchecsEnterpriseApplication; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */

package beans;

import entities.Game;
import interfaces.EchecsSessionBeanRemote;
import java.util.Random;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Manage an {@link EchecsSessionBean}
 * @author Sh1fT
 */
@Stateful
public class EchecsSessionBean implements EchecsSessionBeanRemote {
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;
    private Game game;
    private Integer whoStart;
    private Integer counter;
    private Integer playerId;

    /**
     * Create a new {@link EchecsSessionBean} instance
     */
    public EchecsSessionBean() {
        this.setEntityManagerFactory(
                Persistence.createEntityManagerFactory("EchecsJavaLibraryPU"));
        this.setEntityManager(this.getEntityManagerFactory().createEntityManager());
        this.setEntityTransaction(this.getEntityManager().getTransaction());
        this.setGame(new Game());
        this.setWhoStart(new Random().nextInt(200000000)/100000000+1);
        this.setCounter(2);
        this.setPlayerId(null);
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityTransaction getEntityTransaction() {
        return entityTransaction;
    }

    public void setEntityTransaction(EntityTransaction entityTransaction) {
        this.entityTransaction = entityTransaction;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Integer getWhoStart() {
        return whoStart;
    }

    public void setWhoStart(Integer whoStart) {
        this.whoStart = whoStart;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    /**
     * Initialize the Game
     * @param playerId
     * @param playerName
     * @return 
     */
    @Override
    public Boolean initGame(Integer playerId, String playerName) {
        switch (playerId) {
            case 1:
                this.getGame().setPlayer1(playerName);
                this.setCounter(this.getCounter()-1);
                break;
            case 2:
                this.getGame().setPlayer2(playerName);
                this.setCounter(this.getCounter()-1);
                break;
            default:
                break;
        }
        if (this.getCounter() == 0) {
            this.getGame().setWhostart(this.getWhoStart());
            this.getGame().setStarted(0);
            this.getEntityTransaction().begin();
            this.getEntityManager().persist(this.getGame());
            this.getEntityTransaction().commit();
            return true;
        } else
            return false;
    }

    /**
     * Retrieve the Player Id
     * @param playerId
     * @return 
     */
    @Override
    public Integer retrievePlayerId(Integer playerId) {
        if (this.getPlayerId() != null) {
            switch (this.getPlayerId()) {
                case 1:
                    return 2;
                case 2:
                    return 1;
                default:
                    break;
            }
        } else {
            this.setPlayerId(playerId);
            return playerId;
        }
        return null;
    }

    /**
     * Start the Game
     * @return 
     */
    @Override
    public Boolean startGame() {
        if (this.getGame().getStarted() == 0) {
            this.getGame().setStarted(1);
            this.getEntityTransaction().begin();
            this.getEntityManager().persist(this.getGame());
            this.getEntityTransaction().commit();
            return true;
        } else
            return false;
    }

    /**
     * Set the next Player
     * @param playerId
     * @return 
     */
    @Override
    public Boolean toNextPlayer(Integer playerId) {
        switch (playerId) {
            case 1:
                this.setWhoStart(2);
                return true;
            case 2:
                this.setWhoStart(1);
                return true;
        }
        return false;
    }

    /**
     * Return the current Player
     * @return 
     */
    @Override
    public Integer whoIsPlaying() {
        return this.getWhoStart();
    }
}