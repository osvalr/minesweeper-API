package com.osvalr.minesweeper.domain;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.UUID;

public class Game {

    private String gameId;
    private Date startTime;
    private Field field;

    public Game() {
    }

    public Game(@Nonnull GameSize gameSize) {
        this.gameId = UUID.randomUUID().toString();
        this.field = new Field(gameSize);
        this.startTime = new Date();
        this.field.init();
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
