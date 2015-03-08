package com.andall.firehazard.handlers;

import com.andall.firehazard.Game;
import com.andall.firehazard.states.GameState;
import com.andall.firehazard.states.Menu;
import com.andall.firehazard.states.Play;

import java.util.Stack;

public class GameStateManager {

    public final int PLAY = 0;
    public final int MENU = 1;
    private Game game;
    private Stack<GameState> gameStates;

    public GameStateManager(Game game) {
        this.game = game;
        gameStates = new Stack<GameState>();
        pushState(MENU);
    }

    public Game game() {
        return game;
    }

    public void update(float dt) {
        gameStates.peek().update(dt);
    }

    public void render() {
        gameStates.peek().render();
    }

    private GameState getState(int state) {
        if (state == PLAY) return new Play(this);
        if (state == MENU) return new Menu(this);
        return null;
    }

    public void setState(int state) {
        popState();
        pushState(state);
    }

    public void pushState(int state) {
        gameStates.push(getState(state));
    }

    public void popState() {
        GameState g = gameStates.pop();
        g.dispose();
    }

}
